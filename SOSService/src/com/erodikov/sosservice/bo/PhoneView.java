package com.erodikov.sosservice.bo;

public class PhoneView {
	String type;
	String number;	
	String name;
		
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullView(){
		StringBuilder fullView = new StringBuilder();
		if(name!=null){
			fullView.append(name).append("\n");
		}		
		if(type!=null){
			fullView.append(type).append(" ");
		}
		fullView.append(number);
		return fullView.toString();
	}	
}
