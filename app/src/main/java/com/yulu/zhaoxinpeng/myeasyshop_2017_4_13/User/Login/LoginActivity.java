package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe.RegisteActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.UICallBack;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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

                NetClient.getInstance().Login(username,password).enqueue(new UICallBack() {
                    @Override
                    public void onFailureUI(Call call, IOException e) {
                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseUI(Call call, String body) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.tv_register:
                mActivityUtils.startActivity(RegisteActivity.class);
                break;
        }
    }
}
