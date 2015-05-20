package com.erodikov.sosservice;

public class SosSettings {	
	private String id;
	private String createdAt;
	private String state;
	private String tBegin;
	private String tEnd;
	private Integer lastX;
	private Integer lastY;
	private Integer lastZ;
	private String smsNumber;
	private String smsText;
	private String hotNumber1;
	private String hotNumber2;
	
	public SosSettings(String id, String createdAt, String state, String tBegin, String tEnd, Integer lastX, Integer lastY, Integer lastZ, String smsNumber, String smsText, String hotNumber1, String hotNumber2){
		this.id = id;
		this.createdAt = createdAt;
		this.state = state;
		this.tBegin = tBegin;
		this.tEnd = tEnd;
		this.lastX = lastX;
		this.lastY = lastY;
		this.lastZ = lastZ;
		this.smsNumber = smsNumber;
		this.smsText = smsText;
		this.hotNumber1 = hotNumber1;
		this.hotNumber2 = hotNumber2;
	}
	
	@Override
	public String toString(){
		StringBuilder result = new StringBuilder("");
		
		result.append("id").append(id).append(" ");
		result.append("createdAt").append(createdAt).append(" ");
		result.append("state").append(state).append(" ");
		result.append("tBegin").append(tBegin).append(" ");
		result.append("tEnd").append(tEnd).append(" ");
		result.append("lastX").append(lastX).append(" ");
		result.append("lastY").append(lastY).append(" ");
		result.append("lastZ").append(lastZ).append(" ");
		result.append("smsNumber").append(smsNumber).append(" ");
		result.append("smsText").append(smsText).append(" ");
		result.append("hotNumber1").append(hotNumber1).append(" ");
		result.append("hotNumber2").append(hotNumber2);
		
		
		return result.toString();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String gettBegin() {
		return tBegin;
	}
	public void settBegin(String tBegin) {
		this.tBegin = tBegin;
	}
	public String gettEnd() {
		return tEnd;
	}
	public void settEnd(String tEnd) {
		this.tEnd = tEnd;
	}


	public Integer getLastX() {
		return lastX;
	}

	public void setLastX(Integer lastX) {
		this.lastX = lastX;
	}

	public Integer getLastY() {
		return lastY;
	}

	public void setLastY(Integer lastY) {
		this.lastY = lastY;
	}

	public Integer getLastZ() {
		return lastZ;
	}

	public void setLastZ(Integer lastZ) {
		this.lastZ = lastZ;
	}

	public String getSmsNumber() {
		return smsNumber;
	}
	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	public String getHotNumber1() {
		return hotNumber1;
	}
	public void setHotNumber1(String hotNumber1) {
		this.hotNumber1 = hotNumber1;
	}
	public String getHotNumber2() {
		return hotNumber2;
	}
	public void setHotNumber2(String hotNumber2) {
		this.hotNumber2 = hotNumber2;
	}
}
