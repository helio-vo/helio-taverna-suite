package org.helio.taverna.helio_taverna_suite.common;

import java.util.ArrayList;
import java.util.Collection;

import org.astrogrid.registry.RegistryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Hashtable;


import eu.vamdc.registry.Registry;

public class TapQueryHelper {
	
	//public static String getRepositoryDir() {
	//	return ApplicationRuntime.getInstance().getLocalRepositoryDir().toString();
	//}
	

	public static TapQueryHelperData getQueryHelperInfo(String ivoaID, Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);
			
			String query = "for $x in //RootResource where $x/@status='active' and $x/identifier='"+ ivoaID + "' ";
			query += " return <MyResults><ResourceInfo>";
			query += "<identifier>{$x/identifier}</identifier>";
			query += "<titledata>{$x/title}</titledata>";
			query += "<restURL>{$x/capability[@standardID='ivo://helio-vo.eu/std/HELIO-TAP']/interface/accessURL[@asyncservice='false' and @sqlenabled='true' and @status='active' and @use='full']}</restURL>";
			query += "<soapURL>{$x/capability[@standardID='ivo://helio-vo.eu/std/HELIO-TAP']/interface/accessURL[@asyncservice='false' and @sqlenabled='true' and @status='active' and @use='webservice']}</soapURL>";

			query += "</ResourceInfo></MyResults>";
			
		    Document results = reggie.executeXquery(query);
		    NodeList nl = results.getDocumentElement().getElementsByTagName("title");
		    NodeList nlRestURL = results.getDocumentElement().getElementsByTagName("restURL");
		    NodeList nlSoapURL = results.getDocumentElement().getElementsByTagName("soapURL");
		    NodeList tmp = null;
		    String title = null;
		    if(nl.getLength() > 0) {
		   		title = nl.item(0).getTextContent();
		   	}

			String restURL = null;
			if(nlRestURL.getLength() > 0) {
				tmp = ((Element)nlRestURL.item(0)).getElementsByTagName("accessURL");
				if(tmp.getLength() > 0) {
					restURL = tmp.item(0).getTextContent().trim();
				}
			}
			
			String soapURL = null; 
			if(nlSoapURL.getLength() > 0) {
				soapURL = nlSoapURL.item(0).getTextContent().trim();
				tmp = ((Element)nlSoapURL.item(0)).getElementsByTagName("accessURL");
				if(tmp.getLength() > 0) {
					soapURL = tmp.item(0).getTextContent().trim();
				}
				
			}

			TapQueryHelperData thd = new TapQueryHelperData(title,ivoaID,restURL,soapURL,null);
			return thd;

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static TapQueryHelperData[] getQueryHelperInfo(Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);
			
			String query = "for $x in //RootResource where $x/@status='active' and exists($x/capability[@standardID='ivo://helio-vo.eu/std/HELIO-TAP'])";
			query += " return <MyResults><ResourceInfo>";
			query += "<identifier>{$x/identifier}</identifier>";
			query += "<titledata>{$x/title}</titledata>";
			query += "<restURL>{$x/capability[@standardID='ivo://helio-vo.eu/std/HELIO-TAP']/interface/accessURL[@asyncservice='false' and @sqlenabled='true' and @status='active' and @use='full']}</restURL>";
			query += "<soapURL>{$x/capability[@standardID='ivo://helio-vo.eu/std/HELIO-TAP']/interface/accessURL[@asyncservice='false' and @sqlenabled='true' and @status='active' and @use='webservice']}</soapURL>";
			query += "</ResourceInfo></MyResults>";

			System.out.println("xquery_helio_thd: " + query);
		    Document results = reggie.executeXquery(query);
		    NodeList nlRes = results.getDocumentElement().getElementsByTagName("ResourceInfo");
		    TapQueryHelperData []tq = new TapQueryHelperData[nlRes.getLength()];
		    System.out.println("xquery_helio_thd_length: " + nlRes.getLength());
		    for(int ii = 0;ii < nlRes.getLength();ii++) {
			    NodeList nl = ((Element)nlRes.item(ii)).getElementsByTagName("title");
			    NodeList nlRestURL = ((Element)nlRes.item(ii)).getElementsByTagName("restURL");
			    NodeList nlSoapURL = ((Element)nlRes.item(ii)).getElementsByTagName("soapURL");
			    
			    String title = null;
			    if(nl.getLength() > 0) {
			   		title = nl.item(0).getTextContent();
			   	}
			    
			    nl =  ((Element)nlRes.item(ii)).getElementsByTagName("identifier");
			    System.out.println("nl length = " + nl.getLength());
			    String ivoaID = null;
			    if(nl.getLength() > 0) {
			    	ivoaID = nl.item(0).getTextContent().trim();
			   	}
			    System.out.println("ivoaid: " + ivoaID);

			    String restURL = null; 
			    NodeList nlAccessURL = null;
				if(nlRestURL.getLength() > 0) {
					nlAccessURL = ((Element)nlRestURL.item(0)).getElementsByTagName("accessURL");
					if(nlAccessURL.getLength() > 0) {
						restURL = nlAccessURL.item(0).getTextContent().trim();
					}
				}
				
				String soapURL = null; 

				if(nlSoapURL.getLength() > 0) {
				    nlAccessURL = ((Element)nlSoapURL.item(0)).getElementsByTagName("accessURL");
					if(nlAccessURL.getLength() > 0) {
						soapURL = nlAccessURL.item(0).getTextContent().trim();
					}
				}
				System.out.println("_thd_1 title: " + title + " soapURL: " + soapURL);
				tq[ii] = new TapQueryHelperData(title,ivoaID,restURL,soapURL,null);

		    }
		   
		    return tq;

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	
}