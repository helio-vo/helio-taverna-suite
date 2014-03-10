package org.helio.taverna.helio_taverna_suite;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.helio.taverna.helio_taverna_suite.common.UWSCaller;
import org.helio.taverna.helio_taverna_suite.common.AppInterfaces;
import org.helio.taverna.helio_taverna_suite.common.AppParameters;

import java.io.File;
import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import java.util.Hashtable;
import java.util.Enumeration;


public class CEAActivity extends
		AbstractAsynchronousActivity<CEAActivityConfigurationBean>
		implements AsynchronousActivity<CEAActivityConfigurationBean> {

	
	private CEAActivityConfigurationBean configBean;

	@Override
	public void configure(CEAActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;
		System.out.println("configbean in ceaactivity identi = " + configBean.getCeaIvorn());

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs
		// REQUIRED: (Re)create input/output ports depending on configuration
		if(configBean.getAppInterface() != null) {
			configurePorts();
		}else {
			//if(configBean)
			configureUWSRECOVERPorts();
		}
	}
	
	protected void configureUWSRECOVERPorts() {
		removeInputs();
		removeOutputs();
		addInput("UWSExecutionIDURL", 0, true, null, String.class);
		addOutput("UWSResults", 1);
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();

		// FIXME: Replace with your input and output port definitions
		
		try { 
			AppInterfaces ai = configBean.getAppInterface();
			AppParameters []inputParams = ai.getInputParameters();
			AppParameters []outputParams = ai.getOutputParameters();
			for(int i = 0;i < inputParams.length;i++) {
				
				if(inputParams[i].getMax() == 0 || inputParams[i].getMax() > 1) {
					addInput(inputParams[i].getUIName(), 1, true, null, List.class);
				}else {
					addInput(inputParams[i].getUIName(), 0, true, null, String.class);
				}
			}
			addInput("Optional Abort in Minutes", 0, true, null, Integer.class);
			//addInput("Optional CeaService Ivorn", 0, true, null, String.class);
			
			for(int i = 0;i < outputParams.length;i++) {
				//if(outputParams[j].getMax() == 0 || outputParams[j].getMax() > 1) {
				//	addInput(outputParams[j].getUIName(), 1, true, null, List.class);
				//}else {
					//addOutput("Ref URL " + outputParams[j].getUIName(), 0);
					addOutput(outputParams[i].getUIName(), 0);
				//}
			}
			addOutput("ExecutionID",0);
					
		}catch(Exception e) {
			e.printStackTrace();
		}

			//addOutput("ExecutionID", 0);
			
			//addOutput("ExecutionInformation", 0);
			
			//addOutput("ResultList", 1);

	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public void run() {
				InvocationContext context = callback
						.getContext();
				ReferenceService referenceService = context
						.getReferenceService();
			
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();	
				Hashtable<String,String> results = null;
				String uwsLocation = "";
				if(configBean.getAppInterface() == null) {
					try {
						uwsLocation = (String) referenceService.renderIdentifier(inputs.get("UWSExecutionIDURL"), String.class, context);
						results = UWSCaller.uwsCaller(uwsLocation);
						String uwsrecoverResults = "";
						List<String> resultList = new ArrayList<String>();
						for (Enumeration e = results.keys() ; e.hasMoreElements() ;) {
					    	String key = (String)e.nextElement();
					    	String urlVal = results.get(key);
					    	resultList.add(urlVal);
					    	uwsrecoverResults += key + "=" + urlVal;
					    }					
						T2Reference simpleRef2 = referenceService.register(resultList, 1, true, context);
						//T2Reference simpleRef2 = referenceService.register(uwsrecoverResults, 0, true, context);
						outputs.put("UWSResults",simpleRef2);
						//outputs.put("UWSResults",resultList);
						
						// return map of output data, with empty index array as this is
						// the only and final result (this index parameter is used if
						// pipelining output)
						callback.receiveResult(outputs, new int[0]);
					}catch(Exception e) {
						e.printStackTrace();
					}
					return;

				}
				String toolXMLStr = "<tool xmlns=\"http://www.ivoa.net/xml/CEA/types/v1.2\" ";
				toolXMLStr += "id=\"" + configBean.getAppInterface().getIVOAIdent() + "\" ";
				toolXMLStr += "interface=\"" + configBean.getAppInterface().getName() + "\">";
				AppParameters []ap = null;
				if(configBean.getAppInterface().getInputParameters().length > 0) {
					toolXMLStr += "<input>";
					ap = configBean.getAppInterface().getInputParameters();
					//ReferenceService referenceService = context.getReferenceService();
					String ceaInput;
					boolean indirect = false;
					for(int i = 0;i < ap.length;i++) {
						indirect = false;
						Iterator iterDebug = inputs.keySet().iterator();
						while(iterDebug.hasNext()) {
							System.out.println("input keys = " + iterDebug.next());
						}
						System.out.println("uiname = " + ap[i].getUIName());
						//ceaInput = (String) referenceService.renderIdentifier(inputs.get("CeaAppIvorn"), String.class, context);
						if(inputs.containsKey(ap[i].getUIName())) {
							ceaInput = (String) referenceService.renderIdentifier(inputs.get(ap[i].getUIName()), String.class, context);
							 if(ceaInput.startsWith("http://") || 
								ceaInput.startsWith("ftp://") ||
								ceaInput.startsWith("ivo://")  ||
								ceaInput.startsWith("vos://")) {
								 toolXMLStr += "<parameter id=\"" + ap[i].getId() + "\" indirect=\"true\">"; 
							 }else {
								 toolXMLStr += "<parameter id=\"" + ap[i].getId() + "\" indirect=\"false\">"; 
							 }
							 toolXMLStr += "<value>" + ceaInput + "</value></parameter>";
						}
					}//for
					toolXMLStr += "</input>";
				}
				
				if(configBean.getAppInterface().getOutputParameters().length > 0) {
					/*
					toolXMLStr += "<output>";
					ap = configBean.getAppInterface().getOutputParameters();
					for(int i = 0;i < ap.length;i++) {
						 toolXMLStr += "<parameter id=\"" + ap[i].getId() + "\"><value></value></parameter>";
					}//for
					toolXMLStr += "</output>";
					*/
				}//if
				toolXMLStr += "</tool>";
				java.io.File toolFile = null;
				try {
					System.out.println("tool xml: " + toolXMLStr);
				StringReader sr = new StringReader(toolXMLStr);
				toolFile = java.io.File.createTempFile("taverna",".wkfl");
				toolFile.deleteOnExit();
				System.out.println("toolfile tostring: " + toolFile.toString());
				TransformerFactory.newInstance().newTransformer().transform(new StreamSource(sr), new StreamResult(new java.io.FileOutputStream(toolFile)));
				//results = UWSCaller.uwsCaller(configBean.getAppInterface().getManagedAppServiceLocation(),toolFile);
				uwsLocation = UWSCaller.uwsCaller(configBean.getAppInterface().getManagedAppServiceLocation(),toolFile);
				
				results = UWSCaller.uwsCaller(uwsLocation);
				T2Reference simpleRef3 = referenceService.register(uwsLocation, 0, true, context);
				outputs.put("ExecutionID",simpleRef3);
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				//callback.receiveResult(outputs, new int[0]);

				}catch(Exception e) {
					e.printStackTrace();
					//return null;
				}
				
				//Call Registry for url
				
				if(results.size() == 0) {
					System.out.println("results size = " + results.size());

					//A cheat for now.
					ap = configBean.getAppInterface().getOutputParameters();
					for(int i = 0;i < ap.length;i++) {
						 toolXMLStr += "<parameter id=\"" + ap[i].getId() + "\"><value></value></parameter>";
						 String uwsLocTmp = uwsLocation;
						 if(uwsLocTmp.indexOf("uws/jobs") != -1) {
							 uwsLocTmp = uwsLocTmp.substring(0,uwsLocTmp.indexOf("uws/jobs"));
							 //uwsLocTmp += uwsLocation.substring(uwsLocation.indexOf("uws/jobs")+8);
						 }
						 System.out.println("uwsloctmp = " + uwsLocTmp);
						 T2Reference simpleRefHack = referenceService.register(uwsLocTmp+"jobs"+uwsLocation.substring(uwsLocation.indexOf("uws/jobs")+8) + "/" + ap[i].getId(), 0, true, context);
						 System.out.println("placing uiname = " + ap[i].getUIName() + " with value = " + uwsLocation+"/jobs/" + ap[i].getId());
						 outputs.put(ap[i].getUIName(), simpleRefHack);
					}//for
					
					
				}else {
				    for (Enumeration e = results.keys() ; e.hasMoreElements() ;) {
				    	String key = (String)e.nextElement();
				    	String urlVal = results.get(key);
				    	ap = configBean.getAppInterface().getOutputParameters();
				    	System.out.println("key = " + key + " url = " + urlVal + " and aplength = " + ap.length);			    	
				    	for(int i = 0;i < ap.length;i++) {
				    		System.out.println("ap id = " + ap[i].getId());
				    		if(ap[i].getId().equals(key)) {
				    			T2Reference simpleRef = referenceService.register(urlVal, 0, true, context);
				    			System.out.println("placing uiname = " + ap[i].getUIName() + " with value = " + urlVal);
								outputs.put(ap[i].getUIName(), simpleRef);
				    		}
				    	}
				         //System.out.println(e.nextElement());
				    }
				}
			    System.out.println("now do callback, all finished");
				//j
				//TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(new java.io.FileOutputStream(toolFile)));
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
				
			}
		});
	}

	@Override
	public CEAActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
