package org.sgrp.singer.indexer;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.Main;

public class KeywordIndex extends BaseIndexer implements IndexerInterface {

	protected static KeywordIndex	mgr	= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static KeywordIndex getInstance() {
		if (mgr == null) {
			mgr = new KeywordIndex();
		}
		return mgr;
	}

	public int					count		= 0;

	public RAMDirectory			idx			= null;

	public String				INDEX_DIR	= "keywordindex";

	protected ArrayList<String>	sqllist		= new ArrayList<String>();

	public KeywordIndex(ArrayList<String> sql)
	{
		setIndexDir(INDEX_DIR);
		sqllist = sql;
	}
	
	public KeywordIndex() {
		setIndexDir(INDEX_DIR);
		sqllist.add("select code_ as id, name as name, \"" + AccessionConstants.INSTITUTE + "\" as type, \"\" as parent, fullname as other from inst");
		sqllist.add("select code_ as id, name as name, \"" + AccessionConstants.COLLECTION + "\" as type, instcode_ as parent,respers as other from col");
		sqllist.add("select code_ as id, emailorder as name, \"" + AccessionConstants.COLLECTION_ORDER_EMAIL_ACC + "\" as type, instcode_ as parent,email as other from col");
		sqllist.add("select code_ as id, full_name as name, \"" + AccessionConstants.TAXON + "\" as type, itannex1 as parent, concat(kingdom,' ',diviphylum,' ',class) as other from tax where code_ in (select taxcode_ from acc)");
		sqllist.add("select distinct lower(trim(genus)) as id, genus as name, \"" + AccessionConstants.GENUS + "\" as type, family as parent, \"\" as other from tax where code_ in (select taxcode_ from acc)");
		sqllist.add("select distinct lower(trim(species)) as id, species as name, \"" + AccessionConstants.SPECIES + "\" as type, genus as parent,\"\" as other from tax where code_ in (select taxcode_ from acc)");
		sqllist.add("select distinct Concat(lower(trim(genus)),'|',lower(trim(species)))  as id, concat(genus,' ',species) as name, '" + AccessionConstants.GENUS + AccessionConstants.SPECIES + "' as type, family as parent,\"\" as other from tax where code_ in (select taxcode_ from acc)");
		sqllist.add("select code_ as id, name as name, \"" + AccessionConstants.STATUS + "\" as type, \"\" as parent,\"\" as other from stat");
		sqllist.add("select code_ as id, name as name, \"" + AccessionConstants.SOURCE + "\" as type, \"\" as parent,\"\" as other from src");
		sqllist.add("select lower(code_) as id, name as name, \"" + AccessionConstants.TRUST + "\" as type, \"\" as parent,\"\" as other from trust");
		sqllist.add("select code_ as id, name as name, \"" + AccessionConstants.USER + "\" as type, \"\" as parent,\"\" as other from user");
		sqllist.add("select lower(code_) as id, name as name, \"" + AccessionConstants.COUNTRY + "\" as type, region as parent, devstat as other from cty");
		sqllist.add("select lower(region) as id, region as name, \"" + AccessionConstants.REGION + "\" as type, \"\" as parent, devstat as other from cty group by region");
		sqllist.add("select lower(devstat) as id, devstat as name, \"" + AccessionConstants.DEVSTAT + "\" as type, \"\" as parent,\"\" as other from cty group by devstat");
		sqllist.add("select id as id, link as name, \"" + AccessionConstants.PICTURE + "\" as type, concat(lower(genus),lower(species)) as parent, remarks as other from pics");
    /*
    sqllist.add("SELECT `code_` AS \"id\", `name` AS \"name\", 'in' AS \"type\", '' AS \"parent\", `fullname` as \"other\" FROM `SINGER_WAREHOUSE`.`index_inst`");
    sqllist.add("REPLACE( SELECT `HoldingCollectionCode`, ':', '_' ) AS \"id\", `HoldingCollectionName` AS \"name\", 'co' AS \"type\", LEFT( `HoldingCollectionCode`, 6 ) AS \"parent\", `CollectionCurator` AS \"other\" FROM `SINGER_WAREHOUSE`.`COLLECTIONS`");
    sqllist.add("REPLACE( SELECT `HoldingCollectionCode`, ':', '_' ) AS \"id\", `HoldingCollectionOrderEmail` AS \"name\", 'coea' AS \"type\", LEFT( `HoldingCollectionCode`, 6 ) AS \"parent\", `HoldingCollectionEmail` AS \"other\" FROM `SINGER_WAREHOUSE`.`COLLECTIONS`");
    sqllist.add("REPLACE( SELECT `TaxonCode`, ':', '_' ) AS \"id\", `ScientificName` AS \"name\", 'ta' AS \"type\", `Annex1` AS \"parent\", '' AS \"other\" FROM `SINGER_WAREHOUSE`.`TAXA` WHERE `TaxonCode` IN( SELECT DISTINCT `TaxonCode` FROM `SINGER_WAREHOUSE`.`ACCESSIONS` )");
    sqllist.add("SELECT DISTINCT LOWER( TRIM( `Genus` ) ) AS \"id\", TRIM( `Genus` ) AS \"name\", 'ge' AS \"type\", '' AS \"parent\", '' AS \"other\" FROM `SINGER_WAREHOUSE`.`TAXA` WHERE `TaxonCode` IN( SELECT DISTINCT `TaxonCode` FROM `SINGER_WAREHOUSE`.`ACCESSIONS` )");
    sqllist.add("SELECT DISTINCT IF( `Species` IS NOT NULL, LOWER( TRIM( `Species` ) ), '' ) AS \"id\", IF( `Species` IS NOT NULL, TRIM( `Species` ), '' ) AS \"name\", 'sp' AS \"type\", `Genus` AS \"parent\", '' AS \"other\" FROM `SINGER_WAREHOUSE`.`TAXA` WHERE `TaxonCode` IN( SELECT DISTINCT `TaxonCode` FROM `SINGER_WAREHOUSE`.`ACCESSIONS` )");
    sqllist.add("SELECT DISTINCT CONCAT( LOWER( TRIM( `Genus` ) ), '|', LOWER( TRIM( `Species` ) ) ) AS \"id\", CONCAT( TRIM( `Genus` ), ' ', TRIM( `Species` ) ) AS \"name\", 'gesp' AS \"type\", '' AS \"parent\", '' AS \"OTHER\" FROM `SINGER_WAREHOUSE`.`TAXA` WHERE( (`TaxonCode` IN( SELECT DISTINCT `TaxonCode` FROM `SINGER_WAREHOUSE`.`ACCESSIONS` WHERE `TaxonCode` IS NOT NULL )) AND (LENGTH( CONCAT( LOWER( TRIM( `Genus` ) ), '|', LOWER( TRIM( `Species` ) ) ) ) > 0) )");
    sqllist.add("SELECT `Code` AS \"id\", `Label` AS \"name\", 'st' AS \"type\", IF( `Parent` IS NOT NULL, `Parent`, '' ) AS \"parent\", '' AS \"other\" FROM `ANCILLARY`.`Code_BiologicalStatus` WHERE `Language` = 'eng'");
    sqllist.add("SELECT `Code` AS \"id\", `Label` AS \"name\", 'so' AS \"type\", IF( `Parent` IS NOT NULL, `Parent`, '' ) AS \"parent\", '' AS \"other\" FROM `ANCILLARY`.`Code_AcquisitionSource` WHERE `Language` = 'eng'");
    sqllist.add("SELECT DISTINCT IF( `InTrust` IS NOT NULL, IF( `InTrust` = 1, 'Y', 'N' ), '' ) AS \"id\", IF( `InTrust` = 1, 'In trust', IF( `InTrust` = 0, 'Not in trust', '' ) ) AS \"name\", 'tr' AS \"type\", \"\" AS \"parent\",\"\" AS \"other\" FROM `SINGER_WAREHOUSE`.`ACCESSIONS` WHERE `InTrust` IS NOT NULL");
    sqllist.add("SELECT `CooperatorType` AS \"id\", `ReferenceLabel` AS \"name\", \"us\" AS \"type\", '' AS \"parent\", '' AS \"other\" FROM `ANCILLARY`.`Code_Entity_Type` WHERE `CooperatorType` IS NOT NULL");
    sqllist.add("SELECT LOWER( `ANCILLARY`.`Code_ISO_3166`.`ISO3` ) AS \"id\", `ANCILLARY`.`Code_ISO_3166`.`ReferenceName` AS \"name\", \"cu\" AS \"type\", `ANCILLARY`.`Code_ISO_3166_Regions`.`ReferenceName` AS \"parent\", REPLACE( IF( `ANCILLARY`.`Code_ISO_3166`.`IncomeGroup` IS NOT NULL, `ANCILLARY`.`Code_ISO_3166_IncomeGroups`.`Name`, '' ), ':', '_' ) AS \"other\" FROM `ANCILLARY`.`Code_ISO_3166` LEFT JOIN `ANCILLARY`.`Code_ISO_3166_Regions` ON( `ANCILLARY`.`Code_ISO_3166_Regions`.`Code` = `ANCILLARY`.`Code_ISO_3166`.`Region` ) LEFT JOIN `ANCILLARY`.`Code_ISO_3166_IncomeGroups` ON( `ANCILLARY`.`Code_ISO_3166_IncomeGroups`.`Code` = `ANCILLARY`.`Code_ISO_3166`.`IncomeGroup` ) WHERE `ANCILLARY`.`Code_ISO_3166`.`ISO3` IS NOT NULL");
    sqllist.add("SELECT LOWER( `ANCILLARY`.`Code_ISO_3166_Regions`.`ReferenceName` ) AS \"id\", `ANCILLARY`.`Code_ISO_3166_Regions`.`ReferenceName` AS \"name\", \"re\" AS \"type\", IF( `ANCILLARY`.`Code_ISO_3166_Regions`.`Parent` IS NOT NULL, LOWER( `ParentRegion`.`ReferenceName` ), '' ) AS \"parent\", '' AS \"other\" FROM `ANCILLARY`.`Code_ISO_3166_Regions` LEFT JOIN `ANCILLARY`.`Code_ISO_3166_Regions` `ParentRegion` ON( `ParentRegion`.`Code` = `ANCILLARY`.`Code_ISO_3166_Regions`.`Parent` )");
    sqllist.add("SELECT REPLACE( LOWER( `ANCILLARY`.`Code_ISO_3166_IncomeGroups`.`Name` ), ':', '_' ) AS \"id\", `ANCILLARY`.`Code_ISO_3166_IncomeGroups`.`Name` AS \"name\", \"de\" AS \"type\", IF( `ANCILLARY`.`Code_ISO_3166_IncomeGroups`.`Parent` IS NOT NULL, LOWER( `ParentGroup`.`Name` ), '' ) AS \"parent\", '' AS \"other\" FROM `ANCILLARY`.`Code_ISO_3166_IncomeGroups` LEFT JOIN `ANCILLARY`.`Code_ISO_3166_IncomeGroups` `ParentGroup` ON( `ParentGroup`.`Code` = `ANCILLARY`.`Code_ISO_3166_IncomeGroups`.`Parent` )");
    sqllist.add("SELECT `id` as \"id\", `link` as \"name\", 'pi' as \"type\", CONCAT( LOWER( `genus` ), LOWER( `species` ) ) AS \"parent\", `remarks` as \"other\" FROM  `SINGER_WAREHOUSE`.`pics`");
    */
	}

	public void addKeyword(IndexWriter writer, String id, String name, String type, String parent, String other, boolean fullrefresh) throws Exception {

		String mainId = type + id;
		mainId = mangleKeywordValue(mainId);
		if (!fullrefresh) {
			delete(ID, mainId.toLowerCase(), writer);
		}
		LOG.info("Indexing Keyword:" + name + " type:" + type + " id:" + mainId + " parent:" + parent + " other:" + other);
		try {
			Document doc = new Document();
			doc.add(new Field("f", "a", org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			// System.out.println("Indexing :"+id);
			doc.add(new Field(ID, mainId.toLowerCase(), org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			doc.add(new Field(NAME, name, org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.ANALYZED));
			doc.add(new Field("type", type, org.apache.lucene.document.Field.Store.NO, org.apache.lucene.document.Field.Index.ANALYZED));
			doc.add(new Field(LOWERNAME, BaseIndexer.mangleKeywordValue(name.toLowerCase()), org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.ANALYZED));
			StringBuffer sb = new StringBuffer();
			sb.append(AccessionConstants.makeFormattedString(ID, mainId.toLowerCase()));
			sb.append(AccessionConstants.makeFormattedString(NAME, name));
			sb.append(AccessionConstants.makeFormattedString("code", (id == null ? "" : id.toLowerCase())));
			sb.append(AccessionConstants.makeFormattedString("parent", parent));
			String startswith = null;
            if(name != null && name.length() > 0) {
                startswith = name.toLowerCase().substring(0, 1);
            }
			String startswithValue = startswith;
			if (startswith != null) {
				try {
					Integer.parseInt(startswith);
					startswithValue = "0";
				} catch (Exception e) {
					startswithValue = startswith;
				}
			}
			sb.append(AccessionConstants.makeFormattedString("startswith", startswithValue));
			sb.append(AccessionConstants.makeFormattedString("type", type));
			sb.append(AccessionConstants.makeFormattedString("other", other));
			doc.add(new Field(AccessionConstants.CONTENTS, sb.toString(), org.apache.lucene.document.Field.Store.COMPRESS, org.apache.lucene.document.Field.Index.ANALYZED));
			writer.addDocument(doc, getBaseStopAnalyzer().getInstance("en"));
			doc = null;
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public void addKeyword(String id, String name, String type, String parent, String other, boolean fullrefresh) throws Exception {
		if (!isIndexExists()) {
			createDirIfNonExistant();
		}
		IndexWriter writer = null;
		try {
			writer = getIndexWriter(INDEX_DIR, false);
			writer.setUseCompoundFile(true);
			try {
				addKeyword(writer, id, name, type, parent, other, fullrefresh);
			} catch (Exception e) {
				LOG.error("",e);
			}
			count++;
			if (count % 20 == 0) {
				optimizeLucene(writer);
				// System.gc();
			}
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			if (writer != null) {
				writer.close();
				// setIndexingSemaphore(false, SEMAPHORE_NAME);
			}
		}
	}

	@Override
	public synchronized void generateIndex() {
		try {
			indexKeywordData();
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public String getIndexName() {
		return "Keyword Index";
	}

	public String getShortName() {
		return "Keyword(s)";
	}

	public void indexKeywordData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				LOG.info("Checking if any keywords needs indexing ..");
			} else {
				LOG.info("Indexing Keywords");
				indexKeywords(true);
			}
		}
	}

	public synchronized void indexKeywords(boolean fullrefresh) throws Exception {
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			if (fullrefresh) {
				recursivelyDeleteDirectory(getRootedFile(INDEX_DIR));
			}
			if (!isIndexExists()) {
				createDirIfNonExistant();
			}
			IndexWriter writer = null;
			try {
				int i = 0;
				writer = getIndexWriter(INDEX_DIR, false);
				writer.setUseCompoundFile(true);
				for (int j = 0; j < sqllist.size(); j++) {
					String sql = sqllist.get(j);
					LOG.info("SQL " + sql);

					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String id = rs.getString("id");
						String name = rs.getString("name");
						String type = rs.getString("type");
						String parent = rs.getString("parent");

                        if(name == null)
                            name = "";
						if ((parent == null) || (parent.trim().length() == 0)) {
							parent = "";
						}
						String other = rs.getString("other");
						if ((other == null) || (other.trim().length() == 0)) {
							other = "";
						}
						try {
							addKeyword(writer, id, name, type, parent, other, fullrefresh);
						} catch (Exception e) {
							LOG.error("",e);
						}
						i++;
						if (i % 100 == 0) {
							optimizeLucene(writer);
							// System.gc();
						}
					}
					optimizeLucene(writer);
				}
			} catch (Exception e) {
				LOG.error("",e);
			} finally {
				if (writer != null) {
					writer.close();
					// setIndexingSemaphore(false, SEMAPHORE_NAME);
				}
			}
			stmt.close();
		} catch (Exception se) {
			LOG.error("",se);
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
			optimizeLucene(INDEX_DIR);
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		}
	}

}
