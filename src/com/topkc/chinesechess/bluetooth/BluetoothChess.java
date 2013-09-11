package com.topkc.chinesechess.bluetooth;

public class BluetoothChess {
	
    //判断当前是Service = true   Client = false
    public static boolean isService = true;
    public static boolean isJoinManReady = false;  //加入游戏的玩家是否准备好了
    public static boolean isRedTrun = true;   //当前红棋下棋
	
	public static final int RED_PLAYER = 1;//红色玩家
	public static final int BLACK_PLAYER = 2;//黑色玩家
	public static final int CLIENT = 1;//client
	public static final int SERVER = 2;//Server
	
	public static final String START_GAME = "001";//开始游戏指令
	public static final String IS_READY = "002";//客户端已经准备好
	
	//服务器向界面发出的指令信息
	public static final int CHANGE_CLIENT_STATE = 101; 
	public static final int MOVE_TO = 102; 
	
	//界面向游戏服务器发出的指令信息
	public static final int SEND_CHESS_MOVE = 201; 
	public static final int CHANGE_IMAGE_TO_RED = 202; 
	public static final int CHANGE_IMAGE_TO_BLACK = 203; 
	public static final int RESTART_GAME = 204; 
	
	
	//游戏通信指令
	public static final String CHESS_MOVE = "01";//开始游戏指令

}
