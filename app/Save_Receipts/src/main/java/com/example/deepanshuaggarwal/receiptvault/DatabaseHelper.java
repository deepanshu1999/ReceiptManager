package com.example.deepanshuaggarwal.receiptvault;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.Console;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Deepanshu Aggarwal on 29-03-2018.
 */

public class DatabaseHelper{
    public String DATABASE="ReceiptData";
    String TABLENAME;
    String COL1="RNO";
    String COL2="CATEGORY";
    String COL3="DATE";
    String COL4="WARRANTY";
    String COL5="SELLER";
    String COL6="COMMENT";
    String COL7="IMAGEPATH";
    String COL8="IMAGENAME";
    String COL9="IMAGELINK";
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    EditText date,category,warranty,seller,comment;
    ImageButton imb;
    public  static boolean isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public DatabaseHelper(String TABLENAME, SQLiteDatabase db) {
        this.TABLENAME = TABLENAME;

        String query="CREATE TABLE IF NOT EXISTS "+TABLENAME+"(RNO VARCHAR PRIMARY KEY,CATEGORY VARCHAR,DATE varchar,WARRANTY int(5),SELLER varchar,COMMENT varchar,IMAGEPATH varchar,IMAGENAME varchar,IMAGELINK varchar)";
        Log.i("TABLEQUERY",query);
        db.execSQL(query);

    }
    public static void addrow(String rno,String category, String date,int warranty,String seller,String comment,String imagepath,String imagename,SQLiteDatabase db,String imagelink){

        String query="INSERT INTO Receipt(RNO,CATEGORY,DATE,WARRANTY,SELLER,COMMENT,IMAGEPATH,IMAGENAME,IMAGELINK) VALUES('"+rno+"','"+category+"','"+date+"',"+Integer.valueOf(warranty).toString()+",'"+seller+"','"+comment+"','"+imagepath+"','"+imagename+"','"+imagelink+"')";
        Log.i("ADDROW",query);
        db.execSQL(query);}
    public ArrayList<Receipt> Getall(SQLiteDatabase db){
    Cursor c=db.rawQuery("select * from "+TABLENAME,null);
        ArrayList<Receipt> arr=new ArrayList<>();
        Receipt r=new Receipt("","","","","",0,"","","");
        c.moveToFirst();
        while(c!=null){
            r.rno=c.getString(c.getColumnIndex("RNO"));
            r.warranty=c.getInt(c.getColumnIndex("WARRANTY"));
            r.date=c.getString(c.getColumnIndex("DATE"));
            r.category=c.getString(c.getColumnIndex("CATEGORY"));
            r.seller=c.getString(c.getColumnIndex("seller"));
            r.imagepath=c.getString(c.getColumnIndex("IMAGEPATH"));
            r.imagename=c.getString(c.getColumnIndex("IMAGENAME"));
            r.comment=c.getString(c.getColumnIndex("COMMENT"));
            r.imagelink=c.getString(c.getColumnIndex("IMAGELINK"));
            arr.add(r);
            c.moveToNext();
        }

        return arr;
    }
    public static void modrow(Receipt r,SQLiteDatabase db){
        String sk="UPDATE Receipt set CATEGORY='"+r.category+"',WARRANTY='"+r.warranty+"',SELLER='"+r.seller+"',COMMENT='"+r.comment+"' WHERE RNO='"+r.rno+"'";
        db.execSQL(sk);
    }


    public static void delrow(Receipt r,SQLiteDatabase db){
        String ss="DELETE FROM Receipt WHERE RNO='"+r.rno+"'";
        db.execSQL(ss);
    }

    public static boolean getprodexists(String id,SQLiteDatabase sqlDB) {

        Cursor DB_cursor = null;

        try
        {
            String DB_query = "SELECT * FROM Receipt WHERE RNO='" + id + "';";
            DB_cursor = sqlDB.rawQuery(DB_query, null);

            if (DB_cursor.moveToFirst())
            {
                DB_cursor.close();
                return true;
            }
            else
            {
                DB_cursor.close();
                return false;
            }
        }
        catch (Exception ex)
        {
            DB_cursor.close();
            String DB_message = ex.getMessage();
            return false;
        }
    }

}
