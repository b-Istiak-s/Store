package com.example.projecy.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SqliteExpense extends SQLiteOpenHelper {

    private static final String TABLE_EMPLOYEE="employee";
    private static final String TABLE_EXPENSE="expense";
    private static final String TABLE_SALE="sale";

    public SqliteExpense(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query_employee;
        query_employee="CREATE TABLE IF NOT EXISTS "+TABLE_EMPLOYEE+" ("+Constants.id+" INTEGER PRIMARY KEY,"+Constants.firstName+" TEXT, "+Constants.lastName+" TEXT,"+Constants.address+" TEXT," +
                Constants.contactNumber+" INTEGER,"+Constants.jobStatus+" TEXT,"+Constants.monthlyIncome+" INTEGER)";
        String query_expense;
        query_expense="CREATE TABLE IF NOT EXISTS "+TABLE_EXPENSE+" ("+Constants.id+" INTEGER PRIMARY KEY,"+Constants.productName+" TEXT, "+Constants.productPrice+" TEXT,"+Constants.productVersion+" TEXT," +
                Constants.productPurchaseDay+" TEXT,"+Constants.productPurchaseMonth+" TEXT,"+Constants.productPurchaseYear+" TEXT,"+Constants.productPurchaseTime+" TEXT,"+Constants.productSerial+" TEXT,"+Constants.quantityOfProduct+" TEXT ,"+Constants.productType+" TEXT ,"+Constants.remainingProduct+" TEXT )";
        sqLiteDatabase.execSQL(query_employee);
        sqLiteDatabase.execSQL(query_expense);


        String querySale;
        querySale="CREATE TABLE IF NOT EXISTS "+TABLE_SALE+" ("+Constants.id+" INTEGER PRIMARY KEY,"+Constants.productName+" TEXT, "+Constants.productVersion+" TEXT,"+Constants.productPrice+" TEXT," +
                Constants.salePrice+" INTEGER,"+Constants.extraCost+" TEXT,"+Constants.productSaleDay+" TEXT,"+Constants.productSaleMonth+" TEXT,"+Constants.productSaleYear+" TEXT" +
                ","+Constants.productSaleTime+" TEXT,"+Constants.quantityOfProduct+" TEXT,"+Constants.profit+" TEXT )";

        sqLiteDatabase.execSQL(querySale);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_EMPLOYEE);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_EXPENSE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String productName,String productPrice,String productVersion, String productPurchaseDay, String productPurchaseMonth, String productPurchaseYear, String productPurchaseTime, String productSerial,String quantityOfProduct,String productType)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.productName,productName);
        values.put(Constants.productPrice,productPrice);
        values.put(Constants.productVersion,productVersion);
        values.put(Constants.productPurchaseDay,productPurchaseDay);
        values.put(Constants.productPurchaseMonth,productPurchaseMonth);
        values.put(Constants.productPurchaseYear,productPurchaseYear);
        values.put(Constants.productPurchaseTime,productPurchaseTime);
        values.put(Constants.productSerial,productSerial);
        values.put(Constants.quantityOfProduct,quantityOfProduct);
        values.put(Constants.productType,productType);
        values.put(Constants.remainingProduct,quantityOfProduct);

        long check=db.insert(TABLE_EXPENSE,null,values);
        if (check==-1)  //check returns -1 as data don't insert
        {
            return false;

        }

        else
        {
            return true;
        }

    }
    
    public int check(String productName){
            SQLiteDatabase db=getReadableDatabase();
            Cursor result;
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            result=db.rawQuery("SELECT * FROM "+TABLE_EXPENSE+" WHERE "+Constants.productName+" = ?", new String[]{productName});

            if(result.getCount()!=0){
                result.moveToFirst();  //to point first row
                do {
                    HashMap<String, String> user_data = new HashMap<>();
                    user_data.put(Constants.productVersion, result.getString(3));

                    list.add(user_data);
                }while (result.moveToNext());
                return Integer.parseInt(list.get(list.size()-1).get(Constants.productVersion))+1;
            }else{
                return 1;
            }

    }


    public ArrayList<HashMap<String, String>> display(String month, String year, String productType, boolean yearly,boolean allType)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if (!yearly) {
            if(!allType) {
                result = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + Constants.productPurchaseYear + " = ? AND " + Constants.productPurchaseMonth + " = ? AND " + Constants.productType + " = ? "+" ORDER BY "+Constants.productPurchaseMonth+" DESC ,"+Constants.productPurchaseDay+" DESC ", new String[]{year, month, productType});
            }else{
                result = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + Constants.productPurchaseYear + " = ? AND " + Constants.productPurchaseMonth + " = ? "+" ORDER BY "+Constants.productPurchaseMonth+" DESC ,"+Constants.productPurchaseDay+" DESC ", new String[]{year, month});
            }
        }else{
            if (!allType) {
                result = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + Constants.productPurchaseYear + " = ? AND " + Constants.productType + " = ? "+" ORDER BY "+Constants.productPurchaseMonth+" DESC ,"+Constants.productPurchaseDay+" DESC ", new String[]{year, productType});
            }else{
                result = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + Constants.productPurchaseYear + " = ? "+" ORDER BY "+Constants.productPurchaseMonth+" DESC ,"+Constants.productPurchaseDay+" DESC ", new String[]{year});
            }
        }

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
                user_data.put(Constants.productPurchaseDate, result.getString(4)+"/"+result.getString(5)+"/"+result.getString(6));

                list.add(user_data);
            }while (result.moveToNext());
        }
        return list;

    }


    public HashMap<String, String> specificData(String productName, String productVersion){
        SQLiteDatabase db=getReadableDatabase();
        HashMap<String, String> user_data = null;
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_EXPENSE+" WHERE "+Constants.productName+" = ? AND "+Constants.productVersion+" = ? ",new String[]{productName, productVersion});
        if (result.getCount()!=0){
            result.moveToFirst();
            user_data = new HashMap<>();
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
        }

        return user_data;
    }


    public boolean updateData(int id, String productName,String productPrice,String productVersion, String productPurchaseDay, String productPurchaseMonth, String productPurchaseYear, String productPurchaseTime, String productSerial,String quantityOfProduct,String productType)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Constants.productName,productName);
        values.put(Constants.productPrice,productPrice);
        values.put(Constants.productVersion,productVersion);
//        values.put(Constants.productPurchaseDay,productPurchaseDay);
//        values.put(Constants.productPurchaseMonth,productPurchaseMonth);
//        values.put(Constants.productPurchaseYear,productPurchaseYear);
//        values.put(Constants.productPurchaseTime,productPurchaseTime);
        values.put(Constants.productSerial,productSerial);
        values.put(Constants.quantityOfProduct,quantityOfProduct);
        values.put(Constants.productType,productType);
        values.put(Constants.remainingProduct,quantityOfProduct);

        long check=db.update(TABLE_EXPENSE,values,Constants.id+" = ?",new String[] {String.valueOf(id)});
        if (check==-1){return false;}
        else{ return true;}
    }
}
