package com.example.projecy.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.projecy.R;
import com.example.projecy.model.Constants;
import com.example.projecy.model.SqliteExpense;

public class ExpenseDetailsActivity extends AppCompatActivity {

    EditText etxtProductName, etxtProductPrice, etxtProductVersion, etxtDateOfPurchase,etxtTimeOfPurchase, etxtProductSerial,etxtQuantityOfProduct,etxtRemainingProduct,etxtProductType;
    Button btnEdit, btnUpdate;
    SqliteExpense sqliteExpense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        etxtProductName = findViewById(R.id.etxtExpenseProductNameDetails);
        etxtProductPrice = findViewById(R.id.etxtExpenseProductPriceDetails);
        etxtProductVersion = findViewById(R.id.etxtExpenseProductVersionDetails);
        etxtDateOfPurchase = findViewById(R.id.etxtExpenseDateOfPurchaseDetails);
        etxtTimeOfPurchase = findViewById(R.id.etxtExpenseTimeOfPurchaseDetails);
        etxtProductSerial = findViewById(R.id.etxtExpenseProductSerialDetails);
        etxtQuantityOfProduct = findViewById(R.id.etxtExpenseQuantityOfProductDetails);
        etxtRemainingProduct = findViewById(R.id.etxtExpenseRemainingProduct);
        etxtProductType = findViewById(R.id.etxtExpenseProductTypeDetails);
        btnEdit = findViewById(R.id.btnEditDetails);
        btnUpdate = findViewById(R.id.btnUpdateDetails);
        sqliteExpense = new SqliteExpense(this);


        Bundle extras = getIntent().getExtras();
        String productId = extras.getString(Constants.id);
        String productName = extras.getString(Constants.productName);
        String productPrice = extras.getString(Constants.productPrice);
        String productVersion = extras.getString(Constants.productVersion);
        String quantityOfProduct = extras.getString(Constants.quantityOfProduct);
        String purchaseOfDay = extras.getString(Constants.productPurchaseDay);
        String purchaseOfMonth = extras.getString(Constants.productPurchaseMonth);
        String purchaseOfYear = extras.getString(Constants.productPurchaseYear);
        String productPurchaseTime = extras.getString(Constants.productPurchaseTime);
        String productSerial = extras.getString(Constants.productSerial);
        String productType = extras.getString(Constants.productType);
        String remainingProduct = extras.getString(Constants.remainingProduct);

        getSupportActionBar().setTitle(productName+"'s expense details");

        etxtProductName.setEnabled(false);
        etxtProductPrice.setEnabled(false);
        etxtProductVersion.setEnabled(false);
        etxtDateOfPurchase.setEnabled(false);
        etxtTimeOfPurchase.setEnabled(false);
        etxtProductSerial.setEnabled(false);
        etxtQuantityOfProduct.setEnabled(false);
        etxtProductType.setEnabled(false);
        etxtRemainingProduct.setEnabled(false);

        etxtProductName.setText(productName);
        etxtProductPrice.setText(productPrice);
        etxtProductVersion.setText(productVersion);
        etxtDateOfPurchase.setText(purchaseOfDay+"/"+purchaseOfMonth+"/"+purchaseOfYear);
        etxtTimeOfPurchase.setText(productPurchaseTime);
        etxtProductSerial.setText(productSerial);
        etxtQuantityOfProduct.setText(quantityOfProduct);
        etxtProductType.setText(productType);
        etxtRemainingProduct.setText(remainingProduct);

        if (Integer.parseInt(quantityOfProduct)-Integer.parseInt(remainingProduct)==0){
            btnEdit.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
        }else{
            btnEdit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        }
        btnEdit.setOnClickListener(v->{
            etxtProductName.setEnabled(true);
            etxtProductPrice.setEnabled(true);
//            etxtProductVersion.setEnabled(true);
//            etxtDateOfPurchase.setEnabled(true);
//            etxtTimeOfPurchase.setEnabled(true);
            etxtProductSerial.setEnabled(true);
            etxtQuantityOfProduct.setEnabled(true);
            etxtProductType.setEnabled(true);
//            etxtRemainingProduct.setEnabled(true);
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
                etxtProductVersion.setText(sqliteExpense.check(editable.toString()));
            }
        });
        btnUpdate.setOnClickListener(v->{
            if (etxtProductName.isEnabled()){
                String productTypeUpdate = etxtProductType.getText().toString().trim();
                if (productTypeUpdate.equals("extra") || productTypeUpdate.equals("product")){
                    String productNameUpdate = etxtProductName.getText().toString().trim();
                    String productPriceUpdate = etxtProductPrice.getText().toString().trim();
                    String productVersionUpdate = etxtProductVersion.getText().toString().trim();
                    String productSerialUpdate = etxtProductSerial.getText().toString().trim();
                    String quantityOfProductUpdate = etxtQuantityOfProduct.getText().toString().trim();
                    boolean checkInsertion = sqliteExpense.updateData(Integer.parseInt(productId),productNameUpdate,productPriceUpdate,productVersionUpdate,purchaseOfDay,purchaseOfMonth,purchaseOfYear,productPurchaseTime,productSerialUpdate,quantityOfProductUpdate,productTypeUpdate);
                    if (checkInsertion) {
                        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, ExpenseActivity.class);
                        startActivity(intent);
                        Animatoo.INSTANCE.animateWindmill(this);
                        finish();
                    } else {
                        Toast.makeText(this, "Data didn't update, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Wrong product type!!! Write \"extra\" or \"product\"", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please, edit first.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}