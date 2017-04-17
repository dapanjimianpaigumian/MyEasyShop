package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.RegexUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.string.ok;

public class RegisteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwdAgain)
    EditText mEtPwdAgain;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    private Unbinder unbinder;
    private ActivityUtils mActivityUtils;
    private String Username;
    private String Password;
    private String Pwdagain;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        unbinder = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        initView();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPwd.addTextChangedListener(mTextWatcher);
        mEtPwdAgain.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Username = mEtUsername.getText().toString();
            Password = mEtPwd.getText().toString();
            Pwdagain = mEtPwdAgain.getText().toString();

            boolean canLogin = !(TextUtils.isEmpty(Username) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Pwdagain));
            mBtnRegister.setEnabled(canLogin);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        if (RegexUtils.verifyUsername(Username) != RegexUtils.VERIFY_SUCCESS) {
            mActivityUtils.showToast(R.string.username_rules);
            return;
        } else if (RegexUtils.verifyPassword(Password) != RegexUtils.VERIFY_SUCCESS) {
            mActivityUtils.showToast(R.string.password_rules);
            return;
        } else if (!TextUtils.equals(Password, Pwdagain)) {
            mActivityUtils.showToast(R.string.username_equal_pwd);
            return;
        }
        OkHttpClient mOkHttpClient = new OkHttpClient();

        RequestBody requestBody=new FormBody.Builder()
                .add("username",Username)
                .add("passowrd",Password)
                .build();

        final Request request=new Request.Builder()
                .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=register")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Registe", "网络连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("Registe", "网络连接成功");
                if (response.isSuccessful()) {
                    Log.e("Registe", "服务器成功响应");
                } else {
                    Log.e("Registe", "请求失败");
                }
            }
        });
    }





    //----------------------------网络模块----------------------------------------------------------------------

}
