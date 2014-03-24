package org.helio.taverna.helio_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.helio.taverna.helio_taverna_suite.common.UWSQueryHelper;

import javax.swing.Icon;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

import org.helio.taverna.helio_taverna_suite.common.AppInterfaces;
import org.helio.taverna.helio_taverna_suite.common.CeaRegBuilder;
import eu.vamdc.registry.Registry;
import org.helio.taverna.helio_taverna_suite.common.RegistryUtil;



public class CEAServiceProvider implements ServiceDescriptionProvider {
	
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
		Properties p = new Properties();

		//String []ivornArr = new String[1];
		//ivornArr[0] = "ivo://mssl.ucl.ac.uk/SolarMovieMaker";
		
		try {
			//boolean myceaListExists = false;
			
			boolean myceaListExists = false;
            //System.out.println(tavHome);^M
            File homeDir = ApplicationRuntime.getInstance().getApplicationHomeDir();
            File userConf = new File(homeDir,"conf");
			Registry reggie =  RegistryUtil.getRegistry();

            if(userConf.exists()) {
                    File myceaList = new File(userConf,"my_cealist.properties");
                    if(myceaList.exists()) {
                            //System.out.println("loaded from my_cealist.properties");
                            p.load(new FileInputStream(myceaList));
                            myceaListExists = true;
                    }
            }
            if(!myceaListExists) {
            	String []appsFromReg = UWSQueryHelper.getQueryHelperInfo(reggie);
            	//System.out.println("No defined app file found, grab all from the Registry.");
            	for(int i = 0;i < appsFromReg.length;i++) {
                	p.setProperty("my.ceaapp." + i, appsFromReg[i]);
            	}
            }
           
			String val;
			
			
			//Iterator iter = p.values().iterator();
			Enumeration propertyKeys = p.keys();
			
			Properties pr = new Properties();
			pr.setProperty("return.soapbody","false");
			pr.setProperty("registry.useCache","true");
			//do some looping through all the apps and interface beans.
			while(propertyKeys.hasMoreElements()) {
				val = (String)propertyKeys.nextElement();
				if(val.startsWith("my.ceaapp")) {
					//System.out.println("look up registry app: " + val);
					AppInterfaces []ai = CeaRegBuilder.getCEAApp(p.getProperty(val),reggie);
					if(ai != null) {
						//System.out.println("app interfaces found:" + ai.length);
						for(int m = 0;m < ai.length;m++) {
							CEAServiceDesc ceaService = new CEAServiceDesc();
							ceaService.setCeaInterfaceName(ai[m].getName());
							ceaService.setCeaIvorn(ai[m].getIVOAIdent());
							ceaService.setAppInterface(ai[m]);
							ceaService.setCeaTitle(ai[m].getCEAName());
							results.add(ceaService);
						}
					}//if
				}//if
			}//while
			
			//CEAServiceDesc ceaService = new CEAServiceDesc();
			/*
			UWSRecoverServiceDesc ceaService = new UWSRecoverServiceDesc();
			ceaService.setCeaInterfaceName("UWSRECOVER");
			ceaService.setCeaIvorn("UWSRECOVER");
			ceaService.setAppInterface(null);
			ceaService.setCeaTitle("UWSRECOVER");
			ceaService.setDescription("Recover an Application results that was started in the past.");
			results.add(ceaService);
			*/
			//System.out.println("results size = " + results.size());
			for(int k = 0;k < results.size();k++) {
				//System.out.println("results name loop = " + results.get(k).getName());
				
			}

			// FIXME: Implement the actual service search/lookup instead
			// of dummy for-loop
			
			
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
		return "Helio CEA";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
