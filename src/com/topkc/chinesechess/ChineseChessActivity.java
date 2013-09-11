package com.topkc.chinesechess;

import com.topkc.chinesechess.bluetooth.BluetoothActivity;
import com.topkc.chinesechess.chess.ArchiveManagerActivity;
import com.topkc.chinesechess.chess.ChineseChess;
import com.topkc.chinesechess.chess.GameViewActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ChineseChessActivity extends Activity {
    /** Called when the activity is first created. */
	

	private static final int EXIT = -1; //退出常量
	
	//游戏欢迎菜单界面
	private View welcomeMenuLayout = null;
	private ImageButton startButton = null;
	private ImageButton blueToothButton = null;
	private ImageButton loadingButton = null;
	private ImageButton settingButton = null;
	//private ImageButton aboutButton = null;
	private ImageButton exitButton = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //检查屏幕分辨率目前只支持800*480
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        if(screenWidth<480||screenHeight<800){
        	//System.exit(-1);
        	AlertDialog.Builder dialog = new AlertDialog.Builder(this)
        	.setTitle("提示信息")
        	.setMessage("本软件暂时不支持您的屏幕分辨率，你关注后续版本！谢谢")
        	.setPositiveButton("确定", 
        			new DialogInterface.OnClickListener() {  
                		public void onClick(DialogInterface dialog, int whichButton) {  
                			//Do something  
                			finish();
                		}  
            		}); 
        	dialog.show();
        	//finish();
        }
        //Toast.makeText(this, "Width:"+screenWidth +"  Height:"+screenHeight, Toast.LENGTH_LONG).show();
		 
        
        //设置游戏全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
 		
        //设定弹出Menu的标题和内容
        //this.MenuContentSetting();
        //setContentView(R.layout.welcome_menu);
        this.initWelcomeMenuView();

    }

    /**
     * 欢迎菜单界面设置
     */
    public void initWelcomeMenuView(){
    	
		setContentView(R.layout.welcome_menu);
		
        //view = (View)findViewById(R.id.View01);
        //view.setBackgroundResource(R.drawable.welcome_view);
        
        welcomeMenuLayout = (LinearLayout)findViewById(R.id.WelcomeMenuLayout);
        
		//平铺背景图片
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome_view);
//		BitmapDrawable bd = new BitmapDrawable(bitmap);
//		bd.setTileModeXY(TileMode.CLAMP , TileMode.CLAMP );
//		bd.setDither(true);
//		welcomeMenuLayout.setBackgroundDrawable(bd);
		
		
		//按键事件监听
		startButton = (ImageButton)findViewById(R.id.ImageButton01);
		blueToothButton = (ImageButton)findViewById(R.id.ImageButton02);
		loadingButton = (ImageButton)findViewById(R.id.ImageButton03);
		settingButton = (ImageButton)findViewById(R.id.ImageButton04);
		//aboutButton = (ImageButton)findViewById(R.id.ImageButton05);
		exitButton = (ImageButton)findViewById(R.id.ImageButton06);
		
		startButton.setOnClickListener(new MenuButtonListener() );
		blueToothButton.setOnClickListener(new MenuButtonListener() );
		loadingButton.setOnClickListener(new MenuButtonListener() );
		settingButton.setOnClickListener(new MenuButtonListener() );
		//aboutButton.setOnClickListener(new MenuButtonListener() );
		exitButton.setOnClickListener(new MenuButtonListener() );
    }
    
    /**
     * 欢迎界面按键监听
     * @author WEI
     *
     */
    class MenuButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MediaPlayer clickSound = MediaPlayer.create(ChineseChessActivity.this, R.raw.kill_chessman);//加载欢迎声音
			clickSound.start();
			clickSound.setLooping(true);
			
			switch (v.getId()) {
			case R.id.ImageButton01://游戏开始
				//Toast.makeText(ChineseChessActivity.this,v.toString(), Toast.LENGTH_SHORT).show();
				InitGameView();//加载游戏界面
				break;
			case R.id.ImageButton02://蓝牙游戏，开始配对
				Intent intent = new Intent();
				intent.setClass(ChineseChessActivity.this, BluetoothActivity.class);
				ChineseChessActivity.this.startActivity(intent);
				break;
			case R.id.ImageButton03://加载游戏
				Intent loaingGame_intent = new Intent();
				loaingGame_intent.setClass(ChineseChessActivity.this, ArchiveManagerActivity.class);
				ChineseChessActivity.this.startActivity(loaingGame_intent);
				break;
			case R.id.ImageButton04:
				Intent settingIntent = new Intent();
				settingIntent.setClass(ChineseChessActivity.this, GameSettingActivity.class);
				ChineseChessActivity.this.startActivity(settingIntent);
				break;
			case R.id.ImageButton06:
				//"关闭程序" 返回到HOME界面
				/*
				Intent nowIntent = new Intent();  
	            nowIntent.setAction(Intent.ACTION_MAIN);
	            nowIntent.addCategory(Intent.CATEGORY_HOME);   
	            nowIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            startActivity(nowIntent);
				*/
				//handler.sendEmptyMessage(6);
//				finish();
				ChineseChessActivity.this.onKeyDown(KeyEvent.KEYCODE_BACK, null);
				break;
			default:
				//Toast.makeText(WelcomeMenuActivity.this,v.getId(), Toast.LENGTH_SHORT).show();
				break;
			}
			
		}
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setMessage(getText(R.string.sure_to_stop_app))
					.setNegativeButton(getText(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})

					.setPositiveButton(getText(R.string.confirm),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									android.os.Process.killProcess(android.os.Process.myPid());
								}

							}).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
    
    /**
     * 跳转到GameView界面
     */
    public void InitGameView(){
    	//this.setContentView(new GameView(this,this));
    	
    	//初始化静态成员类ChineseChess
    	ChineseChess.resetChessboard();
    	
    	Intent intent = new Intent();
		intent.setClass(ChineseChessActivity.this, GameViewActivity.class);
		ChineseChessActivity.this.startActivity(intent);
    }
    
    
    //用来更新UI线程中的控件 
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == EXIT){
				finish();
			}
			if(msg.what == 6){
				finish();
			}
		}
    	
    };
    
 

}