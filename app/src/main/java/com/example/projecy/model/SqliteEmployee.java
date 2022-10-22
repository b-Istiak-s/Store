package com.example.projecy.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class SqliteEmployee extends SQLiteOpenHelper {

    private static final String TABLE_EMPLOYEE="employee";
    private static final String TABLE_EXPENSE="expense";
    private static final String TABLE_SALE="sale";

    public SqliteEmployee(@Nullable Context context) {
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
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String firstName,String lastName,int contactNumber, String address, String jobStatus, int monthlyIncome)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.firstName,firstName);
        values.put(Constants.lastName,lastName);
        values.put(Constants.contactNumber,contactNumber);
        values.put(Constants.address,address);
        values.put(Constants.jobStatus,jobStatus);
        values.put(Constants.monthlyIncome,monthlyIncome);

        long check=db.insert(TABLE_EMPLOYEE,null,values);
        if (check==-1)  //check returns -1 as data don't insert
        {
            return false;

        }

        else
        {
            return true;
        }

    }

    public boolean updateData(int id, String firstName,String lastName,int contactNumber, String address, String jobStatus, int monthlyIncome)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.firstName,firstName);
        values.put(Constants.lastName,lastName);
        values.put(Constants.contactNumber,contactNumber);
        values.put(Constants.address,address);
        values.put(Constants.jobStatus,jobStatus);
        values.put(Constants.monthlyIncome,monthlyIncome);

        long check=db.update(TABLE_EMPLOYEE,values,Constants.id+" = ?",new String[] {String.valueOf(id)});
        if (check==-1){return false;}
        else{ return true;}
    }

    public ArrayList<HashMap<String, String>> display()
    {
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
        }
        return list;

    }



    public int deleteData(String id)
    {
        SQLiteDatabase db=getWritableDatabase();

        return db.delete(TABLE_EMPLOYEE,Constants.id+" = ?",new String[] {id});
    }

    public void backup(Context context, String outFileName) {

        //database path
        final String inFileName = context.getDatabasePath("database.db").getAbsolutePath();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(context, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(Context context, String inFileName) {

        final String outFileName = context.getDatabasePath("database.db").toString();


        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            FileOutputStream output = new FileOutputStream(outFileName);

            if (dbFile.exists()) {
                copyFile(fis, output,context);
            }

        } catch (Exception e) {
            Toast.makeText(context, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            Log.e("exception",e.toString());
        }
    }

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile, Context context) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
            Toast.makeText(context, "Imported successfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Log.e("exception",e.toString());
            Toast.makeText(context, "Unable to import database. Retry." + e, Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

}
