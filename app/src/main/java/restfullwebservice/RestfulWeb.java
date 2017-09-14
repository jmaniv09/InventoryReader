package restfullwebservice;

public class RestfulWeb {

	/**
	 * Constante con la información del Servidor.
	 */
	protected final static String HTTP_REST_INVENTORY = "http://itteamprat.manitec.xyz/inventory/";
	/**
	 * Tag inicial de todas las llamadas, para saber si la llamada ha sido
	 * correcta.
	 */
	protected static final String TAG_STATUS = "status";
	
	protected static final String TAG_TOKEN = "HT0R4LfEqrSJkfKC17ib7T01C33XTfsv";
	
	protected static final String TAG_DATA = "devices";
	
	protected static final String TAG_DATA2 = "device";
	
	protected static final String TAG_MESSAGE = "message";

	/**
	 * Tag data, contiene una array con los datos en formato json.
	 */
	protected static final String TAG_ID = "ID";

	/**
	 * Tag id de usuario
	 */
	protected static final String TAG_NAME = "name";

	/**
	 * Tag con la información del sexo del usuario
	 */
	protected static final String TAG_IP = "ip";
	
	protected static final String TAG_SERIAL = "serial";
	
	protected static final String TAG_PLACE = "location";
	
	protected static final String TAG_MODEL = "model";
	
	protected static final String TAG_DATE_INVENTORY = "lastDateInventory";
	
	protected static final String TAG_DATE = "lastDate";
	
	protected static final String TAG_STATUS_DEVICE = "status";
	
	protected static final String TAG_OBSERVATION = "observation";
	
	//Tag devices
	protected static final String TAG_BATTERY = "prat_battery";
	protected static final String TAG_DESKTOP = "prat_desktop";
	protected static final String TAG_TABLET = "prat_tablet";
	protected static final String TAG_EMBITROLLEY = "prat_embitrolley";
	protected static final String TAG_READER_RFID = "prat_reader_rfid";
	protected static final String TAG_HELMET = "prat_helmet";
	protected static final String TAG_LAPTOP = "prat_laptop";
	protected static final String TAG_MC = "prat_mc";
	protected static final String TAG_PRINTER_PAPER = "prat_printer_paper";
	protected static final String TAG_TAB_PRINTER = "prat_tab_printer";
	protected static final String TAG_TEC_PRINTER = "prat_tec_printer";
	protected static final String TAG_TC = "prat_thin_client";
	protected static final String TAG_VOCAL_PRINTER = "prat_vocal_printer";

}
