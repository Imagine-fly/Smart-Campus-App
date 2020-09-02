package com.arima.healthyliving.bycicleriding;

import java.io.FileNotFoundException;
import android.content.AsyncTaskLoader;
import android.content.Context;

public class BycridLoader extends AsyncTaskLoader<BycridData>{
	
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private String mRecordName;

	public BycridLoader(Context context, String recordName) {
		super(context);
		mRecordName = recordName;
	}
	//>2014/03/06-Task_34540-xiangrongji

	@Override
	public BycridData loadInBackground() {
		BycridData data = null;
		try {
			//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
			data =  BycridDataHelper.getInstance().load(mRecordName, false);
			//>2014/03/06-Task_34540-xiangrongji
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
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
