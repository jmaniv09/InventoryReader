package entity;

import java.util.List;

public class ResultGetDevices {
	private Boolean newDevice;
	private Integer numDevices;
	private int error;
	private List<Device> device;
	
	public Boolean getNewDevice(){
		return newDevice;
	}
	
	public void setNewDevice(Boolean newDevice){
		this.newDevice = newDevice;
	}
	
	public Integer getNumDevices(){
		return numDevices;
	}
	
	public void setNumDecies(Integer numDevices){
		this.numDevices = numDevices;
	}
	
	public List<Device> getDevice(){
		return device;
	}
	
	public void setDevice (List<Device> device){
		this.device = device;
	}
	
	public int getError(){
		return error;
	}
	
	public void setError(int error){
		this.error=error;
	}

}
