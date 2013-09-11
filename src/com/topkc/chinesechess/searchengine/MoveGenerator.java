package com.topkc.chinesechess.searchengine;

/**
 * 走法产生器,一个走法的结构
 * @author WEI
 *
 */
public class MoveGenerator {
	
	public ChessMoveData moveList[][] = new ChessMoveData[10][100];
	protected int moveCount;// 记录moveList中走法的数量

	public MoveGenerator() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 100; j++)
				moveList[i][j] = new ChessMoveData();
	}

	// 判断局面position上，从from到to的走法是否合法
	// 合法返回true，否则返回alse
	public boolean isValidMove(int position[][], int fromX, int fromY,
			int toX, int toY) {
		int i;
		int moveChessID, targetID;

		// 目的与起点相同
		if (fromX == toX && fromY == toY)
			return false;

		moveChessID = position[fromY][fromX];
		targetID = position[toY][toX];

		if (ConstData.isSameSide((int) moveChessID, (int) targetID))
			return false;

		switch (moveChessID) { // 红将
		case ConstData.B_KING:
			if (targetID == ConstData.R_KING) {
				if (fromX != toX) {
					return false;
				}
				for (i = fromY + 1; i < toY; ++i)
					if (ConstData.NOCHESS != position[i][fromX])
						return false;
			} else {
				if (toY > 2 || toX < 3 || toX > 5)
					return false;// 九宫之外
				if ((Math.abs(fromX - toX) + Math.abs(fromY - toY)) > 1)
					return false;
			}
			break;

		case ConstData.B_BISHOP:// 黑士
			// 出了九宫之外
			if (toY > 2 || toX > 5 || toX < 3)
				return false;
			// 士走斜线
			if (Math.abs(fromX - toX) != 1 || Math.abs(fromY - toY) != 1)
				return false;
			break;
		case ConstData.R_BISHOP:// 红士
			// 出了九宫之外
			if (toY < 7 || toX < 3 || toX > 5)
				return false;
			// 士走斜线
			if (Math.abs(fromX - toX) != 1 || Math.abs(fromY - toY) != 1)
				return false;
			break;

		case ConstData.B_ELEPHANT:// 黑象
			if (toY > 4)
				return false;// 象不能过河
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2)
				return false;
			if (position[(fromY + toY) / 2][(fromX + toX) / 2] != ConstData.NOCHESS)
				return false;
			break;

		case ConstData.R_ELEPHANT:// 红象
			if (toY < 5)
				return false;// 象不能过河
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2)
				return false;
			if (position[(fromY + toY) / 2][(fromX + toX) / 2] != ConstData.NOCHESS)
				return false;
			break;

		case ConstData.B_PAWN:// 黑兵
			if (toY < fromY)
				return false; // 兵不能倒退
			if (fromY < 5 && toY == fromY)
				return false; // 没有过河只能往前走
			if (toY - fromY + Math.abs(fromX - toX) > 1)
				return false;
			break;

		case ConstData.R_PAWN:// 红兵
			if (toY > fromY)
				return false;// 兵不能倒退
			if (fromY > 4 && toY == fromY)
				return false;
			if (fromY - toY + Math.abs(fromX - toX) > 1)
				return false;// 兵只能走一步
			break;

		case ConstData.R_KING:// 红将
			if (targetID == ConstData.B_KING) {
				if (fromX != toX)
					return false;
				for (i = fromY - 1; i > toY; --i)
					if (position[i][fromX] != ConstData.NOCHESS)
						return false;
			} else {
				if (toX < 3 || toX > 5 || toY < 7)
					return false;
				if (Math.abs(toX - fromX) + Math.abs(toY - fromY) > 1)
					return false;
			}
			break;

		case ConstData.R_CAR: // 车
		case ConstData.B_CAR:
			if (toX != fromX && fromY != toY)
				return false;// 车走直线

			if (fromX == toX) { // 没有棋挡着
				for (i = Math.min(fromY, toY) + 1; i < Math.max(fromY, toY); ++i)
					if (position[i][fromX] != ConstData.NOCHESS)
						return false;
			} else {// 没有棋挡着
				for (i = Math.min(fromX, toX) + 1; i < Math.max(fromX, toX); ++i)
					if (position[fromY][i] != ConstData.NOCHESS)
						return false;
			}
			break;

		case ConstData.R_CANON:
		case ConstData.B_CANON:
			if (toX != fromX && fromY != toY)
				return false;// 炮走直线

			if (toX == fromX) {
				if (targetID == ConstData.NOCHESS)// 炮不是吃子
				{
					// 不吃子，要没有棋挡着
					for (i = Math.min(fromY, toY) + 1; i < Math.max(fromY, toY); ++i)
						if (position[i][fromX] != ConstData.NOCHESS)
							return false;
				} else {
					int count = 0;
					// 吃子，中间必须要有一个棋子
					for (i = Math.min(fromY, toY) + 1; i < Math.max(fromY, toY); ++i)
						if (position[i][fromX] != ConstData.NOCHESS)
							count++;
					if (1 != count)
						return false;
				}
			} else {
				if (targetID == ConstData.NOCHESS) { // 不吃子，要没有棋挡着
					for (i = Math.min(fromX, toX) + 1; i < Math.max(fromX, toX); ++i)
						if (position[fromY][i] != ConstData.NOCHESS)
							return false;
				} else {
					int count = 0;
					// 吃子，中间必须要有一个棋子
					for (i = Math.min(fromX, toX) + 1; i < Math.max(fromX, toX); ++i)
						if (position[fromY][i] != ConstData.NOCHESS)
							count++;
					if (1 != count)
						return false;
				}
			}
			break;

		case ConstData.R_HORSE:
		case ConstData.B_HORSE:
			// 马走日
			if (Math.abs(toX - fromX) + Math.abs(toY - fromY) != 3
					|| toX == fromX || toY == fromY)
				return false;
			if (Math.abs(toX - fromX) == 2) {
				// 有绊脚的棋在
				if (position[fromY][(toX + fromX) / 2] != ConstData.NOCHESS)
					return false;
			} else {
				// 有绊脚的棋在
				if (position[(toY + fromY) / 2][fromX] != ConstData.NOCHESS)
					return false;
			}
			break;

		}
		return true;
	}

	// 产生给定棋盘上的所有合法的走法
	// side 指明产生哪一方的走法，true为红方，false为黑方

	public int createPossibleMove(int position[][], int nPlay, boolean side) {
		int i, j;
		int chessID;
		moveCount = 0;
		// System.out.println("createPossibleMove1");
		for (j = 0; j < 9; ++j)
			for (i = 0; i < 10; ++i) {
				if (ConstData.NOCHESS != position[i][j]) {
					chessID = position[i][j];
					if (!side && ConstData.isRed(chessID))
						continue; // 如果是黑方走法，跳过红棋
					if (side && ConstData.isBlack(chessID))
						continue; // 如果是红方走法，跳过黑棋

					switch (chessID) {
					case ConstData.R_KING:
					case ConstData.B_KING:
						genKingMove(position, i, j, nPlay);
						break;

					case ConstData.R_CAR:
					case ConstData.B_CAR:
						genCarMove(position, i, j, nPlay);
						break;

					case ConstData.R_HORSE:
					case ConstData.B_HORSE:
						genHorseMove(position, i, j, nPlay);
						break;

					case ConstData.R_BISHOP:
						genRBishopMove(position, i, j, nPlay);
						break;

					case ConstData.B_BISHOP:
						genBBishopMove(position, i, j, nPlay);
						break;

					case ConstData.R_ELEPHANT:
					case ConstData.B_ELEPHANT:
						genElephantMove(position, i, j, nPlay);
						break;

					case ConstData.R_CANON:
					case ConstData.B_CANON:
						genCanonMove(position, i, j, nPlay);
						break;

					case ConstData.R_PAWN:
						genRPawnMove(position, i, j, nPlay);
						break;

					case ConstData.B_PAWN:
						genBPawnMove(position, i, j, nPlay);
						break;
					default:
						break;
					}
				}
			}
		return moveCount;
	}

	// 在moveList中插入一个合法的走法
	protected int addMove(int fromX, int fromY, int toX, int toY, int nPlay) {
		if (moveCount >= 80)
			return moveCount;
		moveList[nPlay][moveCount].from.x = fromX;
		moveList[nPlay][moveCount].from.y = fromY;
		moveList[nPlay][moveCount].to.x = toX;
		moveList[nPlay][moveCount].to.y = toY;
		moveCount++;
		return moveCount;
	}

	// 产生王的走法
	protected void genKingMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		for (y = 0; y < 3; ++y)
			for (x = 3; x < 6; ++x) {
				if (isValidMove(position, j, i, x, y))
					addMove(j, i, x, y, nPlay);
			}

		for (y = 7; y < 10; ++y)
			for (x = 3; x < 6; ++x)
				if (isValidMove(position, j, i, x, y))
					addMove(j, i, x, y, nPlay);

	}

	// 产生红士的走法
	protected void genRBishopMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		for (y = 7; y < 10; ++y)
			for (x = 3; x < 6; ++x)
				if (isValidMove(position, j, i, x, y))
					addMove(j, i, x, y, nPlay);
	}

	// 产生黑士的走法
	protected void genBBishopMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		for (y = 0; y < 3; ++y)
			for (x = 3; x < 6; ++x)
				if (isValidMove(position, j, i, x, y))
					addMove(j, i, x, y, nPlay);
	}

	// 产生象的走法
	protected void genElephantMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		x = j + 2;
		y = i + 2;
		if (x < 9 && y < 10 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j + 2;
		y = i - 2;
		if (x < 9 && y >= 0 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j - 2;
		y = i + 2;
		if (x >= 0 && y < 10 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j - 2;
		y = i - 2;
		if (x >= 0 && y >= 0 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);
	}

	// 产生马的走法
	protected void genHorseMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		x = j + 2;
		y = i + 1;
		if (x < 9 && y < 10 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j + 2;
		y = i - 1;
		if (x < 9 && y >= 0 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j + 1;
		y = i + 2;
		if (x < 9 && y < 10 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j + 1;
		y = i - 2;
		if (x < 9 && y >= 0 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j - 1;
		y = i + 2;
		if (x >= 0 && y < 10 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j - 1;
		y = i - 2;
		if (x >= 0 && y >= 0 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j - 2;
		y = i + 1;
		if (x >= 0 && y < 10 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

		x = j - 2;
		y = i - 1;
		if (x >= 0 && y >= 0 && isValidMove(position, j, i, x, y))
			addMove(j, i, x, y, nPlay);

	}

	// 产生车的走法
	protected void genCarMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		int chessID;
		chessID = position[i][j];

		x = j + 1;
		y = i;
		while (x < 9) {
			if (ConstData.NOCHESS == position[y][x])
				addMove(j, i, x, y, nPlay);
			else {
				// 吃子
				if (!ConstData.isSameSide(chessID, position[y][x]))
					addMove(j, i, x, y, nPlay);
				break;
			}
			++x;
		}

		x = j - 1;
		y = i;
		while (x >= 0) {
			if (ConstData.NOCHESS == position[y][x])
				addMove(j, i, x, y, nPlay);
			else {// 吃子
				if (!ConstData.isSameSide(chessID, position[y][x]))
					addMove(j, i, x, y, nPlay);
				break;
			}
			--x;
		}

		x = j;
		y = i + 1;
		while (y < 10) {
			if (ConstData.NOCHESS == position[y][x])
				addMove(j, i, x, y, nPlay);
			else {// 吃子
				if (!ConstData.isSameSide(chessID, position[y][x]))
					addMove(j, i, x, y, nPlay);
				break;
			}
			++y;
		}

		x = j;
		y = i - 1;
		while (y >= 0) {
			if (ConstData.NOCHESS == position[y][x])
				addMove(j, i, x, y, nPlay);
			else {// 吃子
				if (!ConstData.isSameSide(chessID, position[y][x]))
					addMove(j, i, x, y, nPlay);
				break;
			}
			--y;
		}
	}

	// 产生红兵的走法
	protected void genRPawnMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		int chessID;
		chessID = position[i][j];
		y = i - 1;
		x = j;
		if (y >= 0 && !ConstData.isSameSide(chessID, position[y][x]))
			addMove(j, i, x, y, nPlay);
		// 过河
		if (i < 5) {
			y = i;
			x = j + 1;
			if (x < 9 && !ConstData.isSameSide(chessID, position[y][x]))
				addMove(j, i, x, y, nPlay);
			x = j - 1;
			if (x >= 0 && !ConstData.isSameSide(chessID, position[y][x]))
				addMove(j, i, x, y, nPlay);
		}
	}

	// 产生黑兵的走法
	protected void genBPawnMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		int chessID = position[i][j];
		x = j;
		y = i + 1;
		if (y < 10 && !ConstData.isSameSide(chessID, position[y][x]))
			addMove(j, i, x, y, nPlay);
		// 过河了
		if (i > 4) {
			y = i;
			x = j + 1;
			if (x < 9 && !ConstData.isSameSide(chessID, position[y][x]))
				addMove(j, i, x, y, nPlay);
			x = j - 1;
			if (x >= 0 && !ConstData.isSameSide(chessID, position[y][x]))
				addMove(j, i, x, y, nPlay);
		}
	}

	// 炮的走法
	protected void genCanonMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		int chessID;
		boolean flag;
		chessID = position[i][j];

		x = j;
		y = i + 1;
		flag = false;
		while (y < 10) {
			if (ConstData.NOCHESS == position[y][x]) {
				if (!flag)
					addMove(j, i, x, y, nPlay);
			} else {
				if (!flag)
					flag = true;
				else {// 吃子
					if (!ConstData.isSameSide(chessID, position[y][x]))
						addMove(j, i, x, y, nPlay);
					break;
				}

			}
			y++;
		}
		x = j;
		y = i - 1;
		flag = false;
		while (y >= 0) {
			if (ConstData.NOCHESS == position[y][x]) {
				if (!flag)
					addMove(j, i, x, y, nPlay);
			} else {
				if (!flag)
					flag = true;
				else {
					if (!ConstData.isSameSide(chessID, position[y][x]))
						addMove(j, i, x, y, nPlay);
					break;
				}
			}

			--y;
		}

		y = i;
		x = j + 1;
		flag = false;
		while (x < 9) {
			if (ConstData.NOCHESS == position[y][x]) {
				if (!flag)
					addMove(j, i, x, y, nPlay);
			} else {
				if (!flag)
					flag = true;
				else {
					if (!ConstData.isSameSide(chessID, position[y][x]))
						addMove(j, i, x, y, nPlay);
					break;
				}
			}

			x++;
		}

		y = i;
		x = j - 1;
		flag = false;
		while (x >= 0) {
			if (ConstData.NOCHESS == position[y][x]) {
				if (!flag)
					addMove(j, i, x, y, nPlay);
			} else {
				if (!flag)
					flag = true;
				else {
					if (!ConstData.isSameSide(chessID, position[y][x]))
						addMove(j, i, x, y, nPlay);
					break;
				}
			}
			x--;
		}

	}

}