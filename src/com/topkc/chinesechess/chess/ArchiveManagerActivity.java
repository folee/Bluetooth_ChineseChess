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
	
	//管理存档文件的类
	ArchiveContorller archiveContorller = new ArchiveContorller(ArchiveManagerActivity.this);
	
	//在手机界面显示列表
	ListView list = null;
	
	//存放在ListView中像要显示的内容
	ArrayList<HashMap<String, Object>> listItem = null;
	
	//ListView的适配器
	SimpleAdapter listItemAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.archives_display);

		// 绑定Layout里面的ListView
		list = (ListView) findViewById(R.id.archives_display_ListView);

		// 生成动态数组，加入数据
		listItem = new ArrayList<HashMap<String, Object>>();
		
		//读取游戏存档
		String[] fileNames = archiveContorller.getArchiveDir();
		for(int i = 0; i<fileNames.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("archives_display_ItemImage", R.drawable.icon);// 图像资源的ID
			//map.put("archives_display_ItemText01", "存档 " + (i+1));
			map.put("archives_display_ItemText01", "游戏存档 ");
			map.put("archives_display_ItemText02", fileNames[i].subSequence(0, fileNames[i].length()-4));
			listItem.add(map);
		}
		
		// 生成适配器的Item和动态数组对应的元素
		listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.archives_display_item,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "archives_display_ItemImage",
						"archives_display_ItemText01",
						"archives_display_ItemText02" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.archives_display_ItemImage,
						R.id.archives_display_ItemText01,
						R.id.archives_display_ItemText02 });

		// 添加并且显示
		list.setAdapter(listItemAdapter);

		
		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//获得存档名字
				String fileName = (String)listItem.get(arg2).get("archives_display_ItemText02");
				fileName +=".txt";
				
				//加载游戏布局信息
				boolean flag = archiveContorller.readArchiveFile(fileName);
				
				//清除掉标记信息
				ChineseChess.resetChessmanPosition();
				
				//跳转到游戏界面
				Intent intent = new Intent();
				intent.setClass(ArchiveManagerActivity.this, GameViewActivity.class);
				ArchiveManagerActivity.this.startActivity(intent);
				
				//setTitle("点击第" + arg2 + "个项目");
			}
		});

		// 添加长按点击
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("存档选项");
				menu.add(0, 0, 0, "删除");
				menu.add(0, 1, 0, "取消");
			}
		});
	}

	//长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//setTitle("点击了"+clickItem+"长按菜单里面的第" + item.getItemId() + "个项目");
		
		//处理长按菜单
		switch (item.getItemId()) {
		
		case 0://删除当前存档
			//关键代码在这里 ,获取Item的ID
	        AdapterView.AdapterContextMenuInfo menuInfo;  
	        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();  
	        //输出position  
			int selectedPosition = menuInfo.position;//获取点击了第几行
			
			//删除文件
			String fileName = (String)listItem.get(selectedPosition).get("archives_display_ItemText02");
			fileName +=".txt";
			boolean flag = archiveContorller.deleteArchiveFile(fileName);
			if(flag == true){
				//在ListView中删除选中的项目，重新更新
				listItem.remove(selectedPosition);
				listItemAdapter.notifyDataSetChanged();
				list.invalidate();
			}
			break;
		case 1://取消
			
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
}