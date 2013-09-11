package com.topkc.chinesechess.searchengine;

public class SearchEngine {

	// ��ʾ������ķֲ�
	public int m_curPosition[][] = new int[10][9];
	// ��¼�������
	public ChessMoveData m_bestMove = new ChessMoveData();
	// �߷�������
	public MoveGenerator m_moveGen = new MoveGenerator();
	// ��ֵ������
	public Eveluation m_eve = new Eveluation();
	// �������
	public int m_searchDepth;
	// ��ǰ�������
	public int m_maxDepth;

	public ChessMoveData searchAgoodMove(int position[][]) {
		return null;
	}

	// �ƶ�����
	public int makeMove(ChessMoveData data) {
		int chessID;
		chessID = m_curPosition[data.to.y][data.to.x];
		m_curPosition[data.to.y][data.to.x] = m_curPosition[data.from.y][data.from.x];
		m_curPosition[data.from.y][data.from.x] = ConstData.NOCHESS;

		return chessID;
	}

	// ȡ���ƶ�
	public void unMakeMove(ChessMoveData move, int chessID) {
		m_curPosition[move.from.y][move.from.x] = m_curPosition[move.to.y][move.to.x];
		m_curPosition[move.to.y][move.to.x] = chessID;
	}

	// �ж���Ϸʱ�����
	public int isGameOve(int position[][], int depth) {
		int i, j;
		boolean redLive = false, blackLive = false;

		for (i = 0; i < 3; ++i)
			for (j = 3; j < 6; ++j) {
				if (position[i][j] == ConstData.B_KING)
					blackLive = true;
				if (position[i][j] == ConstData.R_KING)
					redLive = true;
			}

		for (i = 7; i < 10; ++i)
			for (j = 3; j < 6; ++j) {
				if (position[i][j] == ConstData.B_KING)
					blackLive = true;
				if (position[i][j] == ConstData.R_KING)
					redLive = true;
			}

		i = (m_maxDepth - depth + 1) % 2;// ȡ��ǰ��ż��־

		if (!redLive)// �콫������
		{
			if (i != 0)
				return 19990 + depth;
			else
				return -19990 - depth;
		}

		if (!blackLive)
			if (0 != i)
				return -19990 - depth;
			else
				return 19990 + depth;

		return 0;// �������ڣ�����0
	}

}