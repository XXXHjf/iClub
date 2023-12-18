package RecyclerViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icluub.R;

/**
 * 封装社团列表的布局ViewHolder
 */
public class ClubViewHolder extends RecyclerView.ViewHolder{
    public ImageView iv_icon;
    public TextView tv_clubName;
    public TextView tv_clubInfo;
    public LinearLayout root_item;
    public ClubViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_icon = itemView.findViewById(R.id.iv_clubList_icon);
        tv_clubName = itemView.findViewById(R.id.tv_clubList_clubName);
        tv_clubInfo = itemView.findViewById(R.id.tv_clubList_clubInfo);
        root_item = itemView.findViewById(R.id.rootView_itemclubList);
    }
}
