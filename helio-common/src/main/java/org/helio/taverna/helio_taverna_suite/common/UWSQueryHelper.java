package org.helio.taverna.helio_taverna_suite.common;

import java.util.ArrayList;
import java.util.Collection;

import org.astrogrid.registry.RegistryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Hashtable;


import eu.vamdc.registry.Registry;

public class UWSQueryHelper {
	
	//public static String getRepositoryDir() {
	//	return ApplicationRuntime.getInstance().getLocalRepositoryDir().toString();
	//}
	

	public static String[] getQueryHelperInfo(Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);

			String query = "for $x in //RootResource where $x/@status='active' and exists($x//applicationReference) ";
			query += " return <MyResults><ResourceInfo>";
			query += "<AppIdentifier>{$x//applicationReference}</AppIdentifier>";
			query += "</ResourceInfo></MyResults>";
			
			//System.out.println("Helio Reg endpoint: " + reggie.registryEndpoint());
		    Document results = reggie.executeXquery(query);
		    NodeList nl = results.getDocumentElement().getElementsByTagName("applicationReference");
		    ArrayList<String> appRef = new ArrayList<String>();
		    for(int i = 0;i < nl.getLength();i++) {
		    	String s = nl.item(i).getTextContent();
		    	if(!appRef.contains(s)) {
		    		appRef.add(nl.item(i).getTextContent());
		    	}//if
		    }//if
		    return appRef.toArray(new String[0]);

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}