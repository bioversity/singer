package org.sgrp.singer.biomissions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap; 


// this has some DB api
import org.sgrp.singer.AccessionServlet;

/**
 * Deals with showing data from the sub_missions table
 * in a variety of different ways.
 */
public class SubMissions {

    public Connection conn;
    public int totalSubMissions;
    public int totalMissions;

    /**
     * Stores the information for the
     * amounts of pages.
     */
    private int itemsPerPage;

    
    public SubMissions(Connection c) {
        // use existing api to connect to MySQL
		conn = c;
    }

    /**
     * Returns a result from an SQL query based on
     * retrieving sub_missions - also does pagination
     */
    public ResultSet getSubMissionsForPage(int pageNum, String searchTerm) throws SQLException, Exception {
        // the pagenum is actually an index,
        // we need to subtract 1 because of the offset logic
        pageNum = pageNum - 1;

        if(pageNum < 0) { // something went wrong
            throw new Exception("Pagenum can't be a negative number");
        }

        // to calculate an offest
        // we must multiply the page num by the itemsPerPage
        int offset = pageNum * itemsPerPage;

        // this is needed for the group_concat limit
        Statement stmtSet = conn.createStatement();
        stmtSet.execute("SET session group_concat_max_len = 300000;");   

        Statement stmt = conn.createStatement();

        // this query is NOT nice
        String query = "select sub_missions.*, missions.Title, "
            +"(select sum(samples.SamplesCount) from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION`) as samplesCount, "
            +" (select group_concat(samples.`ScientificName`) from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION` ) as commaSamples,"
            +" (select group_concat(samples.`OtherNumbers`) from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION` ) as otherNumbers,"
            +" (select distinct group_concat(',',concat_ws(',',admin1, admin2, admin3))  from samples inner join collecting_sites on samples.`ID_COLLECTING_SITE` = collecting_sites.`ID_COLLECTING_SITE` where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION`  ) as admins,"
            +" (select group_concat(concat_ws(',',Firstname, Surname)) from collectors left join cooperators on cooperators.`ID_COOPERATOR` = collectors.`ID_COOPERATOR` where collectors.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION`) as collector_names,"
            // institutes
            +" (select concat_ws(',', (select NAME_NAT from institutes WHERE institutes.INSTCODE = SUBSTRING_INDEX( samples.`INSTCODE`, ';', 1 ) limit 1), (select NAME_NAT from institutes WHERE institutes.INSTCODE = SUBSTRING_INDEX(SUBSTRING_INDEX( samples.`INSTCODE` , ';', 2 ),';',-1) limit 1), (select NAME_NAT from institutes WHERE institutes.INSTCODE = SUBSTRING_INDEX( samples.`INSTCODE` , ';', -1 )  limit 1)) from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION` limit 1) as inst_names,"
            +" (select samples.`INSTCODE` from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION` limit 1) as instcodes,"
            +" cty.Name as countryName"
            +" from sub_missions"
            +" left join missions on sub_missions.`ID_MISSION` = missions.`ID_MISSION`"
            +" left join cty on cty.code_ = sub_missions.`Country`";

        if(searchTerm == null)
            query += " group by id_mission"; // this group by is what gets rid of all sub_missions
        // search - XXX sql injection prone
        if(searchTerm != null) {
            query += " HAVING commaSamples like '%"+searchTerm+"%'"
            +" or otherNumbers like '%"+searchTerm+"%'"
            +" or admins like '%"+searchTerm+"%'"
            +" or collector_names like '%"+searchTerm+"%'"
            +" or inst_names like '%"+searchTerm+"%'"
            +" or sub_missions.Notes like '%"+searchTerm+"%'"
            +" or sub_missions.`ID_SUB_MISSION` like '%"+searchTerm+"%'"
            +" or sub_missions.`StartDate` like '%"+searchTerm+"%'"
            +" or sub_missions.`EndDate` like '%"+searchTerm+"%'"
            +" or cty.Name like '%"+searchTerm+"%'"
            +" or missions.Title like '%"+searchTerm+"%'";
        }

        query += " limit "+itemsPerPage+" offset "+offset;

        ResultSet rs = stmt.executeQuery(query);

        return rs;
    }

    /**
     * Selects all the submission with ID_MISSION = id
     */
    public ResultSet getSubMissionFromMissionId(String id) throws SQLException {
        Statement stmt = conn.createStatement();

        String query = "select sub_missions.*, (select sum(samples.SamplesCount) from samples where `ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION`) as samplesCount, cty.Name as countryName from sub_missions left join cty on cty.code_ = sub_missions.`Country` where sub_missions.ID_MISSION = '"+id+"'";

        ResultSet rs = stmt.executeQuery(query);

        return rs;

    }

    /**
     * Returns the total number of rows in the sub_missions table
     * grouped by the id_mission field
     */
    public int getTotalMissions() throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select id_mission from `sub_missions` group by id_mission");

        rs.last();
        int c = rs.getRow();

        totalMissions = c;

        return c;

    }
    /**
     * Get the total sub_missions
     */
    public int getTotalSubMissions() throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select count(*) as c from `sub_missions`");

        rs.last();
        int c = rs.getInt("c");

        totalSubMissions = c;

        return c;

    }

    /**
     * Total pages amount is the total amount of submissions
     * devided by the itemsPerPage value
     */
    public int getAmountOfPages() {
        return (totalMissions / itemsPerPage) + 1;
    }

    /**
     * Given the current pageNum it returns an array
     * of pages in the current offset - used for showing
     * the pagination links
     */
    public ArrayList<Integer> getPageLinks(int pageNum) {
        int n = ((int)pageNum / itemsPerPage);
        Integer startPage = n * itemsPerPage;

        ArrayList<Integer> pageLinks = new ArrayList<Integer>();

        // iterate and add to the array
        for(int i=0; i < itemsPerPage; i++) {
            if(startPage > getAmountOfPages()) {
                continue; 
            }
            pageLinks.add(++startPage); 
        }

        return pageLinks;
    }

    /**
     * Sets the limit for the items to show on a page
     */
    public void setItemsPerPage(int n) {
        itemsPerPage = n;
    }
    public int getItemsPerPage() {
        return itemsPerPage; 
    }


    /**
     * Gets data for a single submission,
     * uses prepareStatements to avoid SQL injections.
     * <code>
     * select sub_missions.*, 
     * cty.Name as countryName,
     * (select sum(samples.SamplesCount) from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION`) as samplesCount,                                                                   
     * (select samples.`INSTCODE` from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION` limit 1) as instcodes                                                                                                         
     * from sub_missions                                                                                  
     * left join cty on cty.code_ = sub_missions.`Country`
     * where sub_missions.`ID_SUB_MISSION` = 'CN214' 
     * limit 1
     * </code>
     */
    public Map getSingleSubMissionData(String subMissionId) throws SQLException{
        Map data = new HashMap();


        String query = "select sub_missions.*, (select sum(samples.SamplesCount) from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION`) as samplesCount, "
            +"(select samples.`INSTCODE` from samples where samples.`ID_SUB_MISSION` = sub_missions.`ID_SUB_MISSION` limit 1) as instcodes, "
            +"cty.Name as countryName "
            +"from sub_missions "
            +"left join cty on cty.code_ = sub_missions.`Country` "
            +"where sub_missions.ID_SUB_MISSION = ? limit 1";

        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, subMissionId);

        ResultSet rs = prepStmt.executeQuery();
        if(!rs.last()) { // no data
            return data;
        } 


        String insts = parseInstCode(rs.getString("instcodes"));

        data.put("samplesCount", rs.getInt("samplesCount"));
        data.put("subMissionCode", rs.getString("ID_SUB_MISSION"));
        data.put("missionCode", rs.getString("ID_MISSION"));
        data.put("country", rs.getString("countryName"));
        data.put("startDate", rs.getString("StartDate"));
        data.put("endDate", rs.getString("EndDate"));
        data.put("institutes", insts);

        return data;

    }
        
    /**
     * Gets cooperators information for a specific subMissionId.
     * <code>
     * select collectors.*, cooperators.Firstname, `cooperators`.`Surname`, `institutes`.`NAME_NAT` from collectors
     * left join `cooperators` on `cooperators`.`ID_COOPERATOR` = `collectors`.`ID_COOPERATOR`
     * left join `institutes` on `cooperators`.`INSTCODE` = `institutes`.`INSTCODE`
     * where collectors.`ID_SUB_MISSION`='CN454A1'
     * </code>
     */
    public ResultSet getCooperatorsForSubMission(String subMissionId) throws SQLException{
        String query = "select collectors.*, cooperators.Firstname, `cooperators`.`Surname`, `institutes`.`NAME_NAT` from collectors left join `cooperators` on `cooperators`.`ID_COOPERATOR` = `collectors`.`ID_COOPERATOR` left join `institutes` on `cooperators`.`INSTCODE` = `institutes`.`INSTCODE` where collectors.`ID_SUB_MISSION`= ?";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, subMissionId);

        ResultSet rs = prepStmt.executeQuery();

        return rs;
    }

    /**
     * Gets samples for a specific subMissionId.
     * select samples.`CollectingNumber`, samples.`TAXON`, samples.`SamplesCount` from samples
     * where samples.`ID_SUB_MISSION` = 'CN004A1'
     */
    public ResultSet getSamplesForSubMission(String subMissionId) throws SQLException{
        String query = "select samples.`ID_SAMPLE`, samples.`Notes`, samples.`CollectingNumber`, samples.`ScientificName`, samples.OtherNumbers, samples.`SamplesCount`, collecting_sites.`Admin1` as region, collecting_sites.Admin2 as province, collecting_sites.Admin3 as municipality from samples left join `collecting_sites` on collecting_sites.`ID_COLLECTING_SITE` = samples.`ID_COLLECTING_SITE` where samples.`ID_SUB_MISSION` = ?";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, subMissionId);

        ResultSet rs = prepStmt.executeQuery();

        return rs;
    }

    /**
     * Gets batch level samples information for a specific
     * sub mission.
     */
    public ResultSet getBatchSamplesForSubMission(String subMissionId) throws SQLException {
        String query = "select samples.`ScientificName`, sum(samples.`SamplesCount`) as groupedSamplesCount from samples where samples.`ID_SUB_MISSION` = ? group by samples.`ScientificName`";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, subMissionId);

        ResultSet rs = prepStmt.executeQuery();

        return rs;

    }


    /**
     * Get specific sample information
     */
    public ResultSet getSampleId(int sampleId) throws SQLException {
        String query = "select * from samples left join `collecting_sites` on `collecting_sites`.`ID_COLLECTING_SITE` = samples.`ID_COLLECTING_SITE` where `ID_SAMPLE` = ?";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setInt(1, sampleId);

        ResultSet rs = prepStmt.executeQuery();

        return rs;
    }


    /**
     * Gets institutes given a submission id
     */
    public String getInstitutesFromSubMissionId(String subMissionId) throws SQLException {
        String query = "select distribution.`INSTCODE` from distribution where `distribution`.`ID_SUB_MISSION` = ? limit 1;";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, subMissionId);

        ResultSet rs = prepStmt.executeQuery();

        String ret = "";
        if(rs.next()) {
            ret = parseInstCode(rs.getString("INSTCODE"));
        }

        return ret;
    }


    /**
     * Takes care of splitting the instCode argument
     * and finding the correct institute in the db.
     *
     * @param instCode Is something like AUT003;ITA001 etc
     */
    public String parseInstCode(String instCode) throws SQLException {
        if(instCode == null) return "";

        String finalS = new String("");
        Statement stmt = conn.createStatement();

        String[] institutes = instCode.split(";");
        for(int i=0; i<institutes.length; i++) {
            ResultSet rs = stmt.executeQuery("select NAME_NAT from institutes where INSTCODE = '"+institutes[i]+"' limit 1");
            rs.last();

            String instName = rs.getString("NAME_NAT");

            String sep = " - ";

            // need to check against Bioversity-HQ as BIOVERSITY
            // is already hard-coded
            if(!instName.equals("Bioversity-HQ")) 
                finalS = finalS.concat(instName+sep);
        }

        return finalS;

    }

    /**
     * Converts a static integer value to it's actual
     * correct word assigned in Hannes' dictonary file.
     *
     * Returns an empty string if the status doesn't exist
     */
    public String convertSampleStatus(Integer status) {
        Map<Integer, String> stats = new HashMap<Integer, String>();
        stats.put(0, "unknown");
        stats.put(1, "wild");
        stats.put(2, "weedy");
        stats.put(3, "traditional cultivar/landrace");
        stats.put(4, "breeding material");
        stats.put(5, "advanced cultivar");
        stats.put(35, "cultivated");
        stats.put(9, "others");

        String ret = stats.get(status);

        if(ret != null) {
            return ret;
        } else {
            return "";
        }
    }

    /**
     * Converts the COLLSRC coded value
     *
     * @returns empty string if nothing found
     */
    public String convertCollSrc(Integer collsrc) {
        Map<Integer, String> stats = new HashMap<Integer, String>();
        stats.put(0, "unknown");
        stats.put(1, "wild habitat");
        stats.put(2, "farmland");
        stats.put(3, "farmstore");
        stats.put(4, "backyard");
        stats.put(5, "village market");
        stats.put(6, "commercial market");
        stats.put(7, "institute");
        stats.put(8, "treshing floor");
        stats.put(9, "others");

        String ret = stats.get(collsrc);

        if(ret != null) {
            return ret;
        } else {
            return "";
        }
        
    }

   
    /**
     * Gets the coordinates for each individual 
     * sample inside a submission
     */
    public ResultSet getCoordsForAllSamplesInSubMission(String subMissionId) throws SQLException {
        String query = "select samples.`ScientificName`, `collecting_sites`.`LATITUDEdecimal`, `collecting_sites`.`LONGITUDEdecimal` from samples left join `collecting_sites` on `collecting_sites`.`ID_COLLECTING_SITE` = samples.`ID_COLLECTING_SITE` where samples.`ID_SUB_MISSION` = ? AND `collecting_sites`.`LATITUDEdecimal` is not null AND `collecting_sites`.`LONGITUDEdecimal` is not null";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, subMissionId);

        ResultSet rs = prepStmt.executeQuery();

        return rs;

    }

    
}
