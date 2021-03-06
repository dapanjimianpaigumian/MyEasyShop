package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe.RegisteActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.MainActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends MvpActivity<LoginView,LoginPresenter> implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    private ActivityUtils mActivityUtils;
    private String username;
    private String password;
    private Unbinder bind;
    private ProgressDialogFragment mProgressDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        initView();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPwd.addTextChangedListener(mTextWatcher);
    }

    //EditText监听
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username = mEtUsername.getText().toString();
            password = mEtPwd.getText().toString();

            //判断用户名、密码是否为空
            boolean canLogin = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
            mBtnLogin.setEnabled(canLogin);
        }
    };

    //设置为ture，显示返回按钮，但是点击效果需要实现菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                getPresenter().Login(username,password);

                break;
            case R.id.tv_register:
                mActivityUtils.startActivity(RegisteActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public void showPrb() {

        mActivityUtils.hideSoftKeyboard();
        if (mProgressDialogFragment==null) mProgressDialogFragment=new ProgressDialogFragment();
        if (mProgressDialogFragment.isVisible()) return;
        mProgressDialogFragment.show(getSupportFragmentManager(),"dialogFragmenet");
    }

    @Override
    public void hidePrb() {
        mProgressDialogFragment.dismiss();
    }

    @Override
    public void loginFailed() {
        mEtUsername.setText("");
        mEtPwd.setText("");
    }

    @Override
    public void loginSuccess() {
        mActivityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMsg(String msg) {
        mActivityUtils.showToast(msg);
    }
}
