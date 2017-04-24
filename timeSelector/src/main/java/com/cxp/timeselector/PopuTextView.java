package com.cxp.timeselector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by santa on 16/7/20.
 */
public class PopuTextView extends RelativeLayout {
    private View contentView;
    private TimeSelectorView timeSelectorView;
    private PopupWindow mPopupWindow;
    private String mDefault = "";
    private TextView mTextView;
    private TimeSelectorView.TimeChangeListener mListener;
    private int mTextColorUnChecked = 0xff666666;
    private int mTextColorChecked = 0xff378ad3;

    public void setTextViewText(String s) {
        mDefault=s;
        if (mTextView != null) {
            mTextView.setText(mDefault);
        }
    }

    public PopuTextView(Context context) {
        this(context, null);
    }

    public PopuTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopuTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PopuTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        contentView = LayoutInflater.from(context).inflate(R.layout.popu_timeselector, null);

        timeSelectorView = (TimeSelectorView) contentView.findViewById(R.id.timeselector);

        timeSelectorView.setListener(initTimeChangeListener());

        /**初始化PopupWindow*/
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点
        //点击推出,要设置backgroundDrawable
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xffcfcfcf));
        /**设置PopupWindow弹出和退出时候的动画效果*/
//        mPopupWindow.setAnimationStyle(R.style.animotorPdop);
        mPopupWindow.setOutsideTouchable(true);

    }

    public void onClick() {
        mPopupWindow.showAtLocation(mTextView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTextView = (TextView) getChildAt(0);

        if (mTextView.getText() != null && !mTextView.getText().equals("")) {
            mDefault = mTextView.getText().toString();
        }
        mTextView.setText(mDefault);
        mTextView.setTextColor(mTextColorChecked);

    }

    private TimeSelectorView.TimeChangeListener initTimeChangeListener() {
        mListener = new TimeSelectorView.TimeChangeListener() {

            @Override
            public void scrollFinish(String time, Calendar calendar) {
                mTextView.setText(time);
                mTextView.setTextColor(mTextColorChecked);

                if (mDateListener != null) {
                    mDateListener.onDateSelected(calendar);
                }

            }

            @Override
            public void onFinish() {
                if(isSelectRightTime){
                    mPopupWindow.dismiss();
                }
            }

            @Override
            public void onCancle() {
                mPopupWindow.dismiss();
            }
        };
        return mListener;
    }

    private boolean isSelectRightTime;
    public void setRightTimeTrue(boolean b){
        isSelectRightTime=b;
    }
    private OnDateSelectedListener mDateListener;

    public interface OnDateSelectedListener {
        /**
         * 时间选择后的回调
         * @param calendar 选择后的时间对象
         */
        public void onDateSelected(Calendar calendar);
    }

    /**
     * 设置时间选择的回调
     * @param li
     */
    public void setOnDateSelectedListener(OnDateSelectedListener li) {
        mDateListener = li;
    }

}
