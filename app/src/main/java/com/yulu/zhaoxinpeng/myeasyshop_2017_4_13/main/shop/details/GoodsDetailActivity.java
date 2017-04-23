package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login.LoginActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.AvatarLoadOptions;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsDetail;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2017/4/22.
 * 单个商品详情页
 */

public class GoodsDetailActivity extends MvpActivity<GoodsDetailView, GoodsDetailPresenter> implements GoodsDetailView {

    @BindView(R.id.tv_goods_delete)
    TextView mTvGoodsDelete;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    @BindView(R.id.tv_detail_name)
    TextView mTvDetailName;
    @BindView(R.id.tv_detail_price)
    TextView mTvDetailPrice;
    @BindView(R.id.tv_detail_master)
    TextView mTvDetailMaster;
    @BindView(R.id.tv_detail_describe)
    TextView mTvDetailDescribe;
    @BindView(R.id.btn_detail_message)
    Button mBtnDetailMessage;
    @BindView(R.id.tv_goods_error)
    TextView mTvGoodsError;
    private ActivityUtils mActivityUtils;
    private Unbinder bind;
    public User user;
    private String uuid;//商品的uuid
    private ArrayList<ImageView> iv_ArrayList;//存放图片的集合
    private ArrayList<String> uri_ArrayList;//存放图片路径的集合
    private static final String UUID = "uuid";
    //从不同的页面进入详情页的状态值，0 = 从市场页面，1 = 从我的页面进来的
    private static final String STATE = "state";
    private GoodsDetailImagesAdapter mGoodsDetailImagesAdapter;

    private ProgressDialogFragment mProgressDialogFragment;

    //从不同界面跳转至本界面的方法
    public static Intent getStateIntent(Context context, String uuid, int state) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(UUID, uuid);
        intent.putExtra(STATE, state);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        Log.e("R.layout", R.layout.activity_goods_detail + "");
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uri_ArrayList = new ArrayList<>();
        iv_ArrayList = new ArrayList<>();
        mGoodsDetailImagesAdapter = new GoodsDetailImagesAdapter(iv_ArrayList);
        mGoodsDetailImagesAdapter.setClickListener(new GoodsDetailImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                mActivityUtils.showToast("点击图片，跳转至图片ViewPager");
            }
        });
        mViewpager.setAdapter(mGoodsDetailImagesAdapter);

        initView();

    }

    private void initView() {
        uuid = getIntent().getStringExtra(UUID);
        int state = getIntent().getIntExtra(STATE, 0);

        if (state == 1) {//来自我的页面
            mTvGoodsDelete.setVisibility(View.INVISIBLE);
            mBtnDetailMessage.setVisibility(View.GONE);
        }

        presenter.getGoodsDetail(uuid);//获取商品详情数据
        Log.e("uuid", uuid);
    }

    @OnClick({R.id.tv_goods_delete, R.id.btn_detail_message})
    public void onViewClicked(View view) {

        if (CachePreferences.getUser().getName() == null) {
            mActivityUtils.startActivity(LoginActivity.class);
            finish();
            return;
        }
        switch (view.getId()) {
            case R.id.tv_goods_delete:
                //// TODO: 2017/4/22 0021 执行删除操作
                mActivityUtils.showToast("执行删除操作,待实现");
                break;
            case R.id.btn_detail_message:
                // TODO: 2017/4/22 0021 跳转到环信发消息的页面
                mActivityUtils.showToast("跳转到环信发消息的页面,待实现");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @NonNull
    @Override
    public GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter();
    }

    //-------------------------------------------视图接口操作-------------------------------------------------
    @Override
    public void showProgressbar() {
        if (mProgressDialogFragment == null) mProgressDialogFragment = new ProgressDialogFragment();
        if (mProgressDialogFragment.isVisible()) return;
        mProgressDialogFragment.show(getSupportFragmentManager(), "fragment_progress_dialog");
    }

    @Override
    public void hideProgressbar() {
        mProgressDialogFragment.dismiss();
    }

    @Override
    public void setImageUrlData(ArrayList<String> arrayList) {
        uri_ArrayList = arrayList;

        for (int i = 0; i < uri_ArrayList.size(); i++) {
            ImageView imageView = new ImageView(this);
            ImageLoader.getInstance().displayImage(NetApi.IMAGE_URL + uri_ArrayList.get(i),
                    imageView, AvatarLoadOptions.build_item());
            iv_ArrayList.add(imageView);
        }

        mGoodsDetailImagesAdapter.notifyDataSetChanged();
        mIndicator.setViewPager(mViewpager);
    }

    @Override
    public void setData(GoodsDetail goodsDetail, User user) {
        this.user = user;
        mTvDetailName.setText(goodsDetail.getName());
        mTvDetailPrice.setText(getString(R.string.goods_money, goodsDetail.getPrice()));
        mTvDetailMaster.setText(getString(R.string.goods_detail_master, goodsDetail.getMaster()));
        mTvDetailDescribe.setText(goodsDetail.getDescription());
    }

    @Override
    public void showErro(String s) {
        mTvGoodsError.setVisibility(View.VISIBLE);
        mToolbar.setTitle(getString(R.string.goods_detail_out));
    }

    @Override
    public void showToast(String s) {
        mActivityUtils.showToast(s);
    }

    @Override
    public void deleteGoods() {
        finish();
    }

}
