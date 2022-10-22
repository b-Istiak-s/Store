package com.example.projecy.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PdfCreator {

    public void createPDF(Context context, String[] array, ArrayList<HashMap<String, String>> data,String header){


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int pageWidth = 1400;
        int pageHeight = 1400;
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(context.getResources().getColor(android.R.color.holo_blue_bright));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText(header, pageWidth / 2, 60, paint);

        paint.setTextSize(13);
        canvas.drawText(day+"/"+month+"/"+year+" "+hour+":"+minute+":"+second,pageWidth/2,90,paint);


        paint.setColor(context.getResources().getColor(android.R.color.black));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(15);

        int x=40;
        for(int i=0; i<=array.length-1; i++) {
            if (array[i].equals("quantity_of_product") && header.equals("Sales")){
                canvas.drawText("product_sold", x, 110, paint);
            }else {
                canvas.drawText(array[i], x, 110, paint);
            }
            if (i!=array.length-1) {
                canvas.drawLine(x + 130, 100, x + 130, 120, paint);
            }
            if (!header.equals("Sales"))
                x=x+170;
            else
                x=x+140;
        }

        int x1=40;
        int y1=145;
        int page_number = 1;
        String startCanvas1 = "canvas";
        PdfDocument.Page page1 = null;
        Canvas canvas1 = null;
        for (int i=0; i<=data.size()-1;i++){
            if (y1>1380){
                if (startCanvas1.equals("canvas")){
                    pdfDocument.finishPage(page);
                }else{
                    pdfDocument.finishPage(page1);
                }
                PdfDocument.PageInfo pageInfo1 = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, page_number+1).create();
                page1 = pdfDocument.startPage(pageInfo1);
                canvas1 = page1.getCanvas();
                startCanvas1="canvas"+page_number;
                page_number=page_number+1;
                y1=20;
            }else if (startCanvas1.equals("canvas")){
                canvas.drawText(i + 1 + ".", x1 - 30, y1, paint);
            }
            else {
                canvas1.drawText(i + 1 + ".", x1 - 30, y1, paint);
            }
            for (int j=0; j<=array.length-1;j++){
                if (startCanvas1.equals("canvas")) {
                    canvas.drawText(data.get(i).get(array[j]), x1, y1, paint);
                }else{
                    canvas1.drawText(data.get(i).get(array[j]), x1, y1, paint);
                }
                if(j!=array.length-1) {
                    if (!header.equals("Sales"))
                        x1=x1+170;
                    else
                        x1=x1+140;
                }else{
                    x1=40;
                }
            }
            y1=y1+20;
        }
        if(startCanvas1.equals("canvas")){
            pdfDocument.finishPage(page);
        }else{
            pdfDocument.finishPage(page1);
        }

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "/"+header+"_"+year+"_"+month+"_"+day+"_"+hour+"_"+minute+"_"+second+ ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF saved successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();


    }
}
