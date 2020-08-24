package com.example.permissions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageView img;
    private RelativeLayout parent;
    public static final int CAMERA_CODE= 299;
    public static final int RESULT_CODE= 399;
    public static final int SETTING_CODE= 499;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn= findViewById(R.id.btn);
        img= findViewById(R.id.img);
        parent= findViewById(R.id.parent);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePermissions();
            }
        });

    }

    public void handlePermissions()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
            openCamera();
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                showSnackBar();
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
            }
        }
    }

    public void openCamera()
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_CODE);
    }

    public void showSnackBar()
    {
        Snackbar.make(parent, "Camera permission is required to take a picture. Please! allow.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:"+getPackageName()));
                        startActivityForResult(intent, SETTING_CODE);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case RESULT_CODE:
                if(resultCode== RESULT_OK && data!=null)
                {
                    Bundle bundle= data.getExtras();
                    Bitmap bitmap= (Bitmap) bundle.get("data");
                    img.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case CAMERA_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                }
                else
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}