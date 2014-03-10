package org.helio.taverna.helio_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.helio.taverna.helio_taverna_suite.CEAActivity;
import org.helio.taverna.helio_taverna_suite.CEAActivityConfigurationBean;
import org.helio.taverna.helio_taverna_suite.common.AppInterfaces;

public class CEAServiceDesc extends ServiceDescription<CEAActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<CEAActivityConfigurationBean>> getActivityClass() {
		return CEAActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public CEAActivityConfigurationBean getActivityConfiguration() {
		CEAActivityConfigurationBean bean = new CEAActivityConfigurationBean();
		bean.setCeaInterfaceName(this.ceaInterfaceName);
		bean.setCeaIvorn(this.ceaIvorn);
		bean.setAppInterface(this.ceaInterface);
		//bean.setExampleString(exampleString);
		//bean.setExampleUri(exampleUri);
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return ExampleServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return this.ceaTitle + "-" + this.ceaInterfaceName;
	}

	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		
		return Arrays.asList("Helio","UWS Run Codes", "Apps");
	}

	/**
	 * Return a list of data values uniquely identifying this service
	 * description (to avoid duplicates). Include only primary key like fields,
	 * ie. ignore descriptions, icons, etc.
	 */
	@Override
	protected List<? extends Object> getIdentifyingData() {
		// FIXME: Use your fields instead of example fields
		//return Arrays.<Object>asList(exampleString, exampleUri);
		return Arrays.<Object>asList("UWS",ceaIvorn + "-" + ceaInterfaceName);
		//return Arrays.<Object>asList(ceaIvorn + "-" + ceaInterfaceName);
	}

	private String ceaIvorn;
	
	private String ceaInterfaceName;
	
	public String getCeaIvorn() {
		return ceaIvorn;
	}

	public void setCeaIvorn(String ceaIvorn) {
		this.ceaIvorn = ceaIvorn;
	}
	
	private String ceaTitle;
	
	public String getCeaTitle() {
		return ceaTitle;
	}

	public void setCeaTitle(String ceaTitle) {
		this.ceaTitle = ceaTitle;
	}
	
	public String getCeaInterfaceName() {
		return ceaInterfaceName;
	}

	public void setCeaInterfaceName(String ceaInterfaceName) {
		this.ceaInterfaceName = ceaInterfaceName;
	}
	
	private AppInterfaces ceaInterface;
	
	public void setAppInterface(AppInterfaces ceaInterface) {
		this.ceaInterface = ceaInterface;
	}
	
	public AppInterfaces getAppInterface() {
		return this.ceaInterface;
	}

	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	/*
	private String exampleString;
	private URI exampleUri;
	public String getExampleString() {
		return exampleString;
	}
	public URI getExampleUri() {
		return exampleUri;
	}
	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}
	public void setExampleUri(URI exampleUri) {
		this.exampleUri = exampleUri;
	}
	*/

}
