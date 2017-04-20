package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.ItemShow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PersonAdapter extends BaseAdapter {

    private List<ItemShow> mList = new ArrayList<>();

    public PersonAdapter(List<ItemShow> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_info, parent, false);

            mViewHolder = new ViewHolder(convertView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTvName.setText(mList.get(position).getItem_title());
        mViewHolder.mTvContent.setText(mList.get(position).getItem_content());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView mTvName;
        @BindView(R.id.tv_person)
        TextView mTvContent;
        @BindView(R.id.textView2)
        TextView mTvGo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
