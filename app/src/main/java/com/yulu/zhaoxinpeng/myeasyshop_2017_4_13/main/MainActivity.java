package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.feicuiedu.apphx.presentation.contact.list.HxContactListFragment;
import com.feicuiedu.apphx.presentation.conversation.HxConversationListFragment;
import com.nostra13.universalimageloader.utils.L;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.MeFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.ShopFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_title)
    TextView mTitle;
    @BindView(R.id.main_toobar)
    Toolbar mToobar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindViews({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    TextView[] mTextViews;
    @BindView(R.id.tv_shop)
    TextView mTvShop;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.tv_mail_list)
    TextView mTvMailList;
    @BindView(R.id.tv_me)
    TextView mTvMe;

    //点击2次返回，退出程序
    private boolean isExit = false;
    private ActivityUtils mActivityUtils;
    private Unbinder bind;
    private ShopFragment mShopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        initView();
    }

    private void initView() {

        //判断用户是否登录，从而选择不同的适配器
        if (CachePreferences.getUser().getName()==null) {
            mViewPager.setAdapter(unLoginAdapter);
            mViewPager.setCurrentItem(0);
        }else {
            mViewPager.setAdapter(mFragmentStatePagerAdapter);
            mViewPager.setCurrentItem(0);
        }

        //默认刚进来显示市场Fragment
        mTextViews[0].setSelected(true);

        //通过ViewPager滑动监听,控制TextView的图标
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //先全部初始化为未选择状态
                for (TextView textView : mTextViews) {
                    textView.setSelected(false);
                }

                //然后设置选择效果
                mTitle.setText(mTextViews[position].getText());
                mTextViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //用户已登录的适配器
    private FragmentStatePagerAdapter mFragmentStatePagerAdapter =  new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mShopFragment == null) {
                        mShopFragment = new ShopFragment();
                        return mShopFragment;
                    }
                    return mShopFragment;
                case 1:
                    //消息
                    return new HxConversationListFragment();
                case 2:
                    //通讯录
                    return new HxContactListFragment();
                case 3:
                    //我的
                    return new MeFragment();
            }
            return null;
        }
        @Override
        public int getCount() {
            return 4;
        }
    };
    //用户未登录时的适配器
    protected FragmentStatePagerAdapter unLoginAdapter=new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                case 1:
                    return new UnLoginFragment();
                case 2:
                    return new UnLoginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };



    @Override
    public void onBackPressed() {
        //点击2次返回，退出程序
        if (!isExit) {
            isExit = true;
            mActivityUtils.showToast("再按一次退出程序！");
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @OnClick({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    public void onViewClicked(TextView textView) {
        for (int i = 0; i < mTextViews.length; i++) {

            //初始化为未选择状态
            mTextViews[i].setSelected(false);

            //设置标识
            mTextViews[i].setTag(i);
        }
        //设置当前textView的选择效果
        textView.setSelected(true);

        //如果不要平滑效果，可将第二个参数为false
        mViewPager.setCurrentItem((int) textView.getTag(), true);
        mTitle.setText(mTextViews[(int) textView.getTag()].getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
