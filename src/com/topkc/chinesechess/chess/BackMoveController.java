package com.topkc.chinesechess.chess;

import java.util.ArrayList;
import java.util.List;

import com.topkc.chinesechess.util.ChessBackMove;

public class BackMoveController {
	
	public static List<ChessBackMove> backMoveList = new ArrayList() ;//����������岽���List
	
	/**
	 * ���岽�裺�����õ���Ȩ����ܻ���
	 * 1.����ո��µ��ǲ��巵��
	 * 2.�����µ���һ���巵��
	 * 3.��Ȩ���ں�������
	 * 4.���List�������������
	 * @param chessboard
	 */
	public static void backMove(int chessboard[][]){
		System.out.println("Will Back Move and ListSize is :"+backMoveList.size());
		//��Ҫ����List�����������岽����ܻ��壬һ���Ǻ��壬��һ���Ǻ���
		if(backMoveList.size()>=2&&ChineseChess.isRedTurn == true){
			System.out.println("Back Move and ListSize is :"+backMoveList.size());
			//�õ�����ո��µ��ǲ��� 
			ChessBackMove blackChessMove = (ChessBackMove)backMoveList.get(backMoveList.size()-1);
			//�������������껹ԭ
			ChineseChess.chessboard[blackChessMove.chessMove.getFromX()][blackChessMove.chessMove.getFromY()] = 
				ChineseChess.chessboard[blackChessMove.chessMove.getToX()][blackChessMove.chessMove.getToY()];
			//�ѳԵ������ӻ�ԭ
			ChineseChess.chessboard[blackChessMove.chessMove.getToX()][blackChessMove.chessMove.getToY()] = 
				blackChessMove.eatedChessman;
			//�ָ�������;1.�ָ������ǣ�2.��������ǡ���ǰ��ȨΪ����
			ChineseChess.blackChessmanStart[0] = blackChessMove.blackChessmanStart[0];
			ChineseChess.blackChessmanStart[1] = blackChessMove.blackChessmanStart[1];
			ChineseChess.blackChessmanStop[0] = blackChessMove.blackChessmanStop[0];
			ChineseChess.blackChessmanStop[1] = blackChessMove.blackChessmanStop[1];
			ChineseChess.redChessmanStart[0] = -1;ChineseChess.redChessmanStart[1] = -1;//���������
			ChineseChess.redChessmanStop[0] = -1;ChineseChess.redChessmanStop[1] = -1;
			//ɾ����������
			backMoveList.remove(backMoveList.size()-1);
			
			
			//�õ�����ո��µ��ǲ���
			ChessBackMove redChessMove = (ChessBackMove)backMoveList.get(backMoveList.size()-1);
			//�������������껹ԭ
			ChineseChess.chessboard[redChessMove.chessMove.getFromX()][redChessMove.chessMove.getFromY()] = 
				ChineseChess.chessboard[redChessMove.chessMove.getToX()][redChessMove.chessMove.getToY()];
			//�ѳԵ������ӻ�ԭ
			ChineseChess.chessboard[redChessMove.chessMove.getToX()][redChessMove.chessMove.getToY()] = 
				redChessMove.eatedChessman;
			//ɾ����������
			backMoveList.remove(backMoveList.size()-1);
		}
	}
	
	//�������List���������¿�ʼ��Ϸʱ
	public static void clearBackMoveList(){
		backMoveList.clear();
	}
	
}
