package com.topkc.chinesechess.chess;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.topkc.chinesechess.R;

public class ArchiveManagerActivity extends Activity {
	
	//����浵�ļ�����
	ArchiveContorller archiveContorller = new ArchiveContorller(ArchiveManagerActivity.this);
	
	//���ֻ�������ʾ�б�
	ListView list = null;
	
	//�����ListView����Ҫ��ʾ������
	ArrayList<HashMap<String, Object>> listItem = null;
	
	//ListView��������
	SimpleAdapter listItemAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.archives_display);

		// ��Layout�����ListView
		list = (ListView) findViewById(R.id.archives_display_ListView);

		// ���ɶ�̬���飬��������
		listItem = new ArrayList<HashMap<String, Object>>();
		
		//��ȡ��Ϸ�浵
		String[] fileNames = archiveContorller.getArchiveDir();
		for(int i = 0; i<fileNames.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("archives_display_ItemImage", R.drawable.icon);// ͼ����Դ��ID
			//map.put("archives_display_ItemText01", "�浵 " + (i+1));
			map.put("archives_display_ItemText01", "��Ϸ�浵 ");
			map.put("archives_display_ItemText02", fileNames[i].subSequence(0, fileNames[i].length()-4));
			listItem.add(map);
		}
		
		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		listItemAdapter = new SimpleAdapter(this, listItem,// ����Դ
				R.layout.archives_display_item,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { "archives_display_ItemImage",
						"archives_display_ItemText01",
						"archives_display_ItemText02" },
				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.id.archives_display_ItemImage,
						R.id.archives_display_ItemText01,
						R.id.archives_display_ItemText02 });

		// ��Ӳ�����ʾ
		list.setAdapter(listItemAdapter);

		
		// ��ӵ��
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//��ô浵����
				String fileName = (String)listItem.get(arg2).get("archives_display_ItemText02");
				fileName +=".txt";
				
				//������Ϸ������Ϣ
				boolean flag = archiveContorller.readArchiveFile(fileName);
				
				//����������Ϣ
				ChineseChess.resetChessmanPosition();
				
				//��ת����Ϸ����
				Intent intent = new Intent();
				intent.setClass(ArchiveManagerActivity.this, GameViewActivity.class);
				ArchiveManagerActivity.this.startActivity(intent);
				
				//setTitle("�����" + arg2 + "����Ŀ");
			}
		});

		// ��ӳ������
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("�浵ѡ��");
				menu.add(0, 0, 0, "ɾ��");
				menu.add(0, 1, 0, "ȡ��");
			}
		});
	}

	//�����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//setTitle("�����"+clickItem+"�����˵�����ĵ�" + item.getItemId() + "����Ŀ");
		
		//�������˵�
		switch (item.getItemId()) {
		
		case 0://ɾ����ǰ�浵
			//�ؼ����������� ,��ȡItem��ID
	        AdapterView.AdapterContextMenuInfo menuInfo;  
	        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();  
	        //���position  
			int selectedPosition = menuInfo.position;//��ȡ����˵ڼ���
			
			//ɾ���ļ�
			String fileName = (String)listItem.get(selectedPosition).get("archives_display_ItemText02");
			fileName +=".txt";
			boolean flag = archiveContorller.deleteArchiveFile(fileName);
			if(flag == true){
				//��ListView��ɾ��ѡ�е���Ŀ�����¸���
				listItem.remove(selectedPosition);
				listItemAdapter.notifyDataSetChanged();
				list.invalidate();
			}
			break;
		case 1://ȡ��
			
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
}