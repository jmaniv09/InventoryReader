package restfullwebservice;

public class RestfulWeb {

	/**
	 * Constante con la información del Servidor.
	 */
    public final static String HTTP_REST_INVENTORY = "http://itteamprat.manitec.xyz/inventory/";

	public final static String HTTP_REST_GETALLDEVICES= "http://itteamprat.manitec.xyz/inventory/getAllDevices?token=HT0R4LfEqrSJkfKC17ib7T01C33XTfsv";
	public static final String URL_REGISTER_DEVICE = "http://notificationserver.manitec.xyz/RegisterDevice.php";
	public static final String URL_SEND_SINGLE_PUSH = "http://notificationserver.manitec.xyz/sendSinglePush.php";
	public static final String URL_SEND_MULTIPLE_PUSH = "http://notificationserver.manitec.xyz/sendMultiplePush.php";
	public static final String URL_FETCH_DEVICES = "http://notificationserver.manitec.xyz/GetRegisteredDevices.php";
	/**
	 * Tag inicial de todas las llamadas, para saber si la llamada ha sido
	 * correcta.
	 */
    public static final String TAG_STATUS = "status";

    public static final String TAG_TOKEN = "HT0R4LfEqrSJkfKC17ib7T01C33XTfsv";

    public static final String TAG_DATA = "devices";

    public static final String TAG_DATA2 = "device";

    public static final String TAG_MESSAGE = "message";

	/**
	 * Tag data, contiene una array con los datos en formato json.
	 */
	public static final String TAG_ID = "ID";

	/**
	 * Tag id de usuario
	 */
    public static final String TAG_NAME = "name";

	/**
	 * Tag con la información del sexo del usuario
	 */
    public static final String TAG_IP = "ip";

    public static final String TAG_SERIAL = "serial";

    public static final String TAG_PLACE = "location";

    public static final String TAG_MODEL = "model";

    public static final String TAG_DATE_INVENTORY = "lastDateInventory";

    public static final String TAG_DATE = "lastDate";

    public static final String TAG_STATUS_DEVICE = "status";

    public static final String TAG_OBSERVATION = "observation";
	
	//Tag devices
    public static final String TAG_BATTERY = "prat_battery";
    public static final String TAG_DESKTOP = "prat_desktop";
    public static final String TAG_TABLET = "prat_tablet";
    public static final String TAG_EMBITROLLEY = "prat_embitrolley";
    public static final String TAG_READER_RFID = "prat_reader_rfid";
    public static final String TAG_HELMET = "prat_helmet";
    public static final String TAG_LAPTOP = "prat_laptop";
    public static final String TAG_MC = "prat_mc";
    public static final String TAG_PRINTER_PAPER = "prat_printer_paper";
    public static final String TAG_TAB_PRINTER = "prat_tab_printer";
    public static final String TAG_TEC_PRINTER = "prat_tec_printer";
    public static final String TAG_TC = "prat_thin_client";
    public static final String TAG_VOCAL_PRINTER = "prat_vocal_printer";



}
