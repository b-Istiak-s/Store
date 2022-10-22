package com.example.projecy.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQliteProfit extends SQLiteOpenHelper {

    String TABLE_SALE = "sale";
    String TABLE_EXPENSE = "expense";
    String TABLE_EMPLOYEE = "employee";

    public SQliteProfit(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public HashMap<String, String> profit(String day, String year, String month, boolean today){
        HashMap<String, String> money = new HashMap<>();
        SQLiteDatabase db=getReadableDatabase();
        int profit=0;
        int extraCost = 0;
        int sold = 0;
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if (!today)
            result = db.rawQuery("SELECT * FROM " + TABLE_SALE + " WHERE " + Constants.productSaleYear + " = ? AND " + Constants.productSaleMonth + " = ? ", new String[]{year, month});
        else
            result = db.rawQuery("SELECT * FROM " + TABLE_SALE + " WHERE " +Constants.productSaleDay + " = ? AND "+ Constants.productSaleYear + " = ? AND " + Constants.productSaleMonth + " = ? ", new String[]{day,year, month});

        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                HashMap<String, String> user_data = new HashMap<>();
                user_data.put(Constants.id, result.getString(0));
                user_data.put(Constants.productName, result.getString(1));
                user_data.put(Constants.productVersion, result.getString(2));
                user_data.put(Constants.productPrice, result.getString(3));
                user_data.put(Constants.salePrice, result.getString(4));
                user_data.put(Constants.extraCost, result.getString(5));
                user_data.put(Constants.productSaleDay, result.getString(6));
                user_data.put(Constants.productSaleMonth, result.getString(7));
                user_data.put(Constants.productSaleYear, result.getString(8));
                user_data.put(Constants.productSaleTime, result.getString(9));
                user_data.put(Constants.quantityOfProduct, result.getString(10));
                user_data.put(Constants.profit, result.getString(11));

                list.add(user_data);
            }while (result.moveToNext());

            if (list.size()-1>0) {
                for (int i = 0; i < list.size(); i++) {
                    profit = profit + Integer.parseInt(list.get(i).get(Constants.profit));
                    extraCost = extraCost + Integer.parseInt(list.get(i).get(Constants.extraCost));
                    sold = sold + Integer.parseInt(list.get(i).get(Constants.salePrice))*Integer.parseInt(list.get(i).get(Constants.quantityOfProduct));

                }
            }else{
                profit = profit + Integer.parseInt(list.get(0).get(Constants.profit));
                extraCost = extraCost + Integer.parseInt(list.get(0).get(Constants.extraCost));
                sold = sold + Integer.parseInt(list.get(0).get(Constants.salePrice))*Integer.parseInt(list.get(0).get(Constants.quantityOfProduct));
            }
        }
        money.put(Constants.profit, String.valueOf(profit));
        money.put(Constants.extraCost, String.valueOf(extraCost));
        money.put(Constants.salePrice, String.valueOf(sold));
        money.put(Constants.monthlyIncome, employee());
        money.putAll(expense(day, year,month,today));
        return money;
    }

    public HashMap<String, String> expense(String day, String year, String month,boolean today){
        SQLiteDatabase db=getReadableDatabase();
        HashMap<String, String> expenseHashMap = new HashMap<>();
        int expense =0 ;
        int remainingProduct = 0;
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if (!today)
            result = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + Constants.productPurchaseYear + " = ? AND " + Constants.productPurchaseMonth + " = ? ", new String[]{year, month});
        else
            result = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + Constants.productPurchaseDay + " = ? AND " +Constants.productPurchaseYear + " = ? AND " + Constants.productPurchaseMonth + " = ? ", new String[]{day, year, month});


        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                HashMap<String, String> user_data = new HashMap<>();
                user_data.put(Constants.id, result.getString(0));
                user_data.put(Constants.productName, result.getString(1));
                user_data.put(Constants.productPrice, result.getString(2));
                user_data.put(Constants.productVersion, result.getString(3));
                user_data.put(Constants.productPurchaseDay, result.getString(4));
                user_data.put(Constants.productPurchaseMonth, result.getString(5));
                user_data.put(Constants.productPurchaseYear, result.getString(6));
                user_data.put(Constants.productPurchaseTime, result.getString(7));
                user_data.put(Constants.productSerial, result.getString(8));
                user_data.put(Constants.quantityOfProduct, result.getString(9));
                user_data.put(Constants.productType, result.getString(10));
                user_data.put(Constants.remainingProduct, result.getString(11));

                list.add(user_data);
            }while (result.moveToNext());

            if (list.size()-1>0) {
                for (int i = 0; i < list.size(); i++) {
                    expense = expense + Integer.parseInt(list.get(i).get(Constants.productPrice))*Integer.parseInt(list.get(i).get(Constants.quantityOfProduct));
                    remainingProduct = remainingProduct + Integer.parseInt(list.get(i).get(Constants.remainingProduct));
                }
            }else{
                expense = expense + Integer.parseInt(list.get(0).get(Constants.productPrice))*Integer.parseInt(list.get(0).get(Constants.quantityOfProduct));
                remainingProduct = remainingProduct + Integer.parseInt(list.get(0).get(Constants.remainingProduct));
            }
        }

        Cursor forAll = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE, new String[]{});
        int remainingProductTot=0;
        if (forAll.getCount()!=0){
            forAll.moveToFirst();
            ArrayList<HashMap<String, String>> listForCalculatingRemainingProduct = new ArrayList<>();
            do {
                HashMap<String, String> hashMapForCalculatingRemainingProduct = new HashMap<>();
                hashMapForCalculatingRemainingProduct.put(Constants.remainingProductTot,forAll.getString(11));
                listForCalculatingRemainingProduct.add(hashMapForCalculatingRemainingProduct);
            }while(result.moveToNext());
            for (int i=0;i< listForCalculatingRemainingProduct.size();i++){
                remainingProductTot = remainingProductTot+Integer.parseInt(listForCalculatingRemainingProduct.get(i).get(Constants.remainingProductTot));
            }
        }
        expenseHashMap.put(Constants.productPrice, String.valueOf(expense));
        expenseHashMap.put(Constants.remainingProduct, String.valueOf(remainingProduct));
        expenseHashMap.put(Constants.remainingProductTot, String.valueOf(remainingProductTot));
        return expenseHashMap;
    }

    public String employee(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        result=db.rawQuery("SELECT * FROM "+TABLE_EMPLOYEE,null);

        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                HashMap<String, String> user_data = new HashMap<>();
                user_data.put(Constants.id, result.getString(0));
                user_data.put(Constants.firstName, result.getString(1));
                user_data.put(Constants.lastName, result.getString(2));
                user_data.put(Constants.address, result.getString(3));
                user_data.put(Constants.contactNumber, result.getString(4));
                user_data.put(Constants.jobStatus, result.getString(5));
                user_data.put(Constants.monthlyIncome, result.getString(6));

                list.add(user_data);
            }while (result.moveToNext());

            int totalSalary = 0;
            if (list.size()-1>0) {
                for (int i = 0; i < list.size() ; i++) {
                    totalSalary = totalSalary + Integer.parseInt(list.get(i).get(Constants.monthlyIncome));
                }
            }else{
                totalSalary = totalSalary + Integer.parseInt(list.get(0).get(Constants.monthlyIncome));
            }
            return String.valueOf(totalSalary);
        }else{
            return "0";
        }

    }

}
