package com.example.camera2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 100;
    final String TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 0;
    File file;
    FileOutputStream fileoutputstream;
    ByteArrayOutputStream bytearrayoutputstream;
    private boolean isWork = false;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        bytearrayoutputstream = new ByteArrayOutputStream();
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_CAMERA);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
// извлекаем изображение
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);

            imageView.setImageBitmap(thumbnailBitmap);
            file = new File( Environment.getExternalStorageDirectory() + "/SampleImage.png");
            try

            {
                file.createNewFile();
                fileoutputstream = new FileOutputStream(file);
                fileoutputstream.write(bytearrayoutputstream.toByteArray());
                fileoutputstream.close();
                Log.d(TAG, file.getAbsolutePath());
            }

            catch (Exception e)

            {

                e.printStackTrace();

            }
        }
    }

    public void imageViewOnClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            Log.d(TAG, String.valueOf(grantResults[0]) + " " + String.valueOf(grantResults[1]));
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission granted
                isWork = true;
            } else {
                isWork = false;
            }
        }

    }
}