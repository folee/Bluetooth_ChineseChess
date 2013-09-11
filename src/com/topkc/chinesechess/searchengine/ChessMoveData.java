package com.topkc.chinesechess.searchengine;

/**
 * 棋子的行走步骤
 * @author WEI
 *
 */
public class ChessMoveData {
	
	public ChessmanPosition from ;//从哪个坐标过来
	public ChessmanPosition to ;//将要到达哪个坐标
	public int ChessID;
	public int score;
	
	public ChessMoveData(){
		from = new ChessmanPosition();
		to = new ChessmanPosition();
	}
	
	public ChessMoveData(int ChessID, int fromX,int fromY,int toX,int toY,int score){//构造器
		this.ChessID = ChessID;//棋子的类型
		this.from.x = fromX;//棋子的起始坐标
		this.from.y = fromY;
		this.to.x = toX;//棋子的目标点x坐标
		this.to.y = toY;//棋子的目标点y坐标
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "ChessMoveData [ChessID=" + ChessID + ", fromX=" + from.x + ", fromY=" + from.y
				+ ", score=" + score + ", toX=" + to.x + ", toY=" + to.y + "]";
	}
	
}

