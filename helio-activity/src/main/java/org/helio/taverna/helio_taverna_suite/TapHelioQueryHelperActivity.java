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

//import org.helio.taverna.helio_taverna_suite.common.TapQueryHelperData;
import org.helio.taverna.helio_taverna_suite.common.TapQueryHelperData;

public class TapHelioQueryHelperActivity extends
		AbstractAsynchronousActivity<TapHelioQueryHelperActivityConfigurationBean>
		implements AsynchronousActivity<TapHelioQueryHelperActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private TapHelioQueryHelperActivityConfigurationBean configBean;

	@Override
	public void configure(TapHelioQueryHelperActivityConfigurationBean configBean)
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
		if(this.configBean == null) {
			System.out.println("configbean is null in queryhelperconst");
		}else {
			System.out.println("good: configbean is not null in queryhelperconst");
		}

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
		;
		// FIXME: Replace with your input and output port definitions
		if(configBean.getTapQueryHelperData().getRestURL() != null) {
			//REST URL
			addOutput("Helio TAP URL",0);
		}
		if(configBean.getTapQueryHelperData().getSoapURL() != null) {
			addOutput("Helio SOAP URL",0);
		}
		if(configBean.getTapQueryHelperData().getIVOAURL() != null) {
			addOutput("IVOA TAP URL",0);
		}
		addOutput("IVOA ID",0);
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
			
				String where = "";
				String []tmp;
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

				//System.out.println("url ref: " + configBean.getTapQueryHelperData().getURL().trim());
				
				// FIXME: Replace with your input and output port definitions
				if(configBean.getTapQueryHelperData().getRestURL() != null) {
					T2Reference urlRef = referenceService.register(configBean.getTapQueryHelperData().getRestURL().trim(), 0, true, context);
					outputs.put("Helio TAP URL", urlRef);
				}else if(configBean.getTapQueryHelperData().getSoapURL() != null) {
					T2Reference urlRef = referenceService.register(configBean.getTapQueryHelperData().getSoapURL().trim(), 0, true, context);
					outputs.put("Helio SOAP URL", urlRef);
				}else if(configBean.getTapQueryHelperData().getIVOAURL() != null) {
					T2Reference urlRef = referenceService.register(configBean.getTapQueryHelperData().getSoapURL().trim(), 0, true, context);
					outputs.put("IVOA TAP URL", urlRef);
				}
				T2Reference urlRef = referenceService.register(configBean.getTapQueryHelperData().getIdentifier().trim(), 0, true, context);
				outputs.put("IVOA ID", urlRef);
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public TapHelioQueryHelperActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
