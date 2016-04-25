package com.hanaone.tprd.adapter;

import com.hanaone.tprd.db.LevelDataSet;

public interface ListLevelListener {
	public void onSelect(int examLevelId, String examLevelName);
	public void onSelect(LevelDataSet level, DownloadInfo info);
}
