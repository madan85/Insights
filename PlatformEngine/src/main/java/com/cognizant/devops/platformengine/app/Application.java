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
package com.cognizant.devops.platformengine.app;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.cognizant.devops.platformcommons.config.ApplicationConfigCache;
import com.cognizant.devops.platformcommons.config.ApplicationConfigProvider;
import com.cognizant.devops.platformengine.modules.aggregator.EngineAggregatorModule;
import com.cognizant.devops.platformengine.modules.correlation.EngineCorrelatorModule;
import com.cognizant.devops.platformengine.modules.mapper.ProjectMapperModule;

/**
 * Engine execution will start from Application. 1. Load the iSight config 2.
 * Initialize Publisher and subscriber modules 3. Initialize Correlation Module.
 */
public class Application {
	private static Logger log = Logger.getLogger(Application.class.getName());
	
	private static int defaultInterval = 600;
	private Application(){
		
	}
	
	public static void main(String[] args) {
		if(args.length > 0){
			defaultInterval = Integer.valueOf(args[0]);
		}

		// Load isight config
		ApplicationConfigCache.loadConfigCache();
		// Create Default users
		//new EngineUsersModule().onboardDefaultUsers();
		ApplicationConfigProvider.performSystemCheck();
		// Create correlation nodes
		//new EngineCorrelationNodeBuilderModule().initializeCorrelationNodes();
		
		// Subscribe for desired events.
		JobDetail aggrgatorJob = JobBuilder.newJob(EngineAggregatorModule.class)
				.withIdentity("EngineAggregatorModule", "iSight")
				.build();

		Trigger aggregatorTrigger = TriggerBuilder.newTrigger()
				.withIdentity("EngineAggregatorModuleTrigger", "iSight")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(defaultInterval)
						.repeatForever())
				.build();

		// Schedule the Correlation Module.
		JobDetail correlationJob = JobBuilder.newJob(EngineCorrelatorModule.class)
				.withIdentity("EngineCorrelatorModule", "iSight")
				.build();

		Trigger correlationTrigger = TriggerBuilder.newTrigger()
				.withIdentity("EngineCorrelatorModuleTrigger", "iSight")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(defaultInterval)
						.repeatForever())
				.build();
		
		// Schedule the Project Mapping Module.
		JobDetail projectMappingJob = JobBuilder.newJob(ProjectMapperModule.class)
				.withIdentity("ProjectMapperModule", "iSight")
				.build();

		Trigger projectMappingTrigger = TriggerBuilder.newTrigger()
				.withIdentity("ProjectMapperModuleTrigger", "iSight")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(defaultInterval)
						.repeatForever())
				.build();

		// Tell quartz to schedule the job using our trigger
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(aggrgatorJob, aggregatorTrigger);
			scheduler.scheduleJob(correlationJob, correlationTrigger);
			scheduler.scheduleJob(projectMappingJob, projectMappingTrigger);
		} catch (SchedulerException e) {
			log.error(e);
		}
	}
}
