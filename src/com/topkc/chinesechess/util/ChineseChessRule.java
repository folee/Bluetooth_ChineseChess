package com.topkc.chinesechess.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;

import com.topkc.chinesechess.R;

/**
 * ����������Ĺ����࣬������ͨ������canMove����������ʼλ�������λ�� ������ͨ������searchAGoodMove�����õ��Ȼ�����õ��߷�
 * �����е�allPossibleMoves�����õ����ǵ�ǰ��ֵ����е��߷�
 * 
 * @author WEI
 */
public class ChineseChessRule {

	boolean isRedGo = false;// �ǲ��Ǻ췽����
	int[][] chessboard;// ��������
	
	private MediaPlayer chessmanMoveSound = null;//���������
	private MediaPlayer chessmanKillSound = null;//���������
	
	Context context = null;
	
	public ChineseChessRule(int[][] chessboard,Context context) {
		this.chessboard = chessboard;
		this.context = context;
		
		chessmanMoveSound = MediaPlayer.create(context, R.raw.chessman_move);//���������ƶ�����
		chessmanKillSound = MediaPlayer.create(context, R.raw.kill_chessman);//���س���������
		
	}

	/**
	 * ͨ�������������̺͵�ǰλ�ã��������һ�����������λ��
	 * 
	 * @param chessboard
	 * @param thisX
	 * @param thisY
	 * @return
	 */
	public ArrayList canMovePos(int[][] chessboard, int thisX, int thisY) {

		ArrayList movePosList = new ArrayList();

		for (int i = 0; i < chessboard.length; i++) {
			for (int j = 0; j < chessboard[i].length; j++) {// ��������
				// �ж����������ǲ�������ͬһ�������Ǿ����ƶ����Ǹ�λ�ã��ǾͲ���
				if (!this
						.isSameSide(chessboard[thisX][thisY], chessboard[i][j])) {
					// �����������̣��ж�ÿ��λ���ܲ�������
					if (this.canMove(chessboard, thisX, thisY, i, j)) {
						int[] temp = { i, j };
						movePosList.add(temp);
					}
				}
			}
		}
		return movePosList;
	}

	/**
	 * ���ݵ�ǰ�����ж��ǲ��Ǻ�ɫ������
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public boolean isRedChessman(int X, int Y) {

		if (X == -1 || Y == -1)
			return false;// ����λ�ò��Ϸ�����
		if (chessboard[X][Y] <= 14 && chessboard[X][Y] >= 8) {// �жϵ�����Ƿ����Լ�������
			return true;
		}
		return false;
	}

	public boolean canMove(int[][] chessboard, int fromY, int fromX, int toY,
			int toX) {
		int i = 0;
		int j = 0;
		int moveChessID;// ��ʼλ����ʲô����
		int targetID;// Ŀ�ĵ���ʲô���ӻ�յ�
		if (toX < 0) {// ����߳���ʱ
			return false;
		}
		if (toX > 8) {// ���ұ߳���ʱ
			return false;
		}
		if (toY < 0) {// ���ϱ߳���ʱ
			return false;
		}
		if (toY > 9) {// ���±߳���ʱ
			return false;
		}
		if (fromX == toX && fromY == toY) {// Ŀ�ĵ����������ͬ��
			return false;
		}
		moveChessID = chessboard[fromY][fromX];// �õ���ʼ����
		targetID = chessboard[toY][toX];// �ô��յ�����
		if (isSameSide(moveChessID, targetID)) {// �����ͬһ��Ӫ��
			return false;
		}
		switch (moveChessID) {
		case 1:// ��˧
			if (toY > 2 || toX < 3 || toX > 5) {// ���˾Ź���
				return false;
			}
			if ((Math.abs(fromY - toY) + Math.abs(toX - fromX)) > 1) {// ֻ����һ��
				return false;
			}
			break;
		case 5:// ��ʿ
			if (toY > 2 || toX < 3 || toX > 5) {// ���˾Ź���
				return false;
			}
			if (Math.abs(fromY - toY) != 1 || Math.abs(toX - fromX) != 1) {// ��б��
				return false;
			}
			break;
		case 6:// ����
			if (toY > 4) {// ���ܹ���
				return false;
			}
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2) {// ���ߡ����
				return false;
			}
			if (chessboard[(fromY + toY) / 2][(fromX + toX) / 2] != 0) {
				return false;// ���۴�������
			}
			break;
		case 7:// �ڱ�
			if (toY < fromY) {// ���ܻ�ͷ
				return false;
			}
			if (fromY < 5 && fromY == toY) {// ����ǰֻ��ֱ��
				return false;
			}
			if (toY - fromY + Math.abs(toX - fromX) > 1) {// ֻ����һ����������ֱ��
				return false;
			}
			break;
		case 8:// �콫
			if(chessboard[toY][toX] == 1){//��ǰ�콫Ҫ�Ե��ڽ�
				System.out.println("----RedKing:"+toX+"-"+toY+"------");
				/*
				if (fromY != toY)
					return false;
				for (i = fromX - 1; i > toX; --i)
					if (chessboard[i][fromY] != 0) 
						return false;
				 */
				if (fromX != toX)
					return false;
				for (i = fromY - 1; i > toY; --i)
					if (chessboard[i][fromX] != 0)//NOCHESS
						return false;
			}else{
				if (toY < 7 || toX > 5 || toX < 3) {// ���˾Ź���
					return false;
				}
				if ((Math.abs(fromY - toY) + Math.abs(toX - fromX)) > 1) {// ֻ����һ��
					return false;
				}
			}
			break;
		case 2:// �ڳ�
		case 9:// �쳵
			if (fromY != toY && fromX != toX) {// ֻ����ֱ��
				return false;
			}
			if (fromY == toY) {// �ߺ���
				if (fromX < toX) {// ������
					for (i = fromX + 1; i < toX; i++) {// ѭ��
						if (chessboard[fromY][i] != 0) {
							return false;// ����false
						}
					}
				} else {// ������
					for (i = toX + 1; i < fromX; i++) {// ѭ��
						if (chessboard[fromY][i] != 0) {
							return false;// ����false
						}
					}
				}
			} else {// �ߵ�������
				if (fromY < toY) {// ������
					for (j = fromY + 1; j < toY; j++) {
						if (chessboard[j][fromX] != 0)
							return false;// ����false
					}
				} else {// ������
					for (j = toY + 1; j < fromY; j++) {
						if (chessboard[j][fromX] != 0)
							return false;// ����false
					}
				}
			}
			break;
		case 10:// ����
		case 3:// ����
			if (!((Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 2) || (Math
					.abs(toX - fromX) == 2 && Math.abs(toY - fromY) == 1))) {
				return false;// ���ߵĲ�������ʱ
			}
			if (toX - fromX == 2) {// ������
				i = fromX + 1;// �ƶ�
				j = fromY;
			} else if (fromX - toX == 2) {// ������
				i = fromX - 1;// �ƶ�
				j = fromY;
			} else if (toY - fromY == 2) {// ������
				i = fromX;// �ƶ�
				j = fromY + 1;
			} else if (fromY - toY == 2) {// ������
				i = fromX;// �ƶ�
				j = fromY - 1;
			}
			if (chessboard[j][i] != 0)
				return false;// ������
			break;
		case 11:// ��h
		case 4:// ����
			if (fromY != toY && fromX != toX) {// ����ֱ��
				return false;// ����false
			}
			if (chessboard[toY][toX] == 0) {// ������ʱ
				if (fromY == toY) {// ����
					if (fromX < toX) {// ������
						for (i = fromX + 1; i < toX; i++) {
							if (chessboard[fromY][i] != 0) {
								return false;// ����false
							}
						}
					} else {// ������
						for (i = toX + 1; i < fromX; i++) {
							if (chessboard[fromY][i] != 0) {
								return false;// ����false
							}
						}
					}
				} else {// ����
					if (fromY < toY) {// ������
						for (j = fromY + 1; j < toY; j++) {
							if (chessboard[j][fromX] != 0) {
								return false;// ����false
							}
						}
					} else {// ������
						for (j = toY + 1; j < fromY; j++) {
							if (chessboard[j][fromX] != 0) {
								return false;// ����false
							}
						}
					}
				}
			} else {// ����ʱ
				int count = 0;
				if (fromY == toY) {// �ߵ��Ǻ���
					if (fromX < toX) {// ������
						for (i = fromX + 1; i < toX; i++) {
							if (chessboard[fromY][i] != 0) {
								count++;
							}
						}
						if (count != 1) {
							return false;// ����false
						}
					} else {// ������
						for (i = toX + 1; i < fromX; i++) {
							if (chessboard[fromY][i] != 0) {
								count++;
							}
						}
						if (count != 1) {
							return false;// ����false
						}
					}
				} else {// �ߵ�������
					if (fromY < toY) {// ������
						for (j = fromY + 1; j < toY; j++) {
							if (chessboard[j][fromX] != 0) {
								count++;// ����false
							}
						}
						if (count != 1) {
							return false;// ����false
						}
					} else {// ������
						for (j = toY + 1; j < fromY; j++) {
							if (chessboard[j][fromX] != 0) {
								count++;// ����false
							}
						}
						if (count != 1) {
							return false;// ����false
						}
					}
				}
			}
			break;
		case 12:// ����
			if (toY < 7 || toX > 5 || toX < 3) {// ���˾Ź���
				return false;
			}
			if (Math.abs(fromY - toY) != 1 || Math.abs(toX - fromX) != 1) {// ��б��
				return false;
			}
			break;
		case 13:// ����
			if (toY < 5) {// ���ܹ���
				return false;// ����false
			}
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2) {// ���ߡ����
				return false;// ����false
			}
			if (chessboard[(fromY + toY) / 2][(fromX + toX) / 2] != 0) {
				return false;// ���۴�������
			}
			break;
		case 14:// ����
			if (toY > fromY) {// ���ܻ�ͷ
				return false;
			}
			if (fromY > 4 && fromY == toY) {
				return false;// ������
			}
			if (fromY - toY + Math.abs(toX - fromX) > 1) {// ֻ����һ����������ֱ��
				return false;// ����false������
			}
			break;
		default:
			return false;
		}
		return true;
	}
	
	
	/**
	 * �����Ӵ�A�����µ�B���꣬���а����Ϸ���飬�Ϸ�����true
	 * 1.�ж�Toλ�ÿɲ��ɴ�
	 * 2.������ǿհ�λ��
	 * 3.������ǶԷ����ӣ�
	 * @param chessboard
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return
	 */
	public void moveTo(int[][] chessboard,int fromX,int fromY,int toX,int toY){
		boolean flag = false;
		boolean flagSameSide = this.isSameSide(chessboard[fromX][fromY], chessboard[toX][toY]);
		
		if(this.canMove(chessboard, fromX, fromY, toX, toY)){//1.�жϿɲ��ɴ�
			
			if(chessboard[toX][toY]==0){//2.�жϵ�����ǲ��ǿհ�λ��
				chessmanMoveSound.start();//���������ƶ�����
				chessboard[toX][toY] = chessboard[fromX][fromY];
				chessboard[fromX][fromY] = 0;
			}else if(!flagSameSide){//3.�жϵ�����ǶԷ����ӣ��ⲽ��ʵ����ʡ�ԣ�
				chessmanKillSound.start();//���ų���������
				chessboard[toX][toY] = chessboard[fromX][fromY];
				chessboard[fromX][fromY] = 0;
			}
		}
		
	}
	
	
	/**
	 * ͨ������Chessman��ID���ж��ǲ���ͬһ��������
	 * 
	 * @param moveChessID
	 * @param targetID
	 * @return
	 */
	public boolean isSameSide(int moveChessID, int targetID) {// �ж��������Ƿ�Ϊͬһ��Ӫ
		if (targetID == 0) {// ��Ŀ���λ�յ�ʱ
			return false;
		}
		if (moveChessID > 7 && targetID > 7) {// ����Ϊ��ɫ����ʱ
			return true;
		} else if (moveChessID <= 7 && targetID <= 7) {// ��Ϊ��ɫ����ʱ
			return true;
		} else {// �������
			return false;
		}
	}
	
	/**
	 * �жϺ����Ƿ񱻴��
	 * ���ݽ�����ǰ��������
	 * @return
	 */
	public boolean isRedKingAlive(int[][] chessboard){
		
		for (int i = 0; i < chessboard.length; i++) {
			for (int j = 0; j < chessboard[i].length; j++) {
				int redKing = 8;
				if(chessboard[i][j] == redKing){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * �жϺ����Ƿ񱻴��
	 * ���ݽ�����ǰ��������
	 * @return
	 */
	public boolean isBlackKingAlive(int[][] chessboard){
		
		for (int i = 0; i < chessboard.length; i++) {
			for (int j = 0; j < chessboard[i].length; j++) {
				int redKing = 1;
				if(chessboard[i][j] == redKing){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * ������ͨ���÷����õ���ǰ�����õ��߷�
	 */
	public ChessMove searchAGoodMove(int[][] qizi){//��ѯһ���õ��߷�
		List<ChessMove> ret = allPossibleMoves(qizi);//���������߷�
		/*
		try {
			Thread.sleep(4000);//˯�������ӣ��Ա����
		} catch (InterruptedException e) {//�����쳣
			e.printStackTrace();//��ӡ��ջ��Ϣ
		}
		*/
		return ret.get((int)(Math.random()*ret.size()));
	}
	
	
	public List<ChessMove> allPossibleMoves(int qizi[][]){//�������п��ܵ��߷�
		 List<ChessMove> ret = new ArrayList<ChessMove>();//����װ���п��ܵ��߷� 
		 for (int x = 0; x < 10; x++){
			 for (int y = 0; y < 9; y++){//ѭ�����е�����λ��
				 int chessman = qizi[x][y];
				 if (chessman != 0){//����λ�ò�Ϊ��ʱ����������ʱ
					 if(chessman > 7){//�Ǻ췽�������������ʱ����
						 continue;
					 }
					 switch (chessman){
					 	case 1://��˧
					 		if(canMove(qizi, x, y, x, y+1)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x, y+1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x, y-1)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-1, y)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y, 0));
	                    	}
	                    	break;
						case 5://��ʿ
						case 12://����
							if(canMove(qizi, x, y, x-1, y+1)){//������
								ret.add(new ChessMove(chessman, x, y, x-1, y+1, 1));
							}
							if(canMove(qizi, x, y, x-1, y-1)){//������
								ret.add(new ChessMove(chessman, x, y, x-1, y-1, 1));
							}
							if(canMove(qizi, x, y, x+1, y+1)){//������
								ret.add(new ChessMove(chessman, x, y, x+1, y+1, 1));
							}
							if(canMove(qizi, x, y, x+1, y-1)){//������
								ret.add(new ChessMove(chessman, x, y, x+1, y-1, 1));
							}
							break;
	         			case 6://����
	         			case 13://����
							if(canMove(qizi, x, y, x-2, y+2)){//������
								ret.add(new ChessMove(chessman, x, y, x-2, y+2, 1));
							}
							if(canMove(qizi, x, y, x-2, y-2)){//������
								ret.add(new ChessMove(chessman, x, y, x-2, y-2, 1));
							}
							if(canMove(qizi, x, y, x+2, y+2)){//������
								ret.add(new ChessMove(chessman, x, y, x+2, y+2, 1));
							}
							if(canMove(qizi, x, y, x+2, y-2)){//������
								ret.add(new ChessMove(chessman, x, y, x+2, y-2, 1));
							}
	         				break;
	         			case 7://�ڱ�
         					if(canMove(qizi, x, y, x, y+1)){//ֱ��
         						ret.add(new ChessMove(chessman, x, y, x, y+1, 2));
         					}
         					if(y >= 5){//������ 
                                if (canMove(qizi, x, y, x - 1, y)) {//���Ӻ�������
                                    ret.add(new ChessMove(chessman, x, y, x - 1, y, 2));
                                }
                                if (canMove(qizi, x, y, x + 1, y)) {//������������
                                    ret.add(new ChessMove(chessman, x, y, x + 1, y, 2));
                                }
         					}
         					break;
	         			case 14://���
         					if(canMove(qizi, x, y, x, y-1)){//��ǰ��
         						ret.add(new ChessMove(chessman, x, y, x, y-1, 2));
         					}
         					if(y <=4 ){//������
                                if (canMove(qizi, x, y, x - 1, y)) {//���Ӻ�������
                                    ret.add(new ChessMove(chessman, x, y, x - 1, y, 2));
                                }
                                if (canMove(qizi, x, y, x + 1, y)) {//������������
                                    ret.add(new ChessMove(chessman, x, y, x + 1, y, 2));
                                }
         					}
	         				break;
	         			case 8://�콫
	                    	if(canMove(qizi, x, y, x, y+1)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x, y+1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x, y-1)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-1, y)){//������һ��
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y, 0));
	                    	}
	                    	break;
	         			case 2://�ڳ�
	         			case 9://�쳵
	         				for(int i=y+1; i<10; i++){//������
	         					if(canMove(qizi, x, y, x, i)){ //������ʱ
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}else{//��������ʱֱ�� break
	         						break;
	         					}
	         				}
	         				for(int i=y-1; i>-1; i++){//������
	         					if(canMove(qizi, x, y, x, i)){//������ʱ
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}else{//��������ʱ
	         						break;
	         					}
	         				}
	         				for(int j=x-1; j>-1; j++){//������
	         					if(canMove(qizi, x, y, j, y)){//������ʱ 
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}else{//��������ʱ
	         						break;
	         					}
	         				}
	         				for(int j=x+1; j<9; j++){//������
	         					if(canMove(qizi, x, y, j, y)){//������ʱ
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}else{//��������ʱ
	         						break;
	         					}
	         				}
	         				break;
	         			case 10://���� 
	         			case 3://����
	                    	if(canMove(qizi, x, y, x-1, y-2)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y-2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-1, y+2)){//�����ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y+2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y-2)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y-2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y+2)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y+2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-2, y-1)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x-2, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-2, y+1)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x-2, y+1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+2, y-1)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x+2, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+2, y+1)){//�������ߡ��ա���
	                    		ret.add(new ChessMove(chessman, x, y, x+2, y+1, 0));
	                    	}
	         				break;
	         			case 11://��h
	         			case 4://����
	         				for(int i=y+1; i<10; i++){//������ʱ
	         					if(canMove(qizi, x, y, x, i)){//��������ʱ
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}
	         				}
	         				for(int i=y-1; i>-1; i--){//������ʱ
	         					if(canMove(qizi, x, y, x, i)){//��������ʱ
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}
	         				}
	         				for(int j=x-1; j>-1; j--){//������ʱ
	         					if(canMove(qizi, x, y, j, y)){//��������ʱ
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}
	         				}
	         				for(int j=x+1; j<9; j++){//������ʱ
	         					if(canMove(qizi, x, y, j, y)){//��������ʱ
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}
	         				}
	         				break;
					}
				 }
			 }
		 }
		 return ret.isEmpty() ? null : ret;//��ret��û���߷�ʱ�����ؿգ���ʱ����ret
	}
}