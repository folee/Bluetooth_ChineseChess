package com.topkc.chinesechess.util;

/**
 * 该类为棋子的一个走法
 * 包含是什么棋子
 * 起始点的位置
 * 目标点的位置
 * 以及估值时所用到的score
 * @author WEI
 *
 */
public class ChessMove {
	int ChessID;//表明是什么棋子
	int fromX;//起始的坐标
	int fromY;
	int toX;//目的地的坐标
	int toY;
	int score;//值,估值时会用到
	
	public ChessMove(){//空构造器
		
	}
	
	public ChessMove(int ChessID, int fromX,int fromY,int toX,int toY,int score){//构造器
		this.ChessID = ChessID;//棋子的类型
		this.fromX = fromX;//棋子的起始坐标
		this.fromY = fromY;
		this.toX = toX;//棋子的目标点x坐标
		this.toY = toY;//棋子的目标点y坐标
		this.score = score;
	}
	
	
	public int getChessID() {
		return ChessID;
	}
	public void setChessID(int chessID) {
		ChessID = chessID;
	}
	public int getFromX() {
		return fromX;
	}
	public void setFromX(int fromX) {
		this.fromX = fromX;
	}
	public int getFromY() {
		return fromY;
	}
	public void setFromY(int fromY) {
		this.fromY = fromY;
	}
	public int getToX() {
		return toX;
	}
	public void setToX(int toX) {
		this.toX = toX;
	}
	public int getToY() {
		return toY;
	}
	public void setToY(int toY) {
		this.toY = toY;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
