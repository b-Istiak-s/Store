package com.example.projecy.sales;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projecy.R;
import com.example.projecy.expenses.ExpenseDetailsActivity;
import com.example.projecy.model.Constants;
import com.example.projecy.model.SqliteExpense;
import com.example.projecy.model.SqliteSale;

import java.util.HashMap;

public class SalesDetailsActivity extends AppCompatActivity {


    EditText etxtProductName,etxtProductVersion,etxtProductPurchasePrice,etxtProductSalePrice,etxtExtraCost,etxtSaleDate,etxtSaleTime,etxtQuantityOfProduct,etxtProfit;
    Button btnEdit, btnUpdate,btnExpenseDetails;
    SqliteSale sqliteSale;
    SqliteExpense sqliteExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_sales_details);

        etxtProductName = findViewById(R.id.etxtProductNameSaleDetails);
        etxtProductVersion = findViewById(R.id.etxtProductVersionSaleDetails);
        etxtProductPurchasePrice = findViewById(R.id.etxtProductPurchasePriceSaleDetails);
        etxtProductSalePrice = findViewById(R.id.etxtProductSalePriceSaleDetails);
        etxtExtraCost = findViewById(R.id.etxtExtraCostSaleDetails);
        etxtSaleDate = findViewById(R.id.etxtSaleDate);
        etxtSaleTime = findViewById(R.id.etxtSaleTime);
        etxtQuantityOfProduct = findViewById(R.id.etxtQuantityOfProductSaleDetails);
        etxtProfit = findViewById(R.id.etxtProfit);
        btnEdit = findViewById(R.id.btnEditSaleDetails);
        btnUpdate = findViewById(R.id.btnUpdateSaleDetails);
        btnExpenseDetails = findViewById(R.id.btnExpenseDetailsSaleDetails);
        sqliteSale = new SqliteSale(this);
        sqliteExpense = new SqliteExpense(this);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString(Constants.id);
        String productName = extras.getString(Constants.productName);
        String productVersion = extras.getString(Constants.productVersion);
        String productPrice = extras.getString(Constants.productPrice);
        String salePrice = extras.getString(Constants.salePrice);
        String extraCost = extras.getString(Constants.extraCost);
        String saleDay = extras.getString(Constants.productSaleDay);
        String saleMonth = extras.getString(Constants.productSaleMonth);
        String saleYear = extras.getString(Constants.productSaleYear);
        String saleTime = extras.getString(Constants.productSaleTime);
        String quantityOfProduct = extras.getString(Constants.quantityOfProduct);
        String profit = extras.getString(Constants.profit);

        getSupportActionBar().setTitle(productName+"'s sale details");

        etxtProductName.setEnabled(false);
        etxtProductVersion.setEnabled(false);
        etxtProductPurchasePrice.setEnabled(false);
        etxtProductSalePrice.setEnabled(false);
        etxtExtraCost.setEnabled(false);
        etxtSaleDate.setEnabled(false);
        etxtSaleTime.setEnabled(false);
        etxtQuantityOfProduct.setEnabled(false);
        etxtProfit.setEnabled(false);

        etxtProductName.setText(productName);
        etxtProductVersion.setText(productVersion);
        etxtProductPurchasePrice.setText(productPrice);
        etxtProductSalePrice.setText(salePrice);
        etxtExtraCost.setText(extraCost);
        etxtSaleDate.setText(saleDay+"/"+saleMonth+"/"+saleYear);
        etxtSaleTime.setText(saleTime);
        etxtQuantityOfProduct.setText(quantityOfProduct);
        etxtProfit.setText(profit);

        btnEdit.setOnClickListener(v->{
            etxtProductSalePrice.setEnabled(true);
            etxtExtraCost.setEnabled(true);
            etxtQuantityOfProduct.setEnabled(true);
        });

        etxtProductSalePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int salePriceInt;
                if (!editable.toString().isEmpty()) {
                    salePriceInt = Integer.parseInt(editable.toString());
                }else{
                    salePriceInt = 0;
                }
                int quantityOfProductInt = Integer.parseInt(etxtQuantityOfProduct.getText().toString().trim());
                int extraCostInt = Integer.parseInt(etxtExtraCost.getText().toString().trim());
                etxtProfit.setText(String.valueOf(salePriceInt*quantityOfProductInt-Integer.parseInt(productPrice)*quantityOfProductInt-extraCostInt));
            }
        });

        etxtExtraCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int salePriceInt = Integer.parseInt(etxtProductSalePrice.getText().toString().trim());
                int quantityOfProductInt = Integer.parseInt(etxtQuantityOfProduct.getText().toString().trim());
                int extraCostInt;
                if (!editable.toString().isEmpty()) {
                    extraCostInt = Integer.parseInt(editable.toString());
                }else{
                    extraCostInt = 0;
                }
                etxtProfit.setText(String.valueOf(salePriceInt*quantityOfProductInt-Integer.parseInt(productPrice)*quantityOfProductInt-extraCostInt));
            }
        });

        etxtQuantityOfProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int salePriceInt = Integer.parseInt(etxtProductSalePrice.getText().toString().trim());
                int quantityOfProductInt;
                if (!editable.toString().isEmpty()) {
                    quantityOfProductInt = Integer.parseInt(editable.toString());
                }else{
                    quantityOfProductInt = 0;
                }
                int extraCostInt = Integer.parseInt(etxtExtraCost.getText().toString().trim());
                etxtProfit.setText(String.valueOf(salePriceInt*quantityOfProductInt-Integer.parseInt(productPrice)*quantityOfProductInt-extraCostInt));
            }
        });

        btnUpdate.setOnClickListener(v->{
            if (etxtProductSalePrice.isEnabled()){
                String salePriceForSql = etxtProductSalePrice.getText().toString();
                String quantityOfProductSql = etxtQuantityOfProduct.getText().toString().trim();
                String extraCostSql = etxtExtraCost.getText().toString().trim();
                String profitSql  =etxtProfit.getText().toString().trim();

                if (salePriceForSql.isEmpty()){
                    etxtProductSalePrice.setError("Please enter sale price");
                    etxtProductSalePrice.requestFocus();
                }else if (extraCostSql.isEmpty()){
                    etxtExtraCost.setError("Please enter extra cost");
                    etxtExtraCost.requestFocus();
                }else if (quantityOfProductSql.isEmpty()){
                    etxtQuantityOfProduct.setText("Please enter quantity of product");
                    etxtQuantityOfProduct.requestFocus();
                }else if (profitSql!="0") {
                    boolean checkInsertion = sqliteSale.updateData(Integer.parseInt(id),productName,productVersion,salePriceForSql,extraCostSql,quantityOfProductSql,quantityOfProduct,profitSql);
                    if (checkInsertion) {
                        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, SalesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Data didn't update, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                Toast.makeText(this, "Please, edit first.", Toast.LENGTH_SHORT).show();
            }
        });

        btnExpenseDetails.setOnClickListener(v->{
            HashMap<String, String> uData =  sqliteExpense.specificData(productName,productVersion);
            Intent intent = new Intent(this, ExpenseDetailsActivity.class);
            intent.putExtra(Constants.id, uData.get(Constants.id));
            intent.putExtra(Constants.productName, uData.get(Constants.productName));
            intent.putExtra(Constants.productPrice, uData.get(Constants.productPrice));
            intent.putExtra(Constants.productVersion, uData.get(Constants.productVersion));
            intent.putExtra(Constants.quantityOfProduct, uData.get(Constants.quantityOfProduct));
            intent.putExtra(Constants.productPurchaseDay, uData.get(Constants.productPurchaseDay));
            intent.putExtra(Constants.productPurchaseMonth, uData.get(Constants.productPurchaseMonth));
            intent.putExtra(Constants.productPurchaseYear, uData.get(Constants.productPurchaseYear));
            intent.putExtra(Constants.productPurchaseTime, uData.get(Constants.productPurchaseTime));
            intent.putExtra(Constants.productSerial, uData.get(Constants.productSerial));
            intent.putExtra(Constants.productType, uData.get(Constants.productType));
            intent.putExtra(Constants.remainingProduct, uData.get(Constants.remainingProduct));
            startActivity(intent);
        });

    }
}