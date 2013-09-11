package com.topkc.chinesechess.util;

/**
 * 悔棋需要的数据结构
 * @author WEI
 *
 */
public class ChessBackMove {

	public ChessMove chessMove;//当前移动棋子的起始位置和落棋位置
	
	public int eatedChessman = 0;//被吃掉的棋子,可以是空的
	
	public int[] redChessmanStart = {-1,-1};//红色棋子开始位置[X,Y]
	public int[] redChessmanStop = {-1,-1};//红色棋子停止位置[X,Y]
	public int[] blackChessmanStart = {-1,-1};//黑色棋子开始位置[X,Y]
	public int[] blackChessmanStop = {-1,-1};//黑色棋子停止位置[X,Y]
	
	public ChessBackMove(){
		chessMove = new ChessMove();
	}
	
	//得到当前下棋步骤为哪一方
	public boolean isRedChessman(){
		if (chessMove.ChessID <= 14 && chessMove.ChessID >= 8) {
			return true;
		}
		return false;
	}
	public boolean isBlackChessman(){
		if (chessMove.ChessID <= 7 && chessMove.ChessID >= 1) {
			return true;
		}
		return false;
	}
	
	
	
	public ChessMove getChessMove() {
		return chessMove;
	}

	public void setChessMove(ChessMove chessMove) {
		this.chessMove = chessMove;
	}

	public int getEatedChessman() {
		return eatedChessman;
	}

	public void setEatedChessman(int eatedChessman) {
		this.eatedChessman = eatedChessman;
	}

	public int[] getRedChessmanStart() {
		return redChessmanStart;
	}

	public void setRedChessmanStart(int[] redChessmanStart) {
		this.redChessmanStart = redChessmanStart;
	}

	public int[] getRedChessmanStop() {
		return redChessmanStop;
	}

	public void setRedChessmanStop(int[] redChessmanStop) {
		this.redChessmanStop = redChessmanStop;
	}

	public int[] getBlackChessmanStart() {
		return blackChessmanStart;
	}

	public void setBlackChessmanStart(int[] blackChessmanStart) {
		this.blackChessmanStart = blackChessmanStart;
	}

	public int[] getBlackChessmanStop() {
		return blackChessmanStop;
	}

	public void setBlackChessmanStop(int[] blackChessmanStop) {
		this.blackChessmanStop = blackChessmanStop;
	}
	
	
}
