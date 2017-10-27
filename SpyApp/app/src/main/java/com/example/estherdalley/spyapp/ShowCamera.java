package com.example.estherdalley.spyapp;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;



class ShowCamera {
    public ShowCamera(MainActivity mainActivity, Camera cameraObject) {

    extends SurfaceView implements SurfaceHolder.Callback;

        super(context);
        theCamera = camera;
        holdMe = getHolder();
        holdMe.addCallback(this);
    }
    private SurfaceHolder holdMe;
    private Camera theCamera;
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        } catch (IOException ignored) {
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (theCamera != null) {
            theCamera.stopPreview();
            theCamera.release();
            theCamera = null;
        }
    }

    public SurfaceHolder getHolder() {
        return holder;
    }

}
}
