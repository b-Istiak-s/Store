package com.example.projecy.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SqliteSale extends SQLiteOpenHelper {

    private static final String TABLE_EMPLOYEE="employee";
    private static final String TABLE_SALE="sale";
    private static final String TABLE_EXPENSE ="expense";

    public SqliteSale(@Nullable Context context) {
        super(context,Constants.DB_NAME,null,Constants.DB_VERSION);
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
        onCreate(sqLiteDatabase);
    }


    public ArrayList<String> productNameCheck(String productName){
        ArrayList<String> version = new ArrayList<>();

        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        result=db.rawQuery("SELECT * FROM "+TABLE_EXPENSE+" WHERE "+Constants.productName+" = ? ", new String[]{productName});

        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                version.add(result.getString(3));
            }while (result.moveToNext());
            return version;
        }else{
            return null;
        }

    }

    public int productNameVersionCheck(String productName, String version){
        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        result=db.rawQuery("SELECT * FROM "+TABLE_EXPENSE+" WHERE "+Constants.productName+" = ? AND "+Constants.productVersion+" = ? ", new String[]{productName,version});

        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                HashMap<String, String> user_data = new HashMap<>();
                user_data.put(Constants.productPrice, result.getString(2));

                list.add(user_data);
            }while (result.moveToNext());

            return Integer.parseInt(list.get(list.size()-1).get(Constants.productPrice));
        }else{
            return 0;
        }
    }

    public int alreadySold(String productName, String version){
        ArrayList<String> productSold = new ArrayList<>();
        int sum = 0;

        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        result=db.rawQuery("SELECT * FROM "+TABLE_SALE+" WHERE "+Constants.productName+" = ? AND "+Constants.productVersion+" = ? ", new String[]{productName,version});

        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                productSold.add(result.getString(10));
            }while (result.moveToNext());

            for (int i = 0; i <= productSold.size() - 1; i++) {
                sum = sum + Integer.valueOf(productSold.get(i));
            }

            return sum;
        }else{
            return 0;
        }
    }

    public int productRemainingQuantity(String productName, String version){
        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        result=db.rawQuery("SELECT * FROM "+TABLE_EXPENSE+" WHERE "+Constants.productName+" = ? AND "+Constants.productVersion+" = ? ", new String[]{productName,version});

        if(result.getCount()!=0){
            result.moveToFirst();  //to point first row
            do {
                HashMap<String, String> user_data = new HashMap<>();
                user_data.put(Constants.quantityOfProduct, result.getString(9));

                list.add(user_data);
            }while (result.moveToNext());

            return Integer.parseInt(list.get(list.size()-1).get(Constants.quantityOfProduct))-alreadySold(productName, version);
        }else{
            return 0;
        }
    }


    public boolean insertData(String productName, String productVersion, String productPrice, String salePrice, String saleDay, String saleMonth, String saleYear, String saleTime,String extraCost,String quantityOfProduct,String profit)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.productName,productName);
        values.put(Constants.productVersion,productVersion);
        values.put(Constants.productPrice,productPrice);
        values.put(Constants.salePrice,salePrice);
        values.put(Constants.extraCost,extraCost);
        values.put(Constants.productSaleDay,saleDay);
        values.put(Constants.productSaleMonth,saleMonth);
        values.put(Constants.productSaleYear,saleYear);
        values.put(Constants.productSaleTime,saleTime);
        values.put(Constants.quantityOfProduct,quantityOfProduct);
        values.put(Constants.profit,profit);

        long check=db.insert(TABLE_SALE,null,values);
        if (check==-1)  //check returns -1 as data don't insert
        {
            return false;

        }

        else
        {
//            ContentValues values1=new ContentValues();
//            values1.put(Constants.remainingProduct,productRemainingQuantity(productName,productVersion));

            //The following code doesn't work for some cases. As you can see that the values bounded within single quotation mark,
            // so if user uses single quotation mark for values then it will return error. Sometimes, instead of returning of error the code will not just run and will show some warnings on the logcat.
//            db.execSQL("UPDATE "+TABLE_EXPENSE+" SET "+Constants.remainingProduct+" = '"+productRemainingQuantity(productName,productVersion) +"' WHERE "+Constants.productName+" = '"+productName+"' AND "+Constants.productVersion+" = '"+productVersion+"'");

            ContentValues values1 = new ContentValues();
            values1.put(Constants.remainingProduct,productRemainingQuantity(productName,productVersion));
            db.update(TABLE_EXPENSE,values1,Constants.productName+" = ? AND "+Constants.productVersion+" = ? ",new String[]{productName,productVersion});
            return true;
        }

    }

    public ArrayList<HashMap<String, String>> display(String month, String year, boolean yearly)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor result;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if (!yearly) {
            result = db.rawQuery("SELECT * FROM " + TABLE_SALE + " WHERE " + Constants.productSaleYear + " = ? AND " + Constants.productSaleMonth + " = ? "+" ORDER BY "+Constants.productSaleMonth+" DESC ,"+Constants.productSaleDay+" DESC ", new String[]{year, month});
        }else{
            result = db.rawQuery("SELECT * FROM " + TABLE_SALE + " WHERE " + Constants.productSaleYear + " = ? "+" ORDER BY "+Constants.productSaleMonth+" DESC ,"+Constants.productSaleDay+" DESC ", new String[]{year});
        }

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
                user_data.put(Constants.productSaleDate, result.getString(6)+"/"+result.getString(7)+"/"+result.getString(8));
                user_data.put(Constants.productSaleTime, result.getString(9));
                user_data.put(Constants.quantityOfProduct, result.getString(10));
                user_data.put(Constants.profit, result.getString(11));

                list.add(user_data);
            }while (result.moveToNext());
        }
        return list;
    }



    public boolean updateData(int id,String productName, String productVersion, String productSalePrice, String extraCost, String quantityOfProduct,String quantityOfProductOld, String profit)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();

        String changeInQuantity = String.valueOf(Integer.parseInt(quantityOfProductOld)-Integer.parseInt(quantityOfProduct));
        Cursor result;
        result = db.rawQuery("SELECT * FROM "+TABLE_EXPENSE+" WHERE "+Constants.productName+" = ? AND "+ Constants.productVersion+" = ? ",new String[]{productName,productVersion});

        int remainingProduct = 0;
        if(result.getCount()!=0) {
            result.moveToFirst();  //to point first row
            remainingProduct = Integer.parseInt(result.getString(9));
        }
        ContentValues values1 = new ContentValues();
        values1.put(Constants.remainingProduct,String.valueOf(remainingProduct-Integer.parseInt(changeInQuantity)));


        values.put(Constants.salePrice,productSalePrice);
        values.put(Constants.extraCost,extraCost);
        values.put(Constants.quantityOfProduct,quantityOfProduct);
        values.put(Constants.profit,profit);

        long check=db.update(TABLE_SALE,values,Constants.id+" = ?",new String[] {String.valueOf(id)});
        if (check==-1){return false;}
        else{
            db.update(TABLE_EXPENSE,values1,Constants.productName+" = ? AND "+Constants.productVersion+" = ? ",new String[]{productName,productVersion});
            return true;
        }
    }


}
