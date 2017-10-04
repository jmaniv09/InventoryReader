package app.num.barcodescannerproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;


public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
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

        String str = rawResult.getText();
        str = str.trim();

        Intent myIntent = new Intent(MainActivity.this, DeviceActivity.class);
        myIntent.putExtra("device", str); //Optional parameters
        MainActivity.this.startActivity(myIntent);
        Log.w("qrinv1", "ha leido: " + str + " valor typeQR: " );
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
