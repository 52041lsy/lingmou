package com.tencent.nanodetncnn;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.view.SurfaceHolder;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class TextRec extends AppCompatActivity {
    /*读文功能实现*/
    //权限申请：摄像头、存储空间
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private PreviewView previewView;
    private Button captureButton;
    private Button returnButton;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private static final String FILENAME_FORMAT = "yyyyMMdd_HHmmss";
    private TextView textView;
    private TTS tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.text);
        //Toast.makeText(TextRec.this, "Welcome!", Toast.LENGTH_SHORT).show();

        //初始化tts
        tts = new TTS(this, Locale.ENGLISH);

        //绑定页面原件
        previewView = findViewById(R.id.previewView);
        captureButton = findViewById(R.id.buttonClick);
        returnButton=findViewById(R.id.buttonBack);
        textView=findViewById(R.id.text_view);

        //设置按钮监听：点击则拍照
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(TextRec.this, "!!!!!!", Toast.LENGTH_SHORT).show();
                takePicture();
            }
        });

        //设置按钮监听：点击则返回
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tts.isSpeaking()){
                    tts.stop();
                    tts.shutdown();
                }
                Intent intent = new Intent(TextRec.this, Main.class);
                startActivity(intent);
            }
        });

        //若权限已取得，启动摄像头
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    //权限相关
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //开启摄像头及预览画面
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider>  cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    imageCapture = new ImageCapture.Builder().build();
                    CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(TextRec.this, cameraSelector, preview, imageCapture);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Use case binding failed", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    //文字识别全流程实现
    private void takePicture() {
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) {
            return; // 如果 imageCapture 是 null，直接返回
        }
        //文件保存相关
        SimpleDateFormat filenameFormat = new SimpleDateFormat(FILENAME_FORMAT, Locale.US);
        String name = filenameFormat.format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }
        Uri name_n=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(this.getContentResolver(),
                name_n, contentValues).build();

        // 拍照
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "Photo capture failed: " + exception.getMessage(), exception);
                    }

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                        String msg = "Photo capture succeeded: " + output.getSavedUri();
                        Toast.makeText(TextRec.this, msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                        try {
                            recognizeText(output.getSavedUri());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

    }
    //文字识别
    private void recognizeText(Uri imageUri) throws IOException {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromFilePath(this, imageUri);
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        StringBuilder res=new StringBuilder();
                        for (Text.TextBlock block : text.getTextBlocks()) {
                            String blockText = block.getText();
                            res.append(blockText).append("  ");
                            //Toast.makeText(TextRec.this,blockText,Toast.LENGTH_LONG).show();
                        }
                        //将文字显示在页面上
                        textView.setText(res.toString());
                        if(tts.isSpeaking()){
                            tts.stop();
                        }
                        //将识别出的文字转语音
                        tts.speak(res.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textView.setText("识别失败: " + e.getMessage());
                        // 处理错误
                    }
                });
    }
}