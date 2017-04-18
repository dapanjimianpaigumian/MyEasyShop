package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe.RegisteActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements LoginView{

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
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);



        initView();
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

                new LoginPresenter(this).Login(username,password);

                break;
            case R.id.tv_register:
                mActivityUtils.startActivity(RegisteActivity.class);
                break;
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }*/

    @Override
    public void showProgressbar() {

        mProgressDialog=ProgressDialog.show(this,"登录","正在登录中，请稍候~");
    }

    @Override
    public void hideProgressbar() {

        if (mProgressDialog!=null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String s) {
        mActivityUtils.showToast(s);
    }
}
