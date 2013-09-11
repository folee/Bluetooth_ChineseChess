package com.topkc.chinesechess.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;

import com.topkc.chinesechess.R;

/**
 * 该类是象棋的规则类，其他类通过调用canMove方法给出起始位置与结束位置 其他类通过调用searchAGoodMove方法得到最当然棋局最好的走法
 * 而其中的allPossibleMoves方法得到的是当前棋局的所有的走法
 * 
 * @author WEI
 */
public class ChineseChessRule {

	boolean isRedGo = false;// 是不是红方走棋
	int[][] chessboard;// 声明棋盘
	
	private MediaPlayer chessmanMoveSound = null;//下棋的声音
	private MediaPlayer chessmanKillSound = null;//吃棋的声音
	
	Context context = null;
	
	public ChineseChessRule(int[][] chessboard,Context context) {
		this.chessboard = chessboard;
		this.context = context;
		
		chessmanMoveSound = MediaPlayer.create(context, R.raw.chessman_move);//加载棋子移动声音
		chessmanKillSound = MediaPlayer.create(context, R.raw.kill_chessman);//加载吃棋子声音
		
	}

	/**
	 * 通过传递整个棋盘和当前位置，计算出下一步可以落棋的位置
	 * 
	 * @param chessboard
	 * @param thisX
	 * @param thisY
	 * @return
	 */
	public ArrayList canMovePos(int[][] chessboard, int thisX, int thisY) {

		ArrayList movePosList = new ArrayList();

		for (int i = 0; i < chessboard.length; i++) {
			for (int j = 0; j < chessboard[i].length; j++) {// 绘制棋子
				// 判断两颗棋子是不是属于同一方，不是就能移动到那个位置，是就不能
				if (!this
						.isSameSide(chessboard[thisX][thisY], chessboard[i][j])) {
					// 遍历整个棋盘，判断每个位置能不能落棋
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
	 * 根据当前坐标判断是不是红色的棋子
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public boolean isRedChessman(int X, int Y) {

		if (X == -1 || Y == -1)
			return false;// 按键位置不合法返回
		if (chessboard[X][Y] <= 14 && chessboard[X][Y] >= 8) {// 判断点击的是否是自己的棋子
			return true;
		}
		return false;
	}

	public boolean canMove(int[][] chessboard, int fromY, int fromX, int toY,
			int toX) {
		int i = 0;
		int j = 0;
		int moveChessID;// 起始位置是什么棋子
		int targetID;// 目的地是什么棋子或空地
		if (toX < 0) {// 当左边出界时
			return false;
		}
		if (toX > 8) {// 当右边出界时
			return false;
		}
		if (toY < 0) {// 当上边出界时
			return false;
		}
		if (toY > 9) {// 当下边出界时
			return false;
		}
		if (fromX == toX && fromY == toY) {// 目的地与出发点相同，
			return false;
		}
		moveChessID = chessboard[fromY][fromX];// 得到起始棋子
		targetID = chessboard[toY][toX];// 得带终点棋子
		if (isSameSide(moveChessID, targetID)) {// 如果是同一阵营的
			return false;
		}
		switch (moveChessID) {
		case 1:// 黑帅
			if (toY > 2 || toX < 3 || toX > 5) {// 出了九宫格
				return false;
			}
			if ((Math.abs(fromY - toY) + Math.abs(toX - fromX)) > 1) {// 只能走一步
				return false;
			}
			break;
		case 5:// 黑士
			if (toY > 2 || toX < 3 || toX > 5) {// 出了九宫格
				return false;
			}
			if (Math.abs(fromY - toY) != 1 || Math.abs(toX - fromX) != 1) {// 走斜线
				return false;
			}
			break;
		case 6:// 黑象
			if (toY > 4) {// 不能过河
				return false;
			}
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2) {// 相走“田”字
				return false;
			}
			if (chessboard[(fromY + toY) / 2][(fromX + toX) / 2] != 0) {
				return false;// 相眼处有棋子
			}
			break;
		case 7:// 黑兵
			if (toY < fromY) {// 不能回头
				return false;
			}
			if (fromY < 5 && fromY == toY) {// 过河前只能直走
				return false;
			}
			if (toY - fromY + Math.abs(toX - fromX) > 1) {// 只能走一步，并且是直线
				return false;
			}
			break;
		case 8:// 红将
			if(chessboard[toY][toX] == 1){//当前红将要吃掉黑将
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
				if (toY < 7 || toX > 5 || toX < 3) {// 出了九宫格
					return false;
				}
				if ((Math.abs(fromY - toY) + Math.abs(toX - fromX)) > 1) {// 只能走一步
					return false;
				}
			}
			break;
		case 2:// 黑车
		case 9:// 红车
			if (fromY != toY && fromX != toX) {// 只能走直线
				return false;
			}
			if (fromY == toY) {// 走横线
				if (fromX < toX) {// 向右走
					for (i = fromX + 1; i < toX; i++) {// 循环
						if (chessboard[fromY][i] != 0) {
							return false;// 返回false
						}
					}
				} else {// 向左走
					for (i = toX + 1; i < fromX; i++) {// 循环
						if (chessboard[fromY][i] != 0) {
							return false;// 返回false
						}
					}
				}
			} else {// 走的是竖线
				if (fromY < toY) {// 向右走
					for (j = fromY + 1; j < toY; j++) {
						if (chessboard[j][fromX] != 0)
							return false;// 返回false
					}
				} else {// 想左走
					for (j = toY + 1; j < fromY; j++) {
						if (chessboard[j][fromX] != 0)
							return false;// 返回false
					}
				}
			}
			break;
		case 10:// 红马
		case 3:// 黑马
			if (!((Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 2) || (Math
					.abs(toX - fromX) == 2 && Math.abs(toY - fromY) == 1))) {
				return false;// 马走的不是日字时
			}
			if (toX - fromX == 2) {// 向右走
				i = fromX + 1;// 移动
				j = fromY;
			} else if (fromX - toX == 2) {// 向左走
				i = fromX - 1;// 移动
				j = fromY;
			} else if (toY - fromY == 2) {// 向下走
				i = fromX;// 移动
				j = fromY + 1;
			} else if (fromY - toY == 2) {// 向上走
				i = fromX;// 移动
				j = fromY - 1;
			}
			if (chessboard[j][i] != 0)
				return false;// 绊马腿
			break;
		case 11:// 红砲
		case 4:// 黑炮
			if (fromY != toY && fromX != toX) {// 炮走直线
				return false;// 返回false
			}
			if (chessboard[toY][toX] == 0) {// 不吃子时
				if (fromY == toY) {// 横线
					if (fromX < toX) {// 想右走
						for (i = fromX + 1; i < toX; i++) {
							if (chessboard[fromY][i] != 0) {
								return false;// 返回false
							}
						}
					} else {// 向走走
						for (i = toX + 1; i < fromX; i++) {
							if (chessboard[fromY][i] != 0) {
								return false;// 返回false
							}
						}
					}
				} else {// 竖线
					if (fromY < toY) {// 向下走
						for (j = fromY + 1; j < toY; j++) {
							if (chessboard[j][fromX] != 0) {
								return false;// 返回false
							}
						}
					} else {// 向上走
						for (j = toY + 1; j < fromY; j++) {
							if (chessboard[j][fromX] != 0) {
								return false;// 返回false
							}
						}
					}
				}
			} else {// 吃子时
				int count = 0;
				if (fromY == toY) {// 走的是横线
					if (fromX < toX) {// 向右走
						for (i = fromX + 1; i < toX; i++) {
							if (chessboard[fromY][i] != 0) {
								count++;
							}
						}
						if (count != 1) {
							return false;// 返回false
						}
					} else {// 向左走
						for (i = toX + 1; i < fromX; i++) {
							if (chessboard[fromY][i] != 0) {
								count++;
							}
						}
						if (count != 1) {
							return false;// 返回false
						}
					}
				} else {// 走的是竖线
					if (fromY < toY) {// 向下走
						for (j = fromY + 1; j < toY; j++) {
							if (chessboard[j][fromX] != 0) {
								count++;// 返回false
							}
						}
						if (count != 1) {
							return false;// 返回false
						}
					} else {// 向上走
						for (j = toY + 1; j < fromY; j++) {
							if (chessboard[j][fromX] != 0) {
								count++;// 返回false
							}
						}
						if (count != 1) {
							return false;// 返回false
						}
					}
				}
			}
			break;
		case 12:// 红仕
			if (toY < 7 || toX > 5 || toX < 3) {// 出了九宫格
				return false;
			}
			if (Math.abs(fromY - toY) != 1 || Math.abs(toX - fromX) != 1) {// 走斜线
				return false;
			}
			break;
		case 13:// 红相
			if (toY < 5) {// 不能过河
				return false;// 返回false
			}
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2) {// 相走“田”字
				return false;// 返回false
			}
			if (chessboard[(fromY + toY) / 2][(fromX + toX) / 2] != 0) {
				return false;// 相眼处有棋子
			}
			break;
		case 14:// 红卒
			if (toY > fromY) {// 不能回头
				return false;
			}
			if (fromY > 4 && fromY == toY) {
				return false;// 不让走
			}
			if (fromY - toY + Math.abs(toX - fromX) > 1) {// 只能走一步，并且是直线
				return false;// 返回false不让走
			}
			break;
		default:
			return false;
		}
		return true;
	}
	
	
	/**
	 * 将棋子从A坐标下到B坐标，其中包括合法检查，合法返回true
	 * 1.判断To位置可不可达
	 * 2.点击的是空白位置
	 * 3.点击的是对方棋子，
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
		
		if(this.canMove(chessboard, fromX, fromY, toX, toY)){//1.判断可不可达
			
			if(chessboard[toX][toY]==0){//2.判断点击的是不是空白位置
				chessmanMoveSound.start();//播放棋子移动声音
				chessboard[toX][toY] = chessboard[fromX][fromY];
				chessboard[fromX][fromY] = 0;
			}else if(!flagSameSide){//3.判断点击的是对方棋子（这步其实可以省略）
				chessmanKillSound.start();//播放吃棋子声音
				chessboard[toX][toY] = chessboard[fromX][fromY];
				chessboard[fromX][fromY] = 0;
			}
		}
		
	}
	
	
	/**
	 * 通过两个Chessman的ID来判断是不是同一方的棋子
	 * 
	 * @param moveChessID
	 * @param targetID
	 * @return
	 */
	public boolean isSameSide(int moveChessID, int targetID) {// 判断两个子是否为同一阵营
		if (targetID == 0) {// 当目标地位空地时
			return false;
		}
		if (moveChessID > 7 && targetID > 7) {// 当都为红色棋子时
			return true;
		} else if (moveChessID <= 7 && targetID <= 7) {// 都为黑色棋子时
			return true;
		} else {// 其他情况
			return false;
		}
	}
	
	/**
	 * 判断红棋是否被打败
	 * 传递进来当前棋盘数组
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
	 * 判断黑棋是否被打败
	 * 传递进来当前棋盘数组
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
	 * 电脑是通过该方法得到当前棋局最好的走法
	 */
	public ChessMove searchAGoodMove(int[][] qizi){//查询一个好的走法
		List<ChessMove> ret = allPossibleMoves(qizi);//产生所有走法
		/*
		try {
			Thread.sleep(4000);//睡眠四秒钟，以便调试
		} catch (InterruptedException e) {//捕获异常
			e.printStackTrace();//打印堆栈信息
		}
		*/
		return ret.get((int)(Math.random()*ret.size()));
	}
	
	
	public List<ChessMove> allPossibleMoves(int qizi[][]){//产生所有可能的走法
		 List<ChessMove> ret = new ArrayList<ChessMove>();//用来装所有可能的走法 
		 for (int x = 0; x < 10; x++){
			 for (int y = 0; y < 9; y++){//循环所有的棋牌位置
				 int chessman = qizi[x][y];
				 if (chessman != 0){//当次位置不为空时，即有棋子时
					 if(chessman > 7){//是红方，即是玩家棋子时跳过
						 continue;
					 }
					 switch (chessman){
					 	case 1://黑帅
					 		if(canMove(qizi, x, y, x, y+1)){//向下走一格
	                    		ret.add(new ChessMove(chessman, x, y, x, y+1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x, y-1)){//向上走一格
	                    		ret.add(new ChessMove(chessman, x, y, x, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y)){//向左走一格
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-1, y)){//向右走一格
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y, 0));
	                    	}
	                    	break;
						case 5://黑士
						case 12://红仕
							if(canMove(qizi, x, y, x-1, y+1)){//左下走
								ret.add(new ChessMove(chessman, x, y, x-1, y+1, 1));
							}
							if(canMove(qizi, x, y, x-1, y-1)){//左上走
								ret.add(new ChessMove(chessman, x, y, x-1, y-1, 1));
							}
							if(canMove(qizi, x, y, x+1, y+1)){//右下走
								ret.add(new ChessMove(chessman, x, y, x+1, y+1, 1));
							}
							if(canMove(qizi, x, y, x+1, y-1)){//右上走
								ret.add(new ChessMove(chessman, x, y, x+1, y-1, 1));
							}
							break;
	         			case 6://黑象
	         			case 13://红相
							if(canMove(qizi, x, y, x-2, y+2)){//左上走
								ret.add(new ChessMove(chessman, x, y, x-2, y+2, 1));
							}
							if(canMove(qizi, x, y, x-2, y-2)){//左下走
								ret.add(new ChessMove(chessman, x, y, x-2, y-2, 1));
							}
							if(canMove(qizi, x, y, x+2, y+2)){//右下走
								ret.add(new ChessMove(chessman, x, y, x+2, y+2, 1));
							}
							if(canMove(qizi, x, y, x+2, y-2)){//右上走
								ret.add(new ChessMove(chessman, x, y, x+2, y-2, 1));
							}
	         				break;
	         			case 7://黑兵
         					if(canMove(qizi, x, y, x, y+1)){//直走
         						ret.add(new ChessMove(chessman, x, y, x, y+1, 2));
         					}
         					if(y >= 5){//过河了 
                                if (canMove(qizi, x, y, x - 1, y)) {//过河后向左走
                                    ret.add(new ChessMove(chessman, x, y, x - 1, y, 2));
                                }
                                if (canMove(qizi, x, y, x + 1, y)) {//过河走向右走
                                    ret.add(new ChessMove(chessman, x, y, x + 1, y, 2));
                                }
         					}
         					break;
	         			case 14://红兵
         					if(canMove(qizi, x, y, x, y-1)){//向前走
         						ret.add(new ChessMove(chessman, x, y, x, y-1, 2));
         					}
         					if(y <=4 ){//过河了
                                if (canMove(qizi, x, y, x - 1, y)) {//过河后向左走
                                    ret.add(new ChessMove(chessman, x, y, x - 1, y, 2));
                                }
                                if (canMove(qizi, x, y, x + 1, y)) {//过河走向右走
                                    ret.add(new ChessMove(chessman, x, y, x + 1, y, 2));
                                }
         					}
	         				break;
	         			case 8://红将
	                    	if(canMove(qizi, x, y, x, y+1)){//向下走一格
	                    		ret.add(new ChessMove(chessman, x, y, x, y+1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x, y-1)){//向上走一格
	                    		ret.add(new ChessMove(chessman, x, y, x, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y)){//向右走一格
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-1, y)){//向左走一格
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y, 0));
	                    	}
	                    	break;
	         			case 2://黑车
	         			case 9://红车
	         				for(int i=y+1; i<10; i++){//向下走
	         					if(canMove(qizi, x, y, x, i)){ //可以走时
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}else{//不可以走时直接 break
	         						break;
	         					}
	         				}
	         				for(int i=y-1; i>-1; i++){//向上走
	         					if(canMove(qizi, x, y, x, i)){//可以走时
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}else{//不可以走时
	         						break;
	         					}
	         				}
	         				for(int j=x-1; j>-1; j++){//向走走
	         					if(canMove(qizi, x, y, j, y)){//可以走时 
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}else{//不可以走时
	         						break;
	         					}
	         				}
	         				for(int j=x+1; j<9; j++){//向右走
	         					if(canMove(qizi, x, y, j, y)){//可以走时
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}else{//不可以走时
	         						break;
	         					}
	         				}
	         				break;
	         			case 10://红马 
	         			case 3://黑马
	                    	if(canMove(qizi, x, y, x-1, y-2)){//向上左走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y-2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-1, y+2)){//向下走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x-1, y+2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y-2)){//向上右走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y-2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+1, y+2)){//向下右走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x+1, y+2, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-2, y-1)){//向上右走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x-2, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x-2, y+1)){//向下右走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x-2, y+1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+2, y-1)){//向上右走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x+2, y-1, 0));
	                    	}
	                    	if(canMove(qizi, x, y, x+2, y+1)){//向下右走“日”字
	                    		ret.add(new ChessMove(chessman, x, y, x+2, y+1, 0));
	                    	}
	         				break;
	         			case 11://红砲
	         			case 4://黑炮
	         				for(int i=y+1; i<10; i++){//向下走时
	         					if(canMove(qizi, x, y, x, i)){//当可以走时
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}
	         				}
	         				for(int i=y-1; i>-1; i--){//向上走时
	         					if(canMove(qizi, x, y, x, i)){//当可以走时
	         						ret.add(new ChessMove(chessman, x, y, x, i, 0));
	         					}
	         				}
	         				for(int j=x-1; j>-1; j--){//向左走时
	         					if(canMove(qizi, x, y, j, y)){//当可以走时
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}
	         				}
	         				for(int j=x+1; j<9; j++){//向右走时
	         					if(canMove(qizi, x, y, j, y)){//当可以走时
	         						ret.add(new ChessMove(chessman, x, y, j, y, 0));
	         					}
	         				}
	         				break;
					}
				 }
			 }
		 }
		 return ret.isEmpty() ? null : ret;//当ret中没有走法时，返回空，有时返回ret
	}
}