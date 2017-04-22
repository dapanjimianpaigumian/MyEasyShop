package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.AvatarLoadOptions;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsInfo;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {


    //所需数据
    private List<GoodsInfo> list = new ArrayList<>();
    private Context context;

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        ShopViewHolder viewHolder = new ShopViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
//商品名称
        holder.tv_name.setText(list.get(position).getName());
        //商品价格,替换字符串
        String price = context.getString(R.string.goods_money, list.get(position).getPrice());
        holder.tv_price.setText(price);
        //商品图片,图片加载
        ImageLoader.getInstance().displayImage(
                NetApi.IMAGE_URL + list.get(position).getPage(),
                holder.imageView, AvatarLoadOptions.build_item());

        //点击图片，跳转到商品详情页
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClicked(list.get(position));
                }
            }
        });
    }
    //添加数据
    public void addData(List<GoodsInfo> data) {
        list.addAll(data);
        //通知更新
        notifyDataSetChanged();
    }

    //清空数据
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_recycler)
        ImageView imageView;
        @BindView(R.id.tv_item_name)
        TextView tv_name;
        @BindView(R.id.tv_item_price)
        TextView tv_price;
        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    // ############ item的点击事件（接口回调）
    public interface onItemClickListener {

        //点击商品item，跳转到详情页
        void onItemClicked(GoodsInfo goodsInfo);
    }

    private onItemClickListener listener;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
