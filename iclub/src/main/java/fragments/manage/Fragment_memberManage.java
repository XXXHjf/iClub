package fragments.manage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanUser;
import RecyclerViewHolder.MemberViewHolder;
import util.DBUtil;
import util.SPDataUtils;

public class Fragment_memberManage extends Fragment {
    private RecyclerView recv_memberList;
    private MemberListAdapter adapter;
    private List<BeanUser> userList;
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

        BeanUser beanUser = SPDataUtils.getUserInfo(requireContext());
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
        new Thread_getMember().start();
    }

    @Override
    public void onStop() {
        super.onStop();
        userList = new ArrayList<>();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        View view = requireView();
        userList = new ArrayList<>();
        recv_memberList = view.findViewById(R.id.recv_memberList);
        adapter = new MemberListAdapter();
        recv_memberList.setLayoutManager(new LinearLayoutManager(requireContext()));
        recv_memberList.setAdapter(adapter);
    }

    /**
     * 自定义线程：查询成员
     */
    private class Thread_getMember extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM MemberDetailView WHERE clubID=? AND ifPassed=1";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, clubID);
                java.sql.ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    BeanUser bean = BeanUser.resultSetToUser(rs);
                    if (bean.getUserID().equals(presidentID))
                        userList.add(0, bean);
                    else
                        userList.add(bean);
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
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
            final BeanUser bean = userList.get(position);

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
            return userList.size();
        }
    }
}