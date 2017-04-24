package com.cxp.timeselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by santa on 16/7/19.
 */
public class SelectorAdapter extends BaseAdapter {
    private ArrayList<String> mData=new ArrayList<>();
    private Context mContext;
    public int getSize(){
        return mData.size();
    }
    public SelectorAdapter(Context context, ArrayList<String> list) {
        mData = list;
        mContext = context;
    }
    public void setData(ArrayList<String> list){
        mData = list;
        notifyDataSetChanged();
    };
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectorHolder selectorHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_selector, null);
            selectorHolder = new SelectorHolder();
            selectorHolder.textView = (TextView) convertView.findViewById(R.id.selector_text);
            convertView.setTag(selectorHolder);
        } else {
            selectorHolder = (SelectorHolder) convertView.getTag();
        }

//        if(position==0 ){
//            selectorHolder.textView.setText("");
//        }else{
            selectorHolder.textView.setText(mData.get(position));
//        }
        return convertView;
    }

    private class SelectorHolder {
        public TextView textView;
    }
}
