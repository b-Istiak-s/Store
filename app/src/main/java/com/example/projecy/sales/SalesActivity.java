package com.example.projecy.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.PdfCreator;
import com.example.projecy.model.SqliteSale;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SalesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton,btnCreatePdfSales;
    Spinner spinnerMonth,spinnerYear;
    Button btnSearch;
    CheckBox yearly;
    static final String[] Months = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };
    String monthForData;
    String year;
    boolean onlyYear;
    SqliteSale sqliteSale;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_sales);

        recyclerView = findViewById(R.id.recyclerViewSale);
        floatingActionButton = findViewById(R.id.fabAddToSales);
        spinnerMonth = findViewById(R.id.spinnerMonthSale);
        spinnerYear = findViewById(R.id.spinnerYearSale);
        btnSearch = findViewById(R.id.btnSearchSale);
        yearly = findViewById(R.id.checkBoxYearlySale);
        btnCreatePdfSales = findViewById(R.id.createPdfSales);
        getSupportActionBar().setTitle("Sales");

        sqliteSale = new SqliteSale(SalesActivity.this);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<String> years = new ArrayList<>();

        for (int i = 2022; i <= 2050; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Months);
        spinnerYear.setAdapter(adapterYear);
        spinnerMonth.setAdapter(adapterMonths);

        floatingActionButton.setOnClickListener(v->{
            Intent intent = new Intent(this, AddToSalesActivity.class);
            startActivity(intent);
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthForData = String.valueOf(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearly.setOnClickListener(v->{
            if (yearly.isChecked()){
                spinnerMonth.setVisibility(View.GONE);
                onlyYear=true;
            }else{
                spinnerMonth.setVisibility(View.VISIBLE);
                onlyYear=false;
            }
        });

        btnSearch.setOnClickListener(v->{
            //  call the constructor of CustomAdapter to send the reference and data to Adapter
            SalesAdapter customAdapter = new SalesAdapter(this,sqliteSale.display(monthForData,year,onlyYear));
            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        });

        btnCreatePdfSales.setOnClickListener(v->{
            String[] constants = new String[]{Constants.productName,Constants.productVersion,Constants.productPrice,Constants.salePrice,Constants.extraCost,Constants.profit,Constants.productSaleDate,Constants.productSaleTime,Constants.quantityOfProduct};
            PdfCreator pdfCreator = new PdfCreator();
            if (monthForData.isEmpty() && onlyYear!=true){
                Toast.makeText(this, "Please, select month.", Toast.LENGTH_SHORT).show();
            }else if (year.isEmpty()){
                Toast.makeText(this, "Please, select year.", Toast.LENGTH_SHORT).show();
            }else {
                pdfCreator.createPDF(this, constants, sqliteSale.display(monthForData, year, onlyYear), "Sales");
            }
        });
    }
}