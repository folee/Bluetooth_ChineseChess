package com.topkc.chinesechess.searchengine;

/**
 * �߷�������,һ���߷��Ľṹ
 * @author WEI
 *
 */
public class MoveGenerator {
	
	public ChessMoveData moveList[][] = new ChessMoveData[10][100];
	protected int moveCount;// ��¼moveList���߷�������

	public MoveGenerator() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 100; j++)
				moveList[i][j] = new ChessMoveData();
	}

	// �жϾ���position�ϣ���from��to���߷��Ƿ�Ϸ�
	// �Ϸ�����true�����򷵻�alse
	public boolean isValidMove(int position[][], int fromX, int fromY,
			int toX, int toY) {
		int i;
		int moveChessID, targetID;

		// Ŀ���������ͬ
		if (fromX == toX && fromY == toY)
			return false;

		moveChessID = position[fromY][fromX];
		targetID = position[toY][toX];

		if (ConstData.isSameSide((int) moveChessID, (int) targetID))
			return false;

		switch (moveChessID) { // �콫
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
					return false;// �Ź�֮��
				if ((Math.abs(fromX - toX) + Math.abs(fromY - toY)) > 1)
					return false;
			}
			break;

		case ConstData.B_BISHOP:// ��ʿ
			// ���˾Ź�֮��
			if (toY > 2 || toX > 5 || toX < 3)
				return false;
			// ʿ��б��
			if (Math.abs(fromX - toX) != 1 || Math.abs(fromY - toY) != 1)
				return false;
			break;
		case ConstData.R_BISHOP:// ��ʿ
			// ���˾Ź�֮��
			if (toY < 7 || toX < 3 || toX > 5)
				return false;
			// ʿ��б��
			if (Math.abs(fromX - toX) != 1 || Math.abs(fromY - toY) != 1)
				return false;
			break;

		case ConstData.B_ELEPHANT:// ����
			if (toY > 4)
				return false;// ���ܹ���
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2)
				return false;
			if (position[(fromY + toY) / 2][(fromX + toX) / 2] != ConstData.NOCHESS)
				return false;
			break;

		case ConstData.R_ELEPHANT:// ����
			if (toY < 5)
				return false;// ���ܹ���
			if (Math.abs(fromX - toX) != 2 || Math.abs(fromY - toY) != 2)
				return false;
			if (position[(fromY + toY) / 2][(fromX + toX) / 2] != ConstData.NOCHESS)
				return false;
			break;

		case ConstData.B_PAWN:// �ڱ�
			if (toY < fromY)
				return false; // �����ܵ���
			if (fromY < 5 && toY == fromY)
				return false; // û�й���ֻ����ǰ��
			if (toY - fromY + Math.abs(fromX - toX) > 1)
				return false;
			break;

		case ConstData.R_PAWN:// ���
			if (toY > fromY)
				return false;// �����ܵ���
			if (fromY > 4 && toY == fromY)
				return false;
			if (fromY - toY + Math.abs(fromX - toX) > 1)
				return false;// ��ֻ����һ��
			break;

		case ConstData.R_KING:// �콫
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

		case ConstData.R_CAR: // ��
		case ConstData.B_CAR:
			if (toX != fromX && fromY != toY)
				return false;// ����ֱ��

			if (fromX == toX) { // û���嵲��
				for (i = Math.min(fromY, toY) + 1; i < Math.max(fromY, toY); ++i)
					if (position[i][fromX] != ConstData.NOCHESS)
						return false;
			} else {// û���嵲��
				for (i = Math.min(fromX, toX) + 1; i < Math.max(fromX, toX); ++i)
					if (position[fromY][i] != ConstData.NOCHESS)
						return false;
			}
			break;

		case ConstData.R_CANON:
		case ConstData.B_CANON:
			if (toX != fromX && fromY != toY)
				return false;// ����ֱ��

			if (toX == fromX) {
				if (targetID == ConstData.NOCHESS)// �ڲ��ǳ���
				{
					// �����ӣ�Ҫû���嵲��
					for (i = Math.min(fromY, toY) + 1; i < Math.max(fromY, toY); ++i)
						if (position[i][fromX] != ConstData.NOCHESS)
							return false;
				} else {
					int count = 0;
					// ���ӣ��м����Ҫ��һ������
					for (i = Math.min(fromY, toY) + 1; i < Math.max(fromY, toY); ++i)
						if (position[i][fromX] != ConstData.NOCHESS)
							count++;
					if (1 != count)
						return false;
				}
			} else {
				if (targetID == ConstData.NOCHESS) { // �����ӣ�Ҫû���嵲��
					for (i = Math.min(fromX, toX) + 1; i < Math.max(fromX, toX); ++i)
						if (position[fromY][i] != ConstData.NOCHESS)
							return false;
				} else {
					int count = 0;
					// ���ӣ��м����Ҫ��һ������
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
			// ������
			if (Math.abs(toX - fromX) + Math.abs(toY - fromY) != 3
					|| toX == fromX || toY == fromY)
				return false;
			if (Math.abs(toX - fromX) == 2) {
				// �а�ŵ�����
				if (position[fromY][(toX + fromX) / 2] != ConstData.NOCHESS)
					return false;
			} else {
				// �а�ŵ�����
				if (position[(toY + fromY) / 2][fromX] != ConstData.NOCHESS)
					return false;
			}
			break;

		}
		return true;
	}

	// �������������ϵ����кϷ����߷�
	// side ָ��������һ�����߷���trueΪ�췽��falseΪ�ڷ�

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
						continue; // ����Ǻڷ��߷�����������
					if (side && ConstData.isBlack(chessID))
						continue; // ����Ǻ췽�߷�����������

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

	// ��moveList�в���һ���Ϸ����߷�
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

	// ���������߷�
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

	// ������ʿ���߷�
	protected void genRBishopMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		for (y = 7; y < 10; ++y)
			for (x = 3; x < 6; ++x)
				if (isValidMove(position, j, i, x, y))
					addMove(j, i, x, y, nPlay);
	}

	// ������ʿ���߷�
	protected void genBBishopMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		for (y = 0; y < 3; ++y)
			for (x = 3; x < 6; ++x)
				if (isValidMove(position, j, i, x, y))
					addMove(j, i, x, y, nPlay);
	}

	// ��������߷�
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

	// ��������߷�
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

	// ���������߷�
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
				// ����
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
			else {// ����
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
			else {// ����
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
			else {// ����
				if (!ConstData.isSameSide(chessID, position[y][x]))
					addMove(j, i, x, y, nPlay);
				break;
			}
			--y;
		}
	}

	// ����������߷�
	protected void genRPawnMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		int chessID;
		chessID = position[i][j];
		y = i - 1;
		x = j;
		if (y >= 0 && !ConstData.isSameSide(chessID, position[y][x]))
			addMove(j, i, x, y, nPlay);
		// ����
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

	// �����ڱ����߷�
	protected void genBPawnMove(int position[][], int i, int j, int nPlay) {
		int x, y;
		int chessID = position[i][j];
		x = j;
		y = i + 1;
		if (y < 10 && !ConstData.isSameSide(chessID, position[y][x]))
			addMove(j, i, x, y, nPlay);
		// ������
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

	// �ڵ��߷�
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
				else {// ����
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