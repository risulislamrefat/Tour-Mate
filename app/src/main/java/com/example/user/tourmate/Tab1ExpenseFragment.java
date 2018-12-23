package com.example.user.tourmate;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Tab1ExpenseFragment extends Fragment {
    private DatabaseReference mainDB;
    private FirebaseAuth mAuth;
    private String userId;
    private TextView id;
    private BottomSheetBehavior sheetBehavior;
    private Button addExpenseBtn;
    private RecyclerView recyclerView;
    private EditText expenseDetails,expenseAmount;

    private List<ExpenseClass> expenseClassList;
    private ExpenseListAdapter expenseListAdapter;
    int amount=0;
    int amountCheckInteger,expenseAmountInteger;
    Boolean check= false;
    String expenseDetaisText,expenseAmountText;
    private Boolean isFirst = false;
    private String eventId;
    private int estimatedBudget;

    @SuppressLint("ValidFragment")
    public Tab1ExpenseFragment(String eventId,int estimatedBudget) {
        this.eventId = eventId;
        this.estimatedBudget = estimatedBudget;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1_expense, container, false);

        recyclerView = view.findViewById(R.id.showExpenseRecycleView);
        expenseDetails = view.findViewById(R.id.expenseDetails);
        expenseAmount = view.findViewById(R.id.expenseAmount);
        addExpenseBtn = view.findViewById(R.id.addExpenseBtn);
        expenseClassList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseListAdapter = new ExpenseListAdapter(getActivity(), expenseClassList);
        recyclerView.setAdapter(expenseListAdapter);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mainDB = FirebaseDatabase.getInstance().getReference();


        getExpenseFromDB();

        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseDetaisText = expenseDetails.getText().toString();
                expenseAmountText = expenseAmount.getText().toString();

                if (amountCheckInteger>0) {

                    int totalExpenseWithCurrent = amountCheckInteger + Integer.valueOf(expenseAmountText);

                    if (totalExpenseWithCurrent<estimatedBudget){
                        DatabaseReference databaseReference =mainDB.child("Users").child(userId).child("Event").child(eventId).child("Expense").push();
                        ExpenseClass expenseClass = new ExpenseClass(expenseDetaisText,expenseAmountText);
                        databaseReference.setValue(expenseClass);

                    }else {

                        Toast.makeText(getContext(), "Budget Exceded!!", Toast.LENGTH_SHORT).show();
                    }

                }else if (isFirst){

                    int totalExpenseWithCurrent = amountCheckInteger + Integer.valueOf(expenseAmountText);

                    if (totalExpenseWithCurrent<estimatedBudget){
                        DatabaseReference databaseReference =mainDB.child("Users").child(userId).child("Event").child(eventId).child("Expense").push();
                        ExpenseClass expenseClass = new ExpenseClass(expenseDetaisText,expenseAmountText);
                        databaseReference.setValue(expenseClass);

                    }else {

                        Toast.makeText(getContext(), "Budget Exceded!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(getContext(), "Slow network connection! Try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


    private void getExpenseFromDB() {
        amount =0;
        amountCheckInteger = 0;

        mainDB.child("Users").child(userId).child("Event").child(eventId).child("Expense").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                expenseClassList.clear();
                if (dataSnapshot.exists()){
                    for(DataSnapshot child: dataSnapshot.getChildren()) {

                        ExpenseClass expenseClass = child.getValue(ExpenseClass.class);
                        amount = amount+ Integer.parseInt(expenseClass.getExpenseAmount());

                        expenseClassList.add(expenseClass);
                        expenseListAdapter.notifyDataSetChanged();
                    }
                    amountCheckInteger = amount;
                }else {
                    isFirst = true;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
