package com.example.deepanshuaggarwal.receiptvault;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainInterface extends AppCompatActivity implements IPickResult{
    RelativeLayout rl;
    static ImageView imr;
    ImageView imdel;
    private ProgressBar bar;
    ImageView i1,i2,i3;
    static android.support.design.widget.FloatingActionButton fscan;
    static TextView t1,t2,t3,t4,t5;
    static RelativeLayout rl1;
    static GridView lv1;
    static Bitmap rl1_ki_image;
    static Receipt rl1_ki;
    TextView tv2;
    MyCustomAdapter customAdapter;
    ArrayList<Receipt> arr = new ArrayList<>();
    String mCurrentPhotoPath,m_imagePath;
    Uri photoURI;
    Bitmap bitmap = null;
    private String pathget(String filename){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir",Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,filename);
        Form.mpath=mypath.getAbsolutePath();
        return Form.mpath;
    }
    public static void slider(Receipt holder,Bitmap bp){
    rl1.setVisibility(View.VISIBLE);
    lv1.setVisibility(View.INVISIBLE);
    fscan.setVisibility(View.INVISIBLE);


    rl1_ki_image=bp;
    rl1_ki=holder;


    imr.setImageBitmap(bp);
    t1.setText("Category: "+holder.category);
    t2.setText("Warranty: "+holder.warranty);
    t3.setText("Seller: "+holder.seller);
    t4.setText("Product Description: "+holder.comment);
    t5.setText(holder.date);


}
    public void dowheni0(){
        lv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.VISIBLE);
    }
    public void dowheni1(){
        lv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPickResult(PickResult r) {
        bar.setVisibility(View.VISIBLE);
        if (r.getError() == null) {
            Intent i=new Intent(MainInterface.this,Form.class);
            i.putExtra("Image",r.getUri());
            MainInterface.this.startActivity(i);
            } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("status","I Got started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);
        rl1=findViewById(R.id.viewpanel);
        t1=findViewById(R.id.categoryview);
        bar=findViewById(R.id.progressBarDialog);
        t2=findViewById(R.id.warrantyview);
        i1=findViewById(R.id.cancellor);
        imdel=findViewById(R.id.deletion);
        imdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = openOrCreateDatabase("ReceiptData", MODE_PRIVATE, null);
                DatabaseHelper.delrow(rl1_ki,db);
                Intent i=getIntent();
                MainInterface.this.startActivity(i);
            }
        });
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl1.setVisibility(View.INVISIBLE);
                lv1.setVisibility(View.VISIBLE);
                fscan.setVisibility(View.VISIBLE);


            }
        });
        i2=findViewById(R.id.sharing);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon =rl1_ki_image;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));


            }
        });
        i3=findViewById(R.id.editrr);

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bd=new AlertDialog.Builder(MainInterface.this);
                bd.setTitle("Edit");
                View convertView;
                LayoutInflater inflater = (LayoutInflater) MainInterface.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //  MainActivity mainActivity = new MainActivity();
                convertView = inflater.inflate(R.layout.editor,null, false);
                final EditText tt0,t00,t000,t0000;
                tt0=convertView.findViewById(R.id.naman0);
                Log.i("RL!",rl1_ki.category);
                tt0.setText(rl1_ki.category);
                t00=convertView.findViewById(R.id.naman1);
                t00.setText(Integer.toString(rl1_ki.warranty));
                t000=convertView.findViewById(R.id.naman2);
                t000.setText(rl1_ki.seller);
                t0000=convertView.findViewById(R.id.naman3);
                t0000.setText(rl1_ki.comment);
                bd.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get Entries
                        String category,seller,comment;
                        int warranty;
                        category=tt0.getText().toString();
                        warranty=Integer.parseInt(t00.getText().toString());
                        seller=t000.getText().toString();
                        comment=t0000.getText().toString();
                        Receipt r=rl1_ki;
                        r.category=category;
                        r.seller=seller;
                        r.warranty=warranty;
                        r.comment=comment;
                        SQLiteDatabase db = openOrCreateDatabase("ReceiptData", MODE_PRIVATE, null);
                        DatabaseHelper.modrow(r,db);
                        Intent ti=getIntent();
                        MainInterface.this.startActivity(ti);
                        slider(r,rl1_ki_image);

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                    }
                });
                bd.setView(convertView).show();
            }
        });

        //Make another Activity for edit
        t3=findViewById(R.id.sellerview);
        t4=findViewById(R.id.commentview);
        t5=findViewById(R.id.dateview);
        imr=findViewById(R.id.viewer);
        fscan=findViewById(R.id.fabscan);
        lv1=findViewById(R.id.grid);
        lv1.setVisibility(View.INVISIBLE);
        rl=findViewById(R.id.mainrelaly);
        tv2=findViewById(R.id.watermark);
        String Name=getIntent().getStringExtra("Name");
        fscan.setImageResource(R.drawable.camy);

        fscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickImageDialog.build(new PickSetup()).show(MainInterface.this);

            }
        });
        Camera.CameraInfo ap=new Camera.CameraInfo();
        Log.i("Orientation",Integer.toString(ap.orientation));

        final SQLiteDatabase db=openOrCreateDatabase("ReceiptData",MODE_PRIVATE,null);
        // db.execSQL("drop table Receipt");
        DatabaseHelper databaseHelper=new DatabaseHelper("Receipt",db);



        //Start BAckground TAsk instead
        //DatabaseHelper.GetFailedTask(db,MainInterface.this);
        Cursor c = db.rawQuery("select * from Receipt", null);

        //LOLP!!
        c.moveToFirst();
        int i=c.getCount();
        if(i==0){
            dowheni0();
        }
        else
            dowheni1();
        while(i>0){
            Receipt r = new Receipt("", "", "", "", "", 0, "", "", "");
            r.rno=c.getString(0);
            Log.e("RNOS",r.rno);
            // (RNO,CATEGORY,DATE,WARRANTY,SELLER,COMMENT,IMAGEPATH,IMAGENAME,IMAGELINK)
            r.category=c.getString(1);
            r.date=c.getString(2);
            r.warranty=c.getInt(3);
            r.seller=c.getString(4);
            r.comment=c.getString(5);
            r.imagepath=c.getString(6);
            r.imagename=c.getString(7);
            r.imagelink=c.getString(8);
            arr.add(r);
            c.moveToNext();
            i--;
        }
        c.close();
        //Log.e("TAGViewer",arr.get(1).comment);
        customAdapter=new MyCustomAdapter(MainInterface.this,R.layout.listforview,arr);
        // MAke important ones first

        lv1.setAdapter(customAdapter);




    }
}
