package com.example.user.tourmate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private Context context;
    private List<EventClass> eventList;
    private LinearLayoutManager linearLayoutManager;

    public EventListAdapter(Context context, List<EventClass> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View eventItemView = layoutInflater.inflate(R.layout.event_list_item,null);

        return new EventViewHolder(eventItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
            RecyclerView recyclerView;
        Collections.reverse(eventList);
            final EventClass currentEvent = eventList.get(i);
            eventViewHolder.travelDestination.setText(currentEvent.getTravelDestination());
            eventViewHolder.startDate.setText(currentEvent.getFromDate());
            eventViewHolder.endDate.setText(currentEvent.getToDate());
            eventViewHolder.estimatedBudget.setText(currentEvent.getEstimatedBudget());

            eventViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ExpenseActivity.class);
                    String getId = currentEvent.getEventId();
                    String getBudget = currentEvent.getEstimatedBudget();
                    intent.putExtra("eventId",getId);
                    intent.putExtra("budget",getBudget);
                    context.startActivity(intent);

                }
            });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        private TextView travelDestination,startDate,endDate,estimatedBudget;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            travelDestination = itemView.findViewById(R.id.travelDestinationTv);
            startDate = itemView.findViewById(R.id.startDateTv);
            endDate = itemView.findViewById(R.id.endDateTv);
            estimatedBudget = itemView.findViewById(R.id.estimatedBudgetTv);
        }
    }
}
