package com.uw.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
    }

    public void uploadButtonClick(View view) {

       try {

           // 1. Create a reference
           StorageReference storageRef = FirebaseStorage.getInstance().getReference();
           StorageReference lineaMiniRef = storageRef.child("images/linea_mini.jpg");

           // 2. Convert image from Drawable resource to a byte stream
           Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.linea_mini);
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
           byte[] lineaMiniByteStream = baos.toByteArray();

           // 3. Start upload task
           UploadTask uploadTask = lineaMiniRef.putBytes(lineaMiniByteStream);
           uploadTask.addOnFailureListener((exception) -> {
               // handle unsuccessful uploads
           }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Log.i("ImageUpload", "Image successfully uploaded to Firebase.");
               }
           });

       } catch (Exception e) {
           e.printStackTrace();
           Log.i("Error", "Image upload failed");
       }
    }

    public void downloadButtonClick(View view) {

        // 1. Create a reference to the object uploaded
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference liniMiniRef = storageReference.child("images/linea_mini.jpg");

        // 2. Get ImageView object
        final ImageView imageView = findViewById(R.id.imageView);
        final long ONE_MEGABYTE = 1024*1024;

        // 3. Download the image into a byte stream
        liniMiniRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // 4. Data for "images/linea_mini.jpg" is returned - get this into a bitmap object
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // 5. Set the image in imageView
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.i("Error", "Image Download failed.");
            }
        });
    }
}
