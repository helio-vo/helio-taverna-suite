package org.helio.taverna.helio_taverna_suite.common;

import eu.vamdc.registry.Registry;
import java.util.Properties;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;
import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;

public class RegistryUtil {

	private static Registry reggie = null;
	
	public static Registry getRegistry() {
		if(reggie != null) {
			return reggie;
		}
		System.setProperty("return.soapBody","true");
		
		Properties p = new Properties();
		try {
			File homeDir = ApplicationRuntime.getInstance().getApplicationHomeDir();
			
			System.out.println("DEBUG LOCATIONS FILE REPOSITORY: " + 
					ApplicationRuntime.getInstance().getLocalRepositoryDir() + 
					" URL: " + ApplicationRuntime.getInstance().getDefaultRepositoryDir());
			File userConf = new File(homeDir,"conf");
			if(userConf.exists()) {
				File myceaList = new File(userConf,"helio.properties");
				if(myceaList.exists()) {
					System.out.println("loaded from helio.properties");
					p.load(new FileInputStream(myceaList));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		String val;
		Registry reggie = null;
		if(p.containsKey("registry.endpoint")) {
			val = p.getProperty("registry.endpoint");
			System.out.println("creating reg: " + val);
			reggie = new Registry(val);
		}else {
			//"http://localhost:8080/tap_reg/services/RegistryQueryv1_0"
			System.out.println("creating reg:  http://msslkz.mssl.ucl.ac.uk/helio_registry/services/RegistryQueryv1_0");
			reggie = new Registry("http://msslkz.mssl.ucl.ac.uk/helio_registry/services/RegistryQueryv1_0");
		}
		return reggie;
	}
	
	
	
}