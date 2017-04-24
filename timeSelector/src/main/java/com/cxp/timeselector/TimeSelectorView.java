package com.cxp.timeselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.tag;
import static android.R.attr.type;
import static android.media.CamcorderProfile.get;
import static com.cxp.timeselector.SelectorContanst.getDays;
import static com.cxp.timeselector.SelectorContanst.getDaysFromMonth;
import static java.util.Calendar.DAY_OF_MONTH;

/**
 * Created by santa on 16/7/19.
 */
public class TimeSelectorView extends LinearLayout {
    private LinearLayout mContainerContent;
    private RelativeLayout mContainerHeader;
    private TextView mHeaderTitle;
    private TextView mHeadeBottomLeft;
    private TextView mHeaderBottomRight;
    private Paint mPaint = new Paint();
    private ArrayList<ListView> mTimeView;
    private int mTimeViewMidLine = -1;
    //    private int mChildHeight = -1;
    //options
    private int mTextSize = 18;
    private int mTitleColor = Color.BLACK;
    private int mBottomColor = 0xff378ad3;

    private ImageView imageViewTop;
    private ImageView imageViewBottom;
    private RelativeLayout layout;
    private int imageColor = 0xa0cfcfcf;
    //private float imageAlpha = 0.5f;

    private String mType = "y-m-d";

    private TimeChangeListener mListener;
    private int dayPosition;

    public TimeSelectorView(Context context) {
        this(context, null);
    }

    public TimeSelectorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimeSelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setBackgroundColor(0xffffffff);

        float density = context.getResources().getDisplayMetrics().density;


        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeSelectorView, defStyleAttr, defStyleRes);

        String text = array.getString(R.styleable.TimeSelectorView_tsv_type);
        if (text != null && !text.equals("")) {
            mType = text;
        }
//        mType = array.getString(R.styleable.TimeSelectorView_tsv_type);
        array.recycle();


        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(0xFFE1E1E1);

        mContainerHeader = new RelativeLayout(context);
        mContainerHeader.setBackgroundColor(imageColor);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainerHeader.setLayoutParams(layoutParams);
        addView(mContainerHeader);

        mHeaderTitle = new TextView(context);
        RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.addRule(RelativeLayout.CENTER_IN_PARENT);
        mHeaderTitle.setLayoutParams(l);
        mHeaderTitle.setTextSize(mTextSize);
        mHeaderTitle.setTextColor(mTitleColor);
        mHeaderTitle.setText("日期");
        mContainerHeader.addView(mHeaderTitle);

        l = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        l.addRule(RelativeLayout.CENTER_VERTICAL);
        mHeadeBottomLeft = new TextView(context);
        mHeadeBottomLeft.setPadding((int) (density * 8), (int) (density * 8), (int) (density * 8), (int) (density * 8));
        mHeadeBottomLeft.setLayoutParams(l);
        mHeadeBottomLeft.setTextSize(mTextSize);
        mHeadeBottomLeft.setTextColor(mBottomColor);
        mHeadeBottomLeft.setText("取消");
        mHeadeBottomLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onCancle();
                }
            }
        });
        mContainerHeader.addView(mHeadeBottomLeft);

        l = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        l.addRule(RelativeLayout.CENTER_VERTICAL);
        mHeaderBottomRight = new TextView(context);
        mHeaderBottomRight.setLayoutParams(l);
        mHeaderBottomRight.setTextSize(mTextSize);
        mHeaderBottomRight.setTextColor(mBottomColor);
        mHeaderBottomRight.setPadding((int) (density * 8), (int) (density * 8), (int) (density * 8), (int) (density * 8));
        mHeaderBottomRight.setText("确定");
        mHeaderBottomRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    setText();
                    mListener.onFinish();
                }
            }
        });
        mContainerHeader.addView(mHeaderBottomRight);
        layout = new RelativeLayout(context);
        layout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(layout);
        mTimeView = new ArrayList<>();
        mContainerContent = new LinearLayout(context);
        mContainerContent.setOrientation(HORIZONTAL);
        layout.addView(mContainerContent);

        setTimeSelectorType(context, mType);


        imageViewTop = new ImageView(context);
        imageViewTop.setBackgroundColor(imageColor);
        //imageViewTop.setAlpha(imageAlpha);

        imageViewBottom = new ImageView(context);
        imageViewBottom.setBackgroundColor(imageColor);
        // imageViewBottom.setAlpha(imageAlpha);
    }


    private void setTimeSelectorType(Context context, String type) {
        Calendar calendar = Calendar.getInstance();
        Log.d("DEBUG time", calendar.getTimeInMillis() + "");
        Log.d("DEBUG time", calendar.get(Calendar.MONTH) + "");
        Log.d("DEBUG time", calendar.get(DAY_OF_MONTH) + "");
        Log.d("DEBUG time", calendar.get(Calendar.HOUR_OF_DAY) + "");
        Log.d("DEBUG time", calendar.get(Calendar.MINUTE) + "");

        ListView listView;
        if (type.contains("y") || type.contains("Y")) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            ArrayList<String> years = SelectorContanst.getYears();
//            years.add("");
            listView.setAdapter(new SelectorAdapter(context, years));
            listView.setSelection(/*calendar.get(Calendar.YEAR)  +*/  0);
            listView.setTag(0);
            mTimeView.add(listView);
        }
        if (type.contains("m") || type.contains("M")) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            ArrayList<String> months = SelectorContanst.getMonths();
            listView.setAdapter(new SelectorAdapter(context, months));
            listView.setSelection(calendar.get(Calendar.MONTH)+1 );
            listView.setTag(1);
            mTimeView.add(listView);
        }

        if (type.contains("d") || type.contains("D")) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            String y=mTimeView.get(0).getAdapter().getItem(mTimeView.get(0).getFirstVisiblePosition() + (mTimeView.get(0).getChildCount() - 1) / 2).toString();
            if(y.length()>4){
               y=y.substring(0, 4);
            }else{
                y="0";
            }
            int year = Integer.parseInt(y);
            String m=mTimeView.get(1).getAdapter().getItem(mTimeView.get(1).getFirstVisiblePosition() + (mTimeView.get(1).getChildCount() - 1) / 2).toString();
            if(m.length()>1){
                m=m.substring(0, 1);
            }else{
                m="0";
            }
            int month = Integer.parseInt(m);
            ArrayList<String> days = getDays(getMaxDay(year,month));
            listView.setAdapter(new SelectorAdapter(context, days));
            dayPosition = calendar.get(DAY_OF_MONTH) ;
            listView.setSelection(dayPosition);
            listView.setTag(2);
            mTimeView.add(listView);
        }

        if (type.contains("h") || type.contains("H")) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            ArrayList<String> hours = SelectorContanst.getHours();
            listView.setAdapter(new SelectorAdapter(context, hours));
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay==23){
                listView.setSelection(1);
                mTimeView.get(2).setSelection(calendar.get(DAY_OF_MONTH)+1);
            }else{
                listView.setSelection(hourOfDay+1);
            }
            listView.setTag(3);
            mTimeView.add(listView);
        }

        if (type.contains("min") || type.contains("MIN")) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            ArrayList<String> mins = SelectorContanst.getMins();
            listView.setAdapter(new SelectorAdapter(context, mins));
            listView.setSelection(calendar.get(Calendar.MINUTE)+1);
            listView.setTag(4);
            mTimeView.add(listView);
        }

        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < mTimeView.size(); i++) {
            mTimeView.get(i).setLayoutParams(layoutParams);
            mTimeView.get(i).setDividerHeight(0);
            if (i == 0 || i == 1) {
                mTimeView.get(i).setOnScrollListener(new YearScrollListener());
            } else {
                mTimeView.get(i).setOnScrollListener(new TSScrollListener());
            }
            mContainerContent.addView(mTimeView.get(i));
        }

    }

    private void setSelection(ListView lv, int position) {
        lv.setSelection(position);
    }

    public static int getMaxDay(int year,int month) {
        int max=31;
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        if (year == 0) {
            return max;
        }
        return c.get(DAY_OF_MONTH);
    }

    private class YearScrollListener extends TSScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    int tag = (int) view.getTag();
                    String y=mTimeView.get(0).getAdapter().getItem(mTimeView.get(0).getFirstVisiblePosition() + (mTimeView.get(0).getChildCount() - 1) / 2).toString();
                    if(y.length()>4){
                        y=y.substring(0, 4);
                    }else{
                        y="0";
                    }
                    int year = Integer.parseInt(y);
                    if (tag != 2 || tag != 3 || tag != 4) {
                        if ((tag == 0 || tag == 1) ) {
                            String m=mTimeView.get(1).getAdapter().getItem(mTimeView.get(1).getFirstVisiblePosition() + (mTimeView.get(1).getChildCount() - 1) / 2).toString();
                            if(m.length()>1){
                                m=m.substring(0, 2);
                            }else{
                                m="0";
                            }
                            int month = Integer.parseInt(m);
                            ArrayList<String> days=SelectorContanst.getDays(getMaxDay(year,month));
                            ((SelectorAdapter) (mTimeView.get(2).getAdapter())).setData(days);
                        }
                        int dayOfMon = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        mTimeView.get(2).setSelection(dayOfMon+1);
                    }
                    super.onScrollStateChanged(view, scrollState);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    break;
            }
        }
    }

    private class TSScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    View viewTop = (view.getChildAt(0));
                    if (viewTop.getTop() < 0) {
                        if (Math.abs(viewTop.getTop()) > viewTop.getHeight() / 2) {
                            ((ListView) view).smoothScrollToPosition(view.getFirstVisiblePosition() + view.getChildCount() - 1);
                        } else {
                            ((ListView) view).smoothScrollToPosition(view.getFirstVisiblePosition());
                        }
                    }
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    //将字符串写给textview
    private void setText() {
        if (mListener != null) {
            String time = "";
            int year = -1;
            int month = -1;
            int day = -1;
            int hour = -1;
            int mins = -1;
            for (int i = 0; i < mTimeView.size(); i++) {
                ListView listView = mTimeView.get(i);
                String field = listView.getAdapter().getItem(listView.getFirstVisiblePosition() + (listView.getChildCount() - 1) / 2).toString();
                switch (i) {
                    case 0:
                        year = Integer.parseInt(field.substring(0, field.length() - 1));
                        break;
                    case 1:
                        month = Integer.parseInt(field.substring(0, field.length() - 1));
                        break;
                    case 2:
                        day = Integer.parseInt(field.substring(0, field.length() - 1));
                        break;
                    case 3:
                        hour = Integer.parseInt(field.substring(0, field.length() - 1));
                        break;
                    case 4:
                        mins = Integer.parseInt(field.substring(0, field.length() - 1));
                        break;
                }
                time = time.concat(field);
            }
            Calendar calendar = Calendar.getInstance();
            if (year != -1 && month != -1 && day != -1 && hour != -1 && mins != -1) {
                calendar.set(year, month - 1, day, hour, mins);
            }
            mListener.scrollFinish(time, calendar);

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int childHeight = ((ListView) mContainerContent.getChildAt(0)).getChildAt(0).getHeight();
        int count = ((ListView) mContainerContent.getChildAt(0)).getChildCount();
        int offsetItem = (count - 1) / 2;

        int topLine = offsetItem * childHeight + mContainerHeader.getBottom();
        int bottomLine = (offsetItem + 1) * childHeight + mContainerHeader.getBottom();

        if (mTimeViewMidLine == -1) {
            mTimeViewMidLine = (getHeight() + mContainerHeader.getBottom()) / 2;
            createImage(topLine, bottomLine);
            selectCurTime();
        }

        canvas.drawLine(0, mContainerHeader.getBottom(), getWidth(), mContainerHeader.getBottom(), mPaint);
//        canvas.drawLine(0, bottomLine, getWidth(), bottomLine, mPaint);
    }


    private void selectCurTime() {
        for (int i = 0; i < mTimeView.size(); i++) {
            ListView listView = mTimeView.get(i);
            listView.setSelection(listView.getFirstVisiblePosition() - (listView.getChildCount() - 1) / 2);
        }
    }

    private void createImage(int topLine, int bottomLine) {

        imageViewTop.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, topLine - mContainerHeader.getBottom()));
        layout.addView(imageViewTop);

        RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layout.getHeight() - bottomLine + mContainerHeader.getBottom());
        l.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageViewBottom.setLayoutParams(l);
        layout.addView(imageViewBottom);

    }

    public void setListener(TimeChangeListener listener) {
        mListener = listener;
    }

    public interface TimeChangeListener {
        void scrollFinish(String time, Calendar calendar);

        void onFinish();

        void onCancle();
    }
}
