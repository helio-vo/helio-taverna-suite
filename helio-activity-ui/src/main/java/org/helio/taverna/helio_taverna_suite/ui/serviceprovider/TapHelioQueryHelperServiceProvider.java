package org.helio.taverna.helio_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

import org.helio.taverna.helio_taverna_suite.common.TapQueryHelperData;
import org.helio.taverna.helio_taverna_suite.common.TapQueryHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Hashtable;
import org.helio.taverna.helio_taverna_suite.common.RegistryUtil;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;
import eu.vamdc.registry.Registry;

public class TapHelioQueryHelperServiceProvider implements ServiceDescriptionProvider {
	
	private static final URI providerId = URI
		.create("http://www.helio.eu/taverna/plugin/suite");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");
		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		// FIXME: Implement the actual service search/lookup instead
		// of dummy for-loop
		
		try {
			String val;
			Registry reggie =  RegistryUtil.getRegistry();
			
			
			//Iterator iter = p.values().iterator();
			//Enumeration propertyKeys = p.keys();
			
			Properties pr = new Properties();
			pr.setProperty("return.soapbody","false");
			pr.setProperty("registry.useCache","true");
			
						TapQueryHelperData []thd = TapQueryHelper.getQueryHelperInfo(reggie);
						for(int k = 0;k < thd.length;k++) {
							TapHelioQueryHelperServiceDesc tapHelioService = new TapHelioQueryHelperServiceDesc();
							System.out.println("_thd_title_: " + thd[k].getTitle() + " k: " + k + " identifier: " + thd[k].getIdentifier() + " soapurl: " + thd[k].getSoapURL());
							tapHelioService.setTapQueryHelperData(thd[k]);
							results.add(tapHelioService);
						}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		/*
		for (int i = 1; i <= 5; i++) {
			ExampleServiceDesc service = new ExampleServiceDesc();
			// Populate the service description bean
			service.setExampleString("Example " + i);
			service.setExampleUri(URI.create("http://localhost:8192/service"));

			// Optional: set description
			service.setDescription("Service example number " + i);
			results.add(service);
		}
		*/

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);

		// No more results will be coming
		callBack.finished();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return ExampleServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "Helio TAP";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
