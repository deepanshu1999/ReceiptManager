package com.example.deepanshuaggarwal.receiptvault;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<Receipt> {
    RelativeLayout rel;
    private ArrayList<Receipt> list = new ArrayList<>();
    private int layoutRes;

    public MyCustomAdapter(@NonNull Context context, int resource, @NonNull List<Receipt> objects) {
        super(context, resource, objects);
        this.layoutRes = resource;
        this.list.addAll(objects);
        Log.i("Tag",list.toString());

    }
    public static String Converter(String a){
        Log.e("Coverting..",a);
        int month=Integer.parseInt(a.substring(3,5));
        String monthp="";
        switch (month){
            case 1: monthp="Jan";
            break;
            case 2: monthp="Feb";
            break;
            case 3: monthp="Mar";
            break;
            case 4: monthp="Apr";
            break;
            case 5: monthp="May";
            break;
            case 6: monthp="Jun";
            break;
            case 7: monthp="Jul";
            break;
            case 8: monthp="Aug";
            break;
            case 9: monthp="Sep";
            break;
            case 10: monthp="Oct";
            break;
            case 11: monthp="Nov";
            break;
            case 12: monthp="Dec";

        }
        return monthp+" "+a.substring(0,2)+", "+a.substring(6,10);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Receipt getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //  convertView = getLayoutInflater().inflate(layoutRes,parent,false);
        final Context context = getContext();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  MainActivity mainActivity = new MainActivity();
            convertView = inflater.inflate(layoutRes, parent, false);
        }

        final ImageView imageview = convertView.findViewById(R.id.imageid);
        TextView tv1, tv2;
        tv1 = convertView.findViewById(R.id.receiptnum);
        tv2 = convertView.findViewById(R.id.datenum);



        final Receipt holder = getItem(position);
        Bitmap bp = null;

        Picasso.get().load("file:" + holder.imagepath + "/" + holder.imagename).into(imageview);
      /*  try {
            Uri file = Uri.fromFile(new File(holder.imagepath + "/" + holder.imagename));
            bp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), file);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                DownloadImage downloadImage = new DownloadImage();
                bp = downloadImage.doInBackground(holder.imagelink);
            } catch (Exception eie) {
                eie.printStackTrace();
            }
        }
        final Bitmap bitmap = bp;
        if (bitmap == null) {
            Log.e("BITMAP+NULL", "Worw");
        }
        //imageview.setImageBitmap(bitmap);
imageview.setImageBitmap(MainInterface.rotate(holder));*/

        tv1.setText(holder.rno);
        Log.i("TAgi",holder.date.toString());
        tv2.setText(holder.date);

        Log.d("GOT ADDED", holder.rno + "   " + holder.comment);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                MainInterface.slider(holder,bitmap);
            }
        });

convertView.setLongClickable(true);
return  convertView;
    }
}