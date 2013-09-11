package com.topkc.chinesechess.chess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.topkc.chinesechess.R;
import com.topkc.chinesechess.util.TabMenu;

/**
 * GameViewActivity
 * @author wuwei
 *
 */
public class GameViewActivity extends Activity {
	
	//TabMenu 定义
	TabMenu.MenuBodyAdapter[] bodyAdapter = new TabMenu.MenuBodyAdapter[2];// []表示有多少栏选项
	TabMenu.MenuTitleAdapter titleAdapter;
	TabMenu tabMenu;
	int selTitle = 0;

	
	GameView gameView = null;
	LinearLayout gameViewLayout = null;
	
	ImageView gameStatus = null;
	
	//游戏存档控制器
	ArchiveContorller archiveContorller = new ArchiveContorller(GameViewActivity.this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 设置游戏全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.chess_activity);
		
		//初始化游戏界面的布局文件
		gameViewLayout = (LinearLayout) findViewById(R.id.GameViewLayout);
		
//		//平铺背景图片
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_background);
//		BitmapDrawable bd = new BitmapDrawable(bitmap);
//		bd.setTileModeXY(TileMode.CLAMP , TileMode.CLAMP );
//		bd.setDither(true);
//		gameViewLayout.setBackgroundDrawable(bd);
		
		//将棋盘初始化
		gameView = new GameView(this, this);
		gameView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 686));//693
		gameViewLayout.addView(gameView);
		
		//加入ImageButton NewGame 和 Back 按钮
		LinearLayout buttonLinearLayout = new LinearLayout(this);//按钮那部分的横向布局
		RelativeLayout.LayoutParams imageButtonLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		buttonLinearLayout.setPadding(5, 5, 5, 8);
		//NewGame按钮
		ImageButton imageButton01 = new ImageButton(this);
		imageButton01.setBackgroundResource(R.drawable.image_button_newgame);
		buttonLinearLayout.addView(imageButton01, imageButtonLayoutParams);
		//GameStatus
		gameStatus = new ImageView(this);
		Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
		gameStatus.setId(123456);
		gameStatus.setImageBitmap(gameStatusBitmap);
		//gameStatus.setBackgroundColor(Color.BLUE);
		buttonLinearLayout.addView(gameStatus, imageButtonLayoutParams);
		//Back按钮
		ImageButton imageButton02 = new ImageButton(this);
		imageButton02.setBackgroundResource(R.drawable.image_button_back);
		buttonLinearLayout.addView(imageButton02, imageButtonLayoutParams);
		
		gameViewLayout.addView(buttonLinearLayout);
		
		//新游戏按钮监听器
		imageButton01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(3);
				Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
				gameStatus.setImageBitmap(gameStatusBitmap);
			}
		});
		//悔棋按钮监听器;2011-9-1 19:59:16
		imageButton02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
				//gameStatus.setImageBitmap(gameStatusBitmap);
				//System.out.println("hhhhhhhhhhhhhh");
				BackMoveController.backMove(ChineseChess.chessboard);
				gameView.refreshGameView();
			}
		});
		
		
		
		// 初始化AdView,下面两行只用于测试,完成后一定要去掉,参考文挡说明
		//AdViewManager.setConfigExpireTimeout(-1); // 保证每次都从服务器取配置
		//AdViewTargeting.setTestMode(true); // 保证所有选中的广告公司都为测试状态
		//AdViewLayout adViewLayout = new AdViewLayout(this,"SDK20111812070129bb9oj4n571faaka");
		/*
		AdViewLayout adViewLayout = new AdViewLayout(this,"SDK20110217070205c1c75kge9uexp64");
		RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		gameViewLayout.addView(adViewLayout, adViewLayoutParams);
		*/
		gameViewLayout.invalidate();
		
        //设定弹出TabMenu的标题和内容
        this.MenuContentSetting();
		
	}
	
    //用来利用消息机制用来 控制status图片,以及棋权
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 1){
				//黑棋下完切换到红棋下棋
				ChineseChess.isRedTurn = true;
				ChineseChess.isBlackTurn = false;
				System.out.println("handleMessage1:Red Chessman turn!");
				Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
				gameStatus.setImageBitmap(gameStatusBitmap);
			}
			if(msg.what == 2){
				//红棋下完切换到黑棋下棋
				ChineseChess.isRedTurn = false;
				ChineseChess.isBlackTurn = true;
				
				System.out.println("handleMessage1:Black Chessman turn!");
				Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
				gameStatus.setImageBitmap(gameStatusBitmap);
			}
			
			if(msg.what == 3){
				System.out.println("handleMessage3：New Game!");
				reload();
			}
		}
    	
    };
    
    
    /**
     * 重新加载游戏（重新加载当前Activity）
     */
	public void reload() { 
		//初始化棋盘
		ChineseChess.resetChessboard();
		//清除悔棋List数据
		BackMoveController.clearBackMoveList();
		
		/*
		Intent intent = getIntent(); 
		overridePendingTransition(0, 0); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); finish(); 
		overridePendingTransition(0, 0); 
		startActivity(intent); 
		*/
		gameView.refreshGameView();
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//监听手机后退按键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
			exitDialog();
            return false; 
        } 
		return super.onKeyDown(keyCode, event);
	}
	
	/**
     * 在游戏界面点击后退按钮时，提示退出框
     */
    protected void exitDialog() { 
        AlertDialog.Builder builder = new Builder(GameViewActivity.this); 
        builder.setMessage("确定要退出吗?"); 
        builder.setTitle("提示"); 
        
        builder.setPositiveButton("返回主菜单", 
                new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						/*
						Intent intent = new Intent();
						intent.setClass(GameViewActivity.this, ChineseChessActivity.class);
						GameViewActivity.this.startActivity(intent);
						*/
					}
				});
		/*
        builder.setNeutralButton("确定", 
                new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		*/
        builder.setNegativeButton("继续游戏", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 
    
    
    
	/**
	 * TabMenu标题和内容属性的设定
	 */
	public void MenuContentSetting(){
		// 设置分页栏的标题
		titleAdapter = new TabMenu.MenuTitleAdapter(this, new String[] { "常用",
				"工具" }, 16, 0xFF222222, Color.LTGRAY, Color.WHITE);		
		
		// 定义每项分页栏的内容
		bodyAdapter[0] = new TabMenu.MenuBodyAdapter(this, new String[] { "保存游戏",
				"加载游戏", "游戏难度", "退出游戏" }, new int[] { R.drawable.menu_downmanager,
				R.drawable.menu_filemanager, R.drawable.menu_refresh,
				R.drawable.menu_quit }, 11, 0xFFFFFFFF);
		bodyAdapter[1] = new TabMenu.MenuBodyAdapter(this, new String[] { "投诉建议",
				"购买游戏", "关于游戏" }, new int[] { R.drawable.menu_inputurl,
				R.drawable.menu_bookmark, R.drawable.menu_search }, 11,
				0xFFFFFFFF);
		tabMenu = new TabMenu(this, new TitleClickEvent(),
				new BodyClickEvent(), titleAdapter,
				// 0x55123456,//TabMenu的背景颜色
				0xDD000000, R.style.PopupAnimation);// 出现与消失的动画
		tabMenu.update();
		tabMenu.SetTitleSelect(0);
		tabMenu.SetBodyAdapter(bodyAdapter[0]);
	}
	
	/**
	 * TabMenu titleClick 按键事件监听器
	 * @author WEI
	 *
	 */
	class TitleClickEvent implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			selTitle = arg2;
			tabMenu.SetTitleSelect(arg2);
			tabMenu.SetBodyAdapter(bodyAdapter[arg2]);
		}
	}
	/**
	 * TabMenu bodyClick 按键事件监听器
	 * @author WEI
	 *
	 */
	class BodyClickEvent implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			tabMenu.SetBodySelect(arg2, Color.GRAY);
			
			//按钮匹配
			if(selTitle==0 && arg2==0){//第1栏，第1项
				//保存游戏存档
				archiveContorller.saveArchiveFile();
				
				tabMenu.dismiss();//隐藏窗口
				tabMenu.unSetBodySelect(Color.GRAY);//去掉选中标记
				
				
			}
			if(selTitle==0 && arg2==2){//第1栏，第3项
				//测试哦
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(GameViewActivity.this);
				Toast.makeText(GameViewActivity.this,"关闭声音哟"+settings.getBoolean("sound_toggle", false), Toast.LENGTH_SHORT).show();
			}
			if(selTitle==0 && arg2==3){//第1栏，第4项
				finish();
			}
			
			if(selTitle==1 && arg2==0){
				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_author_email)});
				//intent.putExtra(android.content.Intent.EXTRA_TEXT, "Test!!");//设置邮件发送内容
				intent.setType("application/octet-stream"); 
				//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//设置附件
				startActivity(intent);
			}
			//Toast.makeText(ChineseChessActicity.this, str, 500).show();
		}
	}

	/**
	 * TabMenu 创建MENU onCreateOptionsMenu()
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * TabMenu 拦截MENU onMenuOpened()
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (tabMenu != null) {
			if (tabMenu.isShowing())
				tabMenu.dismiss();
			else {
				tabMenu.showAtLocation(findViewById(R.id.GameViewLayout),
						Gravity.BOTTOM, 0, 0);
			}
		}
		return false;// 返回为true 则显示系统menu
	}
}
