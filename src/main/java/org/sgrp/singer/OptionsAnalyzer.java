package org.sgrp.singer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OptionsAnalyzer {
	
	public static Map<String, String> analyzeArgs(String[] args)
	{
		HashMap<String, String> options = new HashMap<String, String>();
		
		for(int i=0;i<args.length;i++)
		{
			if(args[i].equalsIgnoreCase("-properties"))
			{
				AccessionServlet.loadInitData(args[++i]);
			}
			else if(args[i].equalsIgnoreCase("-u"))
			{
				options.put("update", "true");
			}
			else if(args[i].equalsIgnoreCase("-i"))
			{
				options.put("indexes", args[++i]);
			}
			else if(args[i].startsWith("-"))
			{	
				options.put(args[i].substring(1), args[++i]);
			}
		}
		
		return options;
	}

}
