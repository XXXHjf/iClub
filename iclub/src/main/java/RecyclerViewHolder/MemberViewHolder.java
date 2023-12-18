package RecyclerViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icluub.R;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout rootView_item_clubMember;
    public ImageView iv_clubMember_profile;
    public TextView tv_clubMember_name;
    public TextView tv_clubMember_roleName;
    public TextView tv_clubMember_me;
    public View line_top;
    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        rootView_item_clubMember = itemView.findViewById(R.id.rootView_item_clubMember);
        iv_clubMember_profile = itemView.findViewById(R.id.iv_clubMember_profile);
        tv_clubMember_name = itemView.findViewById(R.id.tv_clubMember_name);
        tv_clubMember_roleName = itemView.findViewById(R.id.tv_clubMember_roleName);
        tv_clubMember_me = itemView.findViewById(R.id.tv_clubMember_me);
        line_top = itemView.findViewById(R.id.line_top);
    }
}
