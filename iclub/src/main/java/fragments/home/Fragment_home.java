package fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;
import com.example.icluub.actCenterActivity;
import com.example.icluub.actDetailActivity;
import com.example.icluub.actMineActivity;
import com.example.icluub.clubListActivity;
import com.example.icluub.clubManageActivity;
import com.example.icluub.clubMineActivity;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClubActivity;
import Beans.BeanUser;
import SPTools.appStatusSP;
import SPTools.userSP;
import tools.OperationPromptTool;
import util.DBUtil;

public class Fragment_home extends Fragment implements View.OnClickListener, OnBannerListener, OnPageChangeListener {
    private TextView tv_banner_title;
    private int isPresident = 0;
    private final ArrayList<BeanClubActivity> bannerBeanList = new ArrayList<>();
    private Banner banner_slideshow;
    private BannerHomeAdapter bannerHomeAdapter;
    private static final int REQUEST_CODE_SCAN_ONE = 0X01;

    public Fragment_home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // UI里做了但是暂时不打算实现的功能，先设置为不可见
        view.findViewById(R.id.iv_homeFunction_function01).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tv_home_function_function01).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.iv_homeFunction_function02).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tv_home_function_function02).setVisibility(View.INVISIBLE);

        Thread_getBannerActs thread = new Thread_getBannerActs();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initViews(view);

        BeanUser beanUser = userSP.getUserInfo(requireContext());
        isPresident = beanUser.getIsPresident();
        if (isPresident == 0) {
            view.findViewById(R.id.iv_homeFunction_clubManagement).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.tv_home_function_clubManagement).setVisibility(View.INVISIBLE);
        }

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
        view.findViewById(R.id.iv_QRcode_scanner).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner_slideshow.start();
    }

    /**
     * 初始化组件
     * @param view   视图
     */
    private void initViews(View view) {
        tv_banner_title = view.findViewById(R.id.tv_banner_title);
        bannerHomeAdapter = new BannerHomeAdapter(bannerBeanList);
        banner_slideshow = view.findViewById(R.id.banner_slideshow);
        banner_slideshow.setAdapter(bannerHomeAdapter)
                .addBannerLifecycleObserver(this)       //添加生命周期观察者
                .setIntercept(false)        //是否要拦截事件
                .setBannerRound(30f)        // 圆角
                .setIndicator(new CircleIndicator(getContext()))        // 设置圆形指示器
                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)       // 设置指示器位置在右边
                .setOnBannerListener(this)      // 重写的方法在下面
                .addOnPageChangeListener(this)      // 重写的方法在下面
                .setCurrentItem(0);     // 设置初始位置在0
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
        } else if (view.getId() == R.id.iv_QRcode_scanner) {
            if ( appStatusSP.getHasCameraPermissions(requireContext()) )
                ScanUtil.startScan(requireActivity(), REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().create());
            else
                OperationPromptTool.showMsg(requireContext(),"未获取相机权限");
        }
    }

    /**
     * 重写Banner的点击事件
     * @param data     数据实体
     * @param position 当前位置
     */
    @Override
    public void OnBannerClick(Object data, int position) {
        BeanClubActivity bean = (BeanClubActivity) data;
        Intent intent = new Intent(requireContext(), actDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actID", bean.getActID());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String title = bannerBeanList.get(position).getActTitle();
        tv_banner_title.setText(title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class BannerHomeAdapter extends BannerImageAdapter<BeanClubActivity> {
        public BannerHomeAdapter(List<BeanClubActivity> mData) {
            super(mData);
        }

        @Override
        public void onBindView(BannerImageHolder holder, BeanClubActivity data, int position, int size) {
            Glide.with(holder.imageView)
                    .load(data.getActCover())
                    .thumbnail(Glide.with(holder.itemView).load(R.drawable.ic_loading))   //加载成功前显示一个loading的加载
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))   //设置图片圆角
                    .into(holder.imageView);
        }
    }

    /**
     * 自定义线程：随机抽取四个活动的信息
     */
    private class Thread_getBannerActs extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM ClubActivityView AS t1 " +
                        "WHERE t1.ifPassed = 1 " +
                        "  AND t1.actID >= (" +
                        "    SELECT ROUND(RAND() * ((SELECT MAX(actID) FROM ClubActivityView) - (SELECT MIN(actID) FROM ClubActivityView)) " +
                        "+ (SELECT MIN(actID) FROM ClubActivityView)) AS actID " +
                        "  ) " +
                        "ORDER BY t1.actID " +
                        "LIMIT 1";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                for ( bannerBeanList.size(); bannerBeanList.size() < 4; ) {
                    java.sql.ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        BeanClubActivity bean = BeanClubActivity.resultSetToActivity(rs);
                        if ( !bannerBeanList.contains(bean) )
                            bannerBeanList.add(bean);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }

}