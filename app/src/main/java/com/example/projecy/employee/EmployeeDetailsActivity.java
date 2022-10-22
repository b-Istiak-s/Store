package com.example.projecy.employee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projecy.R;
import com.example.projecy.model.Constants;

public class EmployeeDetailsActivity extends AppCompatActivity {

    TextView txtFirstName,txtLastName, txtAddress,txtContactNumber,txtJobStatus,txtIncome;
    Button btnUpdate, btnCallNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        txtFirstName = findViewById(R.id.txtEmployeeFirstName);
        txtLastName = findViewById(R.id.txtEmployeeLastName);
        txtAddress = findViewById(R.id.txtEmployeeAddress);
        txtContactNumber = findViewById(R.id.txtEmployeeContactNumberDetails);
        txtJobStatus = findViewById(R.id.txtEmployeeJobStatusDetails);
        txtIncome = findViewById(R.id.txtEmployeeMonthlyIncome);
        btnUpdate = findViewById(R.id.btnEditEmployee);
        btnCallNow = findViewById(R.id.btnCallNow);



        Bundle extras = getIntent().getExtras();

        String id = extras.getString(Constants.id, "");
        String firstName = extras.getString(Constants.firstName, "");
        String lastName = extras.getString(Constants.lastName, "");
        String contactNumber = extras.getString(Constants.contactNumber, "");
        String address = extras.getString(Constants.address, "");
        String jobStatus = extras.getString(Constants.jobStatus, "");
        String monthlyIncome = extras.getString(Constants.monthlyIncome, "");

        getSupportActionBar().setTitle(firstName+" "+lastName+"'s details");

        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtAddress.setText(address);
        txtContactNumber.setText(contactNumber);
        txtJobStatus.setText(jobStatus);
        txtIncome.setText(monthlyIncome);

        btnUpdate.setOnClickListener(v->{
            Intent intent = new Intent(this, AddEmployeeActivity.class);
            intent.putExtra(Constants.id,id);
            intent.putExtra(Constants.firstName,firstName);
            intent.putExtra(Constants.lastName,lastName);
            intent.putExtra(Constants.contactNumber,contactNumber);
            intent.putExtra(Constants.address,address);
            intent.putExtra(Constants.jobStatus,jobStatus);
            intent.putExtra(Constants.monthlyIncome,monthlyIncome);
            startActivity(intent);
            finish();
        });


    }
}