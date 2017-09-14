package app.num.barcodescannerproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import entity.Device;
import restfullwebservice.RestfulWebService;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nameDevice = extras.getString("device");
			/*
			 * String delimiter = "-"; String[] temp; temp =
			 * str.split(delimiter); /* for (int i = 0; i < temp.length; i++)
			 * System.out.println(temp[i]);
			 */
			/*
			 * nameDevice = temp[0]; ipDevice = temp[1];
			 */
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
			new GetDeviceByName().execute();
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
					new updateDevice().execute();
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
					new registerDevice().execute();
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
				deviceStatus = (String) statusDevice.getSelectedItem();
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

	/*public void launchScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable",
					Toast.LENGTH_SHORT).show();
		}
	}*/

	public class GetDeviceByName extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(DeviceActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Cargando Dispositivos! Por favor espere unos segundos...!");
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... unused) {
			resultGetDevice = RestfulWebService.getDeviceByName(nameDevice);
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (resultGetDevice != null) {
				switch (resultGetDevice.getError()) {
				case 0:
					// NO HAY ERROR
					try {
						dialog.dismiss();
						dialog = null;
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
					} catch (Exception e) {
						// nothing
					}
					break;

				case 1:
					try {
						dialog.dismiss();
						dialog = null;
						deviceName.setText(nameDevice);
						deviceIP.setText(ipDevice);
						lastDateInventory.setEnabled(false);
						updateDevice.setEnabled(false);
						registerDevice.setEnabled(true);
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					// ERROR WEBSERVICE
					System.out.println("Error WebService");
					break;

				default:
					break;
				}
			}
		}

	}

	public class registerDevice extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(DeviceActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Registrando nuevo dispositivo!");
			dialog.show();
			position = statusDevice.getSelectedItemPosition();
		}

		@Override
		protected Void doInBackground(Void... unused) {
			resultGetDevice = RestfulWebService.registerDevice(nameDevice,
					serialnDevice, ipDevice, placeDevice, position , deviceModels, deviceObserv);
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (resultGetDevice != null) {
				switch (resultGetDevice.getError()) {
				case 0:
					// NO HAY ERROR
					try {
						dialog.dismiss();
						dialog = null;
						Toast.makeText(DeviceActivity.this,
								resultGetDevice.getMessage(),
								Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						// nothing
					}
					break;

				case 1:
					try {
						dialog.dismiss();
						dialog = null;
						Toast.makeText(
								DeviceActivity.this,
								"Error al registrar dispositivo, vuelva a probar mas tarde",
								Toast.LENGTH_SHORT).show();
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					// ERROR WEBSERVICE
					System.out.println("Error WebService");
					break;
				case 2:
					try {
						dialog.dismiss();
						dialog = null;
						Toast.makeText(
								DeviceActivity.this,
								"Error al registrar dispositivo, vuelva a probar mas tarde",
								Toast.LENGTH_SHORT).show();
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					// ERROR WEBSERVICE
					System.out.println("Error WebService");
					break;

				default:
					break;
				}
			}
		}

	}

	public class updateDevice extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(DeviceActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Actualizando dispositivo!");
			dialog.show();
			position =statusDevice.getSelectedItemPosition();

		}
		@Override
		protected Void doInBackground(Void... unused) {
			resultGetDevice = RestfulWebService.updateDevice(nameDevice,
					serialnDevice, ipDevice, placeDevice, position , deviceModels, deviceObserv);
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (resultGetDevice != null) {
				switch (resultGetDevice.getError()) {
				case 0:
					// NO HAY ERROR
					try {
						dialog.dismiss();
						dialog = null;
						Toast.makeText(DeviceActivity.this,
								resultGetDevice.getMessage(),
								Toast.LENGTH_SHORT).show();
						if (nameDevice != null && !nameDevice.isEmpty()) {
							new GetDeviceByName().execute();
						} else {
							updateDevice.setEnabled(false);
						}
					} catch (Exception e) {
						// nothing
					}
					break;

				case 1:
					try {
						dialog.dismiss();
						dialog = null;
						Toast.makeText(
								DeviceActivity.this,
								"Error al registrar dispositivo, vuelva a probar mas tarde",
								Toast.LENGTH_SHORT).show();
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					// ERROR WEBSERVICE
					System.out.println("Error WebService");
					break;
				case 2:
					try {
						dialog.dismiss();
						dialog = null;
						Toast.makeText(
								DeviceActivity.this,
								"Error al registrar dispositivo, vuelva a probar mas tarde",
								Toast.LENGTH_SHORT).show();
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					// ERROR WEBSERVICE
					System.out.println("Error WebService");
					break;

				default:
					break;
				}
			}
		}

	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
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

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				// Toast.makeText(this, "Scan Result = " +
				// data.getStringExtra(ZBarConstants.SCAN_RESULT),
				// Toast.LENGTH_SHORT).show();
				String str = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
				deviceSerial.setText(str);
			} else if (resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if (!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}*/
}