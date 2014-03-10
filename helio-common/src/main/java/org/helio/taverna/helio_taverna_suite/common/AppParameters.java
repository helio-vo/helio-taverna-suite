package org.helio.taverna.helio_taverna_suite.common;


public class AppParameters {

	private String id;
	private String uiName;
	private String type;
	
	private int min = 1;
	private int max = 1;
	
	public AppParameters(String id,String type) {
		this(id,type,null);
	}
	
	public AppParameters(String id,String type,String uiName) {
		this.id=id;
		this.uiName=uiName;
		if(uiName == null){this.uiName= id;}
		this.type=type;
	}
	
	public AppParameters(String id,String type,String uiName,int min, int max) {
		this.id=id;
		this.uiName=uiName;
		if(uiName == null){this.uiName= id;}
		this.type=type;
		this.min = min;
		this.max = max;
		
	}
	
	public String getType() {return this.type;}
	public String getId() {return this.id;}
	public String getUIName() {return this.uiName;}
	public int getMin() {return this.min;}
	public int getMax() {return this.max;}

}