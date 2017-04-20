package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login.LoginActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/16.
 */

public class MeFragment extends Fragment {

    Unbinder unbinder;
    private ActivityUtils mActivityUtils;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            mActivityUtils=new ActivityUtils(this);
            unbinder = ButterKnife.bind(this, view);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_user_head,R.id.tv_login, R.id.tv_person_info, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onViewClicked(View view) {
        //需要判断用户是否登录，从而决定跳转的位置
        if (CachePreferences.getUser().getName() == null){
            mActivityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_login:
                break;
            case R.id.tv_person_info:
                break;
            case R.id.tv_person_goods:
                break;
            case R.id.tv_goods_upload:
                break;
            case R.id.iv_user_head:
                break;
        }

    }
}
