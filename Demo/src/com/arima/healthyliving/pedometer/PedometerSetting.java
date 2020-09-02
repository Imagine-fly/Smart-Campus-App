package com.arima.healthyliving.pedometer;

import java.util.ArrayList;
import java.util.HashMap;

import com.arima.healthyliving.R;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PedometerSetting extends Activity{
	
	private ListView settingView = null;
	private ArrayList<HashMap<String, Object>> mSettingItems = new ArrayList<HashMap<String, Object>>();
	private int SERVICE_DIAL_NUMBER_ITEM = 0;
	private int OPTIONS_SETTING = 1;
	private Context mContext;
	private LinearLayout settingItemView;
	private SettingListAdapter settingListAdapter = null;
	private String mStepWidthSetting;
	private String mStepWidthSettingSub;
	private String mWeightSetting;
	private String mWeightSettingSub;
	private String mSportModeSetting;
	private String mSportModeSettingSub;
	private String mScreenAlwaysOn;
	private String mScreenAlwaysOnSub;
	
	private int mStepWidth = 85;
	private int mWeight = 70;
	private int mMode = 0;
	private int mScreenOn = 0;
	private final int RETURN_SETTING_RESULT = 1;
	public static String mWidthKey = "width";
	public static String mModeKey = "mode";
	public static String mWeightKey = "weight";
	public static String mScreenOnKey = "screenon";
	
	private String [] mPedometerSportMode = new String[2];
	private String [] mPedometerScreenOn = new String[2];

	private static final String[] ADAPTER_FROM = {
        "name",
        "subName",
	};
	private static final int[] ADAPTER_TO = {
		R.id.settingItemName,
		R.id.settingItemSubName,
	};
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null){
        	mStepWidth = intent.getIntExtra(mWidthKey, 70);
        	mWeight = intent.getIntExtra(mWeightKey, 75);
        	mMode = intent.getIntExtra(mModeKey, 0);
        	mScreenOn = intent.getIntExtra(mScreenOnKey, 0);
        }
        initResource();
        prepareSettingsList();
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void initResource() {
		// TODO Auto-generated method stub
		if(settingView == null)
			settingView = (ListView)findViewById(R.id.SettingListItem);
		settingView.setOnItemClickListener(onSettingItemClicked);
		
		mStepWidthSetting = getString(R.string.pedometer_setting_step_width);
		mStepWidthSettingSub = getString(R.string.pedometer_setting_step_width_sub);
		mWeightSetting = getString(R.string.pedometer_setting_weight);
		mWeightSettingSub = getString(R.string.pedometer_setting_weight_sub);
		mSportModeSetting = getString(R.string.pedometer_sport_mode);
		mSportModeSettingSub = getString(R.string.pedometer_setting_sprot_mode_sub);
		mScreenAlwaysOn = getString(R.string.pedometer_setting_light_alway_on);
		mScreenAlwaysOnSub = getString(R.string.pedometer_setting_light_alway_on_sub);
		
		mPedometerSportMode[0] = getResources().getString(R.string.pedometer_sport_mode_walk);
		mPedometerSportMode[1] = getResources().getString(R.string.pedometer_sport_mode_run);

		mPedometerScreenOn[0] = getResources().getString(R.string.pedometer_run_in_background);
		mPedometerScreenOn[1] = getResources().getString(R.string.pedometer_keep_screen_on);
	}

	private void prepareSettingsList() {
		// TODO Auto-generated method stub
		HashMap<String, Object> stepWidth = new HashMap<String, Object>();
		stepWidth.put(ADAPTER_FROM[0], mStepWidthSetting);
		stepWidth.put(ADAPTER_FROM[1], mStepWidthSettingSub);
		mSettingItems.add(stepWidth);
	
		HashMap<String, Object> weightSetting = new HashMap<String, Object>();
		weightSetting.put(ADAPTER_FROM[0], mWeightSetting);
		weightSetting.put(ADAPTER_FROM[1], mWeightSettingSub);
		mSettingItems.add(weightSetting);
		
		HashMap<String, Object> sportMode = new HashMap<String, Object>();
		sportMode.put(ADAPTER_FROM[0], mSportModeSetting);
		sportMode.put(ADAPTER_FROM[1], mSportModeSettingSub);
		mSettingItems.add(sportMode);
		
		HashMap<String, Object> screenOn = new HashMap<String, Object>();
		screenOn.put(ADAPTER_FROM[0], mScreenAlwaysOn);
		screenOn.put(ADAPTER_FROM[1], mScreenAlwaysOnSub);
		mSettingItems.add(screenOn);
		//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
		settingListAdapter = new SettingListAdapter(this, mSettingItems, R.layout.setting_list_item, null);
		//>2013/02/26-34259-HenryPeng
		settingView.setAdapter(settingListAdapter);
	}
	
	private OnItemClickListener onSettingItemClicked = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			int pos = (Integer) view.getTag();
			switch (pos){
				case 0:
					showWeightWidthDialog(true);
					break;
				case 1:
					showWeightWidthDialog(false);
					break;
				case 2:
					showSportModeDialog();
					break;
				case 3:
					showScreenOnDialog();
					break;
				default:
					break;
			}
		}
	};
	
	private void showScreenOnDialog() {
		// TODO Auto-generated method stub
		Builder builder=new android.app.AlertDialog.Builder(this);
		String buttonOK = getResources().getString(R.string.pedometer_OK);
        builder.setTitle(mScreenAlwaysOn);
        
        builder.setSingleChoiceItems(mPedometerScreenOn, mScreenOn, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mScreenOn = which;
			}
		});
        builder.setPositiveButton(buttonOK, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            	//mMode = which;
            }
        });
        builder.create().show();
	}
	
	private void showSportModeDialog(){
		Builder builder=new android.app.AlertDialog.Builder(this);
		String buttonOK = getResources().getString(R.string.pedometer_OK);
        builder.setTitle(mSportModeSetting);
        
        builder.setSingleChoiceItems(mPedometerSportMode, mMode, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mMode = which;
			}
		});
        builder.setPositiveButton(buttonOK, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            	//mMode = which;
            }
        });
        builder.create().show();
	}
	
	private void showWeightWidthDialog(final boolean width){
		int defaultValue = 0;
		
		String dialogTitle;
		String buttonOK = getResources().getString(R.string.pedometer_OK);
		String buttonCancel = getResources().getString(R.string.pedometer_Cancel);

		if(width){
			dialogTitle = mStepWidthSetting;
			defaultValue = mStepWidth;
		}else{
			dialogTitle = mWeightSetting;
			defaultValue = mWeight;
		}
		
		final EditText editText = new EditText(mContext);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		InputFilter[] arrayOfInputFilter = new InputFilter[1];
		arrayOfInputFilter[0] = new InputFilter.LengthFilter(3);
		editText.setFilters(arrayOfInputFilter);
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setView(editText);
		localBuilder.setPositiveButton(buttonOK, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (width){
					Editable vaule = editText.getText();
					if (vaule != null){
						int width = Integer.parseInt(editText.getText().toString());
						if (width != mStepWidth)
							mStepWidth = width;
					}
				}else {
					Editable vaule = editText.getText();
					if (vaule != null){
					int weight = Integer.parseInt(editText.getText().toString());
					if (weight != mWeight)
						mWeight = weight;
					}
				}
			}
		});
		localBuilder.setNegativeButton(buttonCancel, null);
		
		localBuilder.setTitle(dialogTitle);
		editText.setText(String.valueOf(defaultValue));
		editText.setSelection(String.valueOf(defaultValue).length());
		localBuilder.setCancelable(false);
		localBuilder.create();
		localBuilder.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		launchServiceDialNumManagerActivity();
		finish();
	}
	
	public void launchServiceDialNumManagerActivity(){
		Intent intent = new Intent();
		intent.putExtra(mWidthKey, mStepWidth);
		intent.putExtra(mWeightKey, mWeight);
		intent.putExtra(mModeKey, mMode);
		intent.putExtra(mScreenOnKey, mScreenOn);
		setResult(RETURN_SETTING_RESULT, intent);
	}
}