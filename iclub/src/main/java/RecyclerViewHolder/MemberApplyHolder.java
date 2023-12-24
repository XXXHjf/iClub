package RecyclerViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icluub.R;

public class MemberApplyHolder extends RecyclerView.ViewHolder {
    public ImageView iv_mbrApply_icon;
    public TextView tv_mbrApply_name;
    public TextView tv_mbrApply_college;
    public TextView tv_mbrApply_major;
    public TextView tv_mbrApply_phone;
    public TextView tv_mbrApply_weChat;
    public TextView tv_mbrApply_reason;
    public Button button_mbrApply_pass;
    public Button button_mbrApply_unPass;
    public MemberApplyHolder(@NonNull View itemView) {
        super(itemView);
        iv_mbrApply_icon = itemView.findViewById(R.id.iv_mbrApply_icon);
        tv_mbrApply_name = itemView.findViewById(R.id.tv_mbrApply_name);
        tv_mbrApply_college = itemView.findViewById(R.id.tv_mbrApply_college);
        tv_mbrApply_major = itemView.findViewById(R.id.tv_mbrApply_major);
        tv_mbrApply_phone = itemView.findViewById(R.id.tv_mbrApply_phone);
        tv_mbrApply_weChat = itemView.findViewById(R.id.tv_mbrApply_weChat);
        tv_mbrApply_reason = itemView.findViewById(R.id.tv_mbrApply_reason);
        button_mbrApply_pass = itemView.findViewById(R.id.button_mbrApply_pass);
        button_mbrApply_unPass = itemView.findViewById(R.id.button_mbrApply_unPass);
    }
}
