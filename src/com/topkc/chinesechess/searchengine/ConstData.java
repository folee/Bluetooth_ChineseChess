package com.topkc.chinesechess.searchengine;

//数据表示
public class ConstData {

	public final static int NOCHESS = 0; // 无子
	//黑方
	public final static int B_KING = 1; // 黑帅
	public final static int B_CAR = 2; // 黑车
	public final static int B_HORSE = 3; // 黑马
	public final static int B_CANON = 4; // 黑炮
	public final static int B_BISHOP = 5; // 黑士
	public final static int B_ELEPHANT = 6; // 黑象
	public final static int B_PAWN = 7; // 黑兵

	public final static int B_BEGIN = 1;
	public final static int B_END = 7;
	//红方
	public final static int R_KING = 8; // 红将
	public final static int R_CAR = 9; // 红车
	public final static int R_HORSE = 10; // 红马
	public final static int R_CANON = 11; // 红炮
	public final static int R_BISHOP = 12; // 红仕
	public final static int R_ELEPHANT = 13; // 红相
	public final static int R_PAWN = 14; // 红卒

	public final static int R_BEGIN = 8;
	public final static int R_END = 14;

	// 定义每种棋的价值
	// 兵100，士250，象250，马350，炮350车500
	public final static int BASEVALUE_PAWN = 100;
	public final static int BASEVALUE_BISHOP = 250;
	public final static int BASEVALUE_ELEPHANT = 250;
	public final static int BASEVALUE_HORSE = 350;
	public final static int BASEVALUE_CANON = 350;
	public final static int BASEVALUE_CAR = 500;
	public final static int BASEVALUE_KING = 10000;

	// 定义各种棋子的灵活性
	// 也就是每多一个可走位置应加上的值
	// 兵15，士1，象1，车6，马12，炮6，王0
	public final static int FLEXIBLE_PAWN = 15;
	public final static int FLEXIBLE_BISHOP = 1;
	public final static int FLEXIBLE_ELEPHANT = 1;
	public final static int FLEXIBLE_HORSE = 12;
	public final static int FLEXIBLE_CANON = 6;
	public final static int FLEXIBLE_CAR = 6;
	public final static int FLEXIBLE_KING = 0;

	// 初始棋盘
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

	// 判断chessID是否是黑棋
	public static boolean isBlack(int chessID) {
		return (chessID >= B_BEGIN && chessID <= B_END);
	}

	// 判断chessID是否是红棋
	public static boolean isRed(int chessID) {
		return (chessID >= R_BEGIN && chessID <= R_END);
	}

	// 判断chessID1和chessID2是否是同色
	public static boolean isSameSide(int chessID1, int chessID2) {
		return ((isBlack(chessID1) && isBlack(chessID2)) || (isRed(chessID1) && isRed(chessID2)));
	}

}