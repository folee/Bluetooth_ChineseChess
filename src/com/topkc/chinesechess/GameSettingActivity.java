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
 * �̳�PreferenceActivity��
 * ��ʵ��OnPreferenceChangeListener��OnPreferenceClickListener�����ӿ�
 * 
 * @author wuwei
 * 
 */
public class GameSettingActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	String soundToggleKey = "sound_toggle";
	String activateGameKey = "activate_game";
	String gamelevelSettingKey = "gamelevel_setting";
	String complaintsProposalsKey = "complaints_proposals";// �������
	//String purchaseGameKey = "purchase_game";// ������Ϸ
	String aboutGameKey = "about_game";// ������Ϸ

	CheckBoxPreference soundTogglePref;
	CheckBoxPreference activateGamePref;
	ListPreference gamelevelSettingPref;
	Preference complaintsProposalsPref;
	//Preference purchaseGamePref;
	Preference aboutGamePref;

	SharedPreferences settings ;//Preferences �洢
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.game_setting_preferences);

		soundTogglePref = (CheckBoxPreference) findPreference(soundToggleKey);
		activateGamePref = (CheckBoxPreference) findPreference(activateGameKey);
		gamelevelSettingPref = (ListPreference) findPreference(gamelevelSettingKey);
		complaintsProposalsPref = (Preference) findPreference(complaintsProposalsKey);
		//purchaseGamePref = (Preference) findPreference(purchaseGameKey);// ������Ϸ
		aboutGamePref = (Preference) findPreference(aboutGameKey);

		// Ϊ����Preferenceע������ӿ�
		soundTogglePref.setOnPreferenceChangeListener(this);
		soundTogglePref.setOnPreferenceClickListener(this);
		activateGamePref.setOnPreferenceChangeListener(this);
		activateGamePref.setOnPreferenceClickListener(this);
		gamelevelSettingPref.setOnPreferenceChangeListener(this);
		gamelevelSettingPref.setOnPreferenceClickListener(this);
		complaintsProposalsPref.setOnPreferenceChangeListener(this);
		complaintsProposalsPref.setOnPreferenceClickListener(this);
		//purchaseGamePref.setOnPreferenceChangeListener(this);// ������Ϸ
		//purchaseGamePref.setOnPreferenceClickListener(this);// ������Ϸ
		aboutGamePref.setOnPreferenceChangeListener(this);
		aboutGamePref.setOnPreferenceClickListener(this);
		
		
		//��ʼ��SharePreferences
		//settings = getSharedPreferences(ChineseChess.Preferences_STORE_Settings, MODE_PRIVATE);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		// TODO Auto-generated method stub
		// �������false��ʾ�������ı�
		
		if (arg0.getKey().equals(soundToggleKey)) {
			Toast.makeText(this,"������" + !settings.getBoolean(soundToggleKey, false), Toast.LENGTH_SHORT).show();
			//Log.v("SystemSetting", "checkbox preference is changed");
		} 
		
		if (arg0.getKey().equals(gamelevelSettingKey)) {
			Toast.makeText(this,"�ȼ���"+ settings.getString(gamelevelSettingKey, "000"), Toast.LENGTH_SHORT).show();
		} 
		
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		//Ͷ���뽨��
		if (arg0.getKey().equals(complaintsProposalsKey)) {
			
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("application/octet-stream"); 
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_author_email)});
			intent.putExtra(android.content.Intent.EXTRA_TEXT, " ");//�����ʼ���������
			
			//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//���ø���
			startActivity(intent);
			
			
			/*
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
			//�����ı���ʽ  
			emailIntent.setType("plain/text"); 
			//���öԷ��ʼ���ַ  
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{getString(R.string.contact_author_email)});  
			//���ñ�������  
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getString(R.string.contact_author_email));  
			//�����ʼ��ı�����  
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,getString(R.string.contact_author_email));  
			startActivity(Intent.createChooser(emailIntent,"Choose Email Client"));
			*/
 
		}
		
		return true;
	}

}
