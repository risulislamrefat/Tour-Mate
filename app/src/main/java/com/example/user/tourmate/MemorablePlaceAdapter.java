package com.example.user.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MemorablePlaceAdapter extends RecyclerView.Adapter<MemorablePlaceAdapter.ViewHolder> {

    private Context context;
    private List<MemorablePlaceClass> memorablePlaceClassList;

    public MemorablePlaceAdapter(Context context, List<MemorablePlaceClass> memorablePlaceClassList) {
        this.context = context;
        this.memorablePlaceClassList = memorablePlaceClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View memorableItemView = layoutInflater.inflate(R.layout.memorableplaces_model,null);

        return new ViewHolder(memorableItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        MemorablePlaceClass currentItem = memorablePlaceClassList.get(i);
        Picasso.get().load(currentItem.getMemorableImage()).into(viewHolder.memorablePic);
        viewHolder.captionPic.setText(currentItem.getCaptions());

    }

    @Override
    public int getItemCount() {
        return memorablePlaceClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView memorablePic;
        private TextView captionPic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           memorablePic = itemView.findViewById(R.id.memorableImageId);
           captionPic = itemView.findViewById(R.id.captionTvId);

        }
    }
}
