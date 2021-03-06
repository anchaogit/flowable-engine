/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.cmmn.test;

import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.cmmn.engine.CmmnHistoryService;
import org.flowable.cmmn.engine.CmmnManagementService;
import org.flowable.cmmn.engine.CmmnRepositoryService;
import org.flowable.cmmn.engine.CmmnRuntimeService;
import org.flowable.cmmn.engine.CmmnTaskService;
import org.flowable.cmmn.engine.test.impl.CmmnTestRunner;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.interceptor.EngineConfigurationConstants;
import org.flowable.engine.repository.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author Joram Barrez
 */
@RunWith(CmmnTestRunner.class)
public abstract class AbstractProcessEngineIntegrationTest {

    protected static CmmnEngineConfiguration cmmnEngineConfiguration;
    protected static ProcessEngine processEngine;

    protected CmmnRepositoryService cmmnRepositoryService;
    protected CmmnRuntimeService cmmnRuntimeService;
    protected CmmnTaskService cmmnTaskService;
    protected CmmnHistoryService cmmnHistoryService;
    protected CmmnManagementService cmmnManagementService;

    protected RepositoryService processEngineRepositoryService;
    protected RuntimeService processEngineRuntimeService;
    protected TaskService processEngineTaskService;

    @BeforeClass
    public static void bootProcessEngine() {
        if (processEngine == null) {
            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable.cfg.xml").buildProcessEngine();
            cmmnEngineConfiguration = (CmmnEngineConfiguration) processEngine.getProcessEngineConfiguration()
                    .getEngineConfigurations().get(EngineConfigurationConstants.KEY_CMMN_ENGINE_CONFIG);
            CmmnTestRunner.setCmmnEngineConfiguration(cmmnEngineConfiguration);
        }
    }

    @Before
    public void setupServices() {
        this.cmmnRepositoryService = cmmnEngineConfiguration.getCmmnRepositoryService();
        this.cmmnRuntimeService = cmmnEngineConfiguration.getCmmnRuntimeService();
        this.cmmnTaskService = cmmnEngineConfiguration.getCmmnTaskService();
        this.cmmnHistoryService = cmmnEngineConfiguration.getCmmnHistoryService();
        this.cmmnManagementService = cmmnEngineConfiguration.getCmmnManagementService();

        this.processEngineRepositoryService = processEngine.getRepositoryService();
        this.processEngineRuntimeService = processEngine.getRuntimeService();
        this.processEngineTaskService = processEngine.getTaskService();
    }

    @After
    public void cleanup() {
        for (Deployment deployment : processEngineRepositoryService.createDeploymentQuery().list()) {
            processEngineRepositoryService.deleteDeployment(deployment.getId(), true);
        }
    }

}
