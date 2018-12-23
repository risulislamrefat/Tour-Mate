package com.example.user.tourmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class EventsActivity extends AppCompatActivity {
    private EditText fromDate,toDate,travelDestination,estimatedBudget;
    private String destination, startDate,endDate;
    private String budget;
    private DatabaseReference mainDB;
    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener onDateSetListener,onDateSetListener1;
    private Button create,show;
    private String userId;
    private List<EventClass> eventList;
    private EventListAdapter eventListAdapter;
    private String eventId;
    private BottomSheetBehavior sheetBehavior;
    private Button createBtn;
    private List<EventClass> eventClassList;
    private RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        travelDestination = findViewById(R.id.travelDestinationEt);
        estimatedBudget = findViewById(R.id.budgetEt);
        create = findViewById(R.id.createBtn);
        recyclerView = findViewById(R.id.showEventRecycleView);

        setUpReferences();
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetBehavior.setHideable(false);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        sheetBehavior.setHideable(false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mainDB = FirebaseDatabase.getInstance().getReference();





        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventClassList=new ArrayList<>();

        eventListAdapter = new EventListAdapter(this,eventClassList);
        recyclerView.setAdapter(eventListAdapter);


        final EventListAdapter finalEventListAdapter = eventListAdapter;



        mainDB.child("Users").child(userId).child("Event").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    EventClass eventClass = child.getValue(EventClass.class);

                    eventClassList.add(eventClass);
                    finalEventListAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventsActivity.this,
                        R.style.AddedTheme,
                        onDateSetListener, year, month, dayOfMonth);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                Log.d("ShowDate", "onDateSet: dd/mm/yy: "+ dayOfMonth + "." + month + "." + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                fromDate.setText(date);
            }
        };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventsActivity.this,
                        R.style.AddedTheme,
                        onDateSetListener1, year, month, dayOfMonth);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                datePickerDialog.show();
            }
        });

        onDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                Log.d("ShowDate", "onDateSet: dd/mm/yy: "+ dayOfMonth + "." + month + "." + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                toDate.setText(date);
            }
        };


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = travelDestination.getText().toString();
                startDate = fromDate.getText().toString();
                endDate = toDate.getText().toString();
                budget = estimatedBudget.getText().toString();


                DatabaseReference newEvenRef = mainDB.child("Users").child(userId).child("Event");

                String eventId = newEvenRef.push().getKey();

                EventClass eventClass = new EventClass(destination,startDate,endDate,budget,eventId);
                newEvenRef.child(eventId).setValue(eventClass);
                Intent intent = new Intent(EventsActivity.this,EventsActivity.class);
                startActivity(intent);
            }
        });




    }
    private void setUpReferences() {
        LinearLayout layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
    }



}





