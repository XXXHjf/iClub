package homefragments;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.icluub.R;
import com.example.icluub.personalDataActivity;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Fragment_mine extends Fragment implements View.OnClickListener{

    private View view_mine_modifyData;
    private ImageView iv_mine_profile;

    public static final int CHOOSE_PHOTO = 1;   //标识符----打开相册

    private final ActivityResultLauncher pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
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
        view.findViewById(R.id.iv_mine_profile).setOnClickListener(this);


//        获取并加载头像
        ImageView iv_mine_profile = view.findViewById(R.id.iv_mine_profile);
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
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        }
    }
}