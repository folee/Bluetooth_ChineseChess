package com.topkc.chinesechess.bluetooth;


import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.topkc.chinesechess.R;
import com.topkc.chinesechess.util.ChineseChessRule;

/**
 * ������ս����&���� SurfaceView ����
 * @author WEI
 *
 */
public class BluetoothChessView extends SurfaceView implements SurfaceHolder.Callback{
	

	
	private Bitmap gameViewBackground = null;//GameView����ı��������̣�
	//private Bitmap backButton = null, backButtonPressed = null;//���尴ť
	//private Bitmap newGameButton = null,newGameButtonPressed = null;//����Ϸ��ť
	//private Bitmap redChessTurn = null,blackChessTurn = null;//��ǰ˭����
	
	private MediaPlayer chessmanMoveSound = null;//���������
	private MediaPlayer chessmanKillSound = null;//���������
	
	int[] redChessmanStart = {-1,-1};//��ǰ��ɫ���ӿ�ʼλ��[X,Y]
	int[] redChessmanStop = {-1,-1};//��ǰ��ɫ����ֹͣλ��[X,Y]
	int[] blackChessmanStart = {-1,-1};//��ǰ��ɫ���ӿ�ʼλ��[X,Y]
	int[] blackChessmanStop = {-1,-1};//��ǰ��ɫ����ֹͣλ��[X,Y]
	
	private Bitmap[] redChessman = new Bitmap[7];//��ɫ����
	private Bitmap[] blackChessman = new Bitmap[7];//��ɫ����
	
	private Bitmap redChessmanMark = null;//��ɫ���ӱ��
	private Bitmap blackChessmanMark = null;//��ɫ���ӱ��
	
	Canvas canvas = null;//���廭��
	Paint paint = null;//���廭��
	
	int[][] chessboard = new int[][]{//����
			{2,3,6,5,1,5,6,3,2},
			{0,0,0,0,0,0,0,0,0},
			{0,4,0,0,0,0,0,4,0},
			{7,0,7,0,7,0,7,0,7},
			{0,0,0,0,0,0,0,0,0},
			
			{0,0,0,0,0,0,0,0,0},
			{14,0,14,0,14,0,14,0,14},
			{0,11,0,0,0,0,0,11,0},
			{0,0,0,0,0,0,0,0,0},
			{9,10,13,12,8,12,13,10,9},
		};
	
	SurfaceHolder holder = null;
	//Activity activity = null;
	BluetoothActivity bluetoothActivity ;
	
	Thread refreshThread = null;//����ˢ���߳�
	
	ChineseChessRule chineseChessRule = null;//������Ϸ����
	ArrayList movePosList = null;//����췽��һ�� ���������LIST
	
	boolean status = true; //��Ϸ�ĵ�ǰ״̬
	boolean redTurn = true; //��ǰ����������״̬,���ƺ�ɫ�ͺ�ɫstatusͼƬ
	boolean hasSelectedChessman = false;//��ǰ�����Ƿ�ѡ������
	
	//����ı�Status״̬��������
	Bitmap redStatusBitmap = null;
	Bitmap blackStatusBitmap = null;
	//private final Handler parentHandler;
	
	public BluetoothChessView(Context context,BluetoothActivity bluetoothActivity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.bluetoothActivity =  bluetoothActivity;
		//this.activity = bluetoothActivity;
		//parentHandler = handler;
		init();
		
		holder = this.getHolder();
		holder.addCallback(this);
		
		chineseChessRule = new ChineseChessRule(chessboard,this.getContext());//��ʼ��Game���߹���,�ѵ�ǰ���̴��ݹ�ȥ
		refreshThread = new Thread(new RefreshThread());//��ʼ��ˢ���߳�
		

	}
	
	/**
	 * ��ʼ�����е���Դ
	 */
	public void init(){
		gameViewBackground = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_background);//���ر���ͼƬ
		chessmanMoveSound = MediaPlayer.create(this.getContext(), R.raw.chessman_move);//���������ƶ�����
		chessmanKillSound = MediaPlayer.create(this.getContext(), R.raw.kill_chessman);//���س���������
		
		//���а�ť��ʼ��
		//newGameButton = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_button_newgame);
		//newGameButtonPressed = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_button_newgamepressed);
		//backButton = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_button_back);
		//backButtonPressed = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_button_backpressed);
		
		//0��˧      1����       2����     3����     4��ʿ     5����      6����
		redChessman[0] = BitmapFactory.decodeResource(getResources(), R.drawable.red_king);
		redChessman[1] = BitmapFactory.decodeResource(getResources(), R.drawable.red_rook);
		redChessman[2] = BitmapFactory.decodeResource(getResources(), R.drawable.red_horse);
		redChessman[3] = BitmapFactory.decodeResource(getResources(), R.drawable.red_cannon);
		redChessman[4] = BitmapFactory.decodeResource(getResources(), R.drawable.red_guard);
		redChessman[5] = BitmapFactory.decodeResource(getResources(), R.drawable.red_elephant);
		redChessman[6] = BitmapFactory.decodeResource(getResources(), R.drawable.red_pawn);
		
		//0��˧      1����       2����     3����     4��ʿ     5����      6����
		blackChessman[0] = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
		blackChessman[1] = BitmapFactory.decodeResource(getResources(), R.drawable.black_rook);
		blackChessman[2] = BitmapFactory.decodeResource(getResources(), R.drawable.black_horse);
		blackChessman[3] = BitmapFactory.decodeResource(getResources(), R.drawable.black_cannon);
		blackChessman[4] = BitmapFactory.decodeResource(getResources(), R.drawable.black_guard);
		blackChessman[5] = BitmapFactory.decodeResource(getResources(), R.drawable.black_elephant);
		blackChessman[6] = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn);
		
		//��ǰ���ӱ��MARK
		redChessmanMark = BitmapFactory.decodeResource(getResources(), R.drawable.red_mark);
		blackChessmanMark = BitmapFactory.decodeResource(getResources(), R.drawable.black_mark);
		
		//��ʼ���ı�Status״̬����
		redStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_red);
		blackStatusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_gamestatus_black);
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//thread.start();
		refreshThread.run();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	

    //The Handler that gets information back from the BluetoothChatService
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
            	System.out.println("--Game View Handler!!!!!!----------");
                switch (msg.arg1) {
                
                /**
                //�ı���ʾ��ǰ��Ϸ��״̬
                case BluetoothChatService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                */
                }
                break;
            case BluetoothChess.CHANGE_CLIENT_STATE:
            	System.out.println("--Game View Handler!!!!!!----------");
                
                break;
            case 2:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case BluetoothChess.MOVE_TO:
            	int fromX  = msg.getData().getInt("fromX");
            	int fromY  = msg.getData().getInt("fromY");
            	int toX  = msg.getData().getInt("toX");
            	int toY  = msg.getData().getInt("toY");
            	moveTo(fromX, fromY, toX, toY);
            	BluetoothChess.isRedTrun = true;
            	//����һ�߷��͸ı䵱ǰ�����˵���Ϣ
            	Message msg2 = bluetoothActivity.mHandler.obtainMessage(BluetoothChess.CHANGE_IMAGE_TO_RED);
    	        bluetoothActivity.mHandler.sendMessage(msg2);
    	        
    	        //�ж��û��Ƿ��Ѿ�Ӯ�ñ���
    			if(!chineseChessRule.isRedKingAlive(chessboard)){
    				AlertDialog.Builder builder = new Builder(bluetoothActivity); 
    		        builder.setMessage("ȷ��Ҫ�˳���?"); 
    		        builder.setTitle("���ź���������"); 
    		        builder.setPositiveButton("�������˵�", 
    		                new android.content.DialogInterface.OnClickListener() {
    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								bluetoothActivity.finish();
    							}
    						});
    		        builder.setNegativeButton("���¿�ʼ", 
    		                new android.content.DialogInterface.OnClickListener() { 
    		                    @Override 
    		                    public void onClick(DialogInterface dialog, int which) { 
    		                        //dialog.dismiss(); 
    		                    	Message msg5 = bluetoothActivity.mHandler.obtainMessage(BluetoothChess.RESTART_GAME);
    		            	        bluetoothActivity.mHandler.sendMessage(msg5);
    		                    	//bluetoothActivity.handler.sendEmptyMessage(3);//������Ϣ���л�����ɫ״̬ͼƬ
    		                    } 
    		                }); 
    		        builder.create().show(); 
    			}
                break;
            }
        }
    };
	
	/**
	 * û�������Ѿ���ѡ�У�hasSelectChessman��
	 * 1.ȷʵ��������Լ�������
	 * 2.���õ�ǰλ��Ϊ���忪ʼλ��
	 * 3.ѡ�е�������ӣ�������һ�����������λ��		
	 * @param thisX
	 * @param thisY
	 */
	public void noChessmanSelected (int thisX, int thisY){//���ݵ�ǰ����
		
		if(chineseChessRule.isRedChessman(thisX, thisY)){//ȷ����������Լ�������
			redChessmanStart[0] = thisX;
			redChessmanStart[1] = thisY;//�ѵ�ǰ���ӵ�λ�ô�ֵ�� [��ǿ�ʼλ��] ���ڻ����
			
			//ͨ�����ݵ�ǰѡ�е�����λ�ã��õ���һ�����������List<int[][]>
			if(redChessmanStart[0]>=0 && redChessmanStart[1]>=0){
				movePosList =  chineseChessRule.canMovePos(chessboard, redChessmanStart[0], redChessmanStart[1]);
			}
			hasSelectedChessman = true;//��ʶ �Ѿ������ӱ�ѡ��
			redChessmanStop[0]=-1;redChessmanStop[1]=-1;//��ʼ��������
		}
	}
	
	/**
	 * �������Ѿ���ѡ�У�hasSelectChessman��
	 * 1.�ڶ��ε�������Լ�������
	 *		1.�ظ���û�����ӱ�ѡ�з�����
	 * 2.�����Լ�������
	 *		1.����λ��Ϊ�գ�Move
	 *		2.�ǶԷ������ӣ�Move
	 * @param thisX
	 * @param thisY
	 */
	public void hasChessmanSelected(int thisX, int thisY){
		
		//�ڶ��ε�������Լ�������,�ظ���û�����ӱ�ѡ�з�����
		boolean flagSameSide = chineseChessRule.isSameSide(chessboard[redChessmanStart[0]][redChessmanStart[1]], chessboard[thisX][thisY]);
		
		//isICanMove��ǰ����ɲ�������Ϊ�յ�λ�ã��ɲ��ɴ�
		boolean isICanMove = chineseChessRule.canMove(chessboard, redChessmanStart[0], redChessmanStart[1], thisX, thisY);
		if(flagSameSide){
			this.noChessmanSelected(thisX, thisY);
		}else if(isICanMove){//if��Ҫ������ ѡ���Լ������Ӻ����������Է�����Ҳ����ѡ��ΪStopλ�õ����
			//��������Լ������ӣ��������ֿ���1.�հ�2.�Է�����
			int fromX = redChessmanStart[0];int fromY = redChessmanStart[1];
			int toX = thisX; int toY = thisY;
			chineseChessRule.moveTo(chessboard, redChessmanStart[0], redChessmanStart[1], thisX, thisY);
			
			redChessmanStop[0]=thisX;redChessmanStop[1]=thisY;//����������ֹͣλ�ã����ڱ��
			hasSelectedChessman = false;//�Ѿ����� ����Ϊ  û�����ӱ�ѡ��
			
			//���õ�ǰΪ�������壬��������
			//redTurn = false;//�ı��ʶ
			//changeStatusImage();//�ı�ͼƬ
			BluetoothChess.isRedTrun = false;
			//�ı䵱ǰ�ͷ��˵�������ͼƬ
        	Message msg3 = bluetoothActivity.mHandler.obtainMessage(BluetoothChess.CHANGE_IMAGE_TO_BLACK);
	        bluetoothActivity.mHandler.sendMessage(msg3);
	        
	        //��ǰ�ͷ�����������Ĳ�����ʾ Ҫ���
			blackChessmanStart[0] = -1;blackChessmanStart[1] = -1;
			blackChessmanStop[0] = -1;blackChessmanStop[1] = -1;
			refreshThread.run();//���»�ͼ,�ڵ���˼��֮ǰ ˢ����Ļ
			
			//�ж��û��Ƿ��Ѿ�Ӯ�ñ���
			if(!chineseChessRule.isBlackKingAlive(chessboard)){
				AlertDialog.Builder builder = new Builder(bluetoothActivity); 
		        builder.setMessage("ȷ��Ҫ�˳���?"); 
		        builder.setTitle("��ϲ�㣡ȡ��ʤ����"); 
		        builder.setPositiveButton("�������˵�", 
		                new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								bluetoothActivity.finish();
							}
						});
		        builder.setNegativeButton("���¿�ʼ", 
		                new android.content.DialogInterface.OnClickListener() { 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        //dialog.dismiss(); 
		                    	Message msg6 = bluetoothActivity.mHandler.obtainMessage(BluetoothChess.RESTART_GAME);
		            	        bluetoothActivity.mHandler.sendMessage(msg6);
		                    	//bluetoothActivity.handler.sendEmptyMessage(3);//������Ϣ���л�����ɫ״̬ͼƬ
		                    } 
		                }); 
		        builder.create().show(); 
			}
			
			//computerMove();
			//����������ϣ�������Ϣ�����忪ʼ����
			String mChessMove = fromX +"-"+ fromY +"-"+  toX +"-"+  toY ;
			// Send the name of the connected device back to the UI Activity
	        Message msg = bluetoothActivity.mHandler.obtainMessage(BluetoothChess.SEND_CHESS_MOVE);
	        Bundle bundle = new Bundle();
	        bundle.putString("MOVE_KEY", mChessMove);
	        msg.setData(bundle);
	        bluetoothActivity.mHandler.sendMessage(msg);
	        
	        
		}
	}
	
	/**
	 * �������������ܵ���Ϣ�����ƽ�������
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 */
	public void moveTo(int fromX,int fromY,int toX,int toY){
		chessboard[toX][toY] = chessboard[fromX][fromY];
		chessboard[fromX][fromY] = 0; 
		blackChessmanStart[0] = fromX;blackChessmanStart[1] = fromY;
		blackChessmanStop[0] = toX;blackChessmanStop[1] = toY;
		
		//��������� ��ҵĲ�����ʾ Ҫ��������������������
		redChessmanStart[0] = -1;redChessmanStart[1] = -1;
		redChessmanStop[0] = -1;redChessmanStop[1] = -1;
		
		refreshThread.run();//���»�ͼ
	}
	
    /**
     * ��Ļ�ĵ���¼���׽
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int[] pos = getPos(event);//��õ�ǰ���ӵ�������
		
		//�ж��Ƿ�ǰ�û�����
		if(BluetoothChess.isRedTrun){
			
			//ȷ����ǰ���������������
			if(pos[0]<=9&&pos[1]<=8&&pos[0]>=0&&pos[1]>=0){
				if(!hasSelectedChessman){
					this.noChessmanSelected(pos[0], pos[1]);
					refreshThread.run();//���»�ͼ
					return super.onTouchEvent(event);
				}else if(hasSelectedChessman){
					this.hasChessmanSelected(pos[0], pos[1]);
					refreshThread.run();//���»�ͼ
					return super.onTouchEvent(event);
				}
			}
			
		}
		
		

		/*
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			System.out.println("====ACTION_DOWN===");
    		//����GameView�������еİ�ť
    		canvas.drawBitmap(newGameButtonPressed, ChineseChess.GAME_VIEW_BUTTON_NEWGAME_X, ChineseChess.GAME_VIEW_BUTTON_NEWGAME_Y, paint);
    		canvas.drawBitmap(backButtonPressed, ChineseChess.GAME_VIEW_BUTTON_BACK_X, ChineseChess.GAME_VIEW_BUTTON_BACK_Y, paint);
    		refreshThread.run();//���»�ͼ
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			System.out.println("====ACTION_UP===");
    		//����GameView�������еİ�ť

		}
		*/
		return super.onTouchEvent(event);
		//return true;
	}  
	

	/**
	 * �����껻��������ά��
	 * @param e
	 * @return
	 */
	public int[] getPos(MotionEvent e){
		int[] pos = new int[2];
		double x = e.getX();//�õ����λ�õ�x����
		double y = e.getY();//�õ����λ�õ�y����
		
		//������������ĵ���¼� ��������
		int x1 = Math.round((float)((y-BluetoothChessLayout.CHESSBOARD_TOP_SPACING-25)/BluetoothChessLayout.CHESSMAN_SPACING));//ȡ�����ڵ��� -25��Ϊ��Ƭ��λ�ĵ������Ͻ�
		int y1 = Math.round((float)((x-BluetoothChessLayout.CHESSBOARD_LEFT_SPACING-25)/BluetoothChessLayout.CHESSMAN_SPACING));//ȡ�����ڵ���
		if(x1<=9 && y1<=8){
			pos[0] = x1;
			pos[1] = y1;
		}else{          //�����λ�ò�������ʱ
			pos[0] = -1;//��λ����Ϊ������
			pos[1] = -1;
		}
		
		System.out.println("--posX:"+pos[0]+"---posY:"+pos[1]+"--");
		return pos;//���������鷵��
	}
	

	/**
	 * ��Ļˢ���߳�
	 * @author WEI
	 */
    class RefreshThread implements Runnable{  

        @Override  
        public void run() {  
        	canvas = holder.lockCanvas(null);//��ȡ����  
            paint = new Paint();   //��û���
            //paint.setColor(Color.BLUE);  
              
    		canvas.drawColor(Color.WHITE);

    		canvas.drawBitmap(gameViewBackground, 0,0, null);//�屳��  
    		
    		//����GameView�������еİ�ť
    		//if(redChessmanStart[0]==-1){
        	//	canvas.drawBitmap(newGameButton, BluetoothChessLayout.GAME_VIEW_BUTTON_NEWGAME_X, BluetoothChessLayout.GAME_VIEW_BUTTON_NEWGAME_Y, paint);
        	//	canvas.drawBitmap(backButton, BluetoothChessLayout.GAME_VIEW_BUTTON_BACK_X, BluetoothChessLayout.GAME_VIEW_BUTTON_BACK_Y, paint);
    		//}

    		for(int i=0; i<chessboard.length; i++){
    			for(int j=0; j<chessboard[i].length; j++){//��������
    				if(chessboard[i][j] != 0){
    					//canvas.drawBitmap(qizibackground, 9+j*34, 10+i*35, null);//�������ӵı���					
    					if(chessboard[i][j] == 1){//Ϊ��˧ʱ
    						canvas.drawBitmap(blackChessman[0], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 2){//Ϊ�ڳ�ʱ
    						canvas.drawBitmap(blackChessman[1], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 3){//Ϊ����ʱ
    						canvas.drawBitmap(blackChessman[2], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 4){//Ϊ����ʱ
    						canvas.drawBitmap(blackChessman[3], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 5){//Ϊ��ʿʱ
    						canvas.drawBitmap(blackChessman[4], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 6){//Ϊ����ʱ
    						canvas.drawBitmap(blackChessman[5], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 7){//Ϊ�ڱ�ʱ
    						canvas.drawBitmap(blackChessman[6], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}
    					
    					else if(chessboard[i][j] == 8){//Ϊ�콫ʱ
    						canvas.drawBitmap(redChessman[0], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 9){//Ϊ�쳵ʱ
    						canvas.drawBitmap(redChessman[1], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 10){//Ϊ����ʱ
    						canvas.drawBitmap(redChessman[2], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 11){//Ϊ��hʱ
    						canvas.drawBitmap(redChessman[3], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 12){//Ϊ����ʱ
    						canvas.drawBitmap(redChessman[4], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 13){//Ϊ����ʱ
    						canvas.drawBitmap(redChessman[5], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 14){//Ϊ����ʱ
    						canvas.drawBitmap(redChessman[6], BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}
    					
    					/**
    					 * Ϊ�������Mark���(��ɫ�ͺ�ɫ)
    					 * i ��ʾ��ǰ��   j ��ʾ��ǰ��
    					 */
    					if(i==redChessmanStart[0]&&j==redChessmanStart[1]){
    						canvas.drawBitmap(redChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}
    					if(i==redChessmanStop[0]&&j==redChessmanStop[1]){
    						canvas.drawBitmap(blackChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}
    					if(i==blackChessmanStart[0]&&j==blackChessmanStart[1]){
    						canvas.drawBitmap(redChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}
    					if(i==blackChessmanStop[0]&&j==blackChessmanStop[1]){
    						canvas.drawBitmap(blackChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    					}
    					
    				}
    				
    				//Ϊ�������Mark���(��ɫ�ͺ�ɫ)
    				if(i==redChessmanStart[0]&&j==redChessmanStart[1]){
						canvas.drawBitmap(redChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
					}
					if(i==blackChessmanStart[0]&&j==blackChessmanStart[1]){
						canvas.drawBitmap(redChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
					}
						
    				
    				//��ǰ���ӵ���һ��λ�ü��ϱ��
    				if(movePosList!=null){
    					for (Iterator iterator = movePosList.iterator(); iterator.hasNext();) {
    						int[] nextPos = (int[]) iterator.next();
    						if(i==nextPos[0]&&j==nextPos[1]){
    							canvas.drawBitmap(redChessmanMark, BluetoothChessLayout.CHESSBOARD_LEFT_SPACING+j*BluetoothChessLayout.CHESSMAN_SPACING, BluetoothChessLayout.CHESSBOARD_TOP_SPACING+i*BluetoothChessLayout.CHESSMAN_SPACING, paint);
    						}
    					}
    				}
    				
    			}//j��ѭ��
    		}//i��ѭ��
    		
    		
    		
    		movePosList = null;//��ǰ���������λ��movePosList����Ϊ�գ���ֹ������һ��
    		holder.unlockCanvasAndPost(canvas);//�����������ύ���õ�ͼ�� 
        }  
          
    }
    

}