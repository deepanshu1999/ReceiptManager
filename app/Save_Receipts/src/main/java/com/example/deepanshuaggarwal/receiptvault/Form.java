package com.example.deepanshuaggarwal.receiptvault;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

public class Form extends Activity {
    ImageView icon,im;
    EditText date,warranty,seller,comment;
    AutoCompleteTextView category;
    Button imb;
    Uri resultUri;
    String rno=null;
    TextView terror;
    public static String mpath;
    RelativeLayout ropy;
    RelativeLayout rl1;
     ArrayAdapter<String> adapter;
    String[] cats={"Electronics","Groceries","Clothes","Footwears","Furniture","Books and Stationary","Kitchen Accesories","Fashion","Others",""};
    public static void delimage(Uri imageuri){
         File fdelete = new File(imageuri.getPath());
         if (fdelete.exists()) {
             if (fdelete.delete()) {
                 System.out.println("file Deleted :" );
             } else {
                 System.out.println("file not Deleted :");
             }
         }
     }
    private Bitmap loadImageFromStorage(String path,String filename) {

        try {
            File f=new File(path, filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage,String filename){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir",Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private String pathget(String filename){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir",Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,filename);
        mpath=mypath.getAbsolutePath();
        return mpath;
    }
    public void datepicking(View view){
        showDialog(1);
    }
    public String getReceiptNumber(){
        SharedPreferences sharedPreferences=getSharedPreferences("ReceiptNumber",MODE_PRIVATE);
        int k=sharedPreferences.getInt("Current",-1);
        if(k==-1){
            sharedPreferences.edit().putInt("Current",1).apply();
            return "RVAULT1";
        }
        else{

            sharedPreferences.edit().putInt("Current",k+1).apply();
            return "RVAULT"+Integer.valueOf(k+1).toString();
        }
    }
    public boolean checkreqfield(){
        boolean bool=true;
        if(category.getText().length()==0){
            bool=false;
            terror.setVisibility(View.VISIBLE);

            //category.getBackground().mutate().setColorFilter(getResources().getColor(R.color.WArning), PorterDuff.Mode.SRC_ATOP);
        }return bool;}
        public boolean checkallfields(){
        boolean bool=true;
        if(seller.getText().length()==0){
            bool=false;
            seller.setText("NO DATA");
        }
        if(warranty.getText().length()==0){
            bool=false;
            warranty.setText("0");
        }
        if(comment.getText().length()==0){
            bool=false;
            comment.setText("NO DATA");
        }
        if(date.getText().length()==0){
            date.setHint("*Date Required");
            date.getBackground().mutate().setColorFilter(getResources().getColor(R.color.WArning), PorterDuff.Mode.SRC_ATOP);
            bool=false;
            date.setHintTextColor(getResources().getColor(R.color.WArning));

        }

            return bool;

    }
    public void monthpick(String s){
        String[] mon=new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        ArrayList<String> arr=new ArrayList<>();
        arr.addAll(Arrays.asList(mon));
       globals.month=arr.indexOf(s)+1;



    }
    public String monthget(int i){
        String[] mon=new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        ArrayList<String> arr=new ArrayList<>();
        arr.addAll(Arrays.asList(mon));
        return arr.get(i);


    }
    DatePickerDialog.OnDateSetListener dpicklistener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            globals.year=i;
            globals.month=i1;
            Log.i("month",Integer.toString(globals.month));
            globals.day=i2;
            date.setText(Integer.toString(globals.day)+"-"+monthget(globals.month)+"-"+Integer.toString(globals.year));
        }
    };
   TextWatcher tv3=new TextWatcher()
    {
        @Override
        public void afterTextChanged(Editable mEdit)
        {
            cats[9] = mEdit.toString();adapter.notifyDataSetChanged();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        public void onTextChanged(CharSequence s, int start, int before, int count){}
    };

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if(id==1)
            return new DatePickerDialog(Form.this,dpicklistener,globals.year,globals.month,globals.day);
else
    return null;
    }
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Yaha","Yahi a gya");
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Picasso.get().load(resultUri).fit().into(icon);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        icon=findViewById(R.id.icon);
        rl1=findViewById(R.id.fullayout);

        terror=findViewById(R.id.error);
        final Uri imageuri=getIntent().getParcelableExtra("Image");


        CropImage.activity(imageuri)
                .start(this);


            //final Bitmap image=getIntent().getParcelableExtra("Image");

            String result="Failed";


        date=findViewById(R.id.date);
        category=findViewById(R.id.category);
        warranty=findViewById(R.id.warranty);
        seller=findViewById(R.id.seller);
        comment=findViewById(R.id.comment);
        imb=findViewById(R.id.submitform);
            String dateStr = "04/05/2010";

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            globals.day=Integer.parseInt(formattedDate.substring(0,2));
            monthpick(formattedDate.substring(3,6));
            globals.year=Integer.parseInt(formattedDate.substring(7,11));
            date.setText(formattedDate);
        final String imagelink=result;
        adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, cats);
            //Set the number of characters the user must type before the drop down list is shown
            category.setThreshold(1);
            //Set the adapter
            category.setAdapter(adapter);
            category.addTextChangedListener(tv3);
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkreqfield()==true) {
                    rno=getReceiptNumber();
                    final AlertDialog db=new AlertDialog.Builder(Form.this,R.style.DialogeTheme).create();
                   // db.setTitle("NAME");
                   checkallfields();
                    View convertView;
                    LayoutInflater inflater = (LayoutInflater) Form.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //  MainActivity mainActivity = new MainActivity();
                    convertView = inflater.inflate(R.layout.builderforrename,null, false);
                   final EditText ed=convertView.findViewById(R.id.jat2);
                    ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                db.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            }
                            else
                                db.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    });
                   final Button bt1=convertView.findViewById(R.id.katsave);
                   bt1.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           String kk=ed.getText().toString();
                           SQLiteDatabase db = openOrCreateDatabase("ReceiptData", MODE_PRIVATE, null);
                           if(DatabaseHelper.getprodexists(kk,db)==true){
                               ed.clearComposingText();
                               ed.setText("");
                               ed.setHint("*Name Already Exists");
                               ed.setHintTextColor(getResources().getColor(R.color.WArning));
                               //Get button in layout
                           }
                           else if(kk.length()==0){
                               ed.setHint("*Required Field");
                               ed.setHintTextColor(getResources().getColor(R.color.WArning));
                           }
                           else{

                               final String filename=kk+".jpg";
                               Bitmap bitmap = null;
                               try {
                                   bitmap = MediaStore.Images.Media.getBitmap(Form.this.getContentResolver() ,resultUri);
                                  icon.setImageBitmap(bitmap);
                               } catch (IOException e) {
                                   e.printStackTrace();
                                   Toast.makeText(Form.this,"Image cant be stored",Toast.LENGTH_SHORT).show();
                               }
                               final String k=saveToInternalStorage(bitmap,filename);
                              delimage(resultUri);


                               DatabaseHelper.addrow(kk, category.getText().toString(), date.getText().toString(), Integer.parseInt(warranty.getText().toString()), seller.getText().toString(), comment.getText().toString(), k, filename, db, imagelink);
                               Toast.makeText(Form.this, "Receipt Added Successfully", Toast.LENGTH_SHORT).show();
                               Intent ij = new Intent(Form.this, MainInterface.class);
                               Form.this.startActivity(ij);
                               }
                       }

                   });
                   db.setCanceledOnTouchOutside(true);

                   final Button bt2=convertView.findViewById(R.id.katcancel);
                   bt2.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {db.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                           db.dismiss();
                       }
                   });
                   ed.setText(rno);
                    db.setView(convertView);
                    db.show();


                }





            }
        });

    }
}

//Wow I got it