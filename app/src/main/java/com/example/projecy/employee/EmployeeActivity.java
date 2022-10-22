package com.example.projecy.employee;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.PdfCreator;
import com.example.projecy.model.SqliteEmployee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmployeeActivity extends AppCompatActivity {


    FloatingActionButton btnAddEmployee,btnPdfCreator;
    RecyclerView recyclerViewEmployees;
    private RecyclerView.LayoutManager layoutManager;
    SqliteEmployee sqliteEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        btnAddEmployee = findViewById(R.id.fabAddEmployee);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployee);
        getSupportActionBar().setTitle("Employee");
        btnPdfCreator = findViewById(R.id.createPdfEmployee);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewEmployees.setLayoutManager(layoutManager);
        recyclerViewEmployees.setHasFixedSize(true);

        sqliteEmployee = new SqliteEmployee(EmployeeActivity.this);

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        EmployeeAdapter customAdapter = new EmployeeAdapter(EmployeeActivity.this,sqliteEmployee.display());
        recyclerViewEmployees.setAdapter(customAdapter); // set the Adapter to RecyclerView

        btnAddEmployee.setOnClickListener(v->{
            Intent intent = new Intent(EmployeeActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateShrink(this);
        });

        btnPdfCreator.setOnClickListener(v->{
            String[] constants = new String[]{Constants.firstName,Constants.lastName,Constants.contactNumber,Constants.jobStatus,Constants.monthlyIncome,Constants.address};
            PdfCreator pdfCreator = new PdfCreator();
            pdfCreator.createPDF(this,constants,sqliteEmployee.display(),"Employees");
        });

    }

}