package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components.AvatarLoadOptions;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailPicActivity extends AppCompatActivity {

    private static final String PICS = "pics";
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    private Unbinder bind;
    private ArrayList<String> mList;

    public static Intent getPicIntent(Context context,ArrayList<String> picUrllist){
        Intent intent = new Intent(context, GoodsDetailPicActivity.class);
        intent.putExtra(PICS,picUrllist);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail_info);

        mList=new ArrayList<>();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        bind = ButterKnife.bind(this);
        mList=getIntent().getStringArrayListExtra(PICS);

        GoodsDetailImagesAdapter adapter = new GoodsDetailImagesAdapter(getPic(mList));
        mViewpager.setAdapter(adapter);
        adapter.setClickListener(new GoodsDetailImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                finish();
            }
        });
        mIndicator.setViewPager(mViewpager);
    }

    private ArrayList<ImageView> getPic(ArrayList<String> list){
        ArrayList<ImageView> list_pic = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ImageView view=new ImageView(this);
            ImageLoader.getInstance()
                    .displayImage(NetApi.IMAGE_URL+list.get(i),
                            view, AvatarLoadOptions.build_item());
            list_pic.add(view);

            Log.e("list_pic.size()", list_pic.size()+"");
        }
        return list_pic;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //bind.unbind();
    }
}
