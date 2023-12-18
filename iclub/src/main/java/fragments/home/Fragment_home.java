package fragments.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;
import com.example.icluub.actCenterActivity;
import com.example.icluub.actMineActivity;
import com.example.icluub.clubListActivity;
import com.example.icluub.clubManageActivity;
import com.example.icluub.clubMineActivity;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;

import Beans.BeanUser;
import bean.BannerBean_resource;
import util.SPDataUtils;

public class Fragment_home extends Fragment implements View.OnClickListener {

    private TextView tv_banner_title;
    private int isPresident = 0;

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

        initView(view);

        BeanUser beanUser = SPDataUtils.getUserInfo(requireContext());
        isPresident = beanUser.getIsPresident();
        if (isPresident == 0) {
            view.findViewById(R.id.iv_homeFunction_clubManagement).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.tv_home_function_clubManagement).setVisibility(View.INVISIBLE);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = requireView();
        view.findViewById(R.id.iv_homeFunction_clubList).setOnClickListener(this);
        view.findViewById(R.id.iv_homeFunction_myclubList).setOnClickListener(this);
        view.findViewById(R.id.iv_homeFunction_activities).setOnClickListener(this);
        view.findViewById(R.id.iv_homeFunction_myActivities).setOnClickListener(this);
        view.findViewById(R.id.iv_homeFunction_clubManagement).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_homeFunction_clubList) {
            // 创建意图(第二个参数是意图的目标活动)，然后启动意图
            Intent intent = new Intent(requireContext(), clubListActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.iv_homeFunction_myclubList) {
            Intent intent = new Intent(requireContext(), clubMineActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.iv_homeFunction_activities) {
            Intent intent = new Intent(requireContext(), actCenterActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.iv_homeFunction_myActivities) {
            Intent intent = new Intent(requireContext(), actMineActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.iv_homeFunction_clubManagement) {
            Intent intent = new Intent(requireContext(), clubManageActivity.class);
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