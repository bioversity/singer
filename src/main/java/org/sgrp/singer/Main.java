package org.sgrp.singer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.sgrp.singer.indexer.AccessionIndex;
import org.sgrp.singer.indexer.AdditionalLinksIndex;
import org.sgrp.singer.indexer.CoopIndex;
import org.sgrp.singer.indexer.DistributionIndex;
import org.sgrp.singer.indexer.DonorIndex;
import org.sgrp.singer.indexer.InstituteIndex;
import org.sgrp.singer.indexer.KeywordIndex;
import org.sgrp.singer.indexer.MissionCollectionIndex;
import org.sgrp.singer.indexer.MissionCoopIndex;
import org.sgrp.singer.indexer.MissionDistributionIndex;
import org.sgrp.singer.indexer.MissionIndex;
import org.sgrp.singer.indexer.RecepientIndex;

public class Main {

	static
	{
		try
		{
			DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j_indexing.xml").getFile());
		}
		catch (NullPointerException ignored)
		{}
	}

	private static Logger LOG = Logger.getLogger(Main.class);

	/**
	 * This main allow the user to update the SINGER indexes
	 * without
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception{


		LOG.error("error test");
		LOG.warn("error warn");
		LOG.info("info test");
		LOG.debug("info debug");


		Map<String, String> options = OptionsAnalyzer.analyzeArgs(args);
		String indexes = options.get("indexes");
		String[] index;
		Iterator<String> ite = options.keySet().iterator();
		while(ite.hasNext())
		{
			String key = ite.next();
			if(!(key.equalsIgnoreCase("update") || key.equals("indexes")))
			{
				 PropertiesManager.setString( "ACC_SQL", PropertiesManager.getString("ACC_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "COOP_SQL", PropertiesManager.getString("COOP_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "DIST_SQL", PropertiesManager.getString("DIST_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "DONOR_SQL", PropertiesManager.getString("DONOR_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "INST_SQL", PropertiesManager.getString("INST_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "KEYWORDS_SQL", PropertiesManager.getString("KEYWORDS_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "MISSCOLL_SQL", PropertiesManager.getString("MISSCOLL_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "MISSCOOP_SQL", PropertiesManager.getString("MISSCOOP_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "MISSDIST_SQL", PropertiesManager.getString("MISSDIST_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "OLINKS_SQL", PropertiesManager.getString("OLINKS_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "REC_SQL", PropertiesManager.getString("REC_SQL").replaceAll("<<"+key+">>", options.get(key)));
				 PropertiesManager.setString( "MISS_SQL", PropertiesManager.getString("MISS_SQL").replaceAll("<<"+key+">>", options.get(key)));
			}
		}
		System.out.println(PropertiesManager.getString("ACC_SQL"));
		if(indexes !=null)
		{
			index = indexes.split("\\|");
		}
		else
		{
			index = new String[]{"acc"};
		}

		for(int i=0;i<index.length;i++)
		{
			if(index[i].equalsIgnoreCase("acc"))
			{
				options.put("sql", PropertiesManager.getString("ACC_SQL"));
				AccessionIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("coop"))
			{
				options.put("sql", PropertiesManager.getString("COOP_SQL"));
				CoopIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("dist"))
			{
				options.put("sql", PropertiesManager.getString("DIST_SQL"));
				DistributionIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("donor"))
			{
				options.put("sql", PropertiesManager.getString("DONOR_SQL"));
				DonorIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("inst"))
			{
				options.put("sql", PropertiesManager.getString("INST_SQL"));
				InstituteIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("misscoll"))
			{
				options.put("sql", PropertiesManager.getString("MISSCOLL_SQL"));
				MissionCollectionIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("misscoop"))
			{
				options.put("sql", PropertiesManager.getString("MISSCOOP_SQL"));
				MissionCoopIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("missdist"))
			{
				options.put("sql", PropertiesManager.getString("MISSDIST_SQL"));
				MissionDistributionIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("olinks"))
			{
				options.put("sql", PropertiesManager.getString("OLINKS_SQL"));
				AdditionalLinksIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("rec"))
			{
				options.put("sql", PropertiesManager.getString("REC_SQL"));
				RecepientIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("miss"))
			{
				options.put("sql", PropertiesManager.getString("MISS_SQL"));
				MissionIndex.indexFromMain(options);
			}
			else if(index[i].equalsIgnoreCase("keywords"))
			{
				String keywords_sql = PropertiesManager.getString("KEYWORDS_SQL");
				String[] sqllist = keywords_sql.split("\\|\\|");
				ArrayList<String> list = new ArrayList<String>();
				for(int j=0;j<sqllist.length;j++)
				{
					list.add(sqllist[j]);
				}

				KeywordIndex kindex = new KeywordIndex(list);
				kindex.indexKeywords(!(options.get("update") ==null? false : true));
			}
			else
			{
				//Not a valid Index
			}
		}

		//KeywordIndex kindex = new KeywordIndex();
		//System.out.println(kindex.getNamesAndIds("*", AccessionConstants.CONTENTS, kindex.LOWERNAME).size());

	}

}
