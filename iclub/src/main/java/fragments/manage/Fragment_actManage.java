package fragments.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.icluub.R;
import com.example.icluub.actDetailActivity;
import com.example.icluub.createActActivity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClub;
import Beans.BeanClubActivity;
import RecyclerViewHolder.ActManageViewHolder;
import tools.TransitionTool;
import util.DBUtil;

public class Fragment_actManage extends Fragment implements View.OnClickListener {
    private int clubID = -1;
    private RecyclerView recv_actList;
    private List<BeanClubActivity> actList = new ArrayList<>();
    private List<Integer> regNumList = new ArrayList<>();
    private ActListAdapter actListAdapter;
    private ImageView iv_fmAct_addActs;
    private ActManageFragmentListener actListener;

    public Fragment_actManage() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActManageFragmentListener)
            actListener = (ActManageFragmentListener) context;
        else
            throw new ClassCastException(context.toString() + "必须实现OnItemClickListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_manage, container, false);
        Bundle bundle = getArguments();
        clubID = bundle.getInt("clubID");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread_getSQL_actsList().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        iv_fmAct_addActs.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        actList = new ArrayList<>();
        regNumList = new ArrayList<>();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        View view = requireView();
        iv_fmAct_addActs = view.findViewById(R.id.iv_fmAct_addActs);
        recv_actList = view.findViewById(R.id.recv_actList);
        actListAdapter = new ActListAdapter();
        recv_actList.setLayoutManager(new LinearLayoutManager(requireContext()));
        recv_actList.setAdapter(actListAdapter);
    }

    /**
     * 重写OnClick方法
     *
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_fmAct_addActs) {
            actListener.onAddActClick();
        }
    }

    /**
     * 点击添加活动时的接口：回到activity中执行回调函数，以方便传递社团信息
     */
    public interface ActManageFragmentListener {
        void onAddActClick();
    }

    /**
     * 自定义线程：查询本社团的所有活动
     */
    private class Thread_getSQL_actsList extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM ClubActivityView WHERE clubID=? ORDER BY closeTime DESC";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, clubID);
                java.sql.ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    BeanClubActivity bean = BeanClubActivity.resultSetToActivity(rs);
                    actList.add(bean);
                    regNumList.add(rs.getInt(13));
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通知适配器数据集已更改
                        actListAdapter.notifyDataSetChanged();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }

    /**
     * 自定义适配器：活动项的适配器
     */
    private class ActListAdapter extends RecyclerView.Adapter<ActManageViewHolder> {

        @NonNull
        @Override
        public ActManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_act_manage, parent, false);
            ActManageViewHolder actManageViewHolder = new ActManageViewHolder(view);
            return actManageViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ActManageViewHolder holder, int position) {
            final BeanClubActivity bean = actList.get(position);

            holder.tv_actManage_actName.setText(bean.getActTitle());
            Timestamp actStartTime = bean.getStartTime();
            Timestamp actEndTime = bean.getEndTime();
            holder.tv_actManage_timeStart.setText(TransitionTool.TimestampToString(actStartTime));
            holder.tv_actManage_timeEnd.setText(TransitionTool.TimestampToString(actEndTime));

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp actCloseTime = bean.getCloseTime();
            if (bean.getIfPassed() == 0) {
                // 活动申请未通过
                Drawable closeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.res_radius_sign_end);
                holder.tv_actManage_signUp.setBackground(closeDrawable);
                holder.tv_actManage_signUp.setText("暂未通过审批");
            } else {
                if (actEndTime.before(now)) {
                    // 活动已经结束的情况
                    Drawable closeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.res_radius_sign_end);
                    holder.tv_actManage_signUp.setBackground(closeDrawable);
                    holder.tv_actManage_signUp.setText("活动已结束");
                } else if (actCloseTime.before(now)) {
                    // 活动报名截至的情况
                    Drawable endDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.res_radius_sign_ddl);
                    holder.tv_actManage_signUp.setBackground(endDrawable);
                    holder.tv_actManage_signUp.setText("报名已结束");
                } else {
                    // 活动正常报名的情况
                    Drawable onDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.res_radius_sign_on);
                    holder.tv_actManage_signUp.setBackground(onDrawable);
                    String signNum = "报名中 " + regNumList.get(position) + "/" + bean.getMaxNum() + "人";
                    holder.tv_actManage_signUp.setText(signNum);
                }
            }

            Glide.with(requireContext())
                    .load(bean.getActCover())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_actManage_cover);

            holder.rootView_item_actManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireContext(), actDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("actID", bean.getActID());
                    bundle.putBoolean("ifManage", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return actList.size();
        }
    }

}