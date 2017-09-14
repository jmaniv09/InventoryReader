package entity;

/**
 * Entidad usuario.
 * 
 * @author Lidinyu
 */
public class Device {

	// Atributos de clase
	private String idDevice;
	private String nameDevice;
	private String ipDevice;
	private String serialnDevice;
	private String placeDevice;
	private String message;
	private int error;
	private String model;
	private String lastUpdate;
	private String observation;
	private String lastInventory;
	private int status;

	// Constructor por defecto
	public Device() {
		super();
	}

	// Constructor sobrecargado
	public Device(String idDevice, String nameDevice, String ipDevice,
			String placeDevice, String serialnDevice, String message,
			int error, String model, String lastUpdate, String lastInventory, String observation,int status) {
		super();
		this.idDevice = idDevice;
		this.error = error;
		this.nameDevice = nameDevice;
		this.ipDevice = ipDevice;
		this.serialnDevice = serialnDevice;
		this.placeDevice = placeDevice;
		this.message = message;
		this.model = model;
		this.lastUpdate = lastUpdate;
	}

	// Getters y setters
	public String getIdDevice() {
		return idDevice;
	}

	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}

	public String getNameDevice() {
		return nameDevice;
	}

	public void setNameDevice(String nameDevice) {
		this.nameDevice = nameDevice;
	}

	public String getIpDevice() {
		return ipDevice;
	}

	public void setIpDevice(String ipDevice) {
		this.ipDevice = ipDevice;
	}

	public String getSerialDevice() {
		return serialnDevice;
	}

	public void setSerialDevice(String serialnDevice) {
		this.serialnDevice = serialnDevice;
	}

	public String getPlaceDevice() {
		return placeDevice;
	}

	public void setPlaceDevice(String placeDevice) {
		this.placeDevice = placeDevice;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLastInventory() {
		return lastInventory;
	}

	public void setLastInventory(String lastInventory) {
		this.lastInventory = lastInventory;
	}
	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

	// Devuelve false si los usuarios tienen age, name, sex o email diferentes
}
