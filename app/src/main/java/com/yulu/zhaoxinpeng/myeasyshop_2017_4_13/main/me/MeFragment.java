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

    @OnClick({R.id.tv_login, R.id.tv_person_info, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onViewClicked(View view) {
        /*switch (view.getId()) {
            case R.id.tv_login:
                break;
            case R.id.tv_person_info:
                break;
            case R.id.tv_person_goods:
                break;
            case R.id.tv_goods_upload:
                break;
        }*/

        // TODO: 2017/4/14 0014 需要判断用户是否登录，从而决定跳转的位置
        mActivityUtils.startActivity(LoginActivity.class);
    }
}
