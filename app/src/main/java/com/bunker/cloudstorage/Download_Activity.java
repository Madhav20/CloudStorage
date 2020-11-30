package com.bunker.cloudstorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Download_Activity extends AppCompatActivity
{
    ListView listView;
    DatabaseReference reference;
    ArrayList<UploadFile> uploadFiles;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        listView=findViewById(R.id.listView);

        reference= FirebaseDatabase.getInstance().getReference("Files");
        uploadFiles=new ArrayList<UploadFile>();

        pd=new ProgressDialog(this);
        pd.setMessage("Downloading...");
        pd.setCancelable(false);
        pd.show();
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                     UploadFile obj=snapshot.getValue(UploadFile.class);
                    uploadFiles.add(obj);
                }
                MyAdapter adapter=new MyAdapter(Download_Activity.this,uploadFiles);
                listView.setAdapter(adapter);
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });



    }
}
