package com.topkc.chinesechess.chess;

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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.topkc.chinesechess.R;
import com.topkc.chinesechess.searchengine.ChessMoveData;
import com.topkc.chinesechess.searchengine.NegamaxEngine;
import com.topkc.chinesechess.util.ChessBackMove;
import com.topkc.chinesechess.util.ChineseChessRule;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	
	private Bitmap gameViewBackground = null;//GameView界面的背景（棋盘）
	
	//int[] redChessmanStart = {-1,-1};//当前红色棋子开始位置[X,Y]
	//int[] redChessmanStop = {-1,-1};//当前红色棋子停止位置[X,Y]
	//int[] lastBlackChessmanStart = {-1,-1};//当前黑色棋子开始位置[X,Y]
	//int[] lastBlackChessmanStop = {-1,-1};//当前黑色棋子停止位置[X,Y]
	
	private Bitmap[] redChessman = new Bitmap[7];//红色棋子
	private Bitmap[] blackChessman = new Bitmap[7];//黑色棋子
	
	private Bitmap redChessmanMark = null;//红色棋子标记
	private Bitmap blackChessmanMark = null;//黑色棋子标记
	
	Canvas canvas = null;//定义画布
	Paint paint = null;//定义画笔
	
	int[][] chessboard = ChineseChess.chessboard;
	/*
	int[][] chessboard = new int[][]{//棋盘
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
	*/
	SurfaceHolder holder = null;
	GameViewActivity activity = null;
	
	Thread refreshThread = null;//定义刷新线程
	
	ChineseChessRule chineseChessRule = null;//定义游戏规则
	ArrayList movePosList = null;//定义红方下一步 可以落棋的LIST
	
	boolean status = true; //游戏的当前状态
	boolean redTurn = true; //当前红棋下棋人状态,控制红色和黑色status图片
	boolean hasSelectedChessman = false;//当前红旗是否选中棋子
	
	//定义改变Status状态参数定义
	Bitmap redStatusBitmap = null;
	Bitmap blackStatusBitmap = null;
	
	public GameView(Context context,GameViewActivity gameViewActivity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = gameViewActivity;
		init();
		
		holder = this.getHolder();
		holder.addCallback(this);
		
		chineseChessRule = new ChineseChessRule(chessboard,this.getContext());//初始化Game行走规则,把当前棋盘传递过去
		refreshThread = new Thread(new RefreshThread());//初始化刷新线程
		

	}
	
	/**
	 * 初始化所有的资源
	 */
	public void init(){
		
		//初始化静态成员类ChineseChess
		//ChineseChess.resetChessboard();
		
		gameViewBackground = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_background);//加载背景图片
		
		//0：帅      1：车       2：马     3：跑     4：士     5：象      6：兵
		redChessman[0] = BitmapFactory.decodeResource(getResources(), R.drawable.red_king);
		redChessman[1] = BitmapFactory.decodeResource(getResources(), R.drawable.red_rook);
		redChessman[2] = BitmapFactory.decodeResource(getResources(), R.drawable.red_horse);
		redChessman[3] = BitmapFactory.decodeResource(getResources(), R.drawable.red_cannon);
		redChessman[4] = BitmapFactory.decodeResource(getResources(), R.drawable.red_guard);
		redChessman[5] = BitmapFactory.decodeResource(getResources(), R.drawable.red_elephant);
		redChessman[6] = BitmapFactory.decodeResource(getResources(), R.drawable.red_pawn);
		
		//0：帅      1：车       2：马     3：跑     4：士     5：象      6：兵
		blackChessman[0] = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
		blackChessman[1] = BitmapFactory.decodeResource(getResources(), R.drawable.black_rook);
		blackChessman[2] = BitmapFactory.decodeResource(getResources(), R.drawable.black_horse);
		blackChessman[3] = BitmapFactory.decodeResource(getResources(), R.drawable.black_cannon);
		blackChessman[4] = BitmapFactory.decodeResource(getResources(), R.drawable.black_guard);
		blackChessman[5] = BitmapFactory.decodeResource(getResources(), R.drawable.black_elephant);
		blackChessman[6] = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn);
		
		//当前棋子标记MARK
		redChessmanMark = BitmapFactory.decodeResource(getResources(), R.drawable.red_mark);
		blackChessmanMark = BitmapFactory.decodeResource(getResources(), R.drawable.black_mark);
		
		//初始化改变Status状态参数
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
	
	/**
	 * 没有棋子已经被选中（hasSelectChessman）
	 * 1.确实点击的是自己的棋子
	 * 2.设置当前位置为红棋开始位置
	 * 3.选中点击的棋子，计算下一步可是落棋的位置		
	 * @param thisX
	 * @param thisY
	 */
	public void noChessmanSelected (int thisX, int thisY){//传递当前坐标
		
		if(chineseChessRule.isRedChessman(thisX, thisY)){//确定点击的是自己的棋子
			ChineseChess.redChessmanStart[0] = thisX;
			ChineseChess.redChessmanStart[1] = thisY;//把当前棋子的位置传值给 [标记开始位置] 便于画标记
			
			//通过传递当前选中的棋子位置，得到下一步可以落棋的List<int[][]>
			if(ChineseChess.redChessmanStart[0]>=0 && ChineseChess.redChessmanStart[1]>=0){
				movePosList =  chineseChessRule.canMovePos(chessboard, ChineseChess.redChessmanStart[0], ChineseChess.redChessmanStart[1]);
			}
			hasSelectedChessman = true;//标识 已经有棋子被选中
			ChineseChess.redChessmanStop[0]=-1;ChineseChess.redChessmanStop[1]=-1;//初始化落棋标记
		}
	}
	
	/**
	 * 有棋子已经被选中（hasSelectChessman）
	 * 1.第二次点击的是自己的棋子
	 *		1.重复（没有棋子被选中方法）
	 * 2.不是自己的棋子
	 *		1.棋盘位置为空，Move
	 *		2.是对方的棋子，Move
	 * @param thisX
	 * @param thisY
	 */
	public void hasChessmanSelected(int thisX, int thisY){
		
		//第二次点击的是自己的棋子,重复（没有棋子被选中方法）
		boolean flagSameSide = chineseChessRule.isSameSide(chessboard[ChineseChess.redChessmanStart[0]][ChineseChess.redChessmanStart[1]], chessboard[thisX][thisY]);
		
		//isICanMove当前坐标可不可以作为终点位置，可不可达
		boolean isICanMove = chineseChessRule.canMove(chessboard, ChineseChess.redChessmanStart[0], ChineseChess.redChessmanStart[1], thisX, thisY);
		if(flagSameSide){
			this.noChessmanSelected(thisX, thisY);
		}else if(isICanMove){//if主要是消除 选中自己的棋子后，再随意点击对方棋子也可以选中为Stop位置的情况
			
			//在红棋下之前，把当前下棋信息加入悔棋List，怕下完后干扰保存信息;2011-8-31 22:09:44
			ChessBackMove redChessBackMove = new ChessBackMove();
			redChessBackMove.chessMove.setChessID(chessboard[ChineseChess.redChessmanStart[0]][ChineseChess.redChessmanStart[1]]);//当前下的棋子
			redChessBackMove.chessMove.setFromX(ChineseChess.redChessmanStart[0]);
			redChessBackMove.chessMove.setFromY(ChineseChess.redChessmanStart[1]);
			redChessBackMove.chessMove.setToX(thisX);
			redChessBackMove.chessMove.setToY(thisY);
			redChessBackMove.eatedChessman = chessboard[thisX][thisY];
			BackMoveController.backMoveList.add(redChessBackMove);//加入悔棋LIST
			
			//如果不是自己的棋子，则有两种可能1.空白2.对方棋子
			chineseChessRule.moveTo(chessboard, ChineseChess.redChessmanStart[0], ChineseChess.redChessmanStart[1], thisX, thisY);
			
			ChineseChess.redChessmanStop[0]=thisX;ChineseChess.redChessmanStop[1]=thisY;//红棋落棋后的停止位置，便于标记
			hasSelectedChessman = false;//已经落棋 设置为  没有棋子被选中
			
			//设置当前为电脑下棋，电脑下棋
			activity.handler.sendEmptyMessage(2);//发送消息，切换到黑色状态图片
			refreshThread.run();//重新画图,在电脑思考之前 刷新屏幕
			
			
			
			//判断用户是否已经赢得比赛
			if(!chineseChessRule.isBlackKingAlive(chessboard)){
				AlertDialog.Builder builder = new Builder(activity); 
		        builder.setMessage("确定要退出吗?"); 
		        builder.setTitle("恭喜你！取得胜利！"); 
		        builder.setPositiveButton("返回主菜单", 
		                new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								activity.finish();
							}
						});
		        builder.setNegativeButton("重新开始", 
		                new android.content.DialogInterface.OnClickListener() { 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        //dialog.dismiss(); 
		                    	
		                    	activity.handler.sendEmptyMessage(3);//发送消息，切换到红色状态图片
		                    } 
		                }); 
		        builder.create().show(); 
			}
			
			
			//用户下棋完毕，电脑开始下棋
			computerMove();
			
			//判断电脑是否已经赢得比赛
			if(!chineseChessRule.isRedKingAlive(chessboard)){
				AlertDialog.Builder builder = new Builder(activity); 
		        builder.setMessage("确定要退出吗?"); 
		        builder.setTitle("很遗憾！你输了！"); 
		        builder.setPositiveButton("返回主菜单", 
		                new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								activity.finish();
							}
						});
		        builder.setNegativeButton("重新开始", 
		                new android.content.DialogInterface.OnClickListener() { 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        //dialog.dismiss(); 
		                    	
		                    	activity.handler.sendEmptyMessage(3);//发送消息，切换到红色状态图片
		                    } 
		                }); 
		        builder.create().show(); 
			}
		}
	}
	

	/**
	 * 电脑下棋步骤
	 */
	public void computerMove(){
		
		NegamaxEngine negamaxEngine = new NegamaxEngine();
		ChessMoveData chessMoveData = negamaxEngine.searchAgoodMove(chessboard);
		
		//在电脑下棋之前，把当前下棋信息加入悔棋List，怕下完后干扰保存信息;2011-8-31 22:09:44
		ChessBackMove redChessBackMove = new ChessBackMove();
		redChessBackMove.chessMove.setChessID(chessboard[chessMoveData.from.y][chessMoveData.from.x]);//当前下的棋子
		redChessBackMove.chessMove.setFromX(chessMoveData.from.y);
		redChessBackMove.chessMove.setFromY(chessMoveData.from.x);
		redChessBackMove.chessMove.setToX(chessMoveData.to.y);
		redChessBackMove.chessMove.setToY(chessMoveData.to.x);
		redChessBackMove.eatedChessman = chessboard[chessMoveData.to.y][chessMoveData.to.x];
		//悔棋保存标记,保存的是当前下棋步骤的上一步棋的标记。。。。
		redChessBackMove.blackChessmanStart[0]=ChineseChess.blackChessmanStart[0];
		redChessBackMove.blackChessmanStart[1]=ChineseChess.blackChessmanStart[1];
		redChessBackMove.blackChessmanStop[0]=ChineseChess.blackChessmanStop[0];
		redChessBackMove.blackChessmanStop[1]=ChineseChess.blackChessmanStop[1];
		BackMoveController.backMoveList.add(redChessBackMove);//加入悔棋LIST
		
		//电脑下棋步骤
		chessboard[chessMoveData.to.y][chessMoveData.to.x] = chessboard[chessMoveData.from.y][chessMoveData.from.x];
		chessboard[chessMoveData.from.y][chessMoveData.from.x] = 0; 
		ChineseChess.blackChessmanStart[0]=chessMoveData.from.y;ChineseChess.blackChessmanStart[1]=chessMoveData.from.x;
		ChineseChess.blackChessmanStop[0]=chessMoveData.to.y;ChineseChess.blackChessmanStop[1]=chessMoveData.to.x;
		
		//电脑下完后 玩家的步骤显示 要清除，接下来由玩家下棋
		ChineseChess.redChessmanStart[0] = -1;ChineseChess.redChessmanStart[1] = -1;
		ChineseChess.redChessmanStop[0] = -1;ChineseChess.redChessmanStop[1] = -1;
		
		activity.handler.sendEmptyMessage(1);//发送消息，切换到红色状态图片，把棋权交给红棋
		
		refreshThread.run();//重新画图
	}
	
	
    /**
     * 屏幕的点击事件捕捉
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int[] pos = getPos(event);//获得当前棋子的行与列
		
		//确定当前点击坐标是棋盘内
		if(pos[0]<=9&&pos[1]<=8&&pos[0]>=0&&pos[1]>=0){
			if(!hasSelectedChessman){
				this.noChessmanSelected(pos[0], pos[1]);
				refreshThread.run();//重新画图
				return super.onTouchEvent(event);
			}else if(hasSelectedChessman){
				this.hasChessmanSelected(pos[0], pos[1]);
				refreshThread.run();//重新画图
				return super.onTouchEvent(event);
			}
		}
		return super.onTouchEvent(event);
	}  
	

	/**
	 * 将坐标换算成数组的维数
	 * @param e
	 * @return
	 */
	public int[] getPos(MotionEvent e){
		int[] pos = new int[2];
		double x = e.getX();//得到点击位置的x坐标
		double y = e.getY();//得到点击位置的y坐标
		
		//对于棋盘上面的点击事件 做出处理
		int x1 = Math.round((float)((y-ChineseChess.CHESSBOARD_TOP_SPACING-25)/ChineseChess.CHESSMAN_SPACING));//取得所在的行 -25因为照片定位的点在左上角
		int y1 = Math.round((float)((x-ChineseChess.CHESSBOARD_LEFT_SPACING-25)/ChineseChess.CHESSMAN_SPACING));//取得所在的列
		if(x1<=9 && y1<=8){
			pos[0] = x1;
			pos[1] = y1;
		}else{          //点击的位置不是棋盘时
			pos[0] = -1;//将位置设为不可用
			pos[1] = -1;
		}
		
		//System.out.println("--posX:"+pos[0]+"---posY:"+pos[1]+"--");
		return pos;//将坐标数组返回
	}
	

	/**
	 * 屏幕刷新线程
	 * @author WEI
	 */
    class RefreshThread implements Runnable{  

        @Override  
        public void run() {  
        	canvas = holder.lockCanvas(null);//获取画布  
            paint = new Paint();   //获得画笔
            //paint.setColor(Color.BLUE);  
              
    		canvas.drawColor(Color.WHITE);

    		canvas.drawBitmap(gameViewBackground, 0,0, null);//清背景  

    		for(int i=0; i<chessboard.length; i++){
    			for(int j=0; j<chessboard[i].length; j++){//绘制棋子
    				if(chessboard[i][j] != 0){
    					//canvas.drawBitmap(qizibackground, 9+j*34, 10+i*35, null);//绘制棋子的背景					
    					if(chessboard[i][j] == 1){//为黑帅时
    						canvas.drawBitmap(blackChessman[0], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 2){//为黑车时
    						canvas.drawBitmap(blackChessman[1], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 3){//为黑马时
    						canvas.drawBitmap(blackChessman[2], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 4){//为黑炮时
    						canvas.drawBitmap(blackChessman[3], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 5){//为黑士时
    						canvas.drawBitmap(blackChessman[4], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 6){//为黑象时
    						canvas.drawBitmap(blackChessman[5], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 7){//为黑兵时
    						canvas.drawBitmap(blackChessman[6], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}
    					
    					else if(chessboard[i][j] == 8){//为红将时
    						canvas.drawBitmap(redChessman[0], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 9){//为红车时
    						canvas.drawBitmap(redChessman[1], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 10){//为红马时
    						canvas.drawBitmap(redChessman[2], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 11){//为红�时
    						canvas.drawBitmap(redChessman[3], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 12){//为红仕时
    						canvas.drawBitmap(redChessman[4], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 13){//为红相时
    						canvas.drawBitmap(redChessman[5], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}else if(chessboard[i][j] == 14){//为红卒时
    						canvas.drawBitmap(redChessman[6], ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}
    					
    					/**
    					 * 为棋子添加Mark标记(红色和黑色)
    					 * i 表示当前列   j 表示当前行
    					 */
    					if(i==ChineseChess.redChessmanStart[0]&&j==ChineseChess.redChessmanStart[1]){
    						canvas.drawBitmap(redChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}
    					if(i==ChineseChess.redChessmanStop[0]&&j==ChineseChess.redChessmanStop[1]){
    						canvas.drawBitmap(blackChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}
    					if(i==ChineseChess.blackChessmanStart[0]&&j==ChineseChess.blackChessmanStart[1]){
    						canvas.drawBitmap(redChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}
    					if(i==ChineseChess.blackChessmanStop[0]&&j==ChineseChess.blackChessmanStop[1]){
    						canvas.drawBitmap(blackChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    					}
    					
    				}
    				
    				//为棋子添加Mark标记(红色和黑色)
    				if(i==ChineseChess.redChessmanStart[0]&&j==ChineseChess.redChessmanStart[1]){
						canvas.drawBitmap(redChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
					}
					if(i==ChineseChess.blackChessmanStart[0]&&j==ChineseChess.blackChessmanStart[1]){
						canvas.drawBitmap(redChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
					}
						
    				
    				//当前棋子的下一个位置集合标记
    				if(movePosList!=null){
    					for (Iterator iterator = movePosList.iterator(); iterator.hasNext();) {
    						int[] nextPos = (int[]) iterator.next();
    						if(i==nextPos[0]&&j==nextPos[1]){
    							canvas.drawBitmap(redChessmanMark, ChineseChess.CHESSBOARD_LEFT_SPACING+j*ChineseChess.CHESSMAN_SPACING, ChineseChess.CHESSBOARD_TOP_SPACING+i*ChineseChess.CHESSMAN_SPACING, paint);
    						}
    					}
    				}
    				
    			}//j的循环
    		}//i的循环
    		
    		movePosList = null;//当前可以落棋的位置movePosList设置为空，防止干扰下一步
    		holder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像 
        }  
          
    }
    
    /*
     * 刷新当前棋盘x
     */
    public void refreshGameView(){
    	refreshThread.run();//重新画图
    }

}
