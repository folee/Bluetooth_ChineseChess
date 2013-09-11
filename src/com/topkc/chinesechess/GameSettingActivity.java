package com.topkc.chinesechess;

import com.topkc.chinesechess.chess.ChineseChess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * 继承PreferenceActivity，
 * 并实现OnPreferenceChangeListener和OnPreferenceClickListener监听接口
 * 
 * @author wuwei
 * 
 */
public class GameSettingActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	String soundToggleKey = "sound_toggle";
	String activateGameKey = "activate_game";
	String gamelevelSettingKey = "gamelevel_setting";
	String complaintsProposalsKey = "complaints_proposals";// 反馈意见
	//String purchaseGameKey = "purchase_game";// 购买游戏
	String aboutGameKey = "about_game";// 关于游戏

	CheckBoxPreference soundTogglePref;
	CheckBoxPreference activateGamePref;
	ListPreference gamelevelSettingPref;
	Preference complaintsProposalsPref;
	//Preference purchaseGamePref;
	Preference aboutGamePref;

	SharedPreferences settings ;//Preferences 存储
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.game_setting_preferences);

		soundTogglePref = (CheckBoxPreference) findPreference(soundToggleKey);
		activateGamePref = (CheckBoxPreference) findPreference(activateGameKey);
		gamelevelSettingPref = (ListPreference) findPreference(gamelevelSettingKey);
		complaintsProposalsPref = (Preference) findPreference(complaintsProposalsKey);
		//purchaseGamePref = (Preference) findPreference(purchaseGameKey);// 购买游戏
		aboutGamePref = (Preference) findPreference(aboutGameKey);

		// 为各个Preference注册监听接口
		soundTogglePref.setOnPreferenceChangeListener(this);
		soundTogglePref.setOnPreferenceClickListener(this);
		activateGamePref.setOnPreferenceChangeListener(this);
		activateGamePref.setOnPreferenceClickListener(this);
		gamelevelSettingPref.setOnPreferenceChangeListener(this);
		gamelevelSettingPref.setOnPreferenceClickListener(this);
		complaintsProposalsPref.setOnPreferenceChangeListener(this);
		complaintsProposalsPref.setOnPreferenceClickListener(this);
		//purchaseGamePref.setOnPreferenceChangeListener(this);// 购买游戏
		//purchaseGamePref.setOnPreferenceClickListener(this);// 购买游戏
		aboutGamePref.setOnPreferenceChangeListener(this);
		aboutGamePref.setOnPreferenceClickListener(this);
		
		
		//初始化SharePreferences
		//settings = getSharedPreferences(ChineseChess.Preferences_STORE_Settings, MODE_PRIVATE);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		// TODO Auto-generated method stub
		// 如果返回false表示不允许被改变
		
		if (arg0.getKey().equals(soundToggleKey)) {
			Toast.makeText(this,"声音：" + !settings.getBoolean(soundToggleKey, false), Toast.LENGTH_SHORT).show();
			//Log.v("SystemSetting", "checkbox preference is changed");
		} 
		
		if (arg0.getKey().equals(gamelevelSettingKey)) {
			Toast.makeText(this,"等级："+ settings.getString(gamelevelSettingKey, "000"), Toast.LENGTH_SHORT).show();
		} 
		
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		//投诉与建议
		if (arg0.getKey().equals(complaintsProposalsKey)) {
			
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("application/octet-stream"); 
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_author_email)});
			intent.putExtra(android.content.Intent.EXTRA_TEXT, " ");//设置邮件发送内容
			
			//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//设置附件
			startActivity(intent);
			
			
			/*
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
			//设置文本格式  
			emailIntent.setType("plain/text"); 
			//设置对方邮件地址  
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{getString(R.string.contact_author_email)});  
			//设置标题内容  
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getString(R.string.contact_author_email));  
			//设置邮件文本内容  
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,getString(R.string.contact_author_email));  
			startActivity(Intent.createChooser(emailIntent,"Choose Email Client"));
			*/
 
		}
		
		return true;
	}

}
