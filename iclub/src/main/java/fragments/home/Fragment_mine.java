package fragments.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;
import com.example.icluub.actMineActivity;
import com.example.icluub.personalDataActivity;
import com.example.icluub.systemSettingActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Beans.BeanUser;
import SPTools.appStatusSP;
import SPTools.userSP;
import tools.CameraUtils;
import tools.OperationPromptTool;
import util.DBUtil;
import util.OSSUpload;

public class Fragment_mine extends Fragment implements View.OnClickListener {

    public static final int TAKE_PHOTO = 1;  // 启动相机标识
    public static final int SELECT_PHOTO = 2;  // 启动相册标识
    private ImageView iv_mine_profile;
    private TextView tv_mine_name;
    private TextView tv_mine_stuID;
    private BottomSheetDialog bottomSheetDialog;  // 底部弹窗
    private View bottomView;  // 弹窗视图
    private File outputImagePath;  // 存储拍完照后的图片
    private BeanUser beanUser = null;
    private String imagePath;

    public Fragment_mine() {
        // 必须保留一个空的构造方法
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        initView(view);

        // 加载用户的姓名、学号
        beanUser = userSP.getUserInfo(requireContext());
        tv_mine_name.setText(beanUser.getUserName());
        tv_mine_stuID.setText(beanUser.getUserID());
        Glide.with(this)
                .load(beanUser.getProfileURL())
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_mine_profile);

        // 返回这个fragment的布局
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = requireView();
        view.findViewById(R.id.view_mine_myActivities).setOnClickListener(this);
        view.findViewById(R.id.view_mine_modifyData).setOnClickListener(this);
        view.findViewById(R.id.view_mine_systemSettings).setOnClickListener(this);
        iv_mine_profile.setOnClickListener(this);
    }

    /**
     * 初始化绑定组件
     *
     * @param view 此fragment的视图
     */
    private void initView(View view) {
        iv_mine_profile = view.findViewById(R.id.iv_mine_profile);
        tv_mine_name = view.findViewById(R.id.tv_mine_name);
        tv_mine_stuID = view.findViewById(R.id.tv_mine_stuID);
    }

    /**
     * 重写onClick方法
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_mine_modifyData) {
            Intent intent = new Intent(getActivity(), personalDataActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.view_mine_myActivities) {
            Intent intent = new Intent(getActivity(), actMineActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.view_mine_systemSettings) {
            Intent intent = new Intent(getActivity(), systemSettingActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.iv_mine_profile) {
            changeAvatar(view);
        }
    }

    // 点击头像后弹出选择框的方法
    public void changeAvatar(View view) {
        // 初始化底部选择相册/相机弹窗的部件
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom_change_avatar, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

        // 拍照
        tvTakePictures.setOnClickListener(v -> {
            takePhoto();
            bottomSheetDialog.cancel();
        });
        // 打开相册
        tvOpenAlbum.setOnClickListener(v -> {
            openAlbum();
            bottomSheetDialog.cancel();
        });
        // 取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });

        bottomSheetDialog.show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if ( !appStatusSP.getHasCameraPermissions(requireContext()) || getActivity() == null) {
            OperationPromptTool.showMsg(getContext(),"未获取到相机权限");
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getActivity().getExternalCacheDir(), filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(getContext(), outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        if ( !appStatusSP.getHasCameraPermissions(requireContext()) || getActivity() == null ) {
            OperationPromptTool.showMsg(getContext(),"未获取到相册权限");
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO) {
            if (data == null )
                return;
            // 拍照显示图片
            displayImage(outputImagePath.getAbsolutePath());
        } else if (requestCode == SELECT_PHOTO) {
            if (data == null )
                return;
            imagePath = null;
            imagePath = CameraUtils.getImageOnKitKatPath(data, getContext());
            // 相册显示图片
            displayImage(imagePath);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 用当前的时间戳作为上传的照片的名字上传到OSS里，数据库对应位置存储图片的下载路径
                    String fileName = System.currentTimeMillis() + "." + CameraUtils.getImageType(imagePath);
                    OSSUpload.uploadOSS(getContext(), "userAvatar/" + fileName, imagePath);

                    // 修改登陆用户的SP文件
                    userSP.updateAvatar(requireContext(), OSSUpload.getUserAvatarPrefix() + fileName);
                    Connection conn = null;
                    BeanUser beanUser = userSP.getUserInfo(requireContext());
                    try {
                        conn = DBUtil.getConnection();
                        String sql = "update user set profileURL = ? where userID = ?";
                        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setString(1, OSSUpload.getUserAvatarPrefix() + fileName);
                        pst.setString(2, beanUser.getUserID());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DBUtil.close(conn);
                    }
                }
            }).start();
        }
    }

    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if ( !TextUtils.isEmpty(imagePath) ) {
            // 显示图片
            Glide.with(this)
                    .load(imagePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_mine_profile);
        } else {
            OperationPromptTool.showMsg(getContext(), "图片获取失败");
        }
    }


}