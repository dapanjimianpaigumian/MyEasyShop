package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personGoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.ShopAdapter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.ShopView;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details.GoodsDetailActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PersonGoodsActivity extends MvpActivity<ShopView, PersonGoodsPresenter> implements ShopView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout mRefreshLayout;
    @BindView(R.id.tv_load_error)
    TextView mTvLoadError;
    @BindView(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    private Unbinder bind;
    private ActivityUtils mActivityUtils;
    private ShopAdapter mShopAdapter;
    private String pageType = "";//商品类型，空字符串为获取全部商品

    //每次进入本页面，如果没有数据，自动刷新
    @Override
    protected void onStart() {
        super.onStart();
        if (mShopAdapter.getItemCount() == 0) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_goods);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        mShopAdapter = new ShopAdapter();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setOnMenuItemClickListener(menuItemClickListener);

        initView();
    }

    private void initView() {
        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mShopAdapter.setListener(new ShopAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                Intent intent = GoodsDetailActivity.getStateIntent(
                        PersonGoodsActivity.this, goodsInfo.getUuid(), 1);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mShopAdapter);
        //初始化RefreshLayout
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setBackgroundResource(R.color.recycler_bg);
        mRefreshLayout.setDurationToCloseHeader(1000);
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //设置Toolbar菜单
        getMenuInflater().inflate(R.menu.menu_goods_type, menu);
        return true;
    }

    //Toolbar菜单监听
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_household:
                    presenter.refreshData("household");
                    break;
                case R.id.menu_electron:
                    presenter.refreshData("electron");
                    break;
                case R.id.menu_dress:
                    presenter.refreshData("dress");
                    break;
                case R.id.menu_book:
                    presenter.refreshData("book");
                    break;
                case R.id.menu_toy:
                    presenter.refreshData("toy");
                    break;
                case R.id.menu_gift:
                    presenter.refreshData("gift");
                    break;
                case R.id.menu_other:
                    presenter.refreshData("other");
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @NonNull
    @Override
    public PersonGoodsPresenter createPresenter() {
        return new PersonGoodsPresenter();
    }

    @Override
    public void showRefresh() {
        mTvLoadEmpty.setVisibility(View.GONE);
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        mRefreshLayout.refreshComplete();
        if (mShopAdapter.getItemCount() > 0) {
            mActivityUtils.showToast(msg);
            return;
        }
        mTvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        mRefreshLayout.refreshComplete();
        mTvLoadEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideRefresh() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        mShopAdapter.clear();
        if (data != null) mShopAdapter.addData(data);
    }

    @Override
    public void showLoadMoreLoading() {
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        mRefreshLayout.refreshComplete();
        if (mShopAdapter.getItemCount() > 0) {
            mActivityUtils.showToast(msg);
            return;
        }
        mTvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        mActivityUtils.showToast("加载更多~");
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        mShopAdapter.addData(data);
    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }
}
