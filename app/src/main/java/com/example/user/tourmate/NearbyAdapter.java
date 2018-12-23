package com.example.user.tourmate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.tourmate.Nearby.Result;

import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    List<Result> results;

    public NearbyAdapter(List<Result> results) {
        this.results= results;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_nearby,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nameTV.setText(results.get(i).getName());
        viewHolder.rateTV.setText(String.valueOf(results.get(i).getRating()));
        viewHolder.addressTV.setText(results.get(i).getVicinity());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTV,rateTV,addressTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTVID);
            rateTV = itemView.findViewById(R.id.RateTVID);
            addressTV = itemView.findViewById(R.id.AddressTVID);

        }
    }

    public void NewList(List<Result> results){
        this.results = results;
        notifyDataSetChanged();
    }
}
