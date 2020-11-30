package com.bunker.cloudstorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    EditText editText;
    ImageView imageView;
    Button browse,upload,download;
    DatabaseReference ref;
    Uri filePath;
    StorageReference storageReference;
    Aes256Class aes256;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        imageView=findViewById(R.id.imageView);
        browse=findViewById(R.id.browse_button);
        upload=findViewById(R.id.upload_button);
        download=findViewById(R.id.download_button);

        browse.setOnClickListener(this);
        upload.setOnClickListener(this);
        download.setOnClickListener(this);


        ref=FirebaseDatabase.getInstance().getReference("Files");
        storageReference= FirebaseStorage.getInstance().getReference();

       // aes256 = new Aes256Class();
    }

    @Override
    public void onClick(View view)
    {
        if(view==browse)
        {
            selectPicture();
        }
        if (view==upload)
        {
            uploadPicture();
        }

        if (view==download)
        {
        Intent intent=new Intent(MainActivity.this,Download_Activity.class);
        startActivity(intent);
        }
        else
        {

        }
    }

    private void uploadPicture()
    {
        if(filePath !=null)
        {
           final ProgressDialog pd=new ProgressDialog(MainActivity.this);
            pd.setTitle("Upload Image");
            pd.setMessage("Please Wait!");
            pd.show();

             StorageReference str=storageReference.child("files/"+System.currentTimeMillis()
                    +"."+getFileExtension(filePath));
            UploadTask uploadTask=str.putFile(filePath);

            //System.currentTimeMillis() is used to set the name of the file on the cloud.


            //user ko notify krne ke lia
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // also use this statement
                    Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                    //
                    // Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String name=editText.getText().toString();
                            //byte[] encryptedString = aes256.makeAes(name.getBytes(), Cipher.ENCRYPT_MODE);

                            String url=uri.toString();
                            UploadFile file=new UploadFile();
                            Log.e("String  ",name);
                            //Log.e("encryptedString ",new String(encryptedString));
                            //Toast.makeText(MainActivity.this, ""+url, Toast.LENGTH_SHORT).show();
                            file.setName(name);
                            file.setUrl(url);

                            String id=ref.push().getKey();
                            ref.child(id).setValue(file);
                            Toast.makeText(MainActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {

                    pd.setMessage("Uploading "+(100.0*taskSnapshot.getBytesTransferred())/
                            taskSnapshot.getTotalByteCount()+" %");

                }
            });

        }
        else
            Toast.makeText(this, "Please Select Picture", Toast.LENGTH_SHORT).show();



    }

    //jab cloud par file save hogi toh vo kisi b format mai ho skti hai toh islya hume extension
    // provide krni pdti hai jisse mismatch na ho
    //or
    //agar humne extension nhi provide kra toh hume pta ni chlega kis type ki value aaegi humare pas

    private String getFileExtension(Uri uri)
    {
        //kisi dusre app sa data lene ke lia hum contentresolver use krte hai
        ContentResolver cr=getContentResolver();

        //ye ek design pattern ke lia use hota hai
        MimeTypeMap mime=MimeTypeMap.getSingleton();

        //ye file mai sa extension nikal kr deta hai
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void selectPicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        //intent.setType("text/plain");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        if (requestCode==0&&data!=null&& resultCode==RESULT_OK)
        {
             filePath =data.getData();
             imageView.setImageURI(filePath);

        }
        else
            Toast.makeText(this, "Please Select", Toast.LENGTH_SHORT).show();

        super.onActivityResult(requestCode, resultCode, data);
    }

}
