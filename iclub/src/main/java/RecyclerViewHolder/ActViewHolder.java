package RecyclerViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icluub.R;

public class ActViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout rootView_item_activity;
    public TextView tv_activity_actName;
    public ImageView iv_activity_cover;
    public TextView tv_activity_clubName;
    public TextView tv_activity_timeStart;
    public TextView tv_activity_timeEnd;
    public TextView tv_activity_place;
    public TextView tv_activity_signUp;

    public ActViewHolder(@NonNull View itemView) {
        super(itemView);
        rootView_item_activity = itemView.findViewById(R.id.rootView_item_activity);
        tv_activity_actName = itemView.findViewById(R.id.tv_activity_actName);
        iv_activity_cover = itemView.findViewById(R.id.iv_activity_cover);
        tv_activity_clubName = itemView.findViewById(R.id.tv_activity_clubName);
        tv_activity_timeStart = itemView.findViewById(R.id.tv_activity_timeStart);
        tv_activity_timeEnd = itemView.findViewById(R.id.tv_activity_timeEnd);
        tv_activity_place = itemView.findViewById(R.id.tv_activity_place);
        tv_activity_signUp = itemView.findViewById(R.id.tv_activity_signUp);
    }
}
