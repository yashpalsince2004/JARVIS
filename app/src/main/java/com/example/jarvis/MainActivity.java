package com.example.jarvis;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jarvis.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ai.picovoice.porcupine.PorcupineManager;
import ai.picovoice.porcupine.PorcupineException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PorcupineManager porcupineManager;
    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CPU_ABI", "Device ABI: " + Build.SUPPORTED_ABIS[0]);

        checkMicrophonePermission(); // ✅ Request permission

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "Requesting microphone permission...");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        } else {
            Log.d("Permission", "Microphone permission already granted.");
            startWakeWordDetection();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Permission granted, starting wake word detection...");
                startWakeWordDetection();
            } else {
                Log.e("Permission", "Microphone permission denied!");
                Toast.makeText(this, "Microphone permission required for wake word detection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getKeywordFilePath(String fileName) {
        File file = new File(getFilesDir(), fileName); // ✅ Changed from getCacheDir() to getFilesDir()
        if (!file.exists()) {
            try (InputStream is = getAssets().open(fileName);
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
            } catch (IOException e) {
                Log.e("Porcupine", "Error copying keyword file: " + fileName, e);
                return null;
            }
        }
        return file.getAbsolutePath();
    }

    private String getModelFilePath(String fileName) {
        File file = new File(getFilesDir(), fileName); // ✅ Store in files directory
        if (!file.exists()) {
            try (InputStream is = getAssets().open(fileName);
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
            } catch (IOException e) {
                Log.e("Porcupine", "Error copying model file: " + fileName, e);
                return null;
            }
        }
        return file.getAbsolutePath();
    }

    private void startWakeWordDetection() {
        try {
            String keywordPath = getKeywordFilePath("jarvis.ppn");
            String modelPath = getModelFilePath("porcupine_params.pv");

            Log.d("Porcupine", "Keyword Path: " + keywordPath);
            Log.d("Porcupine", "Model Path: " + modelPath);

            if (keywordPath == null || modelPath == null) {
                Log.e("Porcupine", "Keyword or model file not found!");
                Toast.makeText(this, "Keyword or model file not found!", Toast.LENGTH_LONG).show();
                return;
            }

            porcupineManager = new PorcupineManager.Builder()
                    .setAccessKey("Fo2qq4WMKCbgh4dAr6lb3PcSMwo8sMMuwDuth7zSiVEQqYfsqp5IhQ==") // ⚠️ Replace with a valid access key
                    .setKeywordPath(keywordPath)
                    .setModelPath(modelPath)
                    .setSensitivity(0.7f)
                    .build(getApplicationContext(), keywordIndex -> {
                        if (keywordIndex == 0) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Wake Word Detected!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });

            porcupineManager.start();
            Log.d("Porcupine", "Porcupine started successfully.");

        } catch (PorcupineException e) {
            Log.e("Porcupine", "Porcupine failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
