package app.decathlon.inventoryreader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Device;
import entity.ResultGetDevices;
import restfullwebservice.RestfulWeb;


public class ListAllDevices extends Activity {
	ResultGetDevices resultGetDevices;
	private ProgressDialog dialog = null;
	private List<Device> listOfDevices;
	private List<Device> searchDevices;
	private AccountAdapter devicesAdapter;
	private NewAdapter newAdapter;
	private ListView listView;
	private EditText inputSearch;
	private ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_all_devices);

		listView = (ListView) findViewById(R.id.listofdevices);
		inputSearch = (EditText) findViewById(R.id.searchBox);
		searchDevices = new ArrayList<Device>();

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				
				// When user changed the Text
				System.out.println("entra en cambiar texto");
				searchDevices.clear();
				for (Device d : listOfDevices) {
					if (d.getNameDevice() != null
							&& d.getNameDevice().contains(cs)) {
						System.out.println("entra en a�adir");
						searchDevices.add(d);
						System.out.println("device: " + d.getNameDevice());
					} // something here
				}
				
				
				if (searchDevices != null) {
					newAdapter = new NewAdapter(getBaseContext(),
							R.layout.devices_list_inventory, searchDevices);
					listView.setAdapter(newAdapter);
					newAdapter.notifyDataSetChanged();
					System.out.println("lista: " + searchDevices);
				}else{
					devicesAdapter = new AccountAdapter(getBaseContext(),
							R.layout.devices_list_inventory, listOfDevices);
					listView.setAdapter(devicesAdapter);
					devicesAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		//new GetAllDevices().execute();
		GetAllDevices();
	}

	class AccountAdapter extends ArrayAdapter<Device> {

		Context context;

		AccountAdapter(Context context, int resource, List<Device> objects) {
			super(context, resource, objects);
			this.context = context;
		}

		@SuppressLint("ViewHolder")
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			View row = View.inflate(context, R.layout.devices_list_inventory,
					null);

			// Recorrremos la lista de lidins para crear las filas del ListView
			if (position < listOfDevices.size()) {
				// Procedemos a recuperar y mostrar todos los datos
				// pertenecientes al Lidin
				final TextView nameDevice = (TextView) row
						.findViewById(R.id.nameDevice);
				final TextView ipDevice = (TextView) row
						.findViewById(R.id.ipDevice);
				final TextView serialnDevice = (TextView) row
						.findViewById(R.id.serialnDevice);
				final TextView placeDevice = (TextView) row
						.findViewById(R.id.placeDevice);
				final TextView sectorDevice = (TextView) row
						.findViewById(R.id.sectorDevice);
				final TextView lastUpdateDevice = (TextView) row
						.findViewById(R.id.lastUpdateDevice);

				nameDevice.setText(listOfDevices.get(position).getNameDevice());
				ipDevice.setText(listOfDevices.get(position).getIpDevice());
				serialnDevice.setText(listOfDevices.get(position)
						.getSerialDevice());
				placeDevice.setText(listOfDevices.get(position)
						.getPlaceDevice());
				sectorDevice.setText(listOfDevices.get(position).getModel());
				lastUpdateDevice.setText(listOfDevices.get(position)
						.getLastUpdate());
				row.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(parent.getContext(),
								"Has pulsado en: " + nameDevice.getText(),
								Toast.LENGTH_SHORT).show();
						Intent myIntent = new Intent(ListAllDevices.this,
								DeviceActivity.class);
						myIntent.putExtra("device", nameDevice.getText()); // Optional
																			// parameters
						ListAllDevices.this.startActivity(myIntent);
					}
				});

			}

			return (row);
		}
	}
	
	class NewAdapter extends ArrayAdapter<Device> {

		Context context;

		NewAdapter(Context context, int resource, List<Device> objects) {
			super(context, resource, objects);
			this.context = context;
		}

		@SuppressLint("ViewHolder")
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			View row = View.inflate(context, R.layout.devices_list_inventory,
					null);

			// Recorrremos la lista de lidins para crear las filas del ListView
			if (position < searchDevices.size()) {
				// Procedemos a recuperar y mostrar todos los datos
				// pertenecientes al Lidin
				final TextView nameDevice = (TextView) row
						.findViewById(R.id.nameDevice);
				final TextView ipDevice = (TextView) row
						.findViewById(R.id.ipDevice);
				final TextView serialnDevice = (TextView) row
						.findViewById(R.id.serialnDevice);
				final TextView placeDevice = (TextView) row
						.findViewById(R.id.placeDevice);
				final TextView sectorDevice = (TextView) row
						.findViewById(R.id.sectorDevice);
				final TextView lastUpdateDevice = (TextView) row
						.findViewById(R.id.lastUpdateDevice);

				nameDevice.setText(searchDevices.get(position).getNameDevice());
				ipDevice.setText(searchDevices.get(position).getIpDevice());
				serialnDevice.setText(searchDevices.get(position)
						.getSerialDevice());
				placeDevice.setText(searchDevices.get(position)
						.getPlaceDevice());
				sectorDevice.setText(searchDevices.get(position).getModel());
				lastUpdateDevice.setText(searchDevices.get(position)
						.getLastUpdate());
				row.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(parent.getContext(),
								"Has pulsado en: " + nameDevice.getText(),
								Toast.LENGTH_SHORT).show();
						Intent myIntent = new Intent(ListAllDevices.this,
								DeviceActivity.class);
						myIntent.putExtra("device", nameDevice.getText()); // Optional
																			// parameters
						ListAllDevices.this.startActivity(myIntent);
					}
				});

			}

			return (row);
		}
	}

	public void GetAllDevices(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Cargando Dispositivos! Por favor espere unos segundos...!");
		progressDialog.show();

		StringRequest stringRequest = new StringRequest(Request.Method.GET, RestfulWeb.HTTP_REST_GETALLDEVICES,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject obj = new JSONObject(response);

							resultGetDevices = convertResponse(obj);
							printScores(resultGetDevices);
							Toast.makeText(ListAllDevices.this, obj.getString("status"), Toast.LENGTH_SHORT).show();
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


	private void printScores(ResultGetDevices resultGetDevices) {
		// TODO Auto-generated method stub
		if (resultGetDevices != null) {
			listOfDevices = resultGetDevices.getDevice();
			devicesAdapter = new AccountAdapter(getBaseContext(), R.layout.devices_list_inventory, listOfDevices);
			listView.setAdapter(devicesAdapter);
			devicesAdapter.notifyDataSetChanged();
		}
	}

	public void showAlertDialogError() {
		// TODO Auto-generated method stub
		Context context = getApplicationContext();
		CharSequence text = "Error de conexi�n con el Servidor. Por favor pruebe m�s tarde!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	public ResultGetDevices convertResponse(JSONObject jsonObj){
		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);

		try {


			// Primero miramos el status.
			String status = jsonObj.getString(RestfulWeb.TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				List<Device> devices = new ArrayList<Device>();
				if(jsonObj.getString(RestfulWeb.TAG_STATUS).equals("ok")){


					//Tablet
					if(!jsonObj.isNull(RestfulWeb.TAG_TABLET)){
						JSONArray deviceArray2 = jsonObj.getJSONArray(RestfulWeb.TAG_TABLET);
						if(deviceArray2 !=null){
							for (int i = 0; i <deviceArray2.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray2.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//ReaderRFID
					if(!jsonObj.isNull(RestfulWeb.TAG_READER_RFID)){
						JSONArray deviceArray3 = jsonObj.getJSONArray(RestfulWeb.TAG_READER_RFID);
						if(deviceArray3 !=null){
							for (int i = 0; i <deviceArray3.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray3.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//TabletPrinter
					if(!jsonObj.isNull(RestfulWeb.TAG_TAB_PRINTER)){
						JSONArray deviceArray12 = jsonObj.getJSONArray(RestfulWeb.TAG_TAB_PRINTER);
						if(deviceArray12 !=null){
							for (int i = 0; i <deviceArray12.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray12.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//Battery
					if(!jsonObj.isNull(RestfulWeb.TAG_BATTERY)){
						JSONArray deviceArray = jsonObj.getJSONArray(RestfulWeb.TAG_BATTERY);
						if(deviceArray !=null){
							for (int i = 0; i <deviceArray.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//MC
					if(!jsonObj.isNull(RestfulWeb.TAG_MC)){
						JSONArray deviceArray4 = jsonObj.getJSONArray(RestfulWeb.TAG_MC);
						if(deviceArray4 !=null){
							for (int i = 0; i <deviceArray4.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray4.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//VocalPrinter
					if(!jsonObj.isNull(RestfulWeb.TAG_VOCAL_PRINTER)){
						JSONArray deviceArray5 = jsonObj.getJSONArray(RestfulWeb.TAG_VOCAL_PRINTER);
						if(deviceArray5 !=null){
							for (int i = 0; i <deviceArray5.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray5.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}


					//Helmet
					if(!jsonObj.isNull(RestfulWeb.TAG_HELMET)){
						JSONArray deviceArray1 = jsonObj.getJSONArray(RestfulWeb.TAG_HELMET);
						if(deviceArray1 !=null){
							for (int i = 0; i <deviceArray1.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray1.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//ThinClient
					if(!jsonObj.isNull(RestfulWeb.TAG_TC)){
						JSONArray deviceArray6 = jsonObj.getJSONArray(RestfulWeb.TAG_TC);
						if(deviceArray6 !=null){
							for (int i = 0; i <deviceArray6.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray6.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//Desktop
					if(!jsonObj.isNull(RestfulWeb.TAG_DESKTOP)){
						JSONArray deviceArray7 = jsonObj.getJSONArray(RestfulWeb.TAG_DESKTOP);
						if(deviceArray7 !=null){
							for (int i = 0; i <deviceArray7.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray7.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//Laptop
					if(!jsonObj.isNull(RestfulWeb.TAG_LAPTOP)){
						JSONArray deviceArray8 = jsonObj.getJSONArray(RestfulWeb.TAG_LAPTOP);
						if(deviceArray8 !=null){
							for (int i = 0; i <deviceArray8.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray8.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//TecPrinter
					if(!jsonObj.isNull(RestfulWeb.TAG_TEC_PRINTER)){
						JSONArray deviceArray9 = jsonObj.getJSONArray(RestfulWeb.TAG_TEC_PRINTER);
						if(deviceArray9 !=null){
							for (int i = 0; i <deviceArray9.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray9.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//PaperPrinter
					if(!jsonObj.isNull(RestfulWeb.TAG_PRINTER_PAPER)){
						JSONArray deviceArray10 = jsonObj.getJSONArray(RestfulWeb.TAG_PRINTER_PAPER);
						if(deviceArray10 !=null){
							for (int i = 0; i <deviceArray10.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray10.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

					//Embitrolley
					if(!jsonObj.isNull(RestfulWeb.TAG_EMBITROLLEY)){
						JSONArray deviceArray11 = jsonObj.getJSONArray(RestfulWeb.TAG_EMBITROLLEY);
						if(deviceArray11 !=null){
							for (int i = 0; i <deviceArray11.length(); i++){
								Device device = new Device();
								device.setIdDevice(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_ID)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setNameDevice(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_NAME)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setIpDevice(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_IP)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setSerialDevice(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_SERIAL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setPlaceDevice(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_PLACE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setModel(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_MODEL)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastUpdate(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_DATE)
										.getBytes("ISO-8859-1"), "UTF-8"));
								device.setLastInventory(new String(deviceArray11.getJSONObject(i).getString(RestfulWeb.TAG_DATE_INVENTORY)
										.getBytes("ISO-8859-1"), "UTF-8"));
								devices.add(device);
							}
							resultDevices.setError(0);
							resultDevices.setDevice(devices);
						}
					}

				}
			}
		}  catch (IOException e) {
			Log.e("IOException", e.getMessage());
			resultDevices.setError(2);
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
			resultDevices.setError(2);
		} catch (Exception e) {
			Log.e("Exception", e.getMessage());
			resultDevices.setError(2);
		}

		return resultDevices;
	}
}
