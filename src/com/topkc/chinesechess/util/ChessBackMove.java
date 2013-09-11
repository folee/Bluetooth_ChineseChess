package com.topkc.chinesechess.util;

/**
 * ������Ҫ�����ݽṹ
 * @author WEI
 *
 */
public class ChessBackMove {

	public ChessMove chessMove;//��ǰ�ƶ����ӵ���ʼλ�ú�����λ��
	
	public int eatedChessman = 0;//���Ե�������,�����ǿյ�
	
	public int[] redChessmanStart = {-1,-1};//��ɫ���ӿ�ʼλ��[X,Y]
	public int[] redChessmanStop = {-1,-1};//��ɫ����ֹͣλ��[X,Y]
	public int[] blackChessmanStart = {-1,-1};//��ɫ���ӿ�ʼλ��[X,Y]
	public int[] blackChessmanStop = {-1,-1};//��ɫ����ֹͣλ��[X,Y]
	
	public ChessBackMove(){
		chessMove = new ChessMove();
	}
	
	//�õ���ǰ���岽��Ϊ��һ��
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
