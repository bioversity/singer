package org.sgrp.singer.indexer;

import java.util.List;

public interface IndexerInterface {
	public List<IndexData> getIndexList();

	public String getIndexName();

	public String getShortName();
}
