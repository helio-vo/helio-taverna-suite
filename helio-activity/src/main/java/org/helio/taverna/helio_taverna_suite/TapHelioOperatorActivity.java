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

import java.net.URL;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class TapHelioOperatorActivity extends
		AbstractAsynchronousActivity<TapHelioOperatorActivityConfigurationBean>
		implements AsynchronousActivity<TapHelioOperatorActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private TapHelioOperatorActivityConfigurationBean configBean;

	@Override
	public void configure(TapHelioOperatorActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		/*
		if (configBean.getExampleString().equals("invalidExample")) {
			throw new ActivityConfigurationException(
					"Example string can't be 'invalidExample'");
		}
		*/
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs

		// myClient = new MyClient(configBean.getExampleUri());
		// this.service = myClient.getService(configBean.getExampleString());

		
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();
		// FIXME: Replace with your input and output port definitions
		describeTapQueryHelperInputs();
		describeTapQueryHelperOutputs();

	}
	
	private void describeTapQueryHelperInputs()  {
		addInput("Query Input1",0,true,null,String.class);		
		addInput("Query Input2",0,true,null,String.class);
		addInput("Optional Query Input3",0,true,null,String.class);
		addInput("Optional Query Input4",0,true,null,String.class);
		addInput("Optional Query Input5",0,true,null,String.class);
	}
	
	
	private void describeTapQueryHelperOutputs(){	
		addOutput("QueryString", 0);
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
				String select = "SELECT ALL WHERE ";
				//String where = "SELECT ALL WHERE ";
				String []tmp;
				String tmpInput;
				String query1 = (String) referenceService.renderIdentifier(inputs.get("Query Input1"), String.class, context);
				int whereIndex = query1.toUpperCase().indexOf("WHERE");
				if(whereIndex >= 0) {
					query1 = query1.substring(whereIndex+5);
				}
				
				String query2 = (String) referenceService.renderIdentifier(inputs.get("Query Input2"), String.class, context);
				whereIndex = query2.toUpperCase().indexOf("WHERE");
				if(whereIndex >= 0) {
					query2 = query2.substring(whereIndex+5);
				}
				
				String whereStr = query1 + " " + configBean.getOperator() + " " + query2 + " ";
				
				if(inputs.containsKey("Optional Query Input3")){
					query1 = (String) referenceService.renderIdentifier(inputs.get("Optional Query Input3"), String.class, context);
					whereIndex = query1.toUpperCase().indexOf("WHERE");
					if(whereIndex >= 0) {
						query1 = query1.substring(whereIndex+5);
					}

					whereStr += configBean.getOperator() + " " + query1;
				}
				if(inputs.containsKey("Optional Query Input4")){
					query1 = (String) referenceService.renderIdentifier(inputs.get("Optional Query Input4"), String.class, context);
					whereIndex = query1.toUpperCase().indexOf("WHERE");
					if(whereIndex >= 0) {
						query1 = query1.substring(whereIndex+5);
					}

					whereStr += configBean.getOperator() + " " + query1;					
				}
				if(inputs.containsKey("Optional Query Input5")){
					query1 = (String) referenceService.renderIdentifier(inputs.get("Optional Query Input5"), String.class, context);
					whereIndex = query1.toUpperCase().indexOf("WHERE");
					if(whereIndex >= 0) {
						query1 = query1.substring(whereIndex+5);
					}

					whereStr += configBean.getOperator() + " " + query1;		
					//whereStr += configBean.getOperator() + " " + (String) referenceService.renderIdentifier(inputs.get("Optional Query Input5"), String.class, context);
				}
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

				try {
					T2Reference simpleRef = referenceService.register(select + " (" + whereStr + ") ", 0, true, context);
					outputs.put("QueryString", simpleRef);
				}catch(Exception e) {
					e.printStackTrace();
				}				
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public TapHelioOperatorActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
