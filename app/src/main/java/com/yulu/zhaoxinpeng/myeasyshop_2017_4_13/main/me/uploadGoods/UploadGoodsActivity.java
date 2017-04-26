package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.uploadGoods;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ImageUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.MyFileUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.HeadPicPopWindow;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.ProgressDialogFragment;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsUpLoad;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.ImageItem;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UploadGoodsActivity extends MvpActivity<UploadGoodsView, UploadGoodsPresenter> implements UploadGoodsView {

    @BindView(R.id.tv_goods_delete)
    TextView mTvGoodsDelete;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_goods_name)
    EditText mEtGoodsName;
    @BindView(R.id.et_goods_price)
    EditText mEtGoodsPrice;
    @BindView(R.id.tv_goods_type)
    TextView mTvGoodsType;
    @BindView(R.id.et_goods_describe)
    EditText mEtGoodsDescribe;
    @BindView(R.id.btn_goods_load)
    Button mBtnGoodsLoad;
    @BindView(R.id.btn_goods_type)
    Button btnGoodsType;
    private Unbinder bind;
    private final String[] goods_type = {"家用", "电子", "服饰", "玩具", "图书", "礼品", "其它"};
    private final String[] goods_type_num = {"household", "electron", "dress", "toy", "book", "gift", "other"};
    private ActivityUtils mActivityUtils;
    private String str_goods_name;//商品名
    private String str_goods_price;//商品价格
    private String str_goods_type = goods_type_num[0];//商品类型（默认家用）
    private String str_goods_describe;//商品描述
    //模式：普通 =1
    public static final int MODE_DONE = 1;
    //模式：删除 =2
    public static final int MODE_DELETE = 2;
    private int title_mode = MODE_DONE;
    private ArrayList<ImageItem> list = new ArrayList<>();
    private ArrayList<ImageItem> filePhoto;
    private UploadGoodsAdapter mUploadGoodsAdapter;
    private ProgressDialogFragment mProgressDialogFragment;
    private HeadPicPopWindow mHeadPicPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        //PopuWindow
        mHeadPicPopWindow = new HeadPicPopWindow(this, listener);

        //RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        //设置动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);

        //获取缓存文件中的文件
        list = getFilePhoto();
        mUploadGoodsAdapter = new UploadGoodsAdapter(this, list);
        mUploadGoodsAdapter.setListener(clickedListener);
        mRecyclerView.setAdapter(mUploadGoodsAdapter);

        //输入框设置监听
        mEtGoodsName.addTextChangedListener(mTextWatcher);
        mEtGoodsPrice.addTextChangedListener(mTextWatcher);
        mEtGoodsDescribe.addTextChangedListener(mTextWatcher);

    }

    //商品名称，价格，描述输入的监听
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            str_goods_name =  mEtGoodsName.getText().toString();
            str_goods_price = mEtGoodsPrice.getText().toString();
            str_goods_describe = mEtGoodsDescribe.getText().toString();

            boolean can_click = !(TextUtils.isEmpty(str_goods_name) || TextUtils.isEmpty(str_goods_price)
                    || TextUtils.isEmpty(str_goods_describe));
            mBtnGoodsLoad.setEnabled(can_click);
        }
    };

    //适配器中自定义的监听事件
    private UploadGoodsAdapter.OnItemClickedListener clickedListener = new UploadGoodsAdapter.OnItemClickedListener() {
        @Override
        public void onAddClicked() {
            //无图，单击，添加图片
            if (mHeadPicPopWindow != null && mHeadPicPopWindow.isShowing()) {
                mHeadPicPopWindow.dismiss();
            } else if (mHeadPicPopWindow != null) {
                mHeadPicPopWindow.show();
            }
        }

        @Override
        public void onPhotoClicked(ImageItem imageItem, ImageView imageView) {
            //有图，单击，跳转到图片展示页
            Intent intent = new Intent(UploadGoodsActivity.this, UploadGoodsShowImageActivity.class);
            intent.putExtra("bitmap",imageItem.getBitmap());
            intent.putExtra("width",imageView.getWidth());
            intent.putExtra("height",imageView.getHeight());
            startActivity(intent);
        }

        @Override
        public void onLongClicked() {
            //有图，长摁，执行删除相关代码
            title_mode = MODE_DELETE;
            mTvGoodsDelete.setVisibility(View.VISIBLE);
        }
    };

    //图片选择弹窗内的监听事件
    private HeadPicPopWindow.Listener listener = new HeadPicPopWindow.Listener() {
        @Override
        public void toGallery() {
            //打开相册
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(mCropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            //打开相机
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(mCropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    private CropHandler mCropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //需求：裁剪完成后，吧图片保存为bitmap，并且保存到sd中，并且展示出来
            //文件名：就是用系统当前时间，不重复
            String filename = String.valueOf(System.currentTimeMillis());
            //得到 bitmap
            Bitmap bitmap = ImageUtils.readDownsampledImage(uri.getPath(), 1080, 1920);
            //保存到存储中
            MyFileUtils.saveBitmap(bitmap, filename);
            //展示出来
            ImageItem imageItem = new ImageItem();
            imageItem.setImagePath(filename + ".JPEG");
            imageItem.setBitmap(bitmap);
            mUploadGoodsAdapter.add(imageItem);
            mUploadGoodsAdapter.notifyImageItemList();

        }

        @Override
        public void onCropCancel() {
            mActivityUtils.showToast("取消裁剪");
        }

        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast(message);
        }

        @Override
        public CropParams getCropParams() {
            CropParams params = new CropParams();
            params.aspectX = 400;
            params.aspectY = 400;
            return params;
        }

        @Override
        public Activity getContext() {
            return UploadGoodsActivity.this;
        }
    };

    //当Activity拿到图片裁剪的返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CropHelper帮助我们处理结果
        CropHelper.handleResult(mCropHandler, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @NonNull
    @Override
    public UploadGoodsPresenter createPresenter() {
        return new UploadGoodsPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressbar() {
        if (mProgressDialogFragment == null) mProgressDialogFragment = new ProgressDialogFragment();
        mProgressDialogFragment.show(getSupportFragmentManager(), "dialogfragment");
    }

    @Override
    public void hideProgressbar() {
        mProgressDialogFragment.dismiss();
    }

    @Override
    public void uploadSuccess() {
        finish();
    }

    @Override
    public void ShowToast(String s) {
        mActivityUtils.showToast(s);
    }

    //获取缓存文件夹中的文件
    public ArrayList<ImageItem> getFilePhoto() {
        ArrayList<ImageItem> items = new ArrayList<>();
        //拿到所有图片文件
        File[] files = new File(MyFileUtils.SD_PATH).listFiles();
        if (files != null) {
            for (File file : files) {
                //解码file拿到bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(MyFileUtils.SD_PATH + file.getName());
                ImageItem item = new ImageItem();
                item.setImagePath(file.getName());
                item.setBitmap(bitmap);
                items.add(item);
            }
        }
        return items;
    }

    //重写返回方法，实现点击返回改变模式

    @Override
    public void onBackPressed() {
        if (title_mode == MODE_DONE) {
            //删除缓存
            deleteImageCache();
            finish();
        } else if (title_mode == MODE_DELETE) {
            changeMODE();
        }
    }

    //转变模式--改为普通模式
    private void changeMODE() {
        //判断，根据adapter判断当前模式是否是可删除模式
        if (mUploadGoodsAdapter.getMode() == UploadGoodsAdapter.MODE_SELECT) {
            //隐藏删除TV
            mTvGoodsDelete.setVisibility(View.GONE);
            title_mode = MODE_DONE;
            mUploadGoodsAdapter.changeMode(UploadGoodsAdapter.MODE_NORMAL);
            for (int i = 0; i < mUploadGoodsAdapter.getImageItemlist().size(); i++) {
                mUploadGoodsAdapter.getImageItemlist().get(i).setIsCheck(false);
            }
        }
    }

    //删除缓存(删除缓存文件夹中的文件)
    private void deleteImageCache() {
        for (int i = 0; i < mUploadGoodsAdapter.getImageItemlist().size(); i++) {
            MyFileUtils.delFile(mUploadGoodsAdapter.getImageItemlist().get(i).getImagePath());
        }
    }


    @OnClick({R.id.tv_goods_delete, R.id.btn_goods_load,R.id.btn_goods_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //删除
            case R.id.tv_goods_delete:
                ArrayList<ImageItem> del_list = mUploadGoodsAdapter.getImageItemlist();
                int size = del_list.size();
                for (int i = size-1; i >=0; i--) {
                    //删除规则（checkbox被选中）
                    if (del_list.get(i).isCheck()) {
                        //删除缓存文件夹中的缓存
                        MyFileUtils.delFile(del_list.get(i).getImagePath());
                        del_list.remove(i);
                    }
                }
                this.list=del_list;
                mUploadGoodsAdapter.notifyImageItemList();
                changeMODE();
                title_mode=MODE_DONE;
                break;
            case R.id.btn_goods_load:
                if (mUploadGoodsAdapter.getSize()==0) {
                    mActivityUtils.showToast("至少上传一张图片！");
                }
                presenter.uploadgoods(getGoodsInfo(),list);
                break;
            case R.id.btn_goods_type:
                //点击商品类别
                new AlertDialog.Builder(this)
                        .setTitle("商品类型")
                        .setItems(goods_type, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //显示商品类型
                                mTvGoodsType.setText(goods_type[which]);
                                //拿到商品类型的英文描述（用于上传）
                                str_goods_type=goods_type_num[which];
                            }
                        }).create().show();
                break;
        }
    }

    //获取商品信息的方法
    private GoodsUpLoad getGoodsInfo(){
        GoodsUpLoad mGoodsUpLoad = new GoodsUpLoad();
        mGoodsUpLoad.setName(str_goods_name);
        mGoodsUpLoad.setPrice(str_goods_price);
        mGoodsUpLoad.setDescribe(str_goods_describe);
        mGoodsUpLoad.setType(str_goods_type);
        mGoodsUpLoad.setMaster(CachePreferences.getUser().getName());
        return mGoodsUpLoad;
    }
}
