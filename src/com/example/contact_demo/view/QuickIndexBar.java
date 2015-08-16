package com.example.contact_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 快速索引栏的自定义控件
 * 
 * @author Anyway
 * 
 */
public class QuickIndexBar extends View {

	// 快速索引的字母
	public static final String[] INDEX_ARRAYS = new String[] { "#", "A", "B",
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	private Paint mPaint;

	// 控件的宽度
	private int width;
	// 控件的高度
	private int height;
	private Context context;
	// 字母单元格的宽度
	private int cellHeight;
	// 监听器
	private OnIndexChangeListener listener;

	/**
	 * 设置当索引改变的监听器
	 * 
	 */
	public interface OnIndexChangeListener {
		// 当索引改变 @param selectIndex 索引值
		public void onIndexChange(int selectIndex);

		// 当手指抬起
		public void onActionUp();
	}

	public void setOnIndexChangeListener(OnIndexChangeListener listener) {
		this.listener = listener;
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public QuickIndexBar(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	// 初始化画笔
	private void initView() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Rect bounds; // 矩形
		for (int i = 0; i < INDEX_ARRAYS.length; i++) {
			bounds = new Rect();
			mPaint.getTextBounds(INDEX_ARRAYS[i], 0, INDEX_ARRAYS[i].length(),
					bounds);
			mPaint.setTextSize(dip2px(context, 12));
			float x = (width - bounds.width()) / 2.0f;
			float y = (int) (cellHeight / 2.0f + bounds.height() / 2.0f + cellHeight
					* i);
			if (i == size)
				mPaint.setColor(lastSize == -1 ? Color.WHITE : Color.GRAY);
			else
				mPaint.setColor(Color.WHITE);
			// 绘制索引的字母  (x,y)为字母左下角的坐标
			canvas.drawText(INDEX_ARRAYS[i], x, y, mPaint);
		}
	}

	/**
	 * 把dp转换为px
	 * 
	 * @param context
	 * @param dpValue
	 *            dp值
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 得到控件的大小
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		cellHeight = height / INDEX_ARRAYS.length; // 得到字母单元格的高度
	}

	// 上次触摸的字母单元格
	int lastSize = -1;
	// 这次触摸的字母单元格
	int size = -1;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			y = event.getY();
			// 计算出触摸的是哪个字母的单元格
			size = (int) (y / cellHeight);
			if (size >= 0 && size < INDEX_ARRAYS.length) {
				if (size != lastSize) {
					if (listener != null) {
						listener.onIndexChange(size); // 回调监听器的方法
					}
					Log.i("index_changed", INDEX_ARRAYS[size]);
				}
				lastSize = size;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			y = event.getY();
			// 计算出触摸的是哪个字母单元格
			size = (int) (y / cellHeight);
			if (size >= 0 && size < INDEX_ARRAYS.length) {
				if (size != lastSize) {
					if (listener != null) {
						listener.onIndexChange(size); // 回调监听器的方法
					}
					Log.i("index_changed", INDEX_ARRAYS[size]);
				}
				lastSize = size;
			}
			break;
		case MotionEvent.ACTION_UP:
			// 把上次的字母单元格重置
			lastSize = -1;
			listener.onActionUp();
			break;
		}
		invalidate(); // 重绘视图
		return true;
	}

}
