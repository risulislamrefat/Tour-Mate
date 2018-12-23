package com.example.user.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {

    private Context context;
    private List<ExpenseClass> expenseList;
    private LinearLayoutManager linearLayoutManager;

    public ExpenseListAdapter(Context context, List <ExpenseClass> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View expenseItemView = layoutInflater.inflate(R.layout.expense_list_item,null);

        return new ExpenseViewHolder(expenseItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder expenseViewHolder, int i) {
        //Collections.reverse(expenseList);
        ExpenseClass currenExpense = expenseList.get(i);
        expenseViewHolder.expenseDetails.setText(currenExpense.getExpenseDetails());
        expenseViewHolder.expenseAmount.setText(currenExpense.getExpenseAmount());


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView expenseDetails,expenseAmount;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseDetails = itemView.findViewById(R.id.expenseDetailsTv);
            expenseAmount = itemView.findViewById(R.id.expenseAmountTv);
        }
    }
}
