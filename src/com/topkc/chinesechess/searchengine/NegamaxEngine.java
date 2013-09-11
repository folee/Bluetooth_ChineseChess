package com.topkc.chinesechess.searchengine;

public class NegamaxEngine extends SearchEngine {
	
	// �Դ����position״̬�ҳ�һ����ѵ��߷�
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

	// ������ֵ��������
	public int negaMax(int depth) {
		int current = -20000;
		int score = 0;
		int count = 0, i;
		int type;

		i = isGameOve(m_curPosition, depth);
		if (i != 0)// ������0��ʾĳ�������������ˣ�ֱ�ӷ���
		{
			return i;
		}
		boolean side;
		if ((m_maxDepth - depth) % 2 == 1)
			side = true;
		else
			side = false;
		// ������Ҷ�ӽڵ㣬���ع�ֵ��Ϣ
		if (depth <= 0)
			return m_eve.eveluation(m_curPosition, side);
		// �������п��ܵ��߷���
		count = m_moveGen.createPossibleMove(m_curPosition, depth, side);

		for (i = 0; i < count; ++i) { // ����ÿ���߷�
			type = makeMove(m_moveGen.moveList[depth][i]);
			// ���ش����߷��Ĺ�ֵ����
			score = -negaMax(depth - 1);
			// ȡ���ϴ��߷�
			unMakeMove(m_moveGen.moveList[depth][i], type);
			// ����ǰ�߷����ţ����¼����
			if (score > current) {
				current = score;
				// ������˸��ڵ�
				if (depth == m_maxDepth) {// ��¼���ŵ����壬�������浽m_bestMove��
					m_bestMove = m_moveGen.moveList[depth][i];
				}
			}
		}
		return current;
	}
}
