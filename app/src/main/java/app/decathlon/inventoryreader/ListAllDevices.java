package app.decathlon.inventoryreader;

import java.util.ArrayList;
import java.util.List;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import entity.Device;
import entity.ResultGetDevices;
import restfullwebservice.RestfulWebService;

public class ListAllDevices extends Activity {
	ResultGetDevices resultGetDevices;
	private ProgressDialog dialog = null;
	private List<Device> listOfDevices;
	private List<Device> searchDevices;
	private AccountAdapter devicesAdapter;
	private NewAdapter newAdapter;
	private ListView listView;
	private EditText inputSearch;

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

		new GetAllDevices().execute();
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

	public class GetAllDevices extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ListAllDevices.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Cargando Dispositivos! Por favor espere unos segundos...!");
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... unused) {

			resultGetDevices = RestfulWebService.getAllDevices();
			return (null);
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (resultGetDevices != null) {
				switch (resultGetDevices.getError()) {
				case 0:
					// NO HAY ERROR
					try {
						dialog.dismiss();
						dialog = null;
						printScores(resultGetDevices);
					} catch (Exception e) {
						// nothing
					}
					break;

				case 1:
					try {
						dialog.dismiss();
						dialog = null;
						showAlertDialogError();
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					// ERROR WEBSERVICE
					System.out.println("Error WebService");
					break;

				case 2:
					// ERROREXCEPTION
					try {
						dialog.dismiss();
						dialog = null;
						showAlertDialogError();
						// goToTarget = false;
					} catch (Exception e) {
						// nothing
					}
					System.out.println("Error Exception");

					break;

				default:
					break;
				}
			}
		}

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
}
