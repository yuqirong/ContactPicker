package com.yuqirong.contactpicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yuqirong.contactpicker.R;

/**
 * 快速索引栏的自定义控件
 *
 * @author Anyway
 */
public class QuickIndexBar extends View {

    // 快速索引的字母
    public static final String[] INDEX_ARRAYS = new String[]{"#", "A", "B",
            "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    // 画笔
    private Paint mPaint;
    // 控件的宽度
    private int width;
    // 控件的高度
    private int height;
    // 字母单元格的宽度
    private float cellHeight;
    // 监听器
    private OnIndexChangeListener listener;
    // 默认字体颜色
    private int defaultFontColor = Color.WHITE;
    // 默认选中字体颜色
    private int defaultSelectedFontColor = Color.GRAY;
    // 字体颜色
    private int fontColor;
    //选中字体颜色
    private int selectedFontColor;
    // 上次触摸的字母单元格
    int lastSelected = -1;
    // 这次触摸的字母单元格
    int selected = -1;
    // 字体大小
    private float fontSize;
    // 默认字体大小
    private float defaultfontSize = 12;
    private static final String TAG = "QuickIndexBar";

    /**
     * 设置当索引改变的监听器
     */
    public interface OnIndexChangeListener {
        /**
         * 当索引改变
         *
         * @param selectIndex 索引值
         */
        void onIndexChange(int selectIndex);

        /**
         * 当手指抬起
         */
        void onActionUp();
    }

    public void setOnIndexChangeListener(OnIndexChangeListener listener) {
        this.listener = listener;
    }


    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
        fontColor = a.getColor(R.styleable.QuickIndexBar_font_color, defaultFontColor);
        selectedFontColor = a.getColor(R.styleable.QuickIndexBar_selected_font_color, defaultSelectedFontColor);
        fontSize = a.getDimension(R.styleable.QuickIndexBar_font_size,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, defaultfontSize,
                        getContext().getResources().getDisplayMetrics()));
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(fontColor);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(fontSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 遍历画出index
        for (int i = 0; i < INDEX_ARRAYS.length; i++) {
            // 测出字体的宽度
            float x = width / 2 - mPaint.measureText(INDEX_ARRAYS[i]) / 2;
            // 得到字体的高度
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            double fontHeight = Math.ceil(fm.descent - fm.ascent);

            float y = (float) ((i + 1) * cellHeight - cellHeight / 2 + fontHeight / 2);
            if (i == selected) {
                mPaint.setColor(lastSelected == -1 ? fontColor : selectedFontColor);
            } else {
                mPaint.setColor(fontColor);
            }
            // 绘制索引的字母 (x,y)为字母左下角的坐标
            canvas.drawText(INDEX_ARRAYS[i], x, y, mPaint);
        }

    }

    /**
     * 得到控件的大小
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //  得到字母单元格的高度
        cellHeight = height * 1.0f / INDEX_ARRAYS.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                y = event.getY();
                // 计算出触摸的是哪个字母单元格
                selected = (int) (y / cellHeight);
                if (selected >= 0 && selected < INDEX_ARRAYS.length) {
                    if (selected != lastSelected) {
                        if (listener != null) {
                            listener.onIndexChange(selected); // 回调监听器的方法
                        }
                        Log.i(TAG, INDEX_ARRAYS[selected]);
                    }
                    lastSelected = selected;
                }
                break;
            case MotionEvent.ACTION_UP:
                // 把上次的字母单元格重置
                lastSelected = -1;
                listener.onActionUp();
                break;
        }
        invalidate(); // 重绘视图
        return true;
    }

}
