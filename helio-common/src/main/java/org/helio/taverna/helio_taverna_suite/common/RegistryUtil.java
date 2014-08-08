package org.helio.taverna.helio_taverna_suite.common;

import eu.vamdc.registry.Registry;
import java.util.Properties;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;
import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.registry.RegistryException;




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
			
			File userConf = new File(homeDir,"conf");
			System.out.println("userConf dir: " + userConf.toString());
			if(userConf.exists()) {
				File myceaList = new File(userConf,"helio.properties");
				if(myceaList.exists()) {
					//System.out.println("loaded from helio.properties");
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
			System.out.println("Helio endpoint for Registry2: " + val);
			//System.out.println("creating reg: " + val);
			reggie = new Registry(val);
		}else {
			//"http://localhost:8080/tap_reg/services/RegistryQueryv1_0"
			//System.out.println("creating reg:  http://msslkz.mssl.ucl.ac.uk/helio_registry/services/RegistryQueryv1_0");
			System.out.println("Helio endpoint for Registry1:");
			String []regNames = {
					"http://helio.mssl.ucl.ac.uk/helio_registry/services/RegistryQueryv1_0", 
					"http://helio.ukssdc.ac.uk:80/helio_registry/services/RegistryQueryv1_0",
					"http://voparis-helio-astrogrid.obspm.fr/helio-registry/services/RegistryQueryv1_0"
			};
			String regSoapURL = null;
			boolean urlAvailable = false;
			for(int i = 0;i < regNames.length && !urlAvailable;i++) {
				regSoapURL = regNames[i];
				try {
					urlAvailable = pingURL(regNames[i]);
				}catch(RegistryException re) {
					//will ignore this reg exception.  it is a bad url.  Go to the next one
				}
			}
			if(!urlAvailable) {
				throw new java.util.MissingResourceException("Could not ping a valid Registry", "eu.vamdc.registry.Registry", "Registry");
			}
			
			reggie = new Registry(regSoapURL);
		}
		return reggie;
	}
	
	public static boolean pingURL(String url) throws RegistryException {
		boolean urlAvailable = false;

		try{
		    final URLConnection connection = new URL(url).openConnection();
		    connection.connect();
		    urlAvailable = true;
		} catch(final MalformedURLException e){
		    throw new RegistryException("Bad URL: " + url, e);
		} catch(final IOException e){
		    urlAvailable = false;
		}
		return urlAvailable;
	}
	
	
	
}
