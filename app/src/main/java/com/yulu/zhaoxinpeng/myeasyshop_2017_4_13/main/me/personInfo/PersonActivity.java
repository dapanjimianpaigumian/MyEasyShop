package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.pkmmte.view.CircularImageView;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.MainActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.ItemShow;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.os.Build.VERSION_CODES.M;

public class PersonActivity extends MvpActivity<PersonView,PersonPresenter> implements PersonView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_user_head)
    CircularImageView mIvUserHead;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.btn_login_out)
    Button mBtnLoginOut;
    private Unbinder bind;
    private ActivityUtils mActivityUtils;
    private List<ItemShow> mList=new ArrayList<>();
    private PersonAdapter mPersonAdapter;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        bind = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPersonAdapter = new PersonAdapter(mList);
        mListView.setAdapter(mPersonAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mList.clear();
        initList();
        mPersonAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonPresenter createPresenter() {
        return new PersonPresenter();
    }

    //创建List数据源
    private void initList(){
        User user = CachePreferences.getUser();
        mList.add(new ItemShow("用户名",user.getName()));
        mList.add(new ItemShow("昵称",user.getNick_name()));
        mList.add(new ItemShow("环信ID",user.getHx_Id()));
    }

    //设置ListView 监听
    private AdapterView.OnItemClickListener mOnItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    mActivityUtils.showToast(getString(R.string.username_update));
                    break;
                case 1:
                    mActivityUtils.showToast("跳转到昵称修改页面，待实现");
                    break;
                case 2:
                    mActivityUtils.showToast(getString(R.string.id_update));
                    break;
            }
        }
    };

    //监听 Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_user_head, R.id.btn_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                mActivityUtils.showToast("更新头像，待实现");
                break;
            case R.id.btn_login_out:
                //清空本地配置
                CachePreferences.clearAllData();
                //清除所有已经存在的Activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // TODO: 2017/4/20 退出环信ID
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    //--------------------------------------视图接口操作----------------------------------------------------
    @Override
    public void showProgressbar() {

        if (dialogFragment==null) dialogFragment = new ProgressDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"dialogfragment");
    }

    @Override
    public void hideProgressbar() {
        dialogFragment.dismiss();
    }

    @Override
    public void showToast(String s) {
        mActivityUtils.showToast(s);
    }

    @Override
    public void updateAvatar(String url) {
        // TODO: 2017/4/20 加载头像操作
    }
}
