/*******************************************************************************
 * Copyright 2017 Cognizant Technology Solutions
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.cognizant.devops.insightsemail.core.util;

import java.awt.Image;
import java.io.StringWriter;
import java.net.URL;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.cognizant.devops.platformcommons.core.email.EmailConstants;
import com.google.gson.JsonArray;

public class EmailFormatter {
	
	private final static EmailFormatter emailFormatter = new EmailFormatter();
	
	private EmailFormatter() {
	}
	
	public static EmailFormatter getInstance() {
		return emailFormatter;
	}

	public StringWriter populateTemplate(JsonArray array,String templateName,Image cid) {
		StringWriter stringWriter = new StringWriter();
		Template template = initializeTemplate(templateName);
		VelocityContext context = new VelocityContext(); 
		context.put(EmailConstants.ACCORDIANDATA,array);
		context.put("cid",cid);
		template.merge(context,stringWriter);
		System.out.println(stringWriter.toString());
		return stringWriter;
	}

	private Template initializeTemplate(String templateName) {
		VelocityEngine velocityEngine=new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,EmailConstants.CLASSPATH);
		velocityEngine.setProperty(EmailConstants.LOADER,ClasspathResourceLoader.class.getName());
		velocityEngine.init();      
		Template template = velocityEngine.getTemplate(templateName);
		return template;
	}
}