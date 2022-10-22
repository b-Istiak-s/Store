package com.example.projecy.profit;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.SQliteProfit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProfitActivity extends AppCompatActivity {


    Spinner spinnerYear, spinnnerMonth;
    Button btnSearch;
    TextView txtEmployeesSalary,txtMonthExpense,txtTotalSale,txtRemainingProduct,txtProfit,txtAmountOfExtraProductRemaining;
    CheckBox checkBoxOnlyToday;
    SQliteProfit sQliteProfit;
    String yearsForSqlite, monthsForSqlite;
    static final String[] Months = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        spinnerYear = findViewById(R.id.spinnerYearForProfit);
        spinnnerMonth = findViewById(R.id.spinnerMonthForProfit);
        btnSearch = findViewById(R.id.btnSearchForProfit);
        txtEmployeesSalary = findViewById(R.id.txtEmployeesTotalSalary);
        txtMonthExpense = findViewById(R.id.txtMonthExpense);
        txtTotalSale = findViewById(R.id.txtTotalSale);
        txtRemainingProduct = findViewById(R.id.txtAmountOfExtraProduct);
        txtProfit = findViewById(R.id.txtTotalProfit);
        checkBoxOnlyToday = findViewById(R.id.checkBoxOnlyToday);
        txtAmountOfExtraProductRemaining = findViewById(R.id.txtAmountOfExtraProductRemaining);
        getSupportActionBar().setTitle("Profit");


        ArrayList<String> years = new ArrayList<>();

        for (int i = 2022; i <= 2050; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Months);

        spinnerYear.setAdapter(adapterYear);
        spinnnerMonth.setAdapter(adapterMonths);



        spinnnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthsForSqlite=String.valueOf(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearsForSqlite= years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sQliteProfit = new SQliteProfit(this);

        checkBoxOnlyToday.setOnClickListener(v->{
            if (checkBoxOnlyToday.isChecked()){
                btnSearch.setVisibility(View.GONE);
                spinnnerMonth.setVisibility(View.GONE);
                spinnerYear.setVisibility(View.GONE);


                calendar = Calendar.getInstance();
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                HashMap<String,String> money = sQliteProfit.profit(day,year,month,true);
                if (money!=null) {
                    txtEmployeesSalary.setText("Total employee's salary : " + money.get(Constants.monthlyIncome));
                    txtMonthExpense.setText("Today" + "'s total expense is : " + money.get(Constants.productPrice));
                    txtTotalSale.setText("Today" + "'s total sale is : " + money.get(Constants.salePrice));
                    txtRemainingProduct.setText("Today's remaining product is : " + money.get(Constants.remainingProduct));
                    txtAmountOfExtraProductRemaining.setText("Remaining product is : " + money.get(Constants.remainingProductTot));
                    txtProfit.setText("Today" + "'s profit is : " + money.get(Constants.profit));
                }
            }else{
                btnSearch.setVisibility(View.VISIBLE);
                spinnnerMonth.setVisibility(View.VISIBLE);
                spinnerYear.setVisibility(View.VISIBLE);
            }
        });


        btnSearch.setOnClickListener(v->{
            HashMap<String,String> money = sQliteProfit.profit(null,yearsForSqlite,monthsForSqlite,false);
            if (money!=null) {
                txtEmployeesSalary.setText("Total employee's salary : " + money.get(Constants.monthlyIncome));
                txtMonthExpense.setText(Months[Integer.parseInt(monthsForSqlite) - 1] + "'s total expense is : " + money.get(Constants.productPrice));
                txtTotalSale.setText(Months[Integer.parseInt(monthsForSqlite) - 1] + "'s total sale is : " + money.get(Constants.salePrice));
                txtRemainingProduct.setText(Months[Integer.parseInt(monthsForSqlite) - 1] +"'s remaining product is : " + money.get(Constants.remainingProduct));
                txtAmountOfExtraProductRemaining.setText("Remaining product is : " + money.get(Constants.remainingProductTot));
                txtProfit.setText(Months[Integer.parseInt(monthsForSqlite) - 1] + "'s profit is : " + money.get(Constants.profit));
            }

        });

    }
}