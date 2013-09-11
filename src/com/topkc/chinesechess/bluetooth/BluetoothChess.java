package com.topkc.chinesechess.bluetooth;

public class BluetoothChess {
	
    //�жϵ�ǰ��Service = true   Client = false
    public static boolean isService = true;
    public static boolean isJoinManReady = false;  //������Ϸ������Ƿ�׼������
    public static boolean isRedTrun = true;   //��ǰ��������
	
	public static final int RED_PLAYER = 1;//��ɫ���
	public static final int BLACK_PLAYER = 2;//��ɫ���
	public static final int CLIENT = 1;//client
	public static final int SERVER = 2;//Server
	
	public static final String START_GAME = "001";//��ʼ��Ϸָ��
	public static final String IS_READY = "002";//�ͻ����Ѿ�׼����
	
	//����������淢����ָ����Ϣ
	public static final int CHANGE_CLIENT_STATE = 101; 
	public static final int MOVE_TO = 102; 
	
	//��������Ϸ������������ָ����Ϣ
	public static final int SEND_CHESS_MOVE = 201; 
	public static final int CHANGE_IMAGE_TO_RED = 202; 
	public static final int CHANGE_IMAGE_TO_BLACK = 203; 
	public static final int RESTART_GAME = 204; 
	
	
	//��Ϸͨ��ָ��
	public static final String CHESS_MOVE = "01";//��ʼ��Ϸָ��

}
