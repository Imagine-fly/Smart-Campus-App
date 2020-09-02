package com.arima.healthyliving.bycicleriding;

import java.util.Date;
import java.util.List;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

//<2014/02/21-Task_34127-xiangrongji, [AAP1302][BycicleRiding]upload codes for Bycicle Riding screens
public class BycridHistoryActivity extends Activity implements OnItemClickListener {

	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	private static final int LOADER_ID_BYCRID_HISTORY_LIST = 1;
	
	private ListView mListView;
	private LayoutInflater mInflater;
	private LoaderManager.LoaderCallbacks<List<BycridData>> mHistoryListLoaderListener =
			new LoaderManager.LoaderCallbacks<List<BycridData>>() {

		@Override
		public Loader<List<BycridData>> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			return new BycridHistoryLoader(getApplicationContext());
		}

		@Override
		public void onLoadFinished(Loader<List<BycridData>> loader,
				List<BycridData> data) {
			// TODO Auto-generated method stub
			mListView.setAdapter(new HistoryListAdapter(data));
			getLoaderManager().destroyLoader(LOADER_ID_BYCRID_HISTORY_LIST);
		}

		@Override
		public void onLoaderReset(Loader<List<BycridData>> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	//>2014/03/03-Task_34403-xiangrongji
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bycrid_history);
		
		mInflater = getLayoutInflater();
		
		mListView = (ListView) findViewById(R.id.bycrid_hstr_list);
		
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		getLoaderManager().initLoader(LOADER_ID_BYCRID_HISTORY_LIST, null, mHistoryListLoaderListener);
		//>2014/03/03-Task_34403-xiangrongji
		mListView.setOnItemClickListener(this);
	}
	
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	@Override
	protected void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(LOADER_ID_BYCRID_HISTORY_LIST, null, mHistoryListLoaderListener);
	}
	//>2014/03/06-Task_34540-xiangrongji

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		HistoryItemViewHolder holder = (HistoryItemViewHolder) view.getTag();
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		String record_name = holder.record_name;
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), BycridViewerActivity.class);
		intent.putExtra("record_name", record_name);
		//>2014/03/06-Task_34540-xiangrongji
		//>2014/03/03-Task_34403-xiangrongji
		startActivity(intent);
	}
	
	public class HistoryListAdapter extends BaseAdapter {

		private List<BycridData> mList;
		public HistoryListAdapter(List<BycridData> list) {
			mList = list;
		}
		@Override
		public int getCount() {
			if (mList == null) {
				return 0;
			} else {
				return mList.size();
			}
		}

		@Override
		public BycridData getItem(int position) {
			//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
			if (mList == null) {
				return null;
			} else if (position >= mList.size()) {
				return null;
			} else {
				//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
				//return mList.get(mList.size() - 1 - position);
				return mList.get(position);
				//>2014/03/08-Task_34590-xiangrongji
			}
			//>2014/03/06-Task_34540-xiangrongji
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			HistoryItemViewHolder holder;
			
			if (convertView == null) {
				view = mInflater.inflate(R.layout.bycrid_hstr_list_item, parent, false);
				holder = null;
			} else {
				view = convertView;
				holder = (HistoryItemViewHolder) view.getTag();
			}
			
			if (holder == null) {
				holder = new HistoryItemViewHolder();
				holder.mDateTextView = (TextView) view.findViewById(R.id.bycrid_hstr_item_date);
				holder.mPhotoIcon = (ViewGroup) view.findViewById(R.id.bycrid_hstr_item_photo_icon);
				holder.mPhotoNumTextView = (TextView) holder.mPhotoIcon.findViewById(R.id.bycrid_hstr_item_photo_num);
				holder.mDistanceTextView = (TextView) view.findViewById(R.id.bycrid_hstr_item_distance_val);
				holder.mDurationTextView = (TextView) view.findViewById(R.id.bycrid_hstr_item_duration_val);
				holder.mAvgSpeedTextView = (TextView) view.findViewById(R.id.bycrid_hstr_item_avgspd_val);
			}
			
			BycridData item = getItem(position);
			if (item != null) {
				//path
				//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
				holder.record_name = item.record_name;
				//>2014/03/06-Task_34540-xiangrongji
				//date
				Date date = new Date(item.date);
				String dateStr = (String) DateFormat.format("yyyy-MM-dd", item.date);
				holder.mDateTextView.setText(dateStr);
				//picture icon
				if (item.picture_num > 0) {
					holder.mPhotoNumTextView.setText(String.valueOf(item.picture_num));
					//holder.mPhotoIcon.setAlpha(1.0f);
				} else {
					holder.mPhotoNumTextView.setText("0");
					//holder.mPhotoIcon.setAlpha(0.5f);
				}
				//distance
				float dst = (float)item.distance / 1000;
				String strDst = String.format("%.2f", dst);
				holder.mDistanceTextView.setText(strDst);
				//duration
				long sec = item.duration / 1000;
				long min = sec / 60;
				long hour = min / 60;
				sec -= min * 60;
				min -= hour * 60;
				//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
				String strDuration = String.format("%d:%02d:%02d", hour, min, sec); //(String) DateFormat.format("kk:mm:ss", item.duration);
				//>2014/03/06-Task_34540-xiangrongji
				holder.mDurationTextView.setText(strDuration);
				//average speed
				//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
				float speed = 0;
				if (item.duration != 0) {
					speed = (float)item.distance * 3600 / (float)item.duration;
				}
				//>2014/03/08-Task_34590-xiangrongji
				String strAvgSpeed = String.format("%.1f", speed);
				holder.mAvgSpeedTextView.setText(strAvgSpeed);
			}
			
			view.setTag(holder);
			
			return view;
		}
	}
	public class HistoryItemViewHolder {
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		String record_name;
		//>2014/03/06-Task_34540-xiangrongji
		TextView mDateTextView;
		ViewGroup mPhotoIcon;
		TextView mPhotoNumTextView;
		TextView mDistanceTextView;
		TextView mDurationTextView;
		TextView mAvgSpeedTextView;
	}
	//>2014/03/03-Task_34403-xiangrongji
}
//>2014/02/21-Task_34127-xiangrongji
