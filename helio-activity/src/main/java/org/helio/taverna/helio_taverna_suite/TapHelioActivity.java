package org.helio.taverna.helio_taverna_suite;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.helio.taverna.helio_taverna_suite.common.*;

import java.net.URL;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;


//org/helio/taverna/helio_taverna_suite/common/TapQueryHelperData
import org.helio.taverna.helio_taverna_suite.common.TapQueryHelperData;

public class TapHelioActivity extends
		AbstractAsynchronousActivity<TapHelioActivityConfigurationBean>
		implements AsynchronousActivity<TapHelioActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private TapHelioActivityConfigurationBean configBean;

	@Override
	public void configure(TapHelioActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		this.configBean = configBean;
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();
		// FIXME: Replace with your input and output port definitions
		describeTapInputs();
		describeTapOutputs();

	}
	
	private void describeTapInputs()  {
		addInput("URL or Registry Ivorn",1,true,null,String.class);

		if(configBean.getInterfaceType() == 2) {
			addInput("QueryString",0,true,null,String.class); 
		}

		if(configBean.getInterfaceType() < 2) {
			addInput("From",0,true,null,String.class);
			addInput("Optional StartTime",0,true,null,String.class);
			addInput("Optional EndTime",0,true,null,String.class);
			addInput("Optional Where",0,true,null,String.class);
		}
		//don't add urloutput for soap requests.
		if(configBean.getInterfaceType() != 1) {
			addInput("URL Output Reference Only",0,true,null,Boolean.class);
		}
	}
	
	
	private void describeTapOutputs(){	
		addOutput("Votable", 1);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public void run() {
				InvocationContext context = callback.getContext();
				ReferenceService referenceService = context.getReferenceService();
				// Resolve inputs 
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

				Set<String> inputKeys = inputs.keySet();
	    		String []tmpKeys = inputKeys.toArray(new String[0]);
	    		//for(int k = 0;k < tmpKeys.length;k++) {
	    		//	System.out.println("keyset k = " + k + " value = " + tmpKeys[k]);
	    		//}

				
				String startTime = null;
				String endTime = null;
				String where = null;
				String from = null;
				Boolean ref = null;
				String queryString = null;
				if(configBean.getInterfaceType() == 2) {
					queryString = (String) referenceService.renderIdentifier(inputs.get("QueryString"), String.class, context);

					
				}
				startTime = null;
				if(inputs.containsKey("Optional StartTime")) {
					startTime = (String) referenceService.renderIdentifier(inputs.get("Optional StartTime"), String.class, context);	
				}
				endTime = null;
				if(inputs.containsKey("Optional EndTime")) {
					endTime = (String) referenceService.renderIdentifier(inputs.get("Optional EndTime"), String.class, context);
				}
				where = null;
				if(inputs.containsKey("Optional Where")) {
					where = (String) referenceService.renderIdentifier(inputs.get("Optional Where"), String.class, context);
				}
				
				from = (String) referenceService.renderIdentifier(inputs.get("From"), String.class, context);
				
				if(inputs.containsKey("URL Output Reference Only")) {
					//
					ref = (Boolean) referenceService.renderIdentifier(inputs.get("URL Output Reference Only"), Boolean.class, context);
				}
					    		
	    		//System.out.println("ref = " + ref);
	    		
	    		List<String> url_ivorn = (List<String>)referenceService.renderIdentifier(inputs.get("URL or Registry Ivorn"), String.class, context);
	    		//System.out.println("urlivorn length = " + url_ivorn.size());
			    List<String> resultVotable = new ArrayList();

			    String urlQuery = "";
	    		for(int j = 0;j < url_ivorn.size();j++) {
	    			boolean error = false;
	    			String testLook = url_ivorn.get(j);
	    			//System.out.println("testlook: " + testLook);
	    			urlQuery="http://www.test.com/test.jsp";
	    			//System.out.println("SIZE OF STRING ARRAY: "  + testLook.length);
	    			
		    		if(url_ivorn.get(j).startsWith("ivo")) {
		    			TapQueryHelperData td = TapQueryHelper.getQueryHelperInfo(url_ivorn.get(j),RegistryUtil.getRegistry());
		    			if(td != null) {
		    				if(configBean.getInterfaceType()==0) {
		    					urlQuery = td.getRestURL();
		    				}else if(configBean.getInterfaceType() == 1) {
		    					//get soap url
		    					urlQuery = td.getSoapURL();
		    				}else if(configBean.getInterfaceType() == 2) {
		    					urlQuery = td.getIVOAURL();
		    				}else {
		    					System.out.println("ERROR NO InterfaceType found");
		    				}
		    				if(urlQuery == null) {
		    					System.out.println("NO URL FOUND.");
		    				}
		    			}else {
		    				//throw something could not find a tap ivorn
		    			}
		    			
		    			if(url_ivorn == null || url_ivorn.size() == 0) {
		    				//thorw something could not discover a url from ivorn given.
		    			}
		    		}else if(url_ivorn.get(j).startsWith("http")) {
		    			urlQuery = url_ivorn.get(j);
		    		}else {
		    			urlQuery = "MUST HAVE http:// or ivo:// for URL/IVORN parameter";
		    			error = true;
		    			ref = true;
		    		}
		    		
		    		if(configBean.getInterfaceType() == 2) {
			    		if(urlQuery != null && !error && !urlQuery.endsWith("sync") ) {
		    				if(!urlQuery.endsWith("sync/") ) {
			    				if(!urlQuery.endsWith("/")) {
			    					urlQuery +=  "/sync";
			    				}else {
			    					urlQuery +=  "sync";
			    				}//else
		    				}//if
		    			}//if
		    		}
		    		
		    		if(configBean.getInterfaceType() == 0) {
		    			//REST style
		    			urlQuery +="?FROM=" + from;
		    			if(startTime != null && endTime != null) { urlQuery += "&STARTTIME=" + startTime + "&ENDTIME=" + endTime;}
		    			if(where != null) {
		    				if(where.split(",").length >=2) {
		    					urlQuery += "WHERE=" + where;
		    				}else {
		    					urlQuery += "SQLWHERE=" + where;
		    				}
		    			}
		    		}
		    		
					try {
	
						if(configBean.getInterfaceType() == 2) {
							urlQuery += "?REQUEST=doQuery&LANG=ADQL&FORMAT=VOTABLE&QUERY=" + java.net.URLEncoder.encode(queryString,"UTF-8");
						}
						
					    //System.out.println("Resulting URL:  " + urlQuery.toString().toString() + " Interface Type: " + configBean.getInterfaceType());

						if(ref != null && ref.equals(true)) {
							//System.out.println("ok adding urlquery, ref true");
							resultVotable.add(urlQuery.toString());
						}else if(configBean.getInterfaceType() == 0 || configBean.getInterfaceType() == 2) {
							//System.out.println("ref false so grabbign votable.");
						    StringWriter resultOutput = new StringWriter();
						    TransformerFactory.newInstance().newTransformer().transform(new StreamSource(new URL(urlQuery.toString()).openStream()), new StreamResult(resultOutput));
						    resultVotable.add(resultOutput.toString());
						}else if(configBean.getInterfaceType() == 1) {
							//do soap request
						}else {
							//System.out.println("ERROR interfaceType not 0-2");
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
	    		}//for
	    		
				T2Reference simpleRef2 = referenceService.register(resultVotable , 1, true, context);
				outputs.put("Votable", simpleRef2);
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public TapHelioActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
