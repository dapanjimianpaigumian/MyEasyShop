package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.uploadGoods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details.GoodsDetailImagesAdapter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Administrator on 2017/4/25.
 */

public class UploadGoodsAdapter extends RecyclerView.Adapter{

    //适配器数据
    private ArrayList<ImageItem> mImageItemslist=new ArrayList<>();
    private LayoutInflater mInflater;

    public UploadGoodsAdapter(Context context,ArrayList<ImageItem> list){
        mInflater = LayoutInflater.from(context);
        this.mImageItemslist=list;
    }

    //确定ViewType的值
    @Override
    public int getItemViewType(int position) {
        //当position与图片数量相同时，则为加号布局
        if(position==mImageItemslist.size()) return ITEM_TYPE.ITEM_ADD.ordinal();
        return ITEM_TYPE.ITEM_NORMAL.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //判断当前显示item的类型，有图或者无图，从而选择不同的ViewHoler（不同的布局）
        if (viewType== ITEM_TYPE.ITEM_NORMAL.ordinal()) {
            //有图的VeiwHolder
            return new ItemSelectViewHolder(mInflater.inflate(R.layout.layout_item_recyclerview,parent,false));
        }else {
            //无图，显示加号的ViewHolder
            return new ItemAddViewHolder(mInflater.inflate(R.layout.layout_item_recyclerviewlast,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //判断当前的vh是不是ItemSelectViewHolder的实例
        if (holder instanceof ItemSelectViewHolder) {
            //当前数据
            ImageItem imageItem = mImageItemslist.get(position);
            //拿到当前vh(因为已经判断是vh的实例，所以强转)
            final ItemSelectViewHolder item_select= (ItemSelectViewHolder) holder;
            item_select.photo=imageItem;
            //判断模式（正常，可删除）
            if (mode==MODE_SELECT) {
                //可选框可见
                item_select.checkBox.setVisibility(View.VISIBLE);
                //可选框的选择监听
                item_select.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //imageitem中选择状态的改变
                        mImageItemslist.get(position).setIsCheck(isChecked);
                    }
                });
                //可选框的改变（根据imageitem的选择状态）
                item_select.checkBox.setChecked(imageItem.isCheck());
            }else if(mode==MODE_NORMAL){
                //可选框隐藏
                item_select.checkBox.setVisibility(View.GONE);
            }

            //图片设置
            item_select.ivPhoto.setImageBitmap(imageItem.getBitmap());
            item_select.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) listener.onPhotoClicked(item_select.photo,item_select.ivPhoto);
                }
            });
            //长按图片进入删除模式
            item_select.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //长按图片后进入删除模式
                    mode=MODE_SELECT;
                    //更新状态
                    notifyDataSetChanged();
                    if(listener!=null) listener.onLongClicked();
                    return false;
                }
            });
        }

        //如果当前的ViewHolder是ItemAddViewHolder
        else if(holder instanceof ItemAddViewHolder){
            ItemAddViewHolder item_add= (ItemAddViewHolder) holder;

            //根据图片数量判断加号图片的显隐
            if (position==8) {
                item_add.ib_add.setVisibility(View.GONE);
            }else {
                item_add.ib_add.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return Math.min(mImageItemslist.size()+1,8);
    }

    //----------------------逻辑：模式的选择 start-------------------------------------------------------------

    //编辑时的模式，1 = 有图，2 = 无图（显示加号图片的布局）
    private static final int MODE_NORMAL=1;
    private static final int MODE_SELECT=2;

    //代表图片的编辑模式
    public int mode;

    //使用枚举，辨识item类型，添加图片或无图
    private enum ITEM_TYPE{
        ITEM_NORMAL,ITEM_ADD
    }

    //模式设置
    public void changeMode(int mode){
        this.mode=mode;
    }

    //获取当前模式
    public int getMode(){
        return mode;
    }

    //-----------------------------------逻辑：模式的选择 end--------------------------------------------------

    //--------------------------------------------外部调用的方法 Start ----------------------------------------

    //添加图片
    public void add(ImageItem imageItem){
        mImageItemslist.add(imageItem);
    }

    public int getSize(){
        return mImageItemslist.size();
    }

    //返回数据
    public ArrayList<ImageItem> getImageItemlist(){
        return mImageItemslist;
    }

    //刷新数据
    public void notifyImageItemList(){
        notifyDataSetChanged();
    }
    //----------------------------------外部调用的方法 End--------------------------------------------------------


    //显示添加按钮的ViewHolder
    private static class ItemAddViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ib_recycle_add)
        ImageButton ib_add;
        private ItemAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //已经有图片的ViewHolder
    private static class ItemSelectViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.cb_check_photo)
        CheckBox checkBox;
        ImageItem photo;//用来控制checkbox的选择属性
        private ItemSelectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //-----------------------------------------自定义点击事件（接口回调）--------------------------------------

    public interface OnItemClickedListener{

        //无图时，点击添加按钮增加图片
        void onAddClicked();

        //有图时，点击图片跳转到图片展示页
        void onPhotoClicked(ImageItem imageItem,ImageView imageView);

        //有图时，长按图片执行删除操作
        void onLongClicked();
    }

    private OnItemClickedListener listener;

    public void setListener(OnItemClickedListener listener){
        this.listener=listener;
    }
}
