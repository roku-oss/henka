/*
* Copyright (c) 2016-2017, Henka Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and limitations under the License.
*/
package com.roku.henka

import com.roku.henka.executors.BashExecutor
import com.roku.henka.executors.TerraformExecutor
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

/**
 * Defines TerraforTask type. Properties (accepted on task or project level):
 * <ul>
 *     <li> tfDir </li>
 *     <li> tfAction </li>
 *     <li> installTerraform </li>
 *     <li> terraformVersion </li>
 *     <li> terraformBaseDir </li>
 *  </ul>
 *
 */
class TerraformTask extends DefaultTask {
    private final BashExecutor executor;

    String tfDir
    String tfAction
    String tfInitParams = ""

    Boolean installTerraform = false
    String terraformVersion = "0.9.2"
    String terraformBaseDir = "/opt/terraform"

    @Inject
    TerraformTask() {
        executor = new BashExecutor();
    }

    TerraformTask(BashExecutor executor) {
        this.executor = executor
    }

    /**
     * Parses properties and calls an appropriate TerraformExecutor, which is responsible for executing the
     * right Terraform command (plan, apply etc.)
     */
    @TaskAction
    def terraform() {
        if (installTerraform) {
            new TerraformInstaller().installTerraform(terraformBaseDir, terraformVersion)
        }

        populateProperties()
        TerraformExecutor tfExecutor
        if (installTerraform) {
            tfExecutor = new TerraformExecutor("$terraformBaseDir/$terraformVersion/")
        } else {
            tfExecutor = new TerraformExecutor()
        }
        tfExecutor.execute(this, tfAction, tfInitParams)
    }

    private void populateProperties() {
        tfDir = getPropertyFromTaskOrProject(tfDir, "tfDir")
        tfAction = getPropertyFromTaskOrProject(tfAction, "tfAction")
        tfInitParams = (getPropertyFromTaskOrProject(tfInitParams, "tfInitParams"))
        installTerraform = new Boolean(getPropertyFromTaskOrProject(installTerraform, "installTerraform"))
        terraformVersion = getPropertyFromTaskOrProject(terraformVersion, "terraformVersion")
        terraformBaseDir = getPropertyFromTaskOrProject(terraformBaseDir, "terraformBaseDir")
    }

    String getPropertyFromTaskOrProject(Object taskProperty, String propertyName) {
        if (taskProperty == null && !project.hasProperty(propertyName)) {
            throw new GradleScriptException("$propertyName should be defined either as a task- or a project-level property ", null)
        }
        return taskProperty == null ? project.property(propertyName) : taskProperty
    }

}
