package org.helio.taverna.helio_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.helio.taverna.helio_taverna_suite.TapHelioQueryHelperActivity;
import org.helio.taverna.helio_taverna_suite.TapHelioQueryHelperActivityConfigurationBean;

import org.helio.taverna.helio_taverna_suite.common.TapQueryHelperData;

public class TapHelioQueryHelperServiceDesc extends ServiceDescription<TapHelioQueryHelperActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<TapHelioQueryHelperActivityConfigurationBean>> getActivityClass() {
		return TapHelioQueryHelperActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public TapHelioQueryHelperActivityConfigurationBean getActivityConfiguration() {
		TapHelioQueryHelperActivityConfigurationBean bean = new TapHelioQueryHelperActivityConfigurationBean();
		bean.setTapQueryHelperData(this.thd);
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
		return thd.getTitle();
	}

	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		
		return Arrays.asList("Helio", "TAP", "Services");
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
		return Arrays.<Object>asList("helioTAPQueryHelper","services",thd.getIdentifier());
	}
	
	private TapQueryHelperData thd;
	private String interfaceURL;
	
	private int interfaceTypeURL;
		
	public TapQueryHelperData getTapQueryHelperData() {
		return this.thd;
	}
	
	public void setTapQueryHelperData(TapQueryHelperData thd) {
		this.thd = thd;
	}
	
}
