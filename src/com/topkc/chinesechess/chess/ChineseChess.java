package com.topkc.chinesechess.chess;

/**
 * 常量类，用来保存象棋布局位置的信息等
 * @author WEI
 *
 */
public class ChineseChess {
	
	//界面边框
	public static final int CHESSBOARD_TOP_SPACING = 131;
	public static final int CHESSBOARD_LEFT_SPACING = 10;
	public static final int CHESSMAN_SPACING = 52;
	
	//GameView界面按钮的坐标
	public static final int GAME_VIEW_BUTTON_NEWGAME_X = CHESSBOARD_LEFT_SPACING;
	public static final int GAME_VIEW_BUTTON_NEWGAME_Y = CHESSBOARD_TOP_SPACING+10*ChineseChess.CHESSMAN_SPACING+10;
	public static final int GAME_VIEW_BUTTON_BACK_X = CHESSBOARD_LEFT_SPACING+290;
	public static final int GAME_VIEW_BUTTON_BACK_Y = CHESSBOARD_TOP_SPACING+10*ChineseChess.CHESSMAN_SPACING+10;	
	
	public static int[][] chessboard = new int[][]{//棋盘
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
	
	//当前期权在谁的手里
	public static boolean isRedTurn = true;
	public static boolean isBlackTurn = false;
	
	public static int[] redChessmanStart = {-1,-1};//红色棋子开始位置[X,Y]
	public static int[] redChessmanStop = {-1,-1};//红色棋子停止位置[X,Y]
	public static int[] blackChessmanStart = {-1,-1};//黑色棋子开始位置[X,Y]
	public static int[] blackChessmanStop = {-1,-1};//黑色棋子停止位置[X,Y]
	
	//Preferences保存名字
	//public static String Preferences_STORE_Settings = "Settings";
	
	//游戏声音
	//public static boolean Sound_Toggle ;
	
	/**
	 * 重置棋盘，用于开始新的一盘游戏
	 */
	public static void resetChessboard(){
		//重置棋盘		
		chessboard[0][0] = 2; chessboard[0][1] = 3; chessboard[0][2] = 6; chessboard[0][3] = 5; chessboard[0][4] = 1; chessboard[0][5] = 5; chessboard[0][6] = 6; chessboard[0][7] = 3; chessboard[0][8] = 2;
		chessboard[1][0] = 0; chessboard[1][1] = 0; chessboard[1][2] = 0; chessboard[1][3] = 0; chessboard[1][4] = 0; chessboard[1][5] = 0; chessboard[1][6] = 0; chessboard[1][7] = 0; chessboard[1][8] = 0;
		chessboard[2][0] = 0; chessboard[2][1] = 4; chessboard[2][2] = 0; chessboard[2][3] = 0; chessboard[2][4] = 0; chessboard[2][5] = 0; chessboard[2][6] = 0; chessboard[2][7] = 4; chessboard[2][8] = 0;
		chessboard[3][0] = 7; chessboard[3][1] = 0; chessboard[3][2] = 7; chessboard[3][3] = 0; chessboard[3][4] = 7; chessboard[3][5] = 0; chessboard[3][6] = 7; chessboard[3][7] = 0; chessboard[3][8] = 7;
		chessboard[4][0] = 0; chessboard[4][1] = 0; chessboard[4][2] = 0; chessboard[4][3] = 0; chessboard[4][4] = 0; chessboard[4][5] = 0; chessboard[4][6] = 0; chessboard[4][7] = 0; chessboard[4][8] = 0;
		chessboard[5][0] = 0; chessboard[5][1] = 0; chessboard[5][2] = 0; chessboard[5][3] = 0; chessboard[5][4] = 0; chessboard[5][5] = 0; chessboard[5][6] = 0; chessboard[5][7] = 0; chessboard[5][8] = 0;
		chessboard[6][0] = 14;chessboard[6][1] = 0; chessboard[6][2] = 14;chessboard[6][3] = 0; chessboard[6][4] = 14;chessboard[6][5] = 0; chessboard[6][6] = 14;chessboard[6][7] = 0; chessboard[6][8] = 14;
		chessboard[7][0] = 0; chessboard[7][1] = 11;chessboard[7][2] = 0; chessboard[7][3] = 0; chessboard[7][4] = 0; chessboard[7][5] = 0; chessboard[7][6] = 0; chessboard[7][7] = 11;chessboard[7][8] = 0;
		chessboard[8][0] = 0; chessboard[8][1] = 0; chessboard[8][2] = 0; chessboard[8][3] = 0; chessboard[8][4] = 0; chessboard[8][5] = 0; chessboard[8][6] = 0; chessboard[8][7] = 0; chessboard[8][8] = 0;
		chessboard[9][0] = 9; chessboard[9][1] = 10;chessboard[9][2] = 13;chessboard[9][3] = 12;chessboard[9][4] = 8; chessboard[9][5] = 12;chessboard[9][6] = 13;chessboard[9][7] = 10;chessboard[9][8] = 9;
		
		//重置下棋标记
		resetChessmanPosition();
		
	}
	/**
	 * 重新设置下棋标记位
	 */
	public static void resetChessmanPosition(){
		//重置下棋标记
		redChessmanStart[0] = -1;
		redChessmanStart[1] = -1;
		redChessmanStop[0] = -1;
		redChessmanStop[1] = -1;
		blackChessmanStart[0] = -1;
		blackChessmanStart[1] = -1;
		blackChessmanStop[0] = -1;
		blackChessmanStop[1] = -1;
	}
	
	
}
