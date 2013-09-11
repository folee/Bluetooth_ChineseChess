package com.topkc.chinesechess.searchengine;

//���ݱ�ʾ
public class ConstData {

	public final static int NOCHESS = 0; // ����
	//�ڷ�
	public final static int B_KING = 1; // ��˧
	public final static int B_CAR = 2; // �ڳ�
	public final static int B_HORSE = 3; // ����
	public final static int B_CANON = 4; // ����
	public final static int B_BISHOP = 5; // ��ʿ
	public final static int B_ELEPHANT = 6; // ����
	public final static int B_PAWN = 7; // �ڱ�

	public final static int B_BEGIN = 1;
	public final static int B_END = 7;
	//�췽
	public final static int R_KING = 8; // �콫
	public final static int R_CAR = 9; // �쳵
	public final static int R_HORSE = 10; // ����
	public final static int R_CANON = 11; // ����
	public final static int R_BISHOP = 12; // ����
	public final static int R_ELEPHANT = 13; // ����
	public final static int R_PAWN = 14; // ����

	public final static int R_BEGIN = 8;
	public final static int R_END = 14;

	// ����ÿ����ļ�ֵ
	// ��100��ʿ250����250����350����350��500
	public final static int BASEVALUE_PAWN = 100;
	public final static int BASEVALUE_BISHOP = 250;
	public final static int BASEVALUE_ELEPHANT = 250;
	public final static int BASEVALUE_HORSE = 350;
	public final static int BASEVALUE_CANON = 350;
	public final static int BASEVALUE_CAR = 500;
	public final static int BASEVALUE_KING = 10000;

	// ����������ӵ������
	// Ҳ����ÿ��һ������λ��Ӧ���ϵ�ֵ
	// ��15��ʿ1����1����6����12����6����0
	public final static int FLEXIBLE_PAWN = 15;
	public final static int FLEXIBLE_BISHOP = 1;
	public final static int FLEXIBLE_ELEPHANT = 1;
	public final static int FLEXIBLE_HORSE = 12;
	public final static int FLEXIBLE_CANON = 6;
	public final static int FLEXIBLE_CAR = 6;
	public final static int FLEXIBLE_KING = 0;

	// ��ʼ����
	public final static int INITCHESSBOARD[][] = {
			{ 2, 3, 6, 5, 1, 5, 6, 3, 2 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 4, 0, 0, 0, 0, 0, 4, 0 }, 
			{ 7, 0, 7, 0, 7, 0, 7, 0, 7 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 14, 0, 14, 0, 14, 0, 14, 0, 14 },
			{ 0, 11, 0, 0, 0, 0, 0, 11, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 9, 10, 13, 12, 8, 12, 13, 10, 9 } };

	// �ж�chessID�Ƿ��Ǻ���
	public static boolean isBlack(int chessID) {
		return (chessID >= B_BEGIN && chessID <= B_END);
	}

	// �ж�chessID�Ƿ��Ǻ���
	public static boolean isRed(int chessID) {
		return (chessID >= R_BEGIN && chessID <= R_END);
	}

	// �ж�chessID1��chessID2�Ƿ���ͬɫ
	public static boolean isSameSide(int chessID1, int chessID2) {
		return ((isBlack(chessID1) && isBlack(chessID2)) || (isRed(chessID1) && isRed(chessID2)));
	}

}