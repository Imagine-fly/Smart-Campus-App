package com.arima.healthyliving.bycicleriding;

import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class BycridHistoryLoader extends AsyncTaskLoader<List<BycridData>> {

	public BycridHistoryLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<BycridData> loadInBackground() {
		return BycridDataHelper.getInstance().loadHistoryHeaderList();
	}
	
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
    }

}
