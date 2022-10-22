package com.example.projecy.expenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projecy.R;
import com.example.projecy.model.SqliteExpense;

import java.util.Calendar;
import java.util.Locale;

public class AddToExpenseActivity extends AppCompatActivity {

    EditText etxtProductName, etxtProductPrice,etxtProductVersion,etxtProductPurchaseDate,etxtProductPurchaseTime,etxtProductSerial,etxtQuantityOfProduct;
    Button btnSubmit,btnPickTime;
    SqliteExpense sqliteExpense;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private int hour, minute;
    static final String[] productType = new String[] { "On Product", "Extra"};
    Spinner spinnerProductType;
    String productTypeForSqlite;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_expense);

        etxtProductName = findViewById(R.id.etxtExpenseProductName);
        etxtProductPrice = findViewById(R.id.etxtExpenseProductPrice);
        etxtProductVersion = findViewById(R.id.etxtExpenseProductVersion);
        etxtProductPurchaseDate = findViewById(R.id.etxtExpenseDateOfPurchase);
        etxtProductPurchaseTime = findViewById(R.id.etxtExpenseTimeOfPurchase);
        etxtProductSerial = findViewById(R.id.etxtExpenseProductSerial);
        etxtQuantityOfProduct = findViewById(R.id.etxtExpenseQuantityOfProduct);
        btnSubmit = findViewById(R.id.btnAddToExpense);
        spinnerProductType = findViewById(R.id.spinnerAddToExpenseProductType);
        btnPickTime = findViewById(R.id.btnPickTime);
        sqliteExpense = new SqliteExpense(this);
        builder = new AlertDialog.Builder(this);

        getSupportActionBar().setTitle("Add Expense Data");

        ArrayAdapter<String> adapterProductType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productType);

        spinnerProductType.setAdapter(adapterProductType);

        spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    productTypeForSqlite = "product";
                }else{
                    productTypeForSqlite = "extra";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                productTypeForSqlite = "product";
            }
        });

        etxtProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String liveProductName = editable.toString();
                etxtProductVersion.setText(String.valueOf(sqliteExpense.check(liveProductName)));
            }
        });

        btnSubmit.setOnClickListener(v->{
            String productName = etxtProductName.getText().toString().trim();
            String productPrice = etxtProductPrice.getText().toString().trim();
            String productVersion = etxtProductVersion.getText().toString().trim();
            String purchaseDate = etxtProductPurchaseDate.getText().toString().trim();
            String purchaseTime = etxtProductPurchaseTime.getText().toString().trim();
            String productSerial = etxtProductSerial.getText().toString().trim();
            String quantityOfProduct = etxtQuantityOfProduct.getText().toString().trim();

            if (productName.isEmpty()){
                etxtProductName.setError("Enter product name");
                etxtProductName.requestFocus();
            }else if (productPrice.isEmpty()){
                etxtProductPrice.setError("Enter product price");
                etxtProductPrice.requestFocus();
            }else if (productVersion.isEmpty()){
                etxtProductVersion.setError("Enter product version");
                etxtProductVersion.requestFocus();
            }else if (purchaseDate.isEmpty()){
                etxtProductPurchaseDate.setError("Enter purchase date");
                etxtProductPurchaseDate.requestFocus();
            }else if (purchaseTime.isEmpty()){
                etxtProductPurchaseTime.setError("Enter purchase time");
                etxtProductPurchaseTime.requestFocus();
            }else if (quantityOfProduct.isEmpty()){
                etxtQuantityOfProduct.setError("Enter the quantity of product");
                etxtQuantityOfProduct.requestFocus();
            }else{
                builder.setMessage(getString(R.string.want_to_insert_expense_data))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                                boolean checkInsertion = sqliteExpense.insertData(productName,productPrice,productVersion,String.valueOf(day),String.valueOf(month),String.valueOf(year),purchaseTime,productSerial,quantityOfProduct,productTypeForSqlite);
                                if (checkInsertion){
                                    Toast.makeText(this, getString(R.string.inserted_data), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(this, getString(R.string.not_inserted_data), Toast.LENGTH_SHORT).show();
                                }
                        })
                        .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.setTitle(getString(R.string.insert_to_expense));
                alert.show();
            }
        });


        //Date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }


    // date working starts here
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2+1, arg3);
    };

    private void showDate(int year, int month, int day) {
        etxtProductPurchaseDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        this.year = year;
        this.month = month;
        this.day = day;
    }
    //date working ends here


    //time picker
    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            etxtProductPurchaseTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}