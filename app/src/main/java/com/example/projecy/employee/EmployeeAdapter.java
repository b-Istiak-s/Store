package com.example.projecy.employee;

import static com.example.projecy.model.Constants.firstName;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.SqliteEmployee;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>{

    Context context;

    ArrayList<HashMap<String, String>> list;

    //constructor
    public EmployeeAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_data, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(view); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.MyViewHolder holder, int position) {
        holder.txtJobStatus.setText(list.get(position).get(Constants.jobStatus));
        holder.txtName.setText(list.get(position).get(firstName)+" "+list.get(position).get(Constants.lastName));
        holder.txtContactNumber.setText(list.get(position).get(Constants.contactNumber));
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, EmployeeDetailsActivity.class);
            intent.putExtra(firstName,list.get(position).get(firstName));
            intent.putExtra(Constants.lastName,list.get(position).get(Constants.lastName));
            intent.putExtra(Constants.address,list.get(position).get(Constants.address));
            intent.putExtra(Constants.contactNumber,list.get(position).get(Constants.contactNumber));
            intent.putExtra(Constants.jobStatus,list.get(position).get(Constants.jobStatus));
            intent.putExtra(Constants.monthlyIncome,list.get(position).get(Constants.monthlyIncome));
            intent.putExtra(Constants.id,list.get(position).get(Constants.id));
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.want_to_call_or_delete))
                    .setCancelable(true)
                    .setPositiveButton(context.getString(R.string.call), (dialog, id) -> {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+list.get(position).get(Constants.contactNumber)));
                        context.startActivity(intent);
                    })
                    .setNegativeButton(context.getString(R.string.delete), (dialog, id) -> {
                        SqliteEmployee sqliteEmployee = new SqliteEmployee(context);
                        sqliteEmployee.deleteData(list.get(position).get(Constants.id));
                        Toast.makeText(context, "Successfully deleted the "+list.get(position).get(firstName)+"'s data.", Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtContactNumber,txtJobStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtEmployeeName);
            txtContactNumber = itemView.findViewById(R.id.txtEmployeeContactNumber);
            txtJobStatus = itemView.findViewById(R.id.txtEmployeeJobStatus);
        }
    }
}
