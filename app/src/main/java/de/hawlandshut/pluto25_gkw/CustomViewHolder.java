package de.hawlandshut.pluto25_gkw;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public final TextView mLine1;
    public final TextView mLine2;

    public CustomViewHolder(@NonNull View view) {
        super(view);
        mLine1 = (TextView) view.findViewById( R.id.post_view_line1);
        mLine2 = (TextView) view.findViewById( R.id.post_view_line2);
    }
}
