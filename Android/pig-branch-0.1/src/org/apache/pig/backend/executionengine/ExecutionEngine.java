/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.pig.backend.executionengine;

import java.util.Collection;
import java.util.Properties;
import java.util.Map;

import org.apache.pig.backend.datastorage.DataStorage;

/**
 * This is the main interface that various execution engines
 * need to support and it is also the main interface that Pig
 * will need to use to submit jobs for execution, retrieve information
 * about their progress and possibly terminate them.
 *
 */

/**
 * 
 * TODO: provide a manner to generate/collect logging information for DBG purposes
 * 
 * TODO: add keys for properties/statistics
 * 
 */
public interface ExecutionEngine {
    /**
     * Place holder for possible initialization activities.
     */
    public void init() throws ExecException;

    /**
     * Clean-up and releasing of resources.
     */
    public void close() throws ExecException;

    public DataStorage getDataStorage();    
        
    /**
     * Provides configuration information about the execution engine itself.
     * 
     * @return - information about the configuration used to connect to execution engine
     */
    public Properties getConfiguration() throws ExecException;
        
    /**
     * Provides a way to dynamically change configuration parameters
     * at the Execution Engine level.
     * 
     * @param newConfiguration - the new configuration settings
     * @throws when configuration conflicts are detected
     * 
     */
    public void updateConfiguration(Properties newConfiguration) 
        throws ExecException;
        
    /**
     * Provides statistics on the Execution Engine: number of nodes,
     * node failure rates, average load, average job wait time...
     * @return ExecutionEngineProperties
     */
    public Map<String, Object> getStatistics() throws ExecException;

    /**
     * Compiles a logical plan into a physical plan, given a set of configuration
     * properties that apply at the plan-level. For instance desired degree of 
     * parallelism for this plan, which could be different from the "default"
     * one set at the execution engine level.
     * 
     * @param logical plan
     * @param properties
     * @return physical plan
     */
    public ExecPhysicalPlan compile(ExecLogicalPlan plan,
                                    Properties properties)
        throws ExecException;

    public ExecPhysicalPlan compile(ExecLogicalPlan[] plans,
                                    Properties properties)
        throws ExecException;

    /**
     * Execute the physical plan in blocking mode.
     * 
     * @throws
     */
    public ExecJob execute(ExecPhysicalPlan plan) throws ExecException;

    /**
     * Execute the physical plan in non-blocking mode
     * 
     * @throws ExecException
     */
    public ExecJob submit(ExecPhysicalPlan plan) throws ExecException;

    /**
     * Return currently running jobs (can be useful for admin purposes)
     * 
     * @return
     * @throws ExecException
     */
    public Collection<ExecJob> runningJobs(Properties properties) throws ExecException;
    
    /**
     * List scopes that are active in the back-end
     * 
     * @return
     * @throws ExecException
     */
    public Collection<String> activeScopes() throws ExecException;
    
    /**
     * A mechanism to communicate to the back-end that a set of logical plans go out of scope
     * 
     * @param scope
     */
    public void reclaimScope(String scope) throws ExecException;
}
