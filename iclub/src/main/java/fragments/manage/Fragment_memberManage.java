package fragments.manage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;
import com.example.icluub.memberApplyActivity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanUser;
import RecyclerViewHolder.MemberViewHolder;
import util.DBUtil;
import SPTools.userSP;

public class Fragment_memberManage extends Fragment implements View.OnClickListener {
    private RecyclerView recv_memberList;
    private MemberListAdapter adapter;
    private List<BeanUser> passedUserList;
    private List<BeanUser> unPassedUserList;
    private List<String> weChatList;
    private List<String> reasonList;
    private View view_approveBack;
    private TextView tv_clubMember_unapproved;
    private int clubID = -1;
    private String presidentID = null;

    public Fragment_memberManage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_manage, container, false);
        Bundle bundle = getArguments();
        clubID = bundle.getInt("clubID");

        BeanUser beanUser = userSP.getUserInfo(requireContext());
        presidentID = beanUser.getUserID();

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
        new Thread_getAllMember().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        view_approveBack.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        passedUserList = new ArrayList<>();
        unPassedUserList = new ArrayList<>();
        weChatList = new ArrayList<>();
        reasonList = new ArrayList<>();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        View view = requireView();
        passedUserList = new ArrayList<>();
        unPassedUserList = new ArrayList<>();
        weChatList = new ArrayList<>();
        reasonList = new ArrayList<>();
        recv_memberList = view.findViewById(R.id.recv_memberList);
        adapter = new MemberListAdapter();
        recv_memberList.setLayoutManager(new LinearLayoutManager(requireContext()));
        recv_memberList.setAdapter(adapter);
        tv_clubMember_unapproved = view.findViewById(R.id.tv_clubMember_unapproved);
        view_approveBack = view.findViewById(R.id.view_approveBack);
        view_approveBack.setClickable(false);  // 先设为不可点击，如果有未审批的再设为可以点击
    }

    /**
     * 重写onClick方法
     * @param view  视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_approveBack) {
            Intent intent = new Intent(requireContext(), memberApplyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("userList", (Serializable) unPassedUserList);
            bundle.putStringArrayList("weChatList", new ArrayList<>(weChatList));
            bundle.putStringArrayList("reasonList", new ArrayList<>(reasonList));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 自定义线程：查询所有成员，并分类为社团成员、为审批的入社申请学生
     */
    private class Thread_getAllMember extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM MemberDetailView WHERE clubID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, clubID);
                java.sql.ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    BeanUser bean = BeanUser.resultSetToUser(rs);

                    // 获取社员列表
                    if ( rs.getInt("ifPassed") == 1 ) {
                        // 如果查询到的成员是本人(社长),移到列表第一位显示
                        if (bean.getUserID().equals(presidentID))
                            passedUserList.add(0, bean);
                        else
                            passedUserList.add(bean);
                    }
                    // 获取未审批的人员列表
                    else {
                        unPassedUserList.add(bean);
                        weChatList.add(rs.getString(17));
                        reasonList.add(rs.getString(18));
                    }
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通知社员列表的适配器已修改
                        adapter.notifyDataSetChanged();

                        // 入社申请部分的UI处理
                        int num = unPassedUserList.size();
                        tv_clubMember_unapproved.setText(String.valueOf(num));
                        Drawable drawable;
                        if ( num > 0 ) {
                            drawable = ContextCompat.getDrawable(requireContext(), R.drawable.res_radius_sign_on);
                            view_approveBack.setClickable(true);
                        } else {
                            drawable = ContextCompat.getDrawable(requireContext(), R.drawable.res_radius_sign_end);
                            view_approveBack.setClickable(false);
                        }
                        view_approveBack.setBackground(drawable);

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
     * 自定义适配器：社员列表
     */
    private class MemberListAdapter extends RecyclerView.Adapter<MemberViewHolder> {
        @NonNull
        @Override
        public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_club_member, parent, false);
            MemberViewHolder memberViewHolder = new MemberViewHolder(view);
            return memberViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
            final BeanUser bean = passedUserList.get(position);

            if (bean.getUserID().equals(presidentID)) {
                holder.line_top.setVisibility(View.VISIBLE);
                holder.tv_clubMember_me.setVisibility(View.VISIBLE);
                holder.tv_clubMember_roleName.setText("社长");
            }
            holder.tv_clubMember_name.setText(bean.getUserName());
            Glide.with(requireContext())
                    .load(bean.getProfileURL())
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_clubMember_profile);
        }

        @Override
        public int getItemCount() {
            return passedUserList.size();
        }
    }

}