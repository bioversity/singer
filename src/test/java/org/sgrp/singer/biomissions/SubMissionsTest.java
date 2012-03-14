package org.sgrp.singer.biomissions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

import org.sgrp.singer.AccessionServlet;


/**
 * Testing the SubMissions class
 */
public class SubMissionsTest extends TestCase {

    static {
        AccessionServlet.loadInitData("/Users/lucamatteis/bioversity-singer/trunk/src/main/webapp/WEB-INF/singer.properties");
    }

    public void testGetConnection() throws SQLException {
        Connection conn = AccessionServlet.openConnection();

        assertTrue(true);

        conn.close();

    }

    /**
     * Tests that we have data for a single specific ID_SUB_MISSION
     */
    public void testGetSingleSubmissionData() throws SQLException{
        Connection conn = AccessionServlet.openConnection();

        try {
            SubMissions s = new SubMissions(conn);

            // get the specific data for this submission id
            Map data = s.getSingleSubMissionData("CN454A1");

            // let's make sure it exists
            assertTrue(data.size() > 0);

            // make sure our map contains all the fields we need 
            assertTrue(data.containsKey("samplesCount"));
            assertTrue(data.containsKey("subMissionCode"));
            assertTrue(data.containsKey("missionCode"));
            assertTrue(data.containsKey("institutes"));
            assertTrue(data.containsKey("country"));
            assertTrue(data.containsKey("startDate"));
            assertTrue(data.containsKey("endDate"));
        } catch (SQLException e) {

        } finally {
            conn.close();
        }


    }

    /**
     * Gets all the cooperators for a specific ID_SUB_MISSION
     */
    public void testGetCooperators() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {
            SubMissions s = new SubMissions(conn);

            String subMissionId = "CN454A1";
            ResultSet rs = s.getCooperatorsForSubMission(subMissionId);

            if(rs.next()) {
                assertTrue(rs.getString("ID_SUB_MISSION").equals(subMissionId));
                assertTrue(rs.getString("Firstname") != null);
                assertTrue(rs.getString("Surname") != null);
                assertTrue(rs.getString("NAME_NAT") != null);
            }
        } catch (SQLException e) {

        } finally {
            conn.close();
        }

    }

    /**
     * Gets all the samples for a specific ID_SUB_MISSION
     * select samples.`CollectingNumber`, samples.`TAXON`, samples.`SamplesCount` from samples
     * where samples.`ID_SUB_MISSION` = 'CN004A1'
     */
    public void testGetSamples() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {
            SubMissions s = new SubMissions(conn);

            String subMissionId = "CN454A1";
            ResultSet rs = s.getSamplesForSubMission(subMissionId);

            if(rs.next()) {
                assertTrue(rs.getString("CollectingNumber") != null);
                assertTrue(rs.getString("TAXON") != null);
                assertTrue(rs.getString("SamplesCount") != null);
            }
        } catch (SQLException e) {

        } finally {
            conn.close();
        }

    }

    /**
     * Test getting a batch level sample information
     * for a specific sub-mission
     */
    public void testGetBatchSamples() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {
            SubMissions s = new SubMissions(conn);

            String subMissionId = "CN334A1";
            ResultSet rs = s.getBatchSamplesForSubMission(subMissionId);

            if(rs.next()) {
                assertTrue(rs.getString("TAXON") != null);
                assertTrue(rs.getString("groupedSamplesCount") != null);
            }
        } catch (SQLException e) {

        } finally {
            conn.close();
        }
        
    }

    /**
     * Get all sample information for a specific sample
     * using the ID_SAMPLE field.
     */
    public void testGetSingleSample() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {
            SubMissions s = new SubMissions(conn);

            int sampleId = 12788;
            ResultSet rs = s.getSampleId(sampleId);

            if(rs.next()) {
                assertTrue(rs.getInt("ID_SAMPLE") == sampleId);
            }
        } catch (SQLException e) {

        } finally {
            conn.close();
        }
    }
    public void testGetSingleSample2() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {
            SubMissions s = new SubMissions(conn);

            int sampleId = 12788434;
            ResultSet rs = s.getSampleId(sampleId);

            if(rs.next()) {
                assertTrue(rs.getInt("ID_SAMPLE") == sampleId);
            } 
        } catch (SQLException e) {

        } finally {
            AccessionServlet.getCP().freeConnection(conn);
        }
    }


    public void testGetInstitutesFromSubMissionId() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {

            SubMissions s = new SubMissions(conn);

            String instituteNames = s.getInstitutesFromSubMissionId("CN103D");
        } catch (SQLException e) {

        } finally {
            conn.close();
        }

    }

    public void testConvertingSampleStatus() throws SQLException{
        Connection conn = AccessionServlet.openConnection();

        SubMissions s = new SubMissions(conn);

        assertTrue(s.convertSampleStatus(2).equals("weedy"));
        assertTrue(s.convertSampleStatus(18).equals(""));
        assertFalse(s.convertSampleStatus(0).equals(""));
        conn.close();
    }
    public void testConvertingCollSrc() throws SQLException{
        Connection conn = AccessionServlet.openConnection();

        SubMissions s = new SubMissions(conn);

        assertTrue(s.convertCollSrc(0).equals("unknown"));
        assertTrue(s.convertCollSrc(1).equals("wild habitat"));
        assertFalse(s.convertCollSrc(2).equals(""));
        conn.close();
    }

    public void testGettingCoordinatesForAllSamplesInSubMission() throws SQLException {
        Connection conn = AccessionServlet.openConnection();
        try {
            SubMissions s = new SubMissions(conn);
            ResultSet rs = s.getCoordsForAllSamplesInSubMission("CN328B");
            if(rs.next()) {
                assertTrue(rs.getString("LONGITUDEdecimal") != null);

            }
        } catch (SQLException e) {

        } finally {
            conn.close();
        }


    }

}
