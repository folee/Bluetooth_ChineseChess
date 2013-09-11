package com.topkc.chinesechess.bluetooth;

/**
 * 常量类，用来保存象棋布局位置的信息等
 * @author WEI
 *
 */
public class BluetoothChessLayout {
	
	public static final int CHESSBOARD_TOP_SPACING = 131;
	public static final int CHESSBOARD_LEFT_SPACING = 10;
	public static final int CHESSMAN_SPACING = 52;
	
	//GameView界面按钮的坐标
	public static final int GAME_VIEW_BUTTON_NEWGAME_X = CHESSBOARD_LEFT_SPACING;
	public static final int GAME_VIEW_BUTTON_NEWGAME_Y = CHESSBOARD_TOP_SPACING+10*BluetoothChessLayout.CHESSMAN_SPACING+10;
	public static final int GAME_VIEW_BUTTON_BACK_X = CHESSBOARD_LEFT_SPACING+290;
	public static final int GAME_VIEW_BUTTON_BACK_Y = CHESSBOARD_TOP_SPACING+10*BluetoothChessLayout.CHESSMAN_SPACING+10;	
	
	
	
}
