package org.helio.taverna.helio_taverna_suite.common;

import java.util.ArrayList;
import java.util.Collection;

import org.astrogrid.registry.RegistryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Hashtable;


import eu.vamdc.registry.Registry;

public class CeaRegBuilder {
	

	public static AppInterfaces[] getCEAApp(String ivoaID, Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);
			
			
		    Document results = reggie.getResource(ivoaID);
		    NodeList nl = results.getDocumentElement().getElementsByTagName("title");
		    System.out.println("nodelist of title for ceaapp:" + nl.getLength());
		   	String ceaName = nl.item(0).getFirstChild().getNodeValue();
		    //String ceaName = nl.item(0).getTextContent();
		   	//if(nl.item(0).getFirstChild())
		   	nl = results.getDocumentElement().getElementsByTagName("parameterDefinition");
	   	   	String id;
	   		NodeList portRef;
	   		NodeList ports;
	   		String refId;
	   		String type;
	   		NodeList nl2;
	   		String uiName = null;
		   	//Hashtable paramsTable = new Hashtable();
		   	Hashtable<String,AppParameters> ht = new Hashtable<String,AppParameters>();
		   	for(int i = 0;i < nl.getLength();i++) {
		   		uiName = null;
		   		id = nl.item(i).getAttributes().getNamedItem("id").getNodeValue();
		   		type = nl.item(i).getAttributes().getNamedItem("type").getNodeValue();
		   		nl2 = ((Element)nl.item(i)).getElementsByTagName("UIName");
		   		if(nl2.getLength() > 0) {
		   		 	uiName=nl2.item(0).getFirstChild().getNodeValue();
		   		}
		   		AppParameters ap = new AppParameters(id,type,uiName);
		   		ht.put(id,ap);
		   		//System.out.println("adding id for paramdef hashtable = " + id);
		   		
		   	}
		   	nl2 = results.getDocumentElement().getElementsByTagName("interfaceDefinition");
		
	   		AppInterfaces []apps = new AppInterfaces[nl2.getLength()];
	   		AppParameters []inputApps;
	   		AppParameters []outputApps;
		   	for (int i = 0; i < nl2.getLength(); i++) {
		   		id = nl2.item(i).getAttributes().getNamedItem("id").getNodeValue();
		   		ports = ((Element)nl2.item(i)).getElementsByTagName("input");
		   		//System.out.println("interfacedef id = " + id + " and input port num = " + ports.getLength());
		   		inputApps = new AppParameters[0];
		   		outputApps = new AppParameters[0];
		   		
				for(int j = 0;j < ports.getLength();j++) {
					portRef = ((Element)ports.item(j)).getElementsByTagName("pref");
					inputApps = new AppParameters[portRef.getLength()];
					//System.out.println("portref getLength = " + portRef.getLength());
					for(int k = 0;k < portRef.getLength();k++) {
						refId = portRef.item(k).getAttributes().getNamedItem("ref").getNodeValue();
						//System.out.println("adding refId = " + refId + " ht conains = " + ht.containsKey(refId)); 
						inputApps[k] = ht.get(refId);
					}
				}
		   		
		   		ports = ((Element)nl2.item(i)).getElementsByTagName("output");
		   		for(int j = 0;j < ports.getLength();j++) {
					portRef = ((Element)ports.item(j)).getElementsByTagName("pref");
					outputApps = new AppParameters[portRef.getLength()];
					for(int k = 0;k < portRef.getLength();k++) {
						refId = portRef.item(k).getAttributes().getNamedItem("ref").getNodeValue();
						outputApps[k] = ht.get(refId);
					}
				}
		   		String managedAppLocation = getCEAService(ivoaID,reggie);
		   		
				apps[i] = new AppInterfaces(id,ceaName,ivoaID,managedAppLocation);
				apps[i].setInputParams(inputApps);
				apps[i].setOutputParams(outputApps);
		    }
		    return apps;

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static String getCEAService(String ivoaID, Registry reggie) {
		try {
			ArrayList<String> ids = new ArrayList<String>();
			//Registry reggie = new Registry("http://casx019-zone1.ast.cam.ac.uk/registry/services/RegistryQueryv1_0");
		    String query = 
		        "declare namespace ri='http://www.ivoa.net/xml/RegistryInterface/v1.0'; " +
		        "declare namespace xsi='http://www.w3.org/2001/XMLSchema-instance'; " +
		        "for $x in //ri:Resource " + 
		        "where $x/capability[@standardID='ivo://org.astrogrid/std/CEA/v1.0']/managedApplications/applicationReference='" +
		        ivoaID + "' " + 
		        "and $x/@status='active' " +
		        "return $x/capability[@standardID='ivo://org.astrogrid/std/CEA/v1.0']/interface[@xsi:type='cea:UWS-PA']/accessURL";
		    Document results = reggie.executeXquery(query);
		    try {
		    javax.xml.transform.TransformerFactory tfactory = javax.xml.transform.TransformerFactory.newInstance();
		    javax.xml.transform.Transformer xform = tfactory.newTransformer();
		    javax.xml.transform.Source src = new javax.xml.transform.dom.DOMSource(results);
		    java.io.StringWriter writer = new java.io.StringWriter();
		    javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(writer);
		    xform.transform(src, result);
		    System.out.println("XMLDOC=" + writer.toString());
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
		    NodeList nl = results.getDocumentElement().getElementsByTagNameNS("*","accessURL");
		    System.out.println("XQuery ran = " + query);
		    System.out.println("CEAnumber of nodelist found = " + nl.getLength());
		    
		    if(results.getDocumentElement().hasChildNodes()) {
			    System.out.println("URL CEA = " + results.getDocumentElement().getFirstChild().getTextContent() );
		    	return results.getDocumentElement().getFirstChild().getTextContent();
		    }
		    /*
		   	if(nl.getLength() > 0) {
		   		return nl.item(0).getFirstChild().getNodeValue();
		   	}
		   	*/
		   	return null;
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}



