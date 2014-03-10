package org.helio.taverna.helio_taverna_suite;

import java.io.Serializable;
import java.net.URI;

/**
 * Example activity configuration bean.
 * 
 */
public class TapHelioActivityConfigurationBean implements Serializable {

	/*
	 * TODO: Remove this comment.
	 * 
	 * The configuration specifies the variable options and configurations for
	 * an activity that has been added to a workflow. For instance for a WSDL
	 * activity, the configuration contains the URL for the WSDL together with
	 * the method name. String constant configurations contain the string that
	 * is to be returned, while Beanshell script configurations contain both the
	 * scripts and the input/output ports (by subclassing
	 * ActivityPortsDefinitionBean).
	 * 
	 * Configuration beans are serialised as XML (currently by using XMLBeans)
	 * when Taverna is saving the workflow definitions. Therefore the
	 * configuration beans need to follow the JavaBeans style and only have
	 * fields of 'simple' types such as Strings, integers, etc. Other beans can
	 * be referenced as well, as long as they are part of the same plugin.
	 */
	
	private int interfaceType;

	public int getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(int interfaceType) {
		this.interfaceType = interfaceType;
	}

}