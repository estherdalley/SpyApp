package com.example.estherdalley.spyapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Camera cameraObject;
    private ShowCamera showCamera;
    private ImageView pictureTaken;
    private MediaRecorder recorder;
    private String folder_main = "RecordData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureTaken = (ImageView) findViewById(R.id.imageView);
        cameraObject = isCameraAvailable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(showCamera);
// Make directory called RecordData if it doesn't already exist
        File f = new File(Environment.getExternalStorageDirectory(),
                folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        String path = Environment.getExternalStorageDirectory().toString();
// enter the following code statement all on one line
        String filename = path + "/" + folder_main + "/" + String.format("%d.3gp",
                System.currentTimeMillis());
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filename);
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        recorder.start(); // Recording is now started
    }

    public static Camera isCameraAvailable() {
        Camera object = null;
        try {
            object = Camera.open();
        } catch (Exception e) {
        }
        return object;
    }

    private Camera.PictureCallback captureMedia = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
                pictureTaken.setImageBitmap(bitmap);
                // Make directory called RecordData if it doesn't already exist
                File f = new File(Environment.getExternalStorageDirectory(),
                        folder_main);
                if (!f.exists()) {
                    f.mkdirs();
                }
                String path = Environment.getExternalStorageDirectory().toString();
// enter the following code statement all on one line
                String filename = path + "/" + folder_main + "/" + String.format("%d.jpg",
                        System.currentTimeMillis());

                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(filename);
                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
            ///cameraObject.release();
            cameraObject.startPreview();
        }
    };

    public void captureImage (View view) {
        cameraObject.takePicture(null, null, captureMedia);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.stop();
        recorder.reset();
        recorder.release();
    }



}
