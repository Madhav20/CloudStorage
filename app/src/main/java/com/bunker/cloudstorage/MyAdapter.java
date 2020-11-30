package com.bunker.cloudstorage;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.crypto.Cipher;

public class MyAdapter extends ArrayAdapter<String>
{
    Activity activity;
    ArrayList<UploadFile> arrayList;
    Aes256Class aes256;
    public MyAdapter(Activity activity, ArrayList arrayList)
    {
        super(activity,R.layout.custom_layout);
        this.activity=activity;
        this.arrayList=arrayList;
        // aes256 = new Aes256Class(1);
    }

    @Override
    public int getCount()
    {
        return arrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_layout,null);

        UploadFile obj=arrayList.get(position);
        ImageView imageView= view.findViewById(R.id.imageView);
        TextView textView=view.findViewById(R.id.textView);

        String url  = obj.getUrl();
        String name = obj.getName();
        /*System.out.println("size "+arrayList.size());
        for(int i = 0; i < arrayList.size();i++){
            UploadFile o = arrayList.get(i);
            Log.e("stringName ",o.getName());
        }*/
        /*byte[] enycString = name.getBytes();

        byte[] decodedString = aes256.makeAes(enycString, Cipher.DECRYPT_MODE);
*/


        textView.setText(name);

        Glide.with(activity).load(url).into(imageView);

        return view;
    }


}
