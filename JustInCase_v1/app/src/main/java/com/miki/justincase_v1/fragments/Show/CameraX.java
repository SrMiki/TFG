package com.miki.justincase_v1.fragments.Show;


import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraX extends BaseFragment {

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private ImageCapture imageCapture = null;

    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    File outputDirectory;
    private Uri saveUri;
    ExecutorService cameraExecutor;
    private PreviewView previewViewCamera;

    String itemName = "";
    private String operation;

    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera, container, false);
        previewViewCamera = view.findViewById(R.id.PreviewViewCamera);

        bundle = getArguments();
        if (bundle != null) {
            itemName = (String) bundle.getSerializable("itemName");
            operation = (String) bundle.getSerializable("itemOperation");
        }

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // Set up the listener for take photo button
        ImageButton camera_capture_button = view.findViewById(R.id.camera_capture_button);
        camera_capture_button.setOnClickListener(v -> takePhoto());

        outputDirectory = getOutputDirectory();
        cameraExecutor = Executors.newSingleThreadExecutor();

        return view;
    }

    private void takePhoto() {
        ImageCapture rImageCapture = imageCapture;
        // Get a stable reference of the modifiable image capture use case
        String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
        File photoFile = new File(outputDirectory, new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        rImageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        saveUri = Uri.fromFile(photoFile);
                        String msg = getString(R.string.photoCapure);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        bundle.putSerializable("itemPhotoUri", saveUri.toString());
                        bundle.putSerializable("itemOperation", operation);
                        bundle.putSerializable("itemName", itemName);
                        getNav().navigate(R.id.fragment_ShowItems, bundle);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                });
    }
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(() -> {
            try {
                // Camera provider is now guaranteed to be available
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                // Set up the view finder use case to display camera preview
                bindCamera(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get()
                // shouldn't block since the listener is being called, so no need to
                // handle InterruptedException.
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private void bindCamera(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();


        // Set up the capture use case to allow users to take photos
        imageCapture = new ImageCapture.Builder()
                .build();

        // Select back camera as a default
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        // Unbind use cases before rebinding
        cameraProvider.unbindAll();
        // Attach use cases to the camera with the same lifecycle owner
        cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture);

        // Connect the preview use case to the previewView
        preview.setSurfaceProvider(
                previewViewCamera.getSurfaceProvider());
    }

    private File getOutputDirectory() {
        File[] externalMediaDirs = getActivity().getExternalMediaDirs();
        File externalMediaDir = externalMediaDirs[0];
        if (externalMediaDir == null) {
            File ejemploCamara = new File(externalMediaDir, "JustInCase");
            ejemploCamara.mkdirs();
        } else {
            File nulo = new File("", "JustInCase");
        }

        if (externalMediaDir != null && externalMediaDir.exists()) {
            return externalMediaDir;
        } else {
            return getActivity().getFilesDir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getContext(),
                        getString(R.string.toast_warning_cameraPermissions),
                        Toast.LENGTH_SHORT).show();
                getNav().navigate(R.id.fragment_ShowItems);
            }
        } else {
            getNav().navigate(R.id.fragment_ShowItems);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission :
                REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private File createFile(File outputDirectory, String filename, String photoExtension) {
        return new File(outputDirectory, new SimpleDateFormat(filename, Locale.ROOT)
                .format(System.currentTimeMillis()) + photoExtension);
    }
}