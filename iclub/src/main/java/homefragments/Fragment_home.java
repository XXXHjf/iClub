package homefragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;
import com.example.icluub.clubListActivity;
import com.example.icluub.myJoinedClubs;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;

import bean.BannerBean_resource;

public class Fragment_home extends Fragment implements View.OnClickListener {

    private TextView tv_banner_title;

    public Fragment_home() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //轮播图对应的标题
        tv_banner_title = view.findViewById(R.id.tv_banner_title);

        // UI里做了但是暂时不打算实现的功能，先设置为不可见
        view.findViewById(R.id.iv_homeFunction_function01).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tv_home_function_function01).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.iv_homeFunction_function02).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tv_home_function_function02).setVisibility(View.INVISIBLE);

        // 给各个功能设置监听器，跳转到对应的页面
        view.findViewById(R.id.iv_homeFunction_clublist).setOnClickListener(this);
        view.findViewById(R.id.iv_homeFunction_myClublist).setOnClickListener(this);

        initView(view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_homeFunction_clublist) {
            // 创建意图(第二个参数是意图的目标活动)，然后启动意图
            Intent intent = new Intent(requireContext(), clubListActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.iv_homeFunction_myClublist) {
            Intent intent = new Intent(requireContext(), myJoinedClubs.class);
            startActivity(intent);
        }
    }

    private void initView(View view) {
        //创建一个实例类保存轮播图每张图片的信息
        ArrayList<BannerBean_resource> bannerBeanList = new ArrayList<>();
        bannerBeanList.add(new BannerBean_resource("2", "被迫营业被迫营业被迫营业被迫营业被迫营业", R.drawable.forced_working));
        bannerBeanList.add(new BannerBean_resource("1", "啦啦操比赛合影啦啦操比赛合影啦啦操比赛合影", R.drawable.cheerleading_groupphoto));
        bannerBeanList.add(new BannerBean_resource("4", "鲁迪乌斯希露菲", R.drawable.ludiwusi));
        bannerBeanList.add(new BannerBean_resource("3", "省运会合影省运会合影省运会合影省运会合影", R.drawable.provincialgames_groupphoto));

        Banner banner_slideshow = view.findViewById(R.id.banner_slideshow);
        banner_slideshow.setAdapter(new BannerImageAdapter<BannerBean_resource>(bannerBeanList) {
            @Override
            public void onBindView(BannerImageHolder holder, BannerBean_resource bannerBean, int position, int size) {
                Glide.with(holder.imageView).load(bannerBean.getResource())
                        .thumbnail(Glide.with(holder.itemView).load(R.drawable.ic_loading))   //加载成功前显示一个loading的加载
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))   //设置图片圆角
                        .into(holder.imageView);
            }
        });
        banner_slideshow.addBannerLifecycleObserver(this);  //添加生命周期观察者
        banner_slideshow.setIntercept(false);    //是否要拦截事件
        banner_slideshow.setBannerRound(30f); //圆角
        banner_slideshow.setIndicator(new CircleIndicator(getContext())); //圆形指示器 还支持条形指示器等
        banner_slideshow.setIndicatorGravity(IndicatorConfig.Direction.RIGHT);   //设置指示器位置在右边

        // 设置画廊效果，第一个参数是缩放比例，第二个参数是缩放的偏移量
//        banner_slideshow.setBannerGalleryEffect(30, 10);
        // 设置点击事件
//        banner_slideshow.setOnBannerListener(new OnBannerListener() {
//                    @Override
//                    public void OnBannerClick(Object data, int position) {
//                        BannerBean_resource bannerBean = (BannerBean_resource) data;
//                        String title = bannerBean.getTitle();
//                        tv_banner_title.setText(title);
//                        Toast.makeText(Home.this,"位置"+position+"",Toast.LENGTH_SHORT).show();
//                    }
//                });
        // 自定义监听器，用于更新轮播图标题
        banner_slideshow.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String title = bannerBeanList.get(position).getTitle();
                tv_banner_title.setText(title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 初始化标题控件
        tv_banner_title = view.findViewById(R.id.tv_banner_title);
        tv_banner_title.setText(bannerBeanList.get(0).getTitle());
    }

}