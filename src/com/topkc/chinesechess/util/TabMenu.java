package com.topkc.chinesechess.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class TabMenu extends PopupWindow {
	private GridView gvBody, gvTitle;
	private LinearLayout mLayout;
	private MenuTitleAdapter titleAdapter;

	public TabMenu(Context context, OnItemClickListener titleClick,
			OnItemClickListener bodyClick, MenuTitleAdapter titleAdapter,
			int colorBgTabMenu, int aniTabMenu) {
		super(context);

		mLayout = new LinearLayout(context);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		// ����ѡ����
		gvTitle = new GridView(context);
		gvTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		gvTitle.setNumColumns(titleAdapter.getCount());
		gvTitle.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gvTitle.setVerticalSpacing(1);
		gvTitle.setHorizontalSpacing(1);
		gvTitle.setGravity(Gravity.CENTER);
		gvTitle.setOnItemClickListener(titleClick);
		gvTitle.setAdapter(titleAdapter);
		gvTitle.setSelector(new ColorDrawable(Color.TRANSPARENT));// ѡ�е�ʱ��Ϊ͸��ɫ
		this.titleAdapter = titleAdapter;
		// ��ѡ����
		gvBody = new GridView(context);
		gvBody.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		gvBody.setSelector(new ColorDrawable(Color.TRANSPARENT));// ѡ�е�ʱ��Ϊ͸��ɫ
		gvBody.setNumColumns(4);
		gvBody.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gvBody.setVerticalSpacing(10);
		gvBody.setHorizontalSpacing(10);
		gvBody.setPadding(10, 10, 10, 10);
		gvBody.setGravity(Gravity.CENTER);
		gvBody.setOnItemClickListener(bodyClick);
		mLayout.addView(gvTitle);
		mLayout.addView(gvBody);

		// ����Ĭ����
		this.setContentView(mLayout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new ColorDrawable(colorBgTabMenu));// ����TabMenu�˵�����
		this.setAnimationStyle(aniTabMenu);
		this.setFocusable(true);// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ
	}
	
	public void SetTitleSelect(int index) {
		gvTitle.setSelection(index);
		this.titleAdapter.SetFocus(index);
	}
	
	
	public void SetBodySelect(int index, int colorSelBody) {
		int count = gvBody.getChildCount();
		for (int i = 0; i < count; i++) {
			if (i != index)
				((LinearLayout) gvBody.getChildAt(i))
						.setBackgroundColor(Color.TRANSPARENT);
		}
		((LinearLayout) gvBody.getChildAt(index))
				.setBackgroundColor(colorSelBody);
	}
	//ȥ��ͼ�걻ѡ�б�� 2011-9-2 21:53:04 wuwei
	public void unSetBodySelect(int colorSelBody) {
		int count = gvBody.getChildCount();
		for (int i = 0; i < count; i++) {
			((LinearLayout) gvBody.getChildAt(i)).setBackgroundColor(Color.TRANSPARENT);
		}
		//((LinearLayout) gvBody.getChildAt(index)).setBackgroundColor(colorSelBody);
	}
	
	
	public void SetBodyAdapter(MenuBodyAdapter bodyAdapter) {
		gvBody.setAdapter(bodyAdapter);
	}

	/**
	 * �Զ���Adapter��TabMenu��ÿ����ҳ������
	 */
	static public class MenuBodyAdapter extends BaseAdapter {
		private Context mContext;
		private int fontColor, fontSize;
		private String[] texts;
		private int[] resID;

		/**
		 * ����TabMenu�ķ�ҳ����
		 * 
		 * @param context
		 *            ���÷���������
		 * @param texts
		 *            ��ť���ϵ��ַ�������
		 * @param resID
		 *            ��ť���ϵ�ͼ����Դ����
		 * @param fontSize
		 *            ��ť�����С
		 * @param color
		 *            ��ť������ɫ
		 */
		public MenuBodyAdapter(Context context, String[] texts, int[] resID,
				int fontSize, int fontColor) {
			this.mContext = context;
			this.fontColor = fontColor;
			this.texts = texts;
			this.fontSize = fontSize;
			this.resID = resID;
		}

		public int getCount() {
			return texts.length;
		}

		public Object getItem(int position) {

			return makeMenyBody(position);
		}

		public long getItemId(int position) {
			return position;
		}

		private LinearLayout makeMenyBody(int position) {
			LinearLayout result = new LinearLayout(this.mContext);
			result.setOrientation(LinearLayout.VERTICAL);
			result.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL);
			result.setPadding(10, 10, 10, 10);

			TextView text = new TextView(this.mContext);
			text.setText(texts[position]);
			text.setTextSize(fontSize);
			text.setTextColor(fontColor);
			text.setGravity(Gravity.CENTER);
			text.setPadding(5, 5, 5, 5);
			ImageView img = new ImageView(this.mContext);
			img.setBackgroundResource(resID[position]);
			result.addView(img, new LinearLayout.LayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)));
			result.addView(text);
			return result;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return makeMenyBody(position);
		}
	}

	/**
	 * �Զ���Adapter,TabMenu�ķ�ҳ��ǩ����
	 * 
	 */
	static public class MenuTitleAdapter extends BaseAdapter {
		private Context mContext;
		private int fontColor, unselcolor, selcolor;
		private TextView[] title;

		/**
		 * ����TabMenu��title
		 * 
		 * @param context
		 *            ���÷���������
		 * @param titles
		 *            ��ҳ��ǩ���ַ�������
		 * @param fontSize
		 *            �����С
		 * @param fontcolor
		 *            ������ɫ
		 * @param unselcolor
		 *            δѡ����ı���ɫ
		 * @param selcolor
		 *            ѡ����ı���ɫ
		 */
		public MenuTitleAdapter(Context context, String[] titles, int fontSize,
				int fontcolor, int unselcolor, int selcolor) {
			this.mContext = context;
			this.fontColor = fontcolor;
			this.unselcolor = unselcolor;
			this.selcolor = selcolor;
			this.title = new TextView[titles.length];
			for (int i = 0; i < titles.length; i++) {
				title[i] = new TextView(mContext);
				title[i].setText(titles[i]);
				title[i].setTextSize(fontSize);
				title[i].setTextColor(fontColor);
				title[i].setGravity(Gravity.CENTER);
				title[i].setPadding(10, 10, 10, 10);
			}
		}

		public int getCount() {
			return title.length;
		}

		public Object getItem(int position) {
			return title[position];
		}

		public long getItemId(int position) {
			return title[position].getId();
		}

		/**
		 * ����ѡ�е�Ч��
		 */
		private void SetFocus(int index) {
			for (int i = 0; i < title.length; i++) {
				if (i != index) {
					title[i].setBackgroundDrawable(new ColorDrawable(unselcolor));// ����ûѡ�е���ɫ
					title[i].setTextColor(fontColor);// ����ûѡ�����������ɫ
				}
			}
			title[index].setBackgroundColor(0x00);// ����ѡ�������ɫ
			title[index].setTextColor(selcolor);// ����ѡ�����������ɫ
		}
	
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				v = title[position];
			} else {
				v = convertView;
			}
			return v;
		}
	}

}
