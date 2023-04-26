package com.example.shtreet;

import android.Manifest;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Date;


public class TextRecognitionActivity  extends AppCompatActivity {

    ImageView imageview1;
    String uriString = "";
    private Uri uri;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("uri", uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) uri = savedInstanceState.getParcelable("uri");
        setContentView(R.layout.activity_text_recognition);

        //Get all the buttons
        Button pickFileBtn = findViewById(R.id.pickFilebtn);
        Button extractTextBtn = findViewById(R.id.extarctTextbtn);
        Button takePictureBtn = findViewById(R.id.takePicturebtn);

        //Get the image view
        imageview1 = findViewById(R.id.imageView);
        //Ask for permission to use the camera
        EnableCameraPermission();

        pickFileBtn.setOnClickListener(v -> mGetContent.launch("image/*"));

        extractTextBtn.setOnClickListener(v -> {
            if (imageview1.getDrawable() != null) {
                Intent intent = new Intent(TextRecognitionActivity.this, ResultActivity.class);
                intent.putExtra("uri", uriString);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Veuillez choisir une image", Toast.LENGTH_SHORT).show();
            }

        });


        takePictureBtn.setOnClickListener(v -> {
            File file = new File(getExternalFilesDir(null), String.valueOf(new Date().getTime()));
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            activityResultCamera.launch(uri);
        });

    }

    /**
     * Activity to pick a file from the gallery
     */
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageview1.setImageURI(uri);
                    uriString = uri.toString();
                }
            });

    /**
     * Activity to take a picture with the camera
     */
    ActivityResultLauncher<Uri> activityResultCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    getIntent().putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    if (result) {
                        imageview1.setImageDrawable(null);
                        imageview1.setImageURI(uri);
                        uriString = uri.toString();
                    }
                }
            });

    /**
     * Ask for the permission to use the camera to the user.
     */
    public void EnableCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(TextRecognitionActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(TextRecognitionActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(TextRecognitionActivity.this,new String[]{
                    Manifest.permission.CAMERA}, 1);
        }
    }
}


