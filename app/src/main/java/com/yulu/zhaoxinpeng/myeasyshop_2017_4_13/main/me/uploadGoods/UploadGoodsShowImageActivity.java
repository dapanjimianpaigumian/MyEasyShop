package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.uploadGoods;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 商品上传界面的图片展示页
 */
public class UploadGoodsShowImageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_goods)
    ImageView mIvGoods;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load_image_show);
        bind = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        int width = getIntent().getIntExtra("width", 0);
        int height = getIntent().getIntExtra("height", 0);

        mIvGoods.setImageBitmap(bitmap);
        mIvGoods.setMaxWidth(width);
        mIvGoods.setMaxHeight(height);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
