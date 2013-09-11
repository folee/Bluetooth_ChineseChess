package com.topkc.chinesechess.searchengine;

public class NegamaxEngine extends SearchEngine {
	
	// 对传入的position状态找出一步最佳的走法
	public ChessMoveData searchAgoodMove(int position[][]) {
		System.out.println("search.......");
		//m_maxDepth = m_searchDepth;
		m_maxDepth = 2;
		int i, j;
		for (i = 0; i < 10; ++i)
			for (j = 0; j < 9; ++j)
				m_curPosition[i][j] = position[i][j];

		negaMax(m_maxDepth);
		//makeMove(m_bestMove);
		//System.out.println("------"+m_bestMove.toString());
		for (i = 0; i < 10; ++i)
			for (j = 0; j < 9; ++j)
				position[i][j] = m_curPosition[i][j];
		
		return m_bestMove;
	}

	// 负极大值搜索引擎
	public int negaMax(int depth) {
		int current = -20000;
		int score = 0;
		int count = 0, i;
		int type;

		i = isGameOve(m_curPosition, depth);
		if (i != 0)// 不等于0表示某方的王不存在了，直接返回
		{
			return i;
		}
		boolean side;
		if ((m_maxDepth - depth) % 2 == 1)
			side = true;
		else
			side = false;
		// 搜索到叶子节点，返回估值信息
		if (depth <= 0)
			return m_eve.eveluation(m_curPosition, side);
		// 生产所有可能的走法，
		count = m_moveGen.createPossibleMove(m_curPosition, depth, side);

		for (i = 0; i < count; ++i) { // 尝试每种走法
			type = makeMove(m_moveGen.moveList[depth][i]);
			// 返回此种走法的估值分数
			score = -negaMax(depth - 1);
			// 取消上次走法
			unMakeMove(m_moveGen.moveList[depth][i], type);
			// 若当前走法更优，则记录下来
			if (score > current) {
				current = score;
				// 如果到了根节点
				if (depth == m_maxDepth) {// 记录最优的走棋，把它保存到m_bestMove中
					m_bestMove = m_moveGen.moveList[depth][i];
				}
			}
		}
		return current;
	}
}
