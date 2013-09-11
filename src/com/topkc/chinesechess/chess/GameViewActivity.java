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
	
	//TabMenu ����
	TabMenu.MenuBodyAdapter[] bodyAdapter = new TabMenu.MenuBodyAdapter[2];// []��ʾ�ж�����ѡ��
	TabMenu.MenuTitleAdapter titleAdapter;
	TabMenu tabMenu;
	int selTitle = 0;

	
	GameView gameView = null;
	LinearLayout gameViewLayout = null;
	
	ImageView gameStatus = null;
	
	//��Ϸ�浵������
	ArchiveContorller archiveContorller = new ArchiveContorller(GameViewActivity.this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// ������Ϸȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.chess_activity);
		
		//��ʼ����Ϸ����Ĳ����ļ�
		gameViewLayout = (LinearLayout) findViewById(R.id.GameViewLayout);
		
//		//ƽ�̱���ͼƬ
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_background);
//		BitmapDrawable bd = new BitmapDrawable(bitmap);
//		bd.setTileModeXY(TileMode.CLAMP , TileMode.CLAMP );
//		bd.setDither(true);
//		gameViewLayout.setBackgroundDrawable(bd);
		
		//�����̳�ʼ��
		gameView = new GameView(this, this);
		gameView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 686));//693
		gameViewLayout.addView(gameView);
		
		//����ImageButton NewGame �� Back ��ť
		LinearLayout buttonLinearLayout = new LinearLayout(this);//��ť�ǲ��ֵĺ��򲼾�
		RelativeLayout.LayoutParams imageButtonLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		buttonLinearLayout.setPadding(5, 5, 5, 8);
		//NewGame��ť
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
		//Back��ť
		ImageButton imageButton02 = new ImageButton(this);
		imageButton02.setBackgroundResource(R.drawable.image_button_back);
		buttonLinearLayout.addView(imageButton02, imageButtonLayoutParams);
		
		gameViewLayout.addView(buttonLinearLayout);
		
		//����Ϸ��ť������
		imageButton01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(3);
				Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
				gameStatus.setImageBitmap(gameStatusBitmap);
			}
		});
		//���尴ť������;2011-9-1 19:59:16
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
		
		
		
		// ��ʼ��AdView,��������ֻ���ڲ���,��ɺ�һ��Ҫȥ��,�ο��ĵ�˵��
		//AdViewManager.setConfigExpireTimeout(-1); // ��֤ÿ�ζ��ӷ�����ȡ����
		//AdViewTargeting.setTestMode(true); // ��֤����ѡ�еĹ�湫˾��Ϊ����״̬
		//AdViewLayout adViewLayout = new AdViewLayout(this,"SDK20111812070129bb9oj4n571faaka");
		/*
		AdViewLayout adViewLayout = new AdViewLayout(this,"SDK20110217070205c1c75kge9uexp64");
		RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		gameViewLayout.addView(adViewLayout, adViewLayoutParams);
		*/
		gameViewLayout.invalidate();
		
        //�趨����TabMenu�ı��������
        this.MenuContentSetting();
		
	}
	
    //����������Ϣ�������� ����statusͼƬ,�Լ���Ȩ
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 1){
				//���������л�����������
				ChineseChess.isRedTurn = true;
				ChineseChess.isBlackTurn = false;
				System.out.println("handleMessage1:Red Chessman turn!");
				Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
				gameStatus.setImageBitmap(gameStatusBitmap);
			}
			if(msg.what == 2){
				//���������л�����������
				ChineseChess.isRedTurn = false;
				ChineseChess.isBlackTurn = true;
				
				System.out.println("handleMessage1:Black Chessman turn!");
				Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
				gameStatus.setImageBitmap(gameStatusBitmap);
			}
			
			if(msg.what == 3){
				System.out.println("handleMessage3��New Game!");
				reload();
			}
		}
    	
    };
    
    
    /**
     * ���¼�����Ϸ�����¼��ص�ǰActivity��
     */
	public void reload() { 
		//��ʼ������
		ChineseChess.resetChessboard();
		//�������List����
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
		//�����ֻ����˰���
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
			exitDialog();
            return false; 
        } 
		return super.onKeyDown(keyCode, event);
	}
	
	/**
     * ����Ϸ���������˰�ťʱ����ʾ�˳���
     */
    protected void exitDialog() { 
        AlertDialog.Builder builder = new Builder(GameViewActivity.this); 
        builder.setMessage("ȷ��Ҫ�˳���?"); 
        builder.setTitle("��ʾ"); 
        
        builder.setPositiveButton("�������˵�", 
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
        builder.setNeutralButton("ȷ��", 
                new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		*/
        builder.setNegativeButton("������Ϸ", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 
    
    
    
	/**
	 * TabMenu������������Ե��趨
	 */
	public void MenuContentSetting(){
		// ���÷�ҳ���ı���
		titleAdapter = new TabMenu.MenuTitleAdapter(this, new String[] { "����",
				"����" }, 16, 0xFF222222, Color.LTGRAY, Color.WHITE);		
		
		// ����ÿ���ҳ��������
		bodyAdapter[0] = new TabMenu.MenuBodyAdapter(this, new String[] { "������Ϸ",
				"������Ϸ", "��Ϸ�Ѷ�", "�˳���Ϸ" }, new int[] { R.drawable.menu_downmanager,
				R.drawable.menu_filemanager, R.drawable.menu_refresh,
				R.drawable.menu_quit }, 11, 0xFFFFFFFF);
		bodyAdapter[1] = new TabMenu.MenuBodyAdapter(this, new String[] { "Ͷ�߽���",
				"������Ϸ", "������Ϸ" }, new int[] { R.drawable.menu_inputurl,
				R.drawable.menu_bookmark, R.drawable.menu_search }, 11,
				0xFFFFFFFF);
		tabMenu = new TabMenu(this, new TitleClickEvent(),
				new BodyClickEvent(), titleAdapter,
				// 0x55123456,//TabMenu�ı�����ɫ
				0xDD000000, R.style.PopupAnimation);// ��������ʧ�Ķ���
		tabMenu.update();
		tabMenu.SetTitleSelect(0);
		tabMenu.SetBodyAdapter(bodyAdapter[0]);
	}
	
	/**
	 * TabMenu titleClick �����¼�������
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
	 * TabMenu bodyClick �����¼�������
	 * @author WEI
	 *
	 */
	class BodyClickEvent implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			tabMenu.SetBodySelect(arg2, Color.GRAY);
			
			//��ťƥ��
			if(selTitle==0 && arg2==0){//��1������1��
				//������Ϸ�浵
				archiveContorller.saveArchiveFile();
				
				tabMenu.dismiss();//���ش���
				tabMenu.unSetBodySelect(Color.GRAY);//ȥ��ѡ�б��
				
				
			}
			if(selTitle==0 && arg2==2){//��1������3��
				//����Ŷ
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(GameViewActivity.this);
				Toast.makeText(GameViewActivity.this,"�ر�����Ӵ"+settings.getBoolean("sound_toggle", false), Toast.LENGTH_SHORT).show();
			}
			if(selTitle==0 && arg2==3){//��1������4��
				finish();
			}
			
			if(selTitle==1 && arg2==0){
				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_author_email)});
				//intent.putExtra(android.content.Intent.EXTRA_TEXT, "Test!!");//�����ʼ���������
				intent.setType("application/octet-stream"); 
				//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//���ø���
				startActivity(intent);
			}
			//Toast.makeText(ChineseChessActicity.this, str, 500).show();
		}
	}

	/**
	 * TabMenu ����MENU onCreateOptionsMenu()
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// ���봴��һ��
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * TabMenu ����MENU onMenuOpened()
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
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}
}
