package com.topkc.chinesechess.util;

/**
 * ����Ϊ���ӵ�һ���߷�
 * ������ʲô����
 * ��ʼ���λ��
 * Ŀ����λ��
 * �Լ���ֵʱ���õ���score
 * @author WEI
 *
 */
public class ChessMove {
	int ChessID;//������ʲô����
	int fromX;//��ʼ������
	int fromY;
	int toX;//Ŀ�ĵص�����
	int toY;
	int score;//ֵ,��ֵʱ���õ�
	
	public ChessMove(){//�չ�����
		
	}
	
	public ChessMove(int ChessID, int fromX,int fromY,int toX,int toY,int score){//������
		this.ChessID = ChessID;//���ӵ�����
		this.fromX = fromX;//���ӵ���ʼ����
		this.fromY = fromY;
		this.toX = toX;//���ӵ�Ŀ���x����
		this.toY = toY;//���ӵ�Ŀ���y����
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
