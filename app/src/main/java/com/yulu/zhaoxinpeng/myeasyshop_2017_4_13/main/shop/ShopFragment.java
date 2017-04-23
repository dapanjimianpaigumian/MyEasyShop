package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details.GoodsDetailActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R.id.recyclerView;
import static com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R.id.refreshLayout;


public class ShopFragment extends MvpFragment<ShopView,ShopPresenter> implements ShopView{

    @BindView(recyclerView)
    RecyclerView mRecyclerView;//展示商品的列表
    @BindView(refreshLayout)
    PtrClassicFrameLayout mRefreshLayout;//刷新加载的控件
    @BindView(R.id.tv_load_error)
    TextView mTvLoadError;//错误提示
    Unbinder unbinder;
    private ActivityUtils mActivityUtils;
    private ShopAdapter mShopAdapter;

    //获取商品时，商品类型，获取全部商品时为空
    private String pageType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUtils = new ActivityUtils(this);
        mShopAdapter = new ShopAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        //给适配器添加跳转事件
        mShopAdapter.setListener(new ShopAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                Intent intent = GoodsDetailActivity.getStateIntent(getContext(), goodsInfo.getUuid(), 0);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mShopAdapter);

        //初始化RefreshLayout
        //使用本对象作为key，用来记录上一次刷新的事件，如果两次下拉刷新间隔太近，不会触发刷新方法
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        //设置刷新时显示的背景色
        mRefreshLayout.setBackgroundResource(R.color.recycler_bg);
        //关闭header所耗时长
        mRefreshLayout.setDurationToCloseHeader(1500);
        //实现刷新，加载回调
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //加载更多时触发
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新时触发
                presenter.refreshData(pageType);
            }
        });

    }

    @Override
    public ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
        //刷新一下数据
        if (mShopAdapter.getItemCount() == 0){
            mRefreshLayout.autoRefresh();
        }
    }

    //点击错误视图时刷新数据
    @OnClick(R.id.tv_load_error)
    public void onClick(){
        //自动刷新
        mRefreshLayout.autoRefresh();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //  #################################    视图接口相关实现   ###############################
    @Override
    public void showRefresh() {
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        //停止刷新
        mRefreshLayout.refreshComplete();
        //判断是否拿到数据
        if (mShopAdapter.getItemCount() > 0){
            mActivityUtils.showToast(msg);
            return;
        }
        //显示刷新错误提示
        mTvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        mActivityUtils.showToast(getResources().getString(R.string.refresh_more_end));
        //停止刷新
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        //停止刷新
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        //数据清空
        mShopAdapter.clear();
        if (data != null){
            mShopAdapter.addData(data);
        }
    }

    @Override
    public void showLoadMoreLoading() {
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        //停止刷新
        mRefreshLayout.refreshComplete();
        //判断是否拿到数据
        if (mShopAdapter.getItemCount() > 0){
            mActivityUtils.showToast(msg);
            return;
        }
        //显示加载错误提示
        mTvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        mShopAdapter.addData(data);//为适配器添加数据
    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

}
