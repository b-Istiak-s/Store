package com.example.projecy.expenses;

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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.PdfCreator;
import com.example.projecy.model.SqliteExpense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    static final String[] Months = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };
    static final String[] productType = new String[] { "On Product", "Extra"};
     Spinner spinnerMonth,spinnerYear,spinnerProductType;
     CheckBox checkBoxYearly,checkBoxAllType;
     FloatingActionButton fabAddToExpense,btnCreatePdfExpense;
     Button btnSearch;
     RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
     String yearsForSqlite, monthsForSqlite, productTypeForSqlite;
     SqliteExpense sqliteExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        btnCreatePdfExpense = findViewById(R.id.createPdfExpense);
        ArrayList<String> years = new ArrayList<>();

        getSupportActionBar().setTitle("Expense");

        for (int i = 2022; i <= 2050; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Months);
        ArrayAdapter<String> adapterProductType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productType);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerProductType = findViewById(R.id.spinnerProductType);
        checkBoxYearly = findViewById(R.id.checkBoxYearly);
        checkBoxAllType = findViewById(R.id.checkBoxType);
        btnSearch = findViewById(R.id.btnSearch);
        fabAddToExpense = findViewById(R.id.fabAddToExpense);
        recyclerView = findViewById(R.id.recyclerViewExpense);
        sqliteExpense = new SqliteExpense(this);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        spinnerMonth.setAdapter(adapterMonths);
        spinnerYear.setAdapter(adapterYear);
        spinnerProductType.setAdapter(adapterProductType);

        checkBoxYearly.setOnClickListener(v->{
            if(checkBoxYearly.isChecked()){
                spinnerMonth.setVisibility(View.GONE);
            }else{
                spinnerMonth.setVisibility(View.VISIBLE);
            }
        });

        checkBoxAllType.setOnClickListener(v->{
            if (checkBoxAllType.isChecked()){
                spinnerProductType.setVisibility(View.GONE);
            }else{
                spinnerProductType.setVisibility(View.VISIBLE);
            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearsForSqlite = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthsForSqlite = String.valueOf(i+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    productTypeForSqlite = "product";
                }else {
                    productTypeForSqlite = "extra";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSearch.setOnClickListener(v->{
            //  call the constructor of CustomAdapter to send the reference and data to Adapter
            ExpenseAdapter customAdapter = new ExpenseAdapter(this,sqliteExpense.display(monthsForSqlite,yearsForSqlite,productTypeForSqlite,checkBoxYearly.isChecked(),checkBoxAllType.isChecked()));
            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        });

        fabAddToExpense.setOnClickListener(v->{
            Intent intent = new Intent(this, AddToExpenseActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateSpin(this);
        });

        btnCreatePdfExpense.setOnClickListener(v->{
            String[] constants = new String[]{Constants.productName,Constants.productVersion,Constants.productPrice,Constants.productPurchaseDate,Constants.productPurchaseTime,Constants.quantityOfProduct,Constants.productType,Constants.remainingProduct};
            PdfCreator pdfCreator = new PdfCreator();
            if (monthsForSqlite.isEmpty() && checkBoxYearly.isChecked()!=true){
                Toast.makeText(this, "Please, select month.", Toast.LENGTH_SHORT).show();
            }else if (yearsForSqlite.isEmpty()){
                Toast.makeText(this, "Please, select year.", Toast.LENGTH_SHORT).show();
            }else {
                pdfCreator.createPDF(this, constants, sqliteExpense.display(monthsForSqlite, yearsForSqlite, "product", checkBoxYearly.isChecked(), true), "Expenses");
            }
        });
    }
}