package com.example.projecy.employee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.SqliteEmployee;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText etxtFirstName,etxtLastName, etxtContactNumber,etxtAddress,etxtJobStatus,etxtMonthlyIncome;
    Button btnSubmit;
    SqliteEmployee employeeDB;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        etxtFirstName = findViewById(R.id.etxtFirstName);
        etxtLastName = findViewById(R.id.etxtLastName);
        etxtContactNumber = findViewById(R.id.etxtContactNumber);
        etxtAddress = findViewById(R.id.etxtAddress);
        etxtJobStatus = findViewById(R.id.etxtJobStatus);
        etxtMonthlyIncome = findViewById(R.id.etxtMonthlyIncome);
        btnSubmit = findViewById(R.id.btnSubmit);
        builder = new AlertDialog.Builder(this);
        getSupportActionBar().setTitle("Add Employee");

        employeeDB = new SqliteEmployee(AddEmployeeActivity.this);

        Bundle extras = getIntent().getExtras();
        String user_id = null;
        if (extras != null) {
            user_id = extras.getString(Constants.id, "");
            if (user_id!=null) {
                String efirstName = extras.getString(Constants.firstName, "");
                String elastName = extras.getString(Constants.lastName, "");
                String econtactNumber = extras.getString(Constants.contactNumber, "");
                String eaddress = extras.getString(Constants.address, "");
                String ejobStatus = extras.getString(Constants.jobStatus, "");
                String emonthlyIncome = extras.getString(Constants.monthlyIncome, "");

                etxtFirstName.setText(efirstName);
                etxtLastName.setText(elastName);
                etxtContactNumber.setText(econtactNumber);
                etxtAddress.setText(eaddress);
                etxtJobStatus.setText(ejobStatus);
                etxtMonthlyIncome.setText(emonthlyIncome);
                btnSubmit.setText("Update");
            }
        }

        String finalUser_id = user_id;
        btnSubmit.setOnClickListener(v->{

            String firstName = etxtFirstName.getText().toString().trim();
            String lastName = etxtLastName.getText().toString().trim();
            String contactNumber = etxtContactNumber.getText().toString().trim();
            String address = etxtAddress.getText().toString().trim();
            String jobStatus = etxtJobStatus.getText().toString().trim();
            String monthlyIncome = etxtMonthlyIncome.getText().toString().trim();
            if (firstName.isEmpty()){
                etxtFirstName.setError(getString(R.string.empty_first_name_error));
                etxtFirstName.requestFocus();
            }else if(lastName.isEmpty()){
                etxtLastName.setError(getString(R.string.empty_last_name_error));
                etxtLastName.requestFocus();
            }else if(contactNumber.isEmpty()){
                etxtContactNumber.setError(getString(R.string.empty_contact_number_error));
                etxtContactNumber.requestFocus();
            }else if (address.isEmpty()){
                etxtAddress.setError(getString(R.string.empty_address_error));
                etxtAddress.requestFocus();
            }else if (jobStatus.isEmpty()){
                etxtJobStatus.setError(getString(R.string.empty_job_status_error));
                etxtJobStatus.requestFocus();
            }else if(monthlyIncome.isEmpty()){
                etxtMonthlyIncome.setError(getString(R.string.empty_monthly_income_error));
                etxtMonthlyIncome.requestFocus();
            }else{
                if(extras==null) {
                    builder.setMessage(getString(R.string.want_to_insert_employee_data))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                                boolean checkInsertion = employeeDB.insertData(firstName, lastName, Integer.parseInt(contactNumber), address, jobStatus, Integer.parseInt(monthlyIncome));
                                if (checkInsertion) {
                                    Toast.makeText(AddEmployeeActivity.this, getString(R.string.inserted_data), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, EmployeeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddEmployeeActivity.this, getString(R.string.not_inserted_data), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.setTitle(getString(R.string.insert_employee));
                    alert.show();
                }else{
                    builder.setMessage(getString(R.string.want_to_update_employee_data))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                                finish();
                                boolean checkInsertion = employeeDB.updateData(Integer.parseInt(finalUser_id),firstName, lastName, Integer.parseInt(contactNumber), address, jobStatus, Integer.parseInt(monthlyIncome));
                                if (checkInsertion) {
                                    Toast.makeText(AddEmployeeActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, EmployeeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddEmployeeActivity.this, "Data didn't update, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.setTitle(getString(R.string.update_employee));
                    alert.show();
                }
            }
        });

    }
}