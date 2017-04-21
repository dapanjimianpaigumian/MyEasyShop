package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo.nickname;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.RegexUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.attr.id;

public class Nick_NameActivity extends MvpActivity<Nick_NameView,Nick_NamePresenter> implements Nick_NameView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.btn_save)
    Button mBtnSave;
    private Unbinder bind;
    private String nickname;
    private boolean canChange;
    private ActivityUtils mActivityUtils;
    private ProgressDialogFragment mProgressDialogFragment;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        bind = ButterKnife.bind(this);

        mActivityUtils=new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtNickname.addTextChangedListener(mTextWatcher);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
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
            nickname = mEtNickname.getText().toString();

            if (RegexUtils.verifyNickname(nickname) == RegexUtils.VERIFY_SUCCESS) {
                canChange = true;
            }else if(RegexUtils.verifyNickname(nickname)==RegexUtils.VERIFY_LENGTH_ERROR||
                    RegexUtils.verifyNickname(nickname)==RegexUtils.VERIFY_TYPE_ERROR){
                mActivityUtils.showToast(R.string.nickname_rules);
                mEtNickname.setText("");
            }

            mBtnSave.setEnabled(canChange);
        }
    };

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        //mActivityUtils.showToast("已修改成功~ "+nickname);

        Log.e("onViewClicked",nickname);

        mUser = CachePreferences.getUser();
        Log.e("mUser1",mUser.getNick_name());
        mUser.setNick_name(nickname);
        Log.e("mUser2",mUser.getNick_name());

        presenter.changeNickName(mUser);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    //--------------------------------------------视图接口操作----------------------------------------------
    @NonNull
    @Override
    public Nick_NamePresenter createPresenter() {
        return new Nick_NamePresenter();
    }

    @Override
    public void showProgressbar() {
        mActivityUtils.hideSoftKeyboard();
        if (mProgressDialogFragment==null) mProgressDialogFragment=new ProgressDialogFragment();
        if (mProgressDialogFragment.isVisible()) return;
        mProgressDialogFragment.show(getSupportFragmentManager(),"dialogFragmenet");
    }

    @Override
    public void hideProgressbar() {
        mProgressDialogFragment.dismiss();
    }

    @Override
    public void showToast(String s) {
        mActivityUtils.showToast(s);
    }

    @Override
    public void changenickname(User user) {
        CachePreferences.setUser(user);
    }
}
