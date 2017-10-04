package app.num.barcodescannerproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;


public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private Button btn;
    private Button list;
    private boolean cameraActivated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn = (Button) findViewById(R.id.qrscan_btn);
        list = (Button) findViewById(R.id.devices_btn);
        Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/angrybirds.ttf");
        btn.setTypeface(face);
        list.setTypeface(face);
    }

    public void launchQRScanner(View v) {
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);// Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        cameraActivated = true;
    }
    
    public void getAllDevices(View v) {
    	Intent intent = new Intent(MainActivity.this,ListAllDevices.class);
        startActivity(intent);
    }


    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode
        /*
		 * switch (requestCode) { case ZBAR_SCANNER_REQUEST: case
		 * ZBAR_QR_SCANNER_REQUEST:
		 */
        String str = rawResult.getText();
        str = str.trim();
        // str= str.trim(); // lo reinicia a un valor sin caracteres
        // prohibidos.
        Intent myIntent = new Intent(MainActivity.this, DeviceActivity.class);
        myIntent.putExtra("device", str); //Optional parameters
        MainActivity.this.startActivity(myIntent);
        Log.w("qrinv1", "ha leido: " + str + " valor typeQR: " );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.removeAllViews(); //<- here remove all the views, it will make an Activity having no View
                mScannerView.stopCamera(); //<- then stop the camera
                resetView();
            }
        }, 100);
    }

    public void onBackPressed(){
        if(!cameraActivated){
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            resetView();
        }

    }

    public void resetView(){
        cameraActivated = false;
        mScannerView.stopCamera();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
