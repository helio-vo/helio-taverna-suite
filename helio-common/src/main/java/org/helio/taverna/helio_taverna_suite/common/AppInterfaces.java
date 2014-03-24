package org.helio.taverna.helio_taverna_suite.common;

public class AppInterfaces {

	private String name;
	public AppInterfaces(String name,String ceaName, String ivoIdent, String managedAppServiceLocation) {
	 	this.name = name;
	 	this.ceaName = ceaName;
	 	this.ivoIdent = ivoIdent;
	 	//System.out.println("setting up managedapp loc = " + managedAppServiceLocation);
	 	this.managedAppServiceLocation=managedAppServiceLocation;
	}
	
	private String ceaName;
	private String ivoIdent;
	private String managedAppServiceLocation;
	
	private AppParameters []inputParams;
	
	private AppParameters []outputParams;
	
	public String getName(){return this.name;}
	public String getCEAName() {return this.ceaName;}
	public String getIVOAIdent() {return this.ivoIdent;}
	public String getManagedAppServiceLocation() { return this.managedAppServiceLocation;}
	
	public void setInputParams(AppParameters []inputParams) {this.inputParams = inputParams;}
	public void setOutputParams(AppParameters []outputParams) {this.outputParams = outputParams;}
	
	public AppParameters[] getInputParameters() { return this.inputParams;}
	public AppParameters[] getOutputParameters() { return this.outputParams;}
	
}