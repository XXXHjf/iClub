package RecyclerViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icluub.R;

public class ActManageViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout rootView_item_actManage;
    public TextView tv_actManage_actName;
    public ImageView iv_actManage_cover;
    public TextView tv_actManage_timeStart;
    public TextView tv_actManage_timeEnd;
    public TextView tv_actManage_signUp;

    public ActManageViewHolder(@NonNull View itemView) {
        super(itemView);
        rootView_item_actManage = itemView.findViewById(R.id.rootView_item_actManage);
        tv_actManage_actName = itemView.findViewById(R.id.tv_actManage_actName);
        iv_actManage_cover = itemView.findViewById(R.id.iv_actManage_cover);
        tv_actManage_timeStart = itemView.findViewById(R.id.tv_actManage_timeStart);
        tv_actManage_timeEnd = itemView.findViewById(R.id.tv_actManage_timeEnd);
        tv_actManage_signUp = itemView.findViewById(R.id.tv_actManage_signUp);
    }
}
