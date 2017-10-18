package app.decathlon.inventoryreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import entity.Device;
import restfullwebservice.RestfulWeb;


public class DeviceActivity extends Activity {
	private String nameDevice;
	private String ipDevice;
	private String serialnDevice;
	private String placeDevice;
	private String str = null;
	private String deviceStatus;
	private String deviceModels;
	private String deviceObserv;
	private String[] status;
	private String[] models;
	private EditText deviceName;
	private EditText deviceIP;
	private EditText deviceSerial;
	private EditText devicePlace;
	private EditText lastDateInventory;
	private EditText observation;
	private ProgressDialog dialog = null;
	private Device resultGetDevice;
	private Button updateDevice;
	private Button registerDevice;
	private Spinner modelDevice;
	private Spinner statusDevice;
	private ArrayAdapter<String> dataAdapter;
	private ArrayAdapter<String> dataAdapter2;
	private int position;

	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nameDevice = extras.getString("device");
		}

		deviceName = (EditText) findViewById(R.id.eNameDevice);
		deviceIP = (EditText) findViewById(R.id.eIpDevice);
		deviceSerial = (EditText) findViewById(R.id.eSerialDevice);
		devicePlace = (EditText) findViewById(R.id.ePlaceDevice);
		lastDateInventory = (EditText) findViewById(R.id.eLastInventory);
		observation = (EditText) findViewById(R.id.eObservation);
		updateDevice = (Button) findViewById(R.id.updateButton);
		registerDevice = (Button) findViewById(R.id.registerButton);
		modelDevice = (Spinner) findViewById(R.id.spinnerModel);
		statusDevice = (Spinner) findViewById(R.id.spinnerStatus);
		status = getResources().getStringArray(R.array.status_array);
		models = getResources().getStringArray(R.array.model_arrays);

		if (nameDevice != null && !nameDevice.isEmpty()) {
			GetDeviceByName();
		} else {
			updateDevice.setEnabled(false);
		}

		updateDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nameDevice = deviceName.getText().toString();
				ipDevice = deviceIP.getText().toString();
				serialnDevice = deviceSerial.getText().toString();
				placeDevice = devicePlace.getText().toString();
				deviceObserv = observation.getText().toString();
				
				if (nameDevice.equals("") | ipDevice.equals("")
						| serialnDevice.equals("") | placeDevice.equals("")
						| deviceStatus.equals("") | deviceModels.equals("")) {
					showError();
				} else {
					updateDevice();
				}
			}

		});

		registerDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nameDevice = deviceName.getText().toString();
				ipDevice = deviceIP.getText().toString();
				serialnDevice = deviceSerial.getText().toString();
				placeDevice = devicePlace.getText().toString();
				deviceObserv = observation.getText().toString();
				if (nameDevice.equals("") | ipDevice.equals("")
						| serialnDevice.equals("") | placeDevice.equals("")) {
					showError();
				} else {
					registerDevice();
				}
			}

		});

		//lista status
		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, status);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusDevice.setAdapter(dataAdapter);

		statusDevice.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				deviceStatus = String.valueOf(position);
				Toast.makeText(DeviceActivity.this, deviceStatus, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		//lista modelos
		dataAdapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, models);
		dataAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modelDevice.setAdapter(dataAdapter2);

		modelDevice.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				deviceModels = (String) modelDevice.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	protected void showError() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Todos los campos son obligatorios",
				Toast.LENGTH_SHORT).show();
	}



	public void GetDeviceByName(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Cargando Dispositivos! Por favor espere unos segundos...!");
		progressDialog.show();

		StringRequest stringRequest = new StringRequest(Request.Method.GET, RestfulWeb.HTTP_REST_INVENTORY
				+ "getDeviceInfo?token=" + RestfulWeb.TAG_TOKEN + "&nameDevice="
				+ nameDevice,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
                        Toast.makeText(DeviceActivity.this, response, Toast.LENGTH_LONG).show();
						progressDialog.dismiss();
						try {
							JSONObject obj = new JSONObject(response);
							resultGetDevice = getDevice(obj);
							Toast.makeText(DeviceActivity.this, obj.getString("status"), Toast.LENGTH_LONG).show();
							if (resultGetDevice != null) {
								deviceName.setText(resultGetDevice.getNameDevice());
								deviceIP.setText(resultGetDevice.getIpDevice());
								deviceSerial.setText(resultGetDevice.getSerialDevice());
								devicePlace.setText(resultGetDevice.getPlaceDevice());
								lastDateInventory.setText(resultGetDevice
										.getLastInventory());
								observation.setText(resultGetDevice.getObservation());

								lastDateInventory.setFocusable(false);
								lastDateInventory.setClickable(false);

								updateDevice.setEnabled(true);
								registerDevice.setEnabled(false);
								// int spinnerPosition =
								// dataAdapter.getPosition(resultGetDevice.getSector());

								// set the default according to value
								modelDevice.setSelection(getIndex(modelDevice,
										resultGetDevice.getModel()));
								statusDevice.setSelection(resultGetDevice.getStatus());

							}
								} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						showAlertDialogError();
					}
				}) {

		};

		MyVolley.getInstance(this).addToRequestQueue(stringRequest);
	}


	public void registerDevice(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Cargando Dispositivos! Por favor espere unos segundos...!");
		progressDialog.show();

		StringRequest stringRequest = new StringRequest(Request.Method.GET, RestfulWeb.HTTP_REST_INVENTORY
				+ "registDevice?token=" + RestfulWeb.TAG_TOKEN + "&nameDevice="
				+ nameDevice+ "&serialDevice="
				+ serialnDevice + "&ipDevice=" + ipDevice + "&locationDevice=" + placeDevice+ "&model=" + deviceModels + "&status=" + deviceStatus,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject obj = new JSONObject(response);
							Toast.makeText(DeviceActivity.this, obj.getString("status"), Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						showAlertDialogError();
					}
				}) {

		};

		MyVolley.getInstance(this).addToRequestQueue(stringRequest);
	}

	public void updateDevice(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Cargando Dispositivos! Por favor espere unos segundos...!");
		progressDialog.show();

		StringRequest stringRequest = new StringRequest(Request.Method.GET, RestfulWeb.HTTP_REST_INVENTORY + "updateDeviceInfo?token="
				+ RestfulWeb.TAG_TOKEN + "&nameDevice=" + nameDevice + "&serialDevice="
				+ serialnDevice + "&ipDevice=" + ipDevice + "&locationDevice=" + placeDevice + "&status=" + deviceStatus + "&observation=" + deviceObserv + "&model=" + deviceModels,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject obj = new JSONObject(response);
							Toast.makeText(DeviceActivity.this, obj.getString("status"), Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						showAlertDialogError();
					}
				}) {

		};

		MyVolley.getInstance(this).addToRequestQueue(stringRequest);
	}

	public Device getDevice(JSONObject jsonObj){
		Device device = new Device();
		device.setError(0);
		try {

			// Primero miramos el status.
			String status = jsonObj.getString(RestfulWeb.TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				device.setError(1);
				System.out.println(jsonObj.getString(RestfulWeb.TAG_MESSAGE));
				System.out.println("El dispositivo no existe");
			} else {
				System.out.println("Entra aqui");
				JSONObject dataObj = jsonObj.getJSONObject(RestfulWeb.TAG_DATA2);
				device.setIdDevice(new String(dataObj.getString(RestfulWeb.TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(dataObj.getString(RestfulWeb.TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(dataObj.getString(RestfulWeb.TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(dataObj.getString(RestfulWeb.TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(dataObj.getString(RestfulWeb.TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(dataObj.getString(RestfulWeb.TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(dataObj.getString(RestfulWeb.TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(dataObj.getString(RestfulWeb.TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setObservation(new String(dataObj.getString(RestfulWeb.TAG_OBSERVATION)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setStatus(Integer.parseInt(new String(dataObj.getString(RestfulWeb.TAG_STATUS_DEVICE)
						.getBytes("ISO-8859-1"), "UTF-8")));
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */

			}
		}  catch (IOException e) {
			Log.e("Error IOException", e.getMessage());
			device.setError(2);
		} catch (JSONException e) {
			Log.e("Error JSONException", e.getMessage());
			device.setError(2);
		} catch (Exception e) {
			Log.e("Error Exception", e.getMessage());
			device.setError(2);
		}

		return device;
	}


	public int getIndex(Spinner sectorOptions2, String sector) {
		// TODO Auto-generated method stub
		int index = 0;
		for (int i = 0; i < sectorOptions2.getCount(); i++) {
			if (sectorOptions2.getItemAtPosition(i).equals(sector)) {
				index = i;
			}
		}

		return index;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
	}
	public void onBackPressed(){

		finish();

	}

	public void showAlertDialogError() {
		// TODO Auto-generated method stub
		Context context = getApplicationContext();
		CharSequence text = "Error de conexión con el Servidor. Por favor pruebe más tarde!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}