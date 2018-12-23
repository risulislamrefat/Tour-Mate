package com.example.user.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.user.tourmate.forecast.ForcastResponse;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {


    /*List<com.example.user.tourmate.Forcast.List> mainList;*/
    List<com.example.user.tourmate.forecast.List> mainList;

    public ForecastAdapter(List<com.example.user.tourmate.forecast.List> mainList,Context context) {
        this.mainList = mainList;
        this.context = context;
    }

    private Context context;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_forecast,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

       viewHolder.maxTemp.setText(mainList.get(i).getDtTxt()+" "+"Temp: "+mainList.get(i).getMain().getTempMax().toString()+"°C");
       viewHolder.minTemp.setText(mainList.get(i).getMain().getTempMin().toString()+"°C");

    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView maxTemp, minTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maxTemp = itemView.findViewById(R.id.maxTempTVID);
            minTemp = itemView.findViewById(R.id.minTempTVID);
        }
    }

}

