package org.sgrp.singer;

import java.util.ArrayList;
import java.util.List;

public class StatusPage {

	protected static StatusPage	mgr	= null;

	public static StatusPage getInstance() {
		if (mgr == null) {
			mgr = new StatusPage();
		}
		return mgr;
	}

	public List<Object>	indexClasses	= null;

	public StatusPage() {
		indexClasses = new ArrayList<Object>();
		indexClasses.add(org.sgrp.singer.indexer.AccessionIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.DistributionIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.KeywordIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.CoopIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.InstituteIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.DonorIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.RecepientIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.MissionCoopIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.MissionIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.AdditionalLinksIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.AscDataIndex.getInstance());
		indexClasses.add(org.sgrp.singer.indexer.EnvDataIndex.getInstance());

	}

	public List<Object> getIndexClass() {
		return indexClasses;
	}
}
