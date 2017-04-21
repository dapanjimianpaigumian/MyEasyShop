package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login.LoginActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.AvatarLoadOptions;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo.PersonActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/16.
 */

public class MeFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.iv_user_head)
    CircularImageView mIvHead;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_person_info)
    TextView mTvPersonInfo;
    @BindView(R.id.tv_person_goods)
    TextView mTvPersonGoods;
    @BindView(R.id.tv_goods_upload)
    TextView mTvGoodsUpload;
    private ActivityUtils mActivityUtils;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            mActivityUtils = new ActivityUtils(this);
            unbinder = ButterKnife.bind(this, view);

            if (CachePreferences.getUser().getName()!=null) {
                mTvLogin.setText(CachePreferences.getUser().getNick_name());
                //头像加载操作
                ImageLoader.getInstance()
                        //参数，“头像路径（服务器）”，“头像显示的控件”，“加载选项”
                        .displayImage(NetApi.IMAGE_URL+CachePreferences.getUser().getHead_Image(),mIvHead,
                                AvatarLoadOptions.build());
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CachePreferences.getUser().getName()==null) {
            mTvLogin.setText(getString(R.string.login_register));
            mIvHead.setImageResource(R.drawable.user_ico);
            return;
        }else if(CachePreferences.getUser().getName()!=null){
            mTvLogin.setText(CachePreferences.getUser().getNick_name());
            //头像加载操作
            ImageLoader.getInstance()
                    //参数，“头像路径（服务器）”，“头像显示的控件”，“加载选项”
                    .displayImage(NetApi.IMAGE_URL+CachePreferences.getUser().getHead_Image(),mIvHead,
                            AvatarLoadOptions.build());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_user_head, R.id.tv_login, R.id.tv_person_info, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onViewClicked(View view) {
        //需要判断用户是否登录，从而决定跳转的位置
        if (CachePreferences.getUser().getName() == null) {
            mActivityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_login:
                break;
            case R.id.tv_person_info:
                if (CachePreferences.getUser()==null) {
                    mActivityUtils.startActivity(LoginActivity.class);
                }else {
                    mActivityUtils.startActivity(PersonActivity.class);
                }
                break;
            case R.id.tv_person_goods:
                //跳转到我的商品页面
                mActivityUtils.showToast("跳转到我的商品页面,待实现");
                break;
            case R.id.tv_goods_upload:
                //跳转到商品上传的页面
                mActivityUtils.showToast("跳转到商品上传的页面，待实现");
                break;
            case R.id.iv_user_head:
                break;
        }

    }
}
