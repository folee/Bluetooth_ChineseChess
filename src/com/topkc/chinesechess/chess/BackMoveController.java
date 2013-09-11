package com.topkc.chinesechess.chess;

import java.util.ArrayList;
import java.util.List;

import com.topkc.chinesechess.util.ChessBackMove;

public class BackMoveController {
	
	public static List<ChessBackMove> backMoveList = new ArrayList() ;//用来保存悔棋步骤的List
	
	/**
	 * 悔棋步骤：红棋拿到棋权后才能悔棋
	 * 1.黑棋刚刚下的那步棋返回
	 * 2.红棋下的上一步棋返回
	 * 3.棋权还在红棋手里
	 * 4.清空List中最后两条数据
	 * @param chessboard
	 */
	public static void backMove(int chessboard[][]){
		System.out.println("Will Back Move and ListSize is :"+backMoveList.size());
		//需要悔棋List中有两条下棋步骤才能悔棋，一条是黑棋，另一条是红棋
		if(backMoveList.size()>=2&&ChineseChess.isRedTurn == true){
			System.out.println("Back Move and ListSize is :"+backMoveList.size());
			//拿到黑棋刚刚下的那步棋 
			ChessBackMove blackChessMove = (ChessBackMove)backMoveList.get(backMoveList.size()-1);
			//将黑棋下棋坐标还原
			ChineseChess.chessboard[blackChessMove.chessMove.getFromX()][blackChessMove.chessMove.getFromY()] = 
				ChineseChess.chessboard[blackChessMove.chessMove.getToX()][blackChessMove.chessMove.getToY()];
			//把吃掉的棋子还原
			ChineseChess.chessboard[blackChessMove.chessMove.getToX()][blackChessMove.chessMove.getToY()] = 
				blackChessMove.eatedChessman;
			//恢复下棋标记;1.恢复黑棋标记，2.清除红棋标记。当前棋权为红棋
			ChineseChess.blackChessmanStart[0] = blackChessMove.blackChessmanStart[0];
			ChineseChess.blackChessmanStart[1] = blackChessMove.blackChessmanStart[1];
			ChineseChess.blackChessmanStop[0] = blackChessMove.blackChessmanStop[0];
			ChineseChess.blackChessmanStop[1] = blackChessMove.blackChessmanStop[1];
			ChineseChess.redChessmanStart[0] = -1;ChineseChess.redChessmanStart[1] = -1;//清除红棋标记
			ChineseChess.redChessmanStop[0] = -1;ChineseChess.redChessmanStop[1] = -1;
			//删除这条数据
			backMoveList.remove(backMoveList.size()-1);
			
			
			//拿到红棋刚刚下的那步棋
			ChessBackMove redChessMove = (ChessBackMove)backMoveList.get(backMoveList.size()-1);
			//将红棋下棋坐标还原
			ChineseChess.chessboard[redChessMove.chessMove.getFromX()][redChessMove.chessMove.getFromY()] = 
				ChineseChess.chessboard[redChessMove.chessMove.getToX()][redChessMove.chessMove.getToY()];
			//把吃掉的棋子还原
			ChineseChess.chessboard[redChessMove.chessMove.getToX()][redChessMove.chessMove.getToY()] = 
				redChessMove.eatedChessman;
			//删除这条数据
			backMoveList.remove(backMoveList.size()-1);
		}
	}
	
	//清除悔棋List，用于重新开始游戏时
	public static void clearBackMoveList(){
		backMoveList.clear();
	}
	
}
