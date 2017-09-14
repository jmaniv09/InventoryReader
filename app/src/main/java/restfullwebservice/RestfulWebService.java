package restfullwebservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import entity.Device;
import entity.ResultGetDevices;

/**
 * Clase que proporciona la logica de negocio para interactuar con el servidor.
 */

public class RestfulWebService extends RestfulWeb {

	private static int TIME_OUT_CONNEXION = 0; // 12 segundos
	private static int TIME_OUT_SOCKET = 60000; // 12 segundos

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static Device getDeviceByName(String deviceName) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Device device = new Device();
		device.setError(0);

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY
				+ "getDeviceInfo?token=" + TAG_TOKEN + "&nameDevice="
				+ deviceName);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				device.setError(1);
				System.out.println(jsonObj.getString(TAG_MESSAGE));
				System.out.println("El dispositivo no existe");
			} else {
				System.out.println("Entra aqui");
				JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA2);
				device.setIdDevice(new String(dataObj.getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(dataObj.getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(dataObj.getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(dataObj.getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(dataObj.getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(dataObj.getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(dataObj.getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(dataObj.getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setObservation(new String(dataObj.getString(TAG_OBSERVATION)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setStatus(Integer.parseInt(new String(dataObj.getString(TAG_STATUS_DEVICE)
						.getBytes("ISO-8859-1"), "UTF-8")));
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */

			}
		} catch (ClientProtocolException e) {
			Log.e("Error CProtoException", e.getMessage());
			device.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("Error CTimeoutException", e.getMessage());
			device.setError(2);
		} catch (IOException e) {
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

	public static Device updateDevice(String deviceName, String serialNumber,
			String ip, String place, int statusDevice, String deviceModels, String deviceObserv) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			place = URLEncoder.encode(place, "UTF-8");
			deviceName = URLEncoder.encode(deviceName, "UTF-8");
			serialNumber = URLEncoder.encode(serialNumber, "UTF-8");
			ip = URLEncoder.encode(ip, "UTF-8");
			deviceModels = URLEncoder.encode(deviceModels, "UTF-8");
			deviceObserv = URLEncoder.encode(deviceObserv, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Device device = new Device();
		device.setError(0);

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "updateDeviceInfo?token="
				+ TAG_TOKEN + "&nameDevice=" + deviceName + "&serialDevice="
				+ serialNumber + "&ipDevice=" + ip + "&locationDevice=" + place + "&status=" + statusDevice + "&observation=" + deviceObserv + "&model=" + deviceModels);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				device.setError(1);
				System.out.println("El dispositivo no existe");
			} else {
				System.out.println("Entra aqui");
				String message = jsonObj.getString(TAG_STATUS);
				device.setMessage(message);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */

			}
		} catch (ClientProtocolException e) {
			Log.e("CProtException", e.getMessage());
			device.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			device.setError(2);
		} catch (IOException e) {
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

	public static Device registerDevice(String deviceName, String serialNumber,
			String ip, String place, int statusDevice, String deviceModels, String deviceObserv) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			place = URLEncoder.encode(place, "UTF-8");
			deviceName = URLEncoder.encode(deviceName, "UTF-8");
			serialNumber = URLEncoder.encode(serialNumber, "UTF-8");
			ip = URLEncoder.encode(ip, "UTF-8");
			deviceModels = URLEncoder.encode(deviceModels, "UTF-8");
			deviceObserv = URLEncoder.encode(deviceObserv, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Device device = new Device();
		device.setError(0);

		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY
				+ "registDevice?token=" + TAG_TOKEN + "&nameDevice="
				+ deviceName+ "&serialDevice="
				+ serialNumber + "&ipDevice=" + ip + "&locationDevice=" + place+ "&model=" + deviceModels + "&status=" + statusDevice);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				device.setError(1);
				System.out.println("El dispositivo no existe");
			} else {
				System.out.println("Entra aqui");
				String message = jsonObj.getString(TAG_STATUS);
				device.setMessage(message);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */

			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			device.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			device.setError(2);
		} catch (IOException e) {
			Log.e("IOException", e.getMessage());
			device.setError(2);
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
			device.setError(2);
		} catch (Exception e) {
			Log.e("Exception", e.getMessage());
			device.setError(2);
		}

		return device;
	}

	public static ResultGetDevices getAllDevices() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getAllDevices?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				List<Device> devices = new ArrayList<Device>();
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				
				
				//Tablet
				if(!jsonObj.isNull(TAG_TABLET)){
				JSONArray deviceArray2 = jsonObj.getJSONArray(TAG_TABLET);
				if(deviceArray2 !=null){
				for (int i = 0; i <deviceArray2.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray2.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray2.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray2.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray2.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray2.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray2.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray2.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray2.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//ReaderRFID
				if(!jsonObj.isNull(TAG_READER_RFID)){
				JSONArray deviceArray3 = jsonObj.getJSONArray(TAG_READER_RFID);
				if(deviceArray3 !=null){
				for (int i = 0; i <deviceArray3.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray3.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray3.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray3.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray3.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray3.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray3.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray3.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray3.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//TabletPrinter
				if(!jsonObj.isNull(TAG_TAB_PRINTER)){
				JSONArray deviceArray12 = jsonObj.getJSONArray(TAG_TAB_PRINTER);
				if(deviceArray12 !=null){
				for (int i = 0; i <deviceArray12.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray12.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray12.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray12.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray12.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray12.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray12.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray12.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray12.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//Battery
				if(!jsonObj.isNull(TAG_BATTERY)){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_BATTERY);
				if(deviceArray !=null){
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//MC
				if(!jsonObj.isNull(TAG_MC)){
				JSONArray deviceArray4 = jsonObj.getJSONArray(TAG_MC);
				if(deviceArray4 !=null){
				for (int i = 0; i <deviceArray4.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray4.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray4.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray4.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray4.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray4.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray4.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray4.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray4.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//VocalPrinter
				if(!jsonObj.isNull(TAG_VOCAL_PRINTER)){
				JSONArray deviceArray5 = jsonObj.getJSONArray(TAG_VOCAL_PRINTER);
				if(deviceArray5 !=null){
				for (int i = 0; i <deviceArray5.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray5.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray5.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray5.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray5.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray5.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray5.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray5.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray5.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}

				
				//Helmet
				if(!jsonObj.isNull(TAG_HELMET)){
				JSONArray deviceArray1 = jsonObj.getJSONArray(TAG_HELMET);
				if(deviceArray1 !=null){
				for (int i = 0; i <deviceArray1.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray1.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray1.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray1.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray1.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray1.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray1.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray1.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray1.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//ThinClient
				if(!jsonObj.isNull(TAG_TC)){
				JSONArray deviceArray6 = jsonObj.getJSONArray(TAG_TC);
				if(deviceArray6 !=null){
				for (int i = 0; i <deviceArray6.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray6.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray6.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray6.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray6.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray6.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray6.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray6.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray6.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//Desktop
				if(!jsonObj.isNull(TAG_DESKTOP)){
				JSONArray deviceArray7 = jsonObj.getJSONArray(TAG_DESKTOP);
				if(deviceArray7 !=null){
				for (int i = 0; i <deviceArray7.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray7.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray7.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray7.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray7.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray7.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray7.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray7.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray7.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//Laptop
				if(!jsonObj.isNull(TAG_LAPTOP)){
				JSONArray deviceArray8 = jsonObj.getJSONArray(TAG_LAPTOP);
				if(deviceArray8 !=null){
				for (int i = 0; i <deviceArray8.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray8.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray8.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray8.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray8.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray8.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray8.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray8.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray8.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//TecPrinter
				if(!jsonObj.isNull(TAG_TEC_PRINTER)){
				JSONArray deviceArray9 = jsonObj.getJSONArray(TAG_TEC_PRINTER);
				if(deviceArray9 !=null){
				for (int i = 0; i <deviceArray9.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray9.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray9.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray9.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray9.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray9.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray9.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray9.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray9.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//PaperPrinter
				if(!jsonObj.isNull(TAG_PRINTER_PAPER)){
				JSONArray deviceArray10 = jsonObj.getJSONArray(TAG_PRINTER_PAPER);
				if(deviceArray10 !=null){
				for (int i = 0; i <deviceArray10.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray10.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray10.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray10.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray10.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray10.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray10.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray10.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray10.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				//Embitrolley
				if(!jsonObj.isNull(TAG_EMBITROLLEY)){
				JSONArray deviceArray11 = jsonObj.getJSONArray(TAG_EMBITROLLEY);
				if(deviceArray11 !=null){
				for (int i = 0; i <deviceArray11.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray11.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray11.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray11.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray11.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray11.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray11.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray11.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastInventory(new String(deviceArray11.getJSONObject(i).getString(TAG_DATE_INVENTORY)
						.getBytes("ISO-8859-1"), "UTF-8"));
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				}
				}
				
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	public static ResultGetDevices getTablet() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getTablets?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	public static ResultGetDevices geNeo() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getNeo?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	
	public static ResultGetDevices getTec() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getTec?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	public static ResultGetDevices getThinClient() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getThinClient?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	
	public static ResultGetDevices getPrinter() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getPrinter?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	public static ResultGetDevices getPrinterTablet() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getPrinterTablet?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	
	public static ResultGetDevices getLaptop() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getLaptop?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	
	public static ResultGetDevices getEmbitrolley() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getEmbitrolley?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
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
	
	public static ResultGetDevices getPickingPrinter() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ResultGetDevices resultDevices = new ResultGetDevices();
		resultDevices.setError(1);


		// HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(HTTP_REST_INVENTORY + "getPickingPrinter?token="
				+ TAG_TOKEN);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIME_OUT_CONNEXION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpResponse response;

		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject jsonObj = getJSONFromInputStream(entity.getContent());

			// Primero miramos el status.
			String status = jsonObj.getString(TAG_STATUS);
			if (status.equals("error")) {
				// String message = jsonObj.getString(TAG_MESSAGE);
				System.out.println("No hay dispositivos");
			} else {
				System.out.println("Entra aqui");
				if(jsonObj.getString(TAG_STATUS).equals("ok")){
				JSONArray deviceArray = jsonObj.getJSONArray(TAG_DATA);
				if(deviceArray !=null){
				List<Device> devices = new ArrayList<Device>();
				for (int i = 0; i <deviceArray.length(); i++){
					Device device = new Device();
				device.setIdDevice(new String(deviceArray.getJSONObject(i).getString(TAG_ID)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setNameDevice(new String(deviceArray.getJSONObject(i).getString(TAG_NAME)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setIpDevice(new String(deviceArray.getJSONObject(i).getString(TAG_IP)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setSerialDevice(new String(deviceArray.getJSONObject(i).getString(TAG_SERIAL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setPlaceDevice(new String(deviceArray.getJSONObject(i).getString(TAG_PLACE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setModel(new String(deviceArray.getJSONObject(i).getString(TAG_MODEL)
						.getBytes("ISO-8859-1"), "UTF-8"));
				device.setLastUpdate(new String(deviceArray.getJSONObject(i).getString(TAG_DATE)
						.getBytes("ISO-8859-1"), "UTF-8"));
				
				devices.add(device);
				}
				resultDevices.setError(0);
				resultDevices.setDevice(devices);
				/*
				 * frase.setTextoFrase(dataObj.getString(TAG_TEXTO));
				 * frase.setAutorFrase(dataObj.getString(TAG_AUTOR));
				 */
				}
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getMessage());
			resultDevices.setError(2);
		} catch (ConnectTimeoutException e) {
			Log.e("ConnectTimeoutException", e.getMessage());
			resultDevices.setError(2);
		} catch (IOException e) {
			Log.e("Error IOException", e.getMessage());
			resultDevices.setError(2);
		} catch (JSONException e) {
			Log.e("Error JSONException", e.getMessage());
			resultDevices.setError(2);
		} catch (Exception e) {
			Log.e("Error Exception", e.getMessage());
			resultDevices.setError(2);
		}

		return resultDevices;
	}

	private static JSONObject getJSONFromInputStream(InputStream is) {
		JSONObject jObj = null;
		String json = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			json = sb.toString();

			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON ",
					"JSONException " + e.toString());
		} catch (Exception e) {
			Log.e("Error ", "Exception " + e.toString());
		}

		return jObj;
	}
	
}