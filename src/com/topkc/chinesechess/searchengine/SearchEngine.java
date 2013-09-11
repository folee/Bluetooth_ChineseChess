package com.topkc.chinesechess.searchengine;

public class SearchEngine {

	// 表示棋盘棋的分布
	public int m_curPosition[][] = new int[10][9];
	// 记录最佳走棋
	public ChessMoveData m_bestMove = new ChessMoveData();
	// 走法产生器
	public MoveGenerator m_moveGen = new MoveGenerator();
	// 估值产生器
	public Eveluation m_eve = new Eveluation();
	// 搜索深度
	public int m_searchDepth;
	// 当前搜索深度
	public int m_maxDepth;

	public ChessMoveData searchAgoodMove(int position[][]) {
		return null;
	}

	// 移动棋子
	public int makeMove(ChessMoveData data) {
		int chessID;
		chessID = m_curPosition[data.to.y][data.to.x];
		m_curPosition[data.to.y][data.to.x] = m_curPosition[data.from.y][data.from.x];
		m_curPosition[data.from.y][data.from.x] = ConstData.NOCHESS;

		return chessID;
	}

	// 取消移动
	public void unMakeMove(ChessMoveData move, int chessID) {
		m_curPosition[move.from.y][move.from.x] = m_curPosition[move.to.y][move.to.x];
		m_curPosition[move.to.y][move.to.x] = chessID;
	}

	// 判断游戏时候结束
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

		i = (m_maxDepth - depth + 1) % 2;// 取当前奇偶标志

		if (!redLive)// 红将不在了
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

		return 0;// 两将都在，返回0
	}

}