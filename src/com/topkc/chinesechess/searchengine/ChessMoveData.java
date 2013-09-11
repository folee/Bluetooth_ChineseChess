package com.topkc.chinesechess.searchengine;

/**
 * ���ӵ����߲���
 * @author WEI
 *
 */
public class ChessMoveData {
	
	public ChessmanPosition from ;//���ĸ��������
	public ChessmanPosition to ;//��Ҫ�����ĸ�����
	public int ChessID;
	public int score;
	
	public ChessMoveData(){
		from = new ChessmanPosition();
		to = new ChessmanPosition();
	}
	
	public ChessMoveData(int ChessID, int fromX,int fromY,int toX,int toY,int score){//������
		this.ChessID = ChessID;//���ӵ�����
		this.from.x = fromX;//���ӵ���ʼ����
		this.from.y = fromY;
		this.to.x = toX;//���ӵ�Ŀ���x����
		this.to.y = toY;//���ӵ�Ŀ���y����
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "ChessMoveData [ChessID=" + ChessID + ", fromX=" + from.x + ", fromY=" + from.y
				+ ", score=" + score + ", toX=" + to.x + ", toY=" + to.y + "]";
	}
	
}

