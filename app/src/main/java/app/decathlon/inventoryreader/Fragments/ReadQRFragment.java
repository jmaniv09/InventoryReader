package app.decathlon.inventoryreader.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import app.decathlon.inventoryreader.Activities.NavigationDrawer;
import app.decathlon.inventoryreader.DeviceActivity;
import app.decathlon.inventoryreader.ListAllDevices;
import app.decathlon.inventoryreader.MainActivity;
import app.decathlon.inventoryreader.MyVolley;
import app.decathlon.inventoryreader.R;
import entity.SharedPrefManager;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import restfullwebservice.RestfulWeb;

import static android.content.Intent.getIntent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadQRFragment extends Fragment implements ZXingScannerView.ResultHandler, NavigationDrawer.OnBackPressedListener {
    private ZXingScannerView mScannerView;
    private Button btn;
    private Button list;
    private boolean cameraActivated;
    private ProgressDialog progressDialog;

    public ReadQRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Designamos la vista
        View v = inflater.inflate(R.layout.fragment_read_qr, container, false);

        //Para poder realizar el onBackPressed();
        ((NavigationDrawer) getActivity()).setOnBackPressedListener(this);

        //Tipo de letra
        /*Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/angrybirds.ttf");
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/angrybirds.ttf");
        btn.setTypeface(face);
        list.setTypeface(face);*/

        //Elementos layout

        btn = (Button) v.findViewById(R.id.btn_scan);
        list = (Button) v.findViewById(R.id.devices_btn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveTokenGenerated();
            }
        },10000);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQRScanner(v);
            }
        });

        // Inflate the layout for this fragment
        return v;

    }

    public void saveTokenGenerated(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
        final String email = "itteamprat@gmail.com";

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestfulWeb.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //getactivity() or getActivity().getApplicationContext()????
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    ///LANZAMOS LA CAMARA PARA ESCANEAR EL QR
    public void launchQRScanner(View v) {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setAutoFocus(true);// Programmatically initialize the scanner view
        getActivity().setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        cameraActivated = true;
    }


    ////BOTON PARA VER TODOS LOS DISPOSITIVOS
    /*public void getAllDevices(View v) {
        Intent intent = new Intent(getActivity(),ListAllDevices.class);
        startActivity(intent);
    }*/


    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode

        String str = rawResult.getText();
        str = str.trim();

        Intent myIntent = new Intent(getActivity(), DeviceActivity.class);
        myIntent.putExtra("device", str); //Optional parameters
        ReadQRFragment.this.startActivity(myIntent);
        Log.w("qrinv1", "ha leido: " + str + " valor typeQR: " );
    }

/*//MODIFICAR, NO RECOJE EL METODO DE LA CLASE PADRE
    public void onBackPressed(){
        if(!cameraActivated){
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            resetView();
        }
    }*/

    ///ON BACK PRESSED WITH CALLBACK INTERFACE AT THE PRINCIPAL ACTIVITY
    @Override
    public void doBack() {
        if(!cameraActivated){
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            resetView();
        }
    }

    //REINICIAR LA VISTA Y PROCESO
    public void resetView(){
        cameraActivated = false;
        mScannerView.stopCamera();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
