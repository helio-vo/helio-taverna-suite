package org.helio.taverna.helio_taverna_suite.common;

public class TapQueryHelperData extends QueryHelperData {

	private String title;
	
	private String identifier;
	
	private String soapURL;
	
	private String restURL;

	private String ivoaURL;
	
	
	
	public TapQueryHelperData(String title, String identifier, 
				String restURL, String soapURL, String ivoaURL) {
		super(title,identifier);

		this.restURL = restURL;
		this.soapURL = soapURL;
		this.ivoaURL = ivoaURL;
	}
	
	public String getIVOAURL() {return this.ivoaURL;}
	public String getSoapURL() {return this.soapURL;}
	public String getRestURL() {return this.restURL;}
	
	
}