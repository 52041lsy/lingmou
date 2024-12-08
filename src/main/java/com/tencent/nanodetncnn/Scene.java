package com.tencent.nanodetncnn;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.util.concurrent.ListenableFuture;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.embedding.EmbeddingApiResponse;
import com.zhipu.oapi.service.v4.embedding.EmbeddingRequest;
import com.zhipu.oapi.service.v4.file.FileApiResponse;
import com.zhipu.oapi.service.v4.file.QueryFileApiResponse;
import com.zhipu.oapi.service.v4.file.QueryFilesRequest;
import com.zhipu.oapi.service.v4.fine_turning.*;
import com.zhipu.oapi.service.v4.image.CreateImageRequest;
import com.zhipu.oapi.service.v4.image.ImageApiResponse;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Scene extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            android.Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private TTS tts;


    private PreviewView previewView;
    private Button captureButton;
    private Button returnButton;
    private TextView textView;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private static final String FILENAME_FORMAT = "yyyyMMdd_HHmmss";

    private final static Logger logger = LoggerFactory.getLogger(Scene.class);
    private static final String API_SECRET_KEY = "8be0d19c33286b16dd75e652daed122e.ivFJXGzbOKx6dJiv";


    private static final ClientV4 client = new ClientV4.Builder(API_SECRET_KEY)
            .enableTokenCache()
            .networkConfig(300, 100, 100, 100, TimeUnit.SECONDS)
            .connectionPool(new okhttp3.ConnectionPool(8, 1, TimeUnit.SECONDS))
            .build();

    // 请自定义自己的业务id
    private static final String requestIdTemplate = "mycompany-%d";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.text);
        //Toast.makeText(TextRec.this, "Welcome!", Toast.LENGTH_SHORT).show();

        tts = new TTS(this, Locale.CHINA);

        previewView = findViewById(R.id.previewView);
        captureButton = findViewById(R.id.buttonClick);
        returnButton = findViewById(R.id.buttonBack);
        textView = findViewById(R.id.text_view);


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Scene.this, "!!!!!!", Toast.LENGTH_SHORT).show();
                takePicture();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tts.isSpeaking()){
                    tts.stop();
                }
                tts.shutdown();
                Intent intent = new Intent(Scene.this, Main.class);
                startActivity(intent);
            }
        });

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }


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

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

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

                    cameraProvider.bindToLifecycle(Scene.this, cameraSelector, preview, imageCapture);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Use case binding failed", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePicture() {
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) {
            return; // 如果 imageCapture 是 null，直接返回
        }
        SimpleDateFormat filenameFormat = new SimpleDateFormat(FILENAME_FORMAT, Locale.US);
        String name = filenameFormat.format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }
        Uri name_n = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Create output options object which contains file + metadata
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
                        Toast.makeText(Scene.this, msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                        try {
                            //describe(output.getSavedUri());
                            String url=output.getSavedUri().toString();
                            File img=new File(url);
                            uploadImage(img);
                            //testImageToWord(url);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

    }


    //图生文
    public void testImageToWord(String imguri) throws JsonProcessingException {
        Toast.makeText(Scene.this, "run", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ChatMessage> messages = new ArrayList<>();
                    List<Map<String, Object>> contentList = new ArrayList<>();

                    Map<String, Object> textMap = new HashMap<>();
                    textMap.put("type", "text");
                    textMap.put("text", "请用生动的语言详细描述这个场景。如果这是一个交通场景，请详细地描述红绿灯的情况、行人的情况和车辆的情况。如果这是一个大自然的场景，请用更加生动活泼的语气回答。回答以“你的面前有……”开头");

                    Map<String, Object> typeMap = new HashMap<>();
                    typeMap.put("type", "image_url");

                    Map<String, Object> urlMap = new HashMap<>();
                    //urlMap.put("url", "https://img.picgo.net/2024/11/15/_202411151609194ac2945d2117a005.jpg");
                    urlMap.put("url", imguri);
                    typeMap.put("image_url", urlMap);

                    contentList.add(textMap);
                    contentList.add(typeMap);

                    ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), contentList);
                    messages.add(chatMessage);

                    String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

                    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                            .model(Constants.ModelChatGLM4V)
                            .stream(Boolean.FALSE)
                            .invokeMethod(Constants.invokeMethod)
                            .messages(messages)
                            .requestId(requestId)
                            .temperature(0.9f)
                            .build();

                    // 调用API
                    ModelApiResponse modelApiResponse = client.invokeModelApi(chatCompletionRequest);

                    // 处理响应
                    logger.info("model output: {}", mapper.writeValueAsString(modelApiResponse));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String rootNode=mapper.writeValueAsString(modelApiResponse);
                                /*textView.setText(rootNode);*/
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    JsonNode root = mapper.readTree(rootNode);
                                    JsonNode data = root.path("data");
                                    JsonNode choices = data.path("choices");
                                    if (choices.isArray() && choices.size() > 0) {
                                        JsonNode message = choices.get(0).path("message");
                                        String content = message.path("content").asText();
                                        textView.setText(content);
                                        if(tts.isSpeaking()){
                                            tts.stop();
                                        }
                                        tts.speak(content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                } catch (JsonProcessingException e) {
                    logger.error("JSON processing error", e);
                } catch (Exception e) {
                    logger.error("Error during API call", e);
                }
            }
        }).start();
    }

    public void uploadImage(File imageFile) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.helloimg.com/api/v1/upload/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        // 添加文件部分
        builder.addFormDataPart("image", imageFile.getName(),
                RequestBody.create(MediaType.parse("application/octet-stream"), imageFile));
        // 构建RequestBody对象
        RequestBody requestBody = builder.build();

        // 创建文件的RequestBody
        //RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        // 创建MultipartBody.Part
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        // 调用Retrofit接口
        ApiService.Upload service = retrofit.create(ApiService.Upload.class);
        Call<Boolean> call = service.upload(imagePart);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null) {
                    try {
                        String res_body = response.body().toString();
                        JSONObject jsonObject = new JSONObject(res_body);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject links = data.getJSONObject("links");
                        String imageUrl = links.getString("url");
                        testImageToWord(imageUrl);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(Scene.this, "Fail to connect1", Toast.LENGTH_SHORT).show();
            }
        });
    }

}







