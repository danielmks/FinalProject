package com.example.capture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private Camera camera = null;

    public CameraSurfaceView(Context context){
        super(context);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder){
        camera = Camera.open();
        setCameraOrientation();
        try{
            camera.setPreviewDisplay(mHolder);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        camera.startPreview();
    }


    public void surfaceDestroyed(SurfaceHolder holder){
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public boolean capture(Camera.PictureCallback handler){
        if(camera != null){
            camera.takePicture(null,null,handler);
            return true;
        }
        else {
            return false;
        }
    }

    public void setCameraOrientation(){
        if(camera == null){
            return;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0,info);

        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int rotation = manager.getDefaultDisplay().getRotation();

        int degrees = 0;
        switch(rotation){
            case Surface.ROTATION_0: degrees = 0;break;
            case Surface.ROTATION_90: degrees = 90;break;
            case Surface.ROTATION_180: degrees = 180;break;
            case Surface.ROTATION_270: degrees = 270;break;
        }
        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }
        else{
            result = (info.orientation -degrees +360) %360;
        }
        camera.setDisplayOrientation(result);
    }

}

public class SubActivity extends AppCompatActivity implements AutoPermissionsListener {
    CameraSurfaceView cameraView;
    ImageView imageView;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity_main);
        FrameLayout previewFrame = findViewById(R.id.previewFrame);
        imageView = findViewById(R.id.guideline);
        cameraView = new CameraSurfaceView(this);
        previewFrame.addView(cameraView);
        Button capture = findViewById(R.id.capture);

        animation = new AlphaAnimation(0.0f,1.0f);
        imageView.startAnimation(animation);

        capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              //  imageView.clearAnimation();
                takePicture();
                finish();
            }
        });

        animation.setDuration(100);
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);

        AutoPermissions.Companion.loadAllPermissions(this,101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions){
        Toast.makeText(this,"permissions Denied : "+permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions){
        Toast.makeText(this,"permissions Granted : "+permissions.length, Toast.LENGTH_LONG).show();
    }

    public void takePicture() {
        cameraView.capture(new Camera.PictureCallback(){
            public void  onPictureTaken(byte[] data, Camera camera) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    String outUriStr = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Captured  Image","Captured Image using Camera.");
                    if (outUriStr == null) {
                        Log.d("SampleCapture", "Image insert failed.");
                        return;
                    } else {
                        Uri outUri = Uri.parse(outUriStr);
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outUri);
                        sendBroadcast(intent);
                    }
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}