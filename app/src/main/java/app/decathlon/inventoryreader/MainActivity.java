package app.decathlon.inventoryreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import entity.SharedPrefManager;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import restfullwebservice.RestfulWeb;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Button btn;
    private Button list;
    private boolean cameraActivated;
    private ProgressDialog progressDialog;

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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveTokenGenerated();
            }
        },10000);
    }

    public void saveTokenGenerated(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        final String email = "itteamprat@gmail.com";

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestfulWeb.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
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
