package leila.tabletverwaltung;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;


public class ReaderActivity extends AppCompatActivity {
    private ImageView ivBorder;
    private TextView tvBarcodeResult;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    private static boolean barcodeDetected = false;
    //  TestBranch comment
    //  Test2
    //  Test2.1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBarcodeResult = (TextView)findViewById(R.id.tvBarcodeResult);
        ivBorder = (ImageView)findViewById(R.id.ivBorder);
        cameraView = (SurfaceView)findViewById(R.id.camera_view);

        initCamera();
    }




    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ACTION", "STOP");
        cameraSource.stop();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ACTION", "RESTART");
        initCamera();
    }



    private static boolean cameraFocus(CameraSource cameraSource, String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        Camera.Parameters params = camera.getParameters();
                        params.setFocusMode(focusMode);
                        camera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return false;
    }


    private void initCamera(){
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    barcodeDetector = new BarcodeDetector.Builder(ReaderActivity.this).setBarcodeFormats(
                            Barcode.QR_CODE | Barcode.DATA_MATRIX | Barcode.EAN_8 | Barcode.EAN_13 | Barcode.UPC_A | Barcode.UPC_E |Barcode.CODE_128 | Barcode.ITF | Barcode.CODE_39
                    ).build();
                    cameraSource = new CameraSource.Builder(ReaderActivity.this, barcodeDetector).setRequestedPreviewSize(cameraView.getWidth(), cameraView.getHeight()).build();

                    barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                        @Override
                        public void release() {

                        }

                        @Override
                        public void receiveDetections(Detector.Detections<Barcode> detections) {
                            if(barcodeDetected) return;
                            final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                            //  Barcode wurde erkannt
                            if(barcodes.size() != 0){
                                Log.e("BARCODE", barcodes.valueAt(0).displayValue);

                                barcodeDetected = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvBarcodeResult.setText(barcodes.valueAt(0).displayValue);
                                        ivBorder.setImageResource(R.drawable.barcode_border_box_green);
                                        Vibrator v = (Vibrator)ReaderActivity.this.getSystemService(VIBRATOR_SERVICE);
                                        v.vibrate(500);
                                    }
                                });
                            }
                        }
                    });

                    cameraSource.start(cameraView.getHolder());
                    cameraFocus(cameraSource, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }
}
