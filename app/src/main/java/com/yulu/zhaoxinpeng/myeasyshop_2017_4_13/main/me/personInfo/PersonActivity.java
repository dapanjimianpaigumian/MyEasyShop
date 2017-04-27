package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.feicuiedu.apphx.model.HxUserManager;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hyphenate.easeui.controller.EaseUI;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.AvatarLoadOptions;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.HeadPicPopWindow;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.MainActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo.nickname.Nick_NameActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.ItemShow;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetApi;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonActivity extends MvpActivity<PersonView, PersonPresenter> implements PersonView {

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
    private List<ItemShow> mList = new ArrayList<>();
    private PersonAdapter mPersonAdapter;
    private ProgressDialogFragment dialogFragment;
    private HeadPicPopWindow mPopWindow;

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

        //获取用户头像
        updateAvatar(CachePreferences.getUser().getHead_Image());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mList.clear();
        initList();
        mPersonAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        if (cropHandler.getCropParams() != null)
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
    }

    @NonNull
    @Override
    public PersonPresenter createPresenter() {
        return new PersonPresenter();
    }

    //创建List数据源
    private void initList() {
        User user = CachePreferences.getUser();
        mList.add(new ItemShow("用户名", user.getName()));
        mList.add(new ItemShow("昵称", user.getNick_name()));
        mList.add(new ItemShow("环信ID", user.getHx_Id()));
    }

    //设置ListView 监听
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    mActivityUtils.showToast(getString(R.string.username_update));
                    break;
                case 1:
                    mActivityUtils.startActivity(Nick_NameActivity.class);
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
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_user_head, R.id.btn_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                //弹出PopWindow,选择头像（照相机，相册）
                if (mPopWindow == null) {
                    mPopWindow = new HeadPicPopWindow(this, listener);
                }
                if (mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                    return;
                }
                mPopWindow.show();
                break;
            case R.id.btn_login_out:
                //清空本地配置
                CachePreferences.clearAllData();
                //清除所有已经存在的Activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                //退出环信ID
                HxUserManager.getInstance().asyncLogout();
                //登出关掉通知栏中的通知
                EaseUI.getInstance().getNotifier().reset();
                break;
        }
    }

    //-------------------------------------------剪裁图片操作-----------------------------------------------

    // Step 1
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //帮助我们去处理结果（裁剪完成的图像）
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    // Step 2
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //图片裁剪结束后
            //通过uri拿到图片文件
            File file = new File(uri.getPath());
            //上传头像
            presenter.updateAvatar(file);
        }

        @Override
        public void onCropCancel() {
            mActivityUtils.showToast("取消裁剪");
        }

        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast(message);
        }

        //设置裁剪图片的参数
        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 400;
            cropParams.aspectY = 400;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return PersonActivity.this;
        }
    };

    // Step 3
    //图片来源选择弹窗的监听
    private HeadPicPopWindow.Listener listener = new HeadPicPopWindow.Listener() {
        //从相册中选择
        //首先清空裁剪的缓存
        @Override
        public void toGallery() {
            mActivityUtils.showToast("从相册获取图片");
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        //通过相机获取图片
        //首先清空裁剪的缓存
        @Override
        public void toCamera() {
            mActivityUtils.showToast("通过相机获取图片");
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };


    //--------------------------------------视图接口操作----------------------------------------------------
    @Override
    public void showProgressbar() {
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialogfragment");
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
        //头像加载操作
        ImageLoader.getInstance()
                //参数，“头像路径（服务器）”，“头像显示的控件”，“加载选项”
                .displayImage(NetApi.IMAGE_URL+url,mIvUserHead,
                        AvatarLoadOptions.build());
    }
}
