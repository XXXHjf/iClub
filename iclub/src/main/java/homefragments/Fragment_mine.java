package homefragments;

import static android.app.Activity.RESULT_OK;
import static com.example.icluub.Home.hasPermissions;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.Home;
import com.example.icluub.R;
import com.example.icluub.personalDataActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tools.BitmapUtils;
import tools.CameraUtils;

public class Fragment_mine extends Fragment implements View.OnClickListener{

    private View view_mine_modifyData;
    private ImageView iv_mine_profile;
    private BottomSheetDialog bottomSheetDialog;  // 底部弹窗
    private View bottomView;  // 弹窗视图
    private File outputImagePath;  // 存储拍完照后的图片
    public static final int TAKE_PHOTO = 1;  // 启动相机标识
    public static final int SELECT_PHOTO = 2;  // 启动相册标识
    private String base64Pic;  // Base64
    private Bitmap orc_bitmap;  // 拍照和相册获取图片的Bitmap
    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)  //不做磁盘缓存
            .skipMemoryCache(true);  //不做内存缓存





    public static final int CHOOSE_PHOTO = 1;   //标识符----打开相册

    private final ActivityResultLauncher pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
            }
        }
    });

    public Fragment_mine() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);

//        view_mine_modifyData = view.findViewById(R.id.view_mine_modifyData);

        view.findViewById(R.id.view_mine_modifyData).setOnClickListener(this);

        iv_mine_profile = view.findViewById(R.id.iv_mine_profile);
        iv_mine_profile.setOnClickListener(this);


        // 获取并加载头像
        Glide.with(this)
                .load(R.drawable.ludiwusi)
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_mine_profile);

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_mine_modifyData) {
            Intent intent = new Intent(getActivity(), personalDataActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.iv_mine_profile) {
            changeAvatar(view);
        }
    }

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
            showMsg("拍照");
            bottomSheetDialog.cancel();
        });
        // 打开相册
        tvOpenAlbum.setOnClickListener(v -> {
            openAlbum();
            showMsg("打开相册");
            bottomSheetDialog.cancel();
        });
        // 取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });

        bottomSheetDialog.show();
    }

    // Toast提示
    private void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (!hasPermissions || getActivity() == null) {
            showMsg("未获取到权限");
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
        if (!hasPermissions) {
            showMsg("未获取到权限");
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    /**
     * 返回到Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO) {
            // 拍照显示图片
            displayImage(outputImagePath.getAbsolutePath());
        } else if (resultCode == SELECT_PHOTO) {
            String imagePath = null;
            imagePath = CameraUtils.getImageOnKitKatPath(data, getContext());
            // 相册显示图片
            displayImage(imagePath);
        }
    }

    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            // 显示图片
            Glide.with(this)
                    .load(imagePath)
                    .apply(requestOptions)
                    .into(iv_mine_profile);
            // 压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            // 转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            showMsg("图片获取失败");
        }
    }





}