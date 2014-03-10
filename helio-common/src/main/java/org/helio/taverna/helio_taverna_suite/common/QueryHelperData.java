package org.helio.taverna.helio_taverna_suite.common;

public class QueryHelperData {

	private String title;
	private String identifier;
	
	public QueryHelperData(String title, String identifier) {
	 	this.title = title;
	 	this.identifier = identifier;
	}
	
	public String getTitle(){return this.title;}
	public String getIdentifier(){return this.identifier;}
	
}