package com.example.projecy.sales;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projecy.R;
import com.example.projecy.model.SqliteSale;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddToSalesActivity extends AppCompatActivity {

    EditText etxtProductName, etxtPurchasePrice, etxtSalePrice, etxtTimeOfSale, etxtDateOfSale, etxtQuantityOfSale;
    EditText etxtExtraCost;
    Button btnAdd;
    private Calendar calendar;
    private int year, month, day;
    private int hour, minute;
    Spinner spinnerVersion;
    ArrayList<String> version;
    String versionForSqlite;

    SqliteSale sqliteSale;
    String name;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_add_to_sales);

        etxtProductName = findViewById(R.id.etxtProductName);
        etxtPurchasePrice = findViewById(R.id.etxtPurchasePrice);
        etxtSalePrice = findViewById(R.id.etxtSellingPrice);
        etxtTimeOfSale = findViewById(R.id.etxtTimeOfSale);
        etxtDateOfSale = findViewById(R.id.etxtDateOfSale);
        etxtQuantityOfSale = findViewById(R.id.etxtQuantityOfGoodsSold);
        etxtExtraCost = findViewById(R.id.etxtExtraCost);
        btnAdd = findViewById(R.id.btnAddSale);
        spinnerVersion = findViewById(R.id.spinnerVersion);
        sqliteSale = new SqliteSale(this);
        builder = new AlertDialog.Builder(this);
        getSupportActionBar().setTitle("Add Sale Data");

        etxtProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                version = sqliteSale.productNameCheck(editable.toString());
                if (version!=null) {
                    ArrayAdapter<String> adapterProductType = new ArrayAdapter<>(AddToSalesActivity.this, android.R.layout.simple_spinner_item, version);
                    spinnerVersion.setAdapter(adapterProductType);
                    name = editable.toString();
                }
            }
        });

        spinnerVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    etxtPurchasePrice.setText(String.valueOf(sqliteSale.productNameVersionCheck(name, version.get(i))));
                    versionForSqlite = String.valueOf(version.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        etxtQuantityOfSale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etxtQuantityOfSale.setError("Your remaining quantity of product is "+sqliteSale.productRemainingQuantity(etxtProductName.getText().toString().trim(),versionForSqlite), getDrawable(android.R.drawable.stat_sys_warning));
                }
                etxtQuantityOfSale.requestFocus();
            }
        });

        btnAdd.setOnClickListener(v -> {
            String productName = etxtProductName.getText().toString().trim();
            String purchasePrice = etxtPurchasePrice.getText().toString().trim();
            String salePrice = etxtSalePrice.getText().toString().trim();
            String timeOfSale = etxtTimeOfSale.getText().toString().trim();
            String dateOfSale = etxtDateOfSale.getText().toString().trim();
            String quantityOfSale = etxtQuantityOfSale.getText().toString().trim();
            String extraCost = etxtExtraCost.getText().toString().trim();

            if (productName == null) {
                etxtProductName.setError("Enter the product's name");
                etxtProductName.requestFocus();
            }
            else if (purchasePrice == null) {
                etxtPurchasePrice.setError("Enter the product's purchase price");
                etxtPurchasePrice.requestFocus();
            } else if (salePrice == null) {
                etxtSalePrice.setError("Enter the product's selling price");
                etxtSalePrice.requestFocus();
            } else if (timeOfSale == null) {
                etxtTimeOfSale.setError("Enter the time of selling the product");
                etxtTimeOfSale.requestFocus();
            } else if (dateOfSale == null) {
                etxtDateOfSale.setError("Enter the date of selling the product");
                etxtDateOfSale.requestFocus();
            } else if (quantityOfSale == null) {
                etxtQuantityOfSale.setError("Enter the quantity of product sold");
                etxtQuantityOfSale.requestFocus();
            } else {
                if (Integer.parseInt(quantityOfSale)>sqliteSale.productRemainingQuantity(productName,versionForSqlite)){
                    Toast.makeText(this, "Please, calculate the quantity of product properly. You have "+sqliteSale.productRemainingQuantity(productName,versionForSqlite)+" of "+productName+" and version : "+versionForSqlite + " in stock.", Toast.LENGTH_LONG).show();
                }else {
                    String profit = String.valueOf(Integer.parseInt(salePrice) * Integer.parseInt(quantityOfSale) - Integer.parseInt(purchasePrice) * Integer.parseInt(quantityOfSale) - Integer.parseInt(extraCost));
                    builder.setMessage("Profit=" + profit + "; " + getString(R.string.want_to_insert_sale_data))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                                boolean checkInsertion = sqliteSale.insertData(productName, versionForSqlite, purchasePrice, salePrice, String.valueOf(day), String.valueOf(month), String.valueOf(year), timeOfSale, extraCost, quantityOfSale, profit);
                                if (checkInsertion) {
                                    Toast.makeText(this, getString(R.string.inserted_data)+" You have "+ (-Integer.parseInt(quantityOfSale) + sqliteSale.productRemainingQuantity(productName, versionForSqlite)) +" of "+productName+" and version : "+versionForSqlite, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(this, getString(R.string.not_inserted_data), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.setTitle(getString(R.string.insert_to_sale));
                    alert.show();
                }
            }
        });


        //Date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
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
        showDate(arg1, arg2 + 1, arg3);
    };

    private void showDate(int year, int month, int day) {
        etxtDateOfSale.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        this.year = year;
        this.month = month;
        this.day = day;
    }
    //date working ends here


    //time picker
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            etxtTimeOfSale.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}