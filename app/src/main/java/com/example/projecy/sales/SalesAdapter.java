package com.example.projecy.sales;

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

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> {


    Context context;

    ArrayList<HashMap<String, String>> list;


    //constructor
    public SalesAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_data, parent, false);
        // set the view's size, margins, paddings and layout parameters
        SalesAdapter.MyViewHolder vh = new SalesAdapter.MyViewHolder(view); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.MyViewHolder holder, int position) {
        holder.txtProductName.setText(list.get(position).get(Constants.productName));
        holder.txtProductVersion.setText("Version : "+list.get(position).get(Constants.productVersion));
        holder.txtPurchasePrice.setText("Purchase Price : "+list.get(position).get(Constants.productPrice));
        holder.txtQuantity.setText("Quantity Sold : "+list.get(position).get(Constants.quantityOfProduct));
        holder.txtSalePrice.setText("Sale Price : "+list.get(position).get(Constants.salePrice));
        holder.txtTime.setText("Time : "+list.get(position).get(Constants.productSaleDay)+"/"+list.get(position).get(Constants.productSaleMonth)+"/"+list.get(position).get(Constants.productSaleYear)+" "+list.get(position).get(Constants.productSaleTime));
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, SalesDetailsActivity.class);
            intent.putExtra(Constants.id,list.get(position).get(Constants.id));
            intent.putExtra(Constants.productName,list.get(position).get(Constants.productName));
            intent.putExtra(Constants.productVersion,list.get(position).get(Constants.productVersion));
            intent.putExtra(Constants.productPrice,list.get(position).get(Constants.productPrice));
            intent.putExtra(Constants.salePrice,list.get(position).get(Constants.salePrice));
            intent.putExtra(Constants.extraCost,list.get(position).get(Constants.extraCost));
            intent.putExtra(Constants.productSaleDay,list.get(position).get(Constants.productSaleDay));
            intent.putExtra(Constants.productSaleMonth,list.get(position).get(Constants.productSaleMonth));
            intent.putExtra(Constants.productSaleYear,list.get(position).get(Constants.productSaleYear));
            intent.putExtra(Constants.productSaleTime,list.get(position).get(Constants.productSaleTime));
            intent.putExtra(Constants.quantityOfProduct,list.get(position).get(Constants.quantityOfProduct));
            intent.putExtra(Constants.profit,list.get(position).get(Constants.profit));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductVersion, txtPurchasePrice, txtQuantity, txtSalePrice,txtTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductNameSale);
            txtProductVersion = itemView.findViewById(R.id.txtProductVersionSale);
            txtPurchasePrice = itemView.findViewById(R.id.txtProductPurchasePriceSale);
            txtQuantity = itemView.findViewById(R.id.txtQuantityOfProductSold);
            txtSalePrice = itemView.findViewById(R.id.txtProductSalePrice);
            txtTime = itemView.findViewById(R.id.txtDateSale);
        }
    }
}
