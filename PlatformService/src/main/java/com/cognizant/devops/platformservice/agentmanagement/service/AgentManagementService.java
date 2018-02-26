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

package com.cognizant.devops.platformservice.agentmanagement.service;

import java.util.List;

import com.google.gson.JsonObject;


public interface AgentManagementService {

	public String registerAgent(String toolName,String agentVersion,String osversion,String configDetails);
	public String installAgent(String agentId,String toolName,String fileName,String osversion);
	public String startStopAgent(String agentId,String action);
	public String updateAgent(String agentId, String configDetails, String toolName, String agentVersion, String osversion);
	
	//This is used during Agent registration, provide list of Agents, with version from docroot.
	public JsonObject getAgentDetails();
	//For agent registration, gives you RAW config.json from docroot
	public JsonObject getConfigFile(String version, String tool);
	
	//Provides currently registered Agents in DB
	public List<AgentConfigTO> getRegisteredAgents();
	//Returns config.json for request AgentId from Db
	public AgentConfigTO getAgentDetails(String agentId);
}
 