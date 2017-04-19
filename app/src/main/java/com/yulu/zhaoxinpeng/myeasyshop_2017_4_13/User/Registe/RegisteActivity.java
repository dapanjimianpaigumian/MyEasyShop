package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.RegexUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.AlertDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisteActivity extends MvpActivity<RegisteView,RegistePresenter> implements RegisteView{

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
    private ProgressDialogFragment mProgressDialogFragment;

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

    @NonNull
    @Override
    public RegistePresenter createPresenter() {
        return new RegistePresenter();
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

        getPresenter().Registe(Username,Password);
    }

    @Override
    public void showPrb() {

        mActivityUtils.hideSoftKeyboard();

        if (mProgressDialogFragment==null) {
            mProgressDialogFragment=new ProgressDialogFragment();
        }

        if (mProgressDialogFragment.isVisible()) return;

        mProgressDialogFragment.show(getSupportFragmentManager(),"dialogFragment");
    }

    @Override
    public void hidePrb() {
        mProgressDialogFragment.dismiss();
    }

    @Override
    public void registerSuccess() {

        mActivityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void registerFailed() {
        mEtUsername.setText("");
        mEtPwd.setText("");
        mEtPwdAgain.setText("");
    }

    @Override
    public void showMsg(String msg) {
        mActivityUtils.showToast(msg);
    }

}
