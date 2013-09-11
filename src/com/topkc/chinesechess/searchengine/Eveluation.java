package com.topkc.chinesechess.searchengine;

import android.graphics.Point;

public class Eveluation {
	// ������ӻ�����ֵ������
	protected int m_baseValue[] = new int[15];
	// �����������ֵ������
	protected int m_flexValue[] = new int[15];
	// ���ÿһλ�ñ���в����Ϣ
	protected int m_attackPos[][] = new int[10][9];
	// ���ÿһλ�ñ���������Ϣ
	protected int m_guardPos[][] = new int[10][9];
	// ���ÿһλ���ϵ�����������
	protected int m_flexPos[][] = new int[10][9];
	// ���ÿһλ�������ӵ��ܼ�ֵ
	protected int m_chessValue[][] = new int[10][9];
	// ��¼һ���ӵ����λ�ø���
	protected int m_posCount;

	protected Point m_relatePos[] = new Point[20];//

	// ������ӵĸ���λ���Ϸ����ķֲ�
	protected final int REDP[][] = { 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 90, 90, 110, 120, 120, 120, 110, 90, 90 },
			{ 90, 90, 110, 120, 120, 120, 110, 90, 90 },
			{ 70, 90, 110, 110, 110, 110, 110, 90, 70 },
			{ 70, 70, 70, 70, 70, 70, 70, 70, 70 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },

	};
	
	// �ڱ����Ӹ���λ���ϵķ����ֲ�
	protected final int BLACKP[][] = { 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 70, 70, 70, 70, 70, 70, 70, 70, 70 },
			{ 70, 90, 110, 110, 110, 110, 110, 90, 70 },
			{ 90, 90, 110, 120, 120, 120, 110, 90, 90 },
			{ 90, 90, 110, 120, 120, 120, 110, 90, 90 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, };

	public Eveluation() {
		for (int i = 0; i < 20; i++)
			m_relatePos[i] = new Point();

		m_baseValue[ConstData.B_KING] = ConstData.BASEVALUE_KING;
		m_baseValue[ConstData.B_CAR] = ConstData.BASEVALUE_CAR;
		m_baseValue[ConstData.B_HORSE] = ConstData.BASEVALUE_HORSE;
		m_baseValue[ConstData.B_ELEPHANT] = ConstData.BASEVALUE_ELEPHANT;
		m_baseValue[ConstData.B_BISHOP] = ConstData.BASEVALUE_BISHOP;
		m_baseValue[ConstData.B_CANON] = ConstData.BASEVALUE_CANON;
		m_baseValue[ConstData.B_PAWN] = ConstData.BASEVALUE_PAWN;

		m_baseValue[ConstData.R_KING] = ConstData.BASEVALUE_KING;
		m_baseValue[ConstData.R_CAR] = ConstData.BASEVALUE_CAR;
		m_baseValue[ConstData.R_HORSE] = ConstData.BASEVALUE_HORSE;
		m_baseValue[ConstData.R_ELEPHANT] = ConstData.BASEVALUE_ELEPHANT;
		m_baseValue[ConstData.R_BISHOP] = ConstData.BASEVALUE_BISHOP;
		m_baseValue[ConstData.R_PAWN] = ConstData.BASEVALUE_PAWN;
		m_baseValue[ConstData.R_CANON] = ConstData.BASEVALUE_CANON;

		m_flexValue[ConstData.B_KING] = ConstData.FLEXIBLE_KING;
		m_flexValue[ConstData.B_CAR] = ConstData.FLEXIBLE_CAR;
		m_flexValue[ConstData.B_HORSE] = ConstData.FLEXIBLE_HORSE;
		m_flexValue[ConstData.B_ELEPHANT] = ConstData.FLEXIBLE_ELEPHANT;
		m_flexValue[ConstData.B_BISHOP] = ConstData.FLEXIBLE_BISHOP;
		m_flexValue[ConstData.B_CANON] = ConstData.FLEXIBLE_CANON;
		m_flexValue[ConstData.B_PAWN] = ConstData.FLEXIBLE_PAWN;

		m_flexValue[ConstData.R_KING] = ConstData.FLEXIBLE_KING;
		m_flexValue[ConstData.R_CAR] = ConstData.FLEXIBLE_CAR;
		m_flexValue[ConstData.R_HORSE] = ConstData.FLEXIBLE_HORSE;
		m_flexValue[ConstData.R_ELEPHANT] = ConstData.FLEXIBLE_ELEPHANT;
		m_flexValue[ConstData.R_BISHOP] = ConstData.FLEXIBLE_BISHOP;
		m_flexValue[ConstData.R_CANON] = ConstData.FLEXIBLE_CANON;
		m_flexValue[ConstData.R_PAWN] = ConstData.FLEXIBLE_PAWN;
	}

	// ��ֵ�������Դ�������̴��,isRed�����ֵ�˭����
	public int eveluation(int position[][], boolean isRed) {
		int i, j, k;

		int chessType, targetType;
		for (i = 0; i < 10; i++)
			for (j = 0; j < 9; j++) {
				m_chessValue[i][j] = 0;
				m_attackPos[i][j] = 0;
				m_guardPos[i][j] = 0;
				m_flexPos[i][j] = 0;
			}

		for (i = 0; i < 10; ++i)
			for (j = 0; j < 9; ++j) {
				if (ConstData.NOCHESS != position[i][j])// ������
				{
					chessType = position[i][j];
					getRelatePiece(position, j, i);// ��������Ϣ
					for (k = 0; k < m_posCount; ++k) {
						targetType = position[m_relatePos[k].y][m_relatePos[k].x];
						if (targetType == ConstData.NOCHESS)// ���������
						{
							m_flexPos[i][j]++;
						} else {
							if (ConstData.isSameSide(chessType, targetType))// �����Լ���������
							{
								m_guardPos[m_relatePos[k].y][m_relatePos[k].x]++;
							} else// ��в
							{
								m_attackPos[m_relatePos[k].y][m_relatePos[k].x]++;
								m_flexPos[i][j]++;
								switch (targetType) {
								case ConstData.R_KING:// �Ǻ콫����в
									if (!isRed)
										return 18888;// ����û���ֵ��췽��
									break;

								case ConstData.B_KING:// ��������в
									if (isRed)
										return 18888;// �ֵ��췽��
									break;

								default:
									m_attackPos[m_relatePos[k].y][m_relatePos[k].x] += (30 + (m_baseValue[targetType] - m_baseValue[chessType]) / 10) / 10;
									break;
								}

							}
						}
					}
				}
			}
		// ͳ�ƹ�ֵ����
		for (i = 0; i < 10; i++)
			for (j = 0; j < 9; ++j) {
				if (position[i][j] != ConstData.NOCHESS) {
					chessType = position[i][j];
					m_chessValue[i][j]++;
					m_chessValue[i][j] += m_flexValue[chessType]
							* m_flexPos[i][j];
					m_chessValue[i][j] += getBingValue(position, j, i);
				}
			}

		int halfValue;
		// ͳ�ƹ�������в�򱣻�����Ϣ��ֵ
		for (i = 0; i < 10; ++i)
			for (j = 0; j < 9; ++j) {
				if (position[i][j] != ConstData.NOCHESS) {
					chessType = position[i][j];
					halfValue = m_baseValue[chessType] / 16;
					m_chessValue[i][j] += m_baseValue[chessType];

					if (ConstData.isRed(chessType))// ��λ���Ǻ���
					{
						if (m_attackPos[i][j] != 0)// ��в
						{
							if (isRed)// �ֵ��췽��
							{
								if (chessType == ConstData.R_KING) // ����в���ǽ�
								{
									m_chessValue[i][j] -= 20;
								} else // ����в������������
								{
									m_chessValue[i][j] -= halfValue * 2;
									if (m_guardPos[i][j] > 0) // ���Լ������ӱ���
										m_chessValue[i][j] += halfValue;
								}
							} else // �ֵ��ڷ���
							{
								if (chessType == ConstData.R_KING)
									return 18888;
								m_chessValue[i][j] -= halfValue * 10;
								if (m_guardPos[i][j] > 0)
									m_chessValue[i][j] += halfValue * 9;
							}
							m_chessValue[i][j] -= m_attackPos[i][j];
						} else {// û������в��λ��
							if (m_guardPos[i][j] > 0)
								m_chessValue[i][j] += 5;
						}
					} else {// ��λ���Ǻ���
						if (m_attackPos[i][j] > 0) {
							if (!isRed)// �ֵ��ڷ�����
							{
								if (chessType == ConstData.B_KING)// �ڽ��ܵ���в
								{
									m_chessValue[i][j] -= 20;
								} else// ����в������������
								{
									m_chessValue[i][j] -= halfValue * 2;
									if (m_guardPos[i][j] > 0)// ��λ���ܵ�����
										m_chessValue[i][j] += halfValue;
								}
							} else {// �ֵ��췽����
								if (chessType == ConstData.B_KING)
									return 18888;
								m_chessValue[i][j] -= halfValue * 10;
								if (m_guardPos[i][j] > 0)// ��λ�����屣��
									m_chessValue[i][j] += halfValue * 9;
							}
							m_chessValue[i][j] -= m_attackPos[i][j];
						} else {// ��λ��û�б���в
							if (m_guardPos[i][j] > 0)// ��λ���ܵ�����
								m_chessValue[i][j] += 5;
						}
					}
				}
			}

		int redValue = 0;
		int blackValue = 0;
		// ͳ�ƺ�ڷ���������
		for (i = 0; i < 10; ++i) {
			for (j = 0; j < 9; ++j) {
				chessType = position[i][j];
				if (chessType != ConstData.NOCHESS) {
					if (ConstData.isRed(chessType)) {

						redValue += m_chessValue[i][j];
					} else
						blackValue += m_chessValue[i][j];
				}
			}
		}
		if (isRed)
			return (redValue - blackValue);
		else
			return (blackValue - redValue);
	}

	// ��һ��λ�ü�����ض���
	protected void addPoint(int x, int y) {
		m_relatePos[m_posCount].x = x;
		m_relatePos[m_posCount].y = y;
		m_posCount++;
	}

	// �ж�λ��(fx,fy)�Ƿ��ܹ�����(tx, ty);
	protected boolean canTouch(int position[][], int fx, int fy, int tx, int ty) {
		int i;
		int moveID, targetID;

		if (fx == tx && fy == ty)
			return false;

		moveID = position[fy][fx];
		targetID = position[ty][tx];

		switch (moveID) {
		// Ҫ�ߵ����Ǻڷ���
		case ConstData.B_KING:
			// Ŀ�����Ǻ췽��
			if (targetID == ConstData.R_KING) { // ����һ��ֱ����
				if (fx != tx)
					return false;
				// ���м��Ƿ����嵲��
				for (i = fy + 1; i < ty; i++)
					if (ConstData.NOCHESS != position[i][fx])
						return false;
			}
			// Ŀ������������
			else { // Ҫ�ھŹ�֮������
				if (ty > 2 || tx < 3 || tx > 5)
					return false;
				// ÿ��ֻ����һ������
				if (Math.abs(tx - fx) + Math.abs(ty - fy) != 1)
					return false;
			}
			break;
		// ͬ��
		case ConstData.R_KING:
			if (targetID == ConstData.B_KING) {
				if (fx != tx)
					return false;
				for (i = fy - 1; i > ty; --i)
					if (ConstData.NOCHESS != position[i][fx])
						return false;
			} else {
				if (ty < 7 || tx < 3 || tx > 5)
					return false;
				if (Math.abs(tx - fx) + Math.abs(ty - fy) != 1)
					return false;
			}
			break;

		case ConstData.B_BISHOP:
			// ʿҪ�ھŹ�֮��
			if (ty > 2 || tx < 3 || tx > 5)
				return false;
			// ����б��
			if (Math.abs(tx - fx) != 1 || Math.abs(ty - fy) != 1)
				return false;
			break;

		case ConstData.R_BISHOP:
			// ʿҪ�ھŹ�֮��
			if (ty < 7 || tx < 3 || tx > 5)
				return false;
			// ����б��
			if (Math.abs(tx - fx) != 1 || Math.abs(ty - fy) != 1)
				return false;
			break;

		case ConstData.B_ELEPHANT:
			if (ty > 4)
				return false;// �಻�ܹ���
			if (Math.abs(tx - fx) != 2 || Math.abs(ty - fy) != 2)
				return false;// ������
			if (ConstData.NOCHESS != position[(ty + fy) / 2][(tx + fx) / 2])
				return false;// ���۲���������
			break;

		case ConstData.R_ELEPHANT:
			if (ty < 5)
				return false;
			if (Math.abs(tx - fx) != 2 || Math.abs(ty - fy) != 2)
				return false;
			if (ConstData.NOCHESS != position[(ty + fy) / 2][(tx + fx) / 2])
				return false;
			break;

		case ConstData.B_PAWN:
			if (ty < fy)
				return false;// �����ܵ�����
			if (fy < 5 && tx != fx)
				return false;// û�й��Ӳ��ܺ�����
			if (Math.abs(tx - fx) + Math.abs(ty - fy) != 1)
				return false;// �ж�ʱ������һ��
			break;

		case ConstData.R_PAWN:
			if (ty > fy)
				return false;
			if (fy > 4 && tx != fx)
				return false;
			if (Math.abs(tx - fx) + Math.abs(ty - fy) != 1)
				return false;
			break;

		case ConstData.B_CAR:
		case ConstData.R_CAR:
			if (tx != fx && ty != fy)
				return false;// ������ֱ��
			if (tx == fx) { // ������
				for (i = Math.min(ty, fy) + 1; i < Math.max(ty, fy); ++i)
					if (ConstData.NOCHESS != position[i][tx])// �ж��м��Ƿ����嵲��
						return false;
			} else {
				for (i = Math.min(tx, fx) + 1; i < Math.max(tx, fx); ++i)
					if (ConstData.NOCHESS != position[fy][i])
						return false;
			}
			break;

		case ConstData.B_HORSE:
		case ConstData.R_HORSE:
			if (tx == fx || ty == fy)
				return false;
			if (Math.abs(tx - fx) + Math.abs(ty - fy) != 3)
				return false;
			if (Math.abs(tx - fx) == 2) { // �ж����������ڰ����
				if (ConstData.NOCHESS != position[fy][(tx + fx) / 2])
					return false;
			} else { // �ж����������ڰ����
				if (ConstData.NOCHESS != position[(ty + fy) / 2][fx])
					return false;
			}
			break;

		case ConstData.R_CANON:
		case ConstData.B_CANON:
			if (tx != fx && ty != fy)
				return false;// ����ֱ��
			if (ConstData.NOCHESS == position[ty][tx]) {
				if (tx == fx) {
					for (i = Math.min(ty, fy) + 1; i < Math.max(ty, fy); ++i)
						if (ConstData.NOCHESS != position[i][fx])
							return false;
				} else {
					for (i = Math.min(tx, fx) + 1; i < Math.max(tx, fx); ++i)
						if (ConstData.NOCHESS != position[fy][i])
							return false;
				}
			} else {
				int count = 0;
				if (tx == fx) {
					for (i = Math.min(ty, fy) + 1; i < Math.max(ty, fy); ++i)
						if (ConstData.NOCHESS != position[i][fx])
							count++;
					if (1 != count)
						return false;
				} else {
					for (i = Math.min(tx, fx) + 1; i < Math.max(tx, fx); ++i)
						if (ConstData.NOCHESS != position[fy][i])
							count++;
					if (1 != count)
						return false;
				}

			}
			break;
		default:
			return false;
		}
		return true;
	}

	// �о����ƶ�λ����ص�����
	protected int getRelatePiece(int position[][], int j, int i) {
		m_posCount = 0;
		int chessID;
		boolean flag;
		int x, y;
		chessID = position[i][j];
		switch (chessID) {
		case ConstData.R_KING:
		case ConstData.B_KING:
			for (y = 0; y < 3; ++y)
				for (x = 3; x < 6; ++x)
					if (canTouch(position, j, i, x, y))
						addPoint(x, y);

			for (y = 7; y < 10; ++y)
				for (x = 3; x < 6; ++x)
					if (canTouch(position, j, i, x, y))
						addPoint(x, y);
			break;

		case ConstData.R_BISHOP:
			for (y = 7; y < 10; ++y)
				for (x = 3; x < 6; ++x)
					if (canTouch(position, j, i, x, y))
						addPoint(x, y);
			break;

		case ConstData.B_BISHOP:
			for (y = 0; y < 3; ++y)
				for (x = 3; x < 6; ++x)
					if (canTouch(position, j, i, x, y))
						addPoint(x, y);
			break;

		case ConstData.R_ELEPHANT:
		case ConstData.B_ELEPHANT:
			x = j + 2;
			y = i + 2;
			if (y < 10 && x < 9 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j + 2;
			y = i - 2;
			if (x < 9 && y >= 0 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j - 2;
			y = i + 2;
			if (x >= 0 && y < 10 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j - 2;
			y = i - 2;
			if (x >= 0 && y >= 0 && canTouch(position, j, i, x, y))
				addPoint(x, y);
			break;

		case ConstData.R_HORSE:
		case ConstData.B_HORSE:
			x = j + 2;
			y = i + 1;
			if (x < 9 && y < 10 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j + 2;
			y = i - 1;
			if (x < 9 && y >= 0 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j + 1;
			y = i + 2;
			if (x < 9 && y < 10 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j + 1;
			y = i - 2;
			if (x < 9 && y >= 0 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j - 1;
			y = i + 2;
			if (x >= 0 && y < 10 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j - 1;
			y = i - 2;
			if (x >= 0 && y >= 0 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j - 2;
			y = i + 1;
			if (x >= 0 && y < 10 && canTouch(position, j, i, x, y))
				addPoint(x, y);

			x = j - 2;
			y = i - 1;
			if (x >= 0 && y >= 0 && canTouch(position, j, i, x, y))
				addPoint(x, y);
			break;

		case ConstData.R_CAR:
		case ConstData.B_CAR:
			x = j + 1;
			y = i;
			while (x < 9) {
				if (ConstData.NOCHESS == position[y][x])
					addPoint(x, y);
				else {
					addPoint(x, y);
					break;
				}
				++x;
			}
			x = j - 1;
			y = i;
			while (x >= 0) {
				if (ConstData.NOCHESS == position[y][x])
					addPoint(x, y);
				else {
					addPoint(x, y);
					break;
				}
				--x;
			}
			x = j;
			y = i + 1;
			while (y < 10) {
				if (ConstData.NOCHESS == position[y][x])
					addPoint(x, y);
				else {
					addPoint(x, y);
					break;
				}
				++y;
			}
			x = j;
			y = i - 1;
			while (y >= 0) {
				if (ConstData.NOCHESS == position[y][x])
					addPoint(x, y);
				else {
					addPoint(x, y);
					break;
				}
				--y;
			}
			break;

		case ConstData.R_PAWN:
			y = i - 1;
			x = j;
			if (y >= 0)
				addPoint(x, y);
			if (i < 5) {
				y = i;
				x = j + 1;
				if (x < 9)
					addPoint(x, y);
				x = j - 1;
				if (x >= 0)
					addPoint(x, y);
			}
			break;

		case ConstData.B_PAWN:
			y = i + 1;
			x = j;
			if (y < 10)
				addPoint(x, y);
			if (i > 4) {
				y = i;
				x = j + 1;
				if (x < 9)
					addPoint(x, y);
				x = j - 1;
				if (x >= 0)
					addPoint(x, y);
			}
			break;

		case ConstData.R_CANON:
		case ConstData.B_CANON:
			flag = false;
			x = j + 1;
			y = i;
			while (x < 9) {
				if (ConstData.NOCHESS == position[y][x]) {
					if (!flag)
						addPoint(x, y);
				} else {
					if (!flag)
						flag = true;
					else {
						addPoint(x, y);
						break;
					}
				}
				++x;
			}

			flag = false;
			x = j - 1;
			y = i;
			while (x >= 0) {
				if (ConstData.NOCHESS == position[y][x]) {
					if (!flag)
						addPoint(x, y);
				} else {
					if (!flag)
						flag = true;
					else {
						addPoint(x, y);
						break;
					}
				}
				x--;
			}

			flag = false;
			x = j;
			y = i + 1;
			while (y < 10) {
				if (ConstData.NOCHESS == position[y][x]) {
					if (!flag)
						addPoint(x, y);
				} else {
					if (!flag)
						flag = true;
					else {
						addPoint(x, y);
						break;
					}
				}
				++y;
			}

			flag = false;
			x = j;
			y = i - 1;
			while (y >= 0) {
				if (ConstData.NOCHESS == position[y][x]) {
					if (!flag)
						addPoint(x, y);
				} else {
					if (!flag)
						flag = true;
					else {
						addPoint(x, y);
						break;
					}
				}
				--y;
			}

		}// end switch();

		return m_posCount;
	}

	protected int getBingValue(int position[][], int x, int y) {
		if (position[y][x] == ConstData.R_PAWN)
			return REDP[y][x];
		if (position[y][x] == ConstData.B_PAWN)
			return BLACKP[y][x];
		return 0;
	}

}