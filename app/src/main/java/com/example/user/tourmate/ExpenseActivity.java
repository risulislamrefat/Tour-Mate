package com.example.user.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class ExpenseActivity extends AppCompatActivity {
    private DatabaseReference mainDB;
    private FirebaseAuth mAuth;
    private String userId,estimatedBudget;
    private TextView id;
    private BottomSheetBehavior sheetBehavior;
    private Button addExpenseBtn;
    private RecyclerView recyclerView;
    private EditText expenseDetails,expenseAmount;
    public String eventIdT;
    private List<ExpenseClass> expenseClassList;
    private ExpenseListAdapter expenseListAdapter;
    int amount=0;
    public int amountCheckInteger,estimatedBudgetInteger,expenseAmountInteger;
    Boolean check= false;
    String expenseDetaisText,expenseAmountText;
    private Boolean isFirst = false;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        final Intent intent = getIntent();
        eventIdT = intent.getStringExtra("eventId");
        estimatedBudget = intent.getStringExtra("budget");
        estimatedBudgetInteger = Integer.parseInt(estimatedBudget);

        Tab1ExpenseFragment fragmentExpense = new Tab1ExpenseFragment(eventIdT,estimatedBudgetInteger);
        Tab2MemorableFragment fragmentMemorable = new Tab2MemorableFragment(eventIdT);
        adapter.addFragment(fragmentExpense,"Expenses");
        adapter.addFragment(fragmentMemorable,"Memorable Places");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
