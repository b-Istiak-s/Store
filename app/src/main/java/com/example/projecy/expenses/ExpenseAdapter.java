package com.example.projecy.expenses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecy.R;
import com.example.projecy.model.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {


    Context context;

    ArrayList<HashMap<String, String>> list;


    //constructor
    public ExpenseAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_data, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ExpenseAdapter.MyViewHolder vh = new ExpenseAdapter.MyViewHolder(view); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.MyViewHolder holder, int position) {
        holder.txtProductName.setText("Name : "+list.get(position).get(Constants.productName));
        holder.txtProductPrice.setText("Price : "+list.get(position).get(Constants.productPrice));
        holder.txtProductVersion.setText("Version : "+list.get(position).get(Constants.productVersion));
        holder.txtProductQuantity.setText("Quantity : "+list.get(position).get(Constants.quantityOfProduct));
        holder.txtDateTime.setText("Date & Time : "+list.get(position).get(Constants.productPurchaseDay)+"/"+list.get(position).get(Constants.productPurchaseMonth)+"/"+list.get(position).get(Constants.productPurchaseYear)+"   "+list.get(position).get(Constants.productPurchaseTime));

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ExpenseDetailsActivity.class);
            intent.putExtra(Constants.id,list.get(position).get(Constants.id));
            intent.putExtra(Constants.productName,list.get(position).get(Constants.productName));
            intent.putExtra(Constants.productPrice,list.get(position).get(Constants.productPrice));
            intent.putExtra(Constants.productVersion,list.get(position).get(Constants.productVersion));
            intent.putExtra(Constants.quantityOfProduct,list.get(position).get(Constants.quantityOfProduct));
            intent.putExtra(Constants.productPurchaseDay,list.get(position).get(Constants.productPurchaseDay));
            intent.putExtra(Constants.productPurchaseMonth,list.get(position).get(Constants.productPurchaseMonth));
            intent.putExtra(Constants.productPurchaseYear,list.get(position).get(Constants.productPurchaseYear));
            intent.putExtra(Constants.productPurchaseTime,list.get(position).get(Constants.productPurchaseTime));
            intent.putExtra(Constants.productSerial,list.get(position).get(Constants.productSerial));
            intent.putExtra(Constants.productType,list.get(position).get(Constants.productType));
            intent.putExtra(Constants.remainingProduct,list.get(position).get(Constants.remainingProduct));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductVersion, txtProductQuantity, txtDateTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductVersion = itemView.findViewById(R.id.txtProductVersion);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
        }
    }
}
