/*
* Copyright (c) 2016, Henka Contributors
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
import com.roku.henka.executors.TerraformExecutorFactory
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

/**
 * Defines TerraforTask type. Properties (accepted on task or project level):
 * <ul>
 *     <li> tfDir </li>
 *     <li> tfVarFile </li>
 *     <li> tfAction </li>
 *     <li> tfConfS3Region </li>
 *     <li> tfConfS3Bucket </li>
 *     <li> tfConfS3Key </li>
 *     <li> tfConfS3KmsKey </li>
 *     <li> tfFailOnPlanChanges </li>
 *  </ul>
 *
 */
class TerraformTask extends DefaultTask {
    private final BashExecutor executor;

    String tfDir
    String tfVarFile
    String tfAction

    String tfConfS3Region = "us-east-1"
    String tfConfS3Bucket
    String tfConfS3Key
    String tfConfS3KmsKey
    Boolean tfFailOnPlanChanges = false

    @Inject
    TerraformTask() {
        executor = new BashExecutor();
    }

    TerraformTask(BashExecutor executor) {
        this.executor = executor
    }

    @TaskAction
    /**
     * Parses properties and calls an appropriate TerraformExecutor, which is responsible for executing the
     * right Terraform command (plan, apply or refresh)
     */
    def terraform() {
        populateProperties()
        TerraformExecutor tfExecutor = TerraformExecutorFactory.createFor(tfAction)
        tfExecutor.execute(this)
    }

    private void populateProperties() {
        tfDir = getPropertyFromTaskOrProject(tfDir, "tfDir")
        tfAction = getPropertyFromTaskOrProject(tfAction, "tfAction")
        tfVarFile = getPropertyFromTaskOrProject(tfVarFile, "tfVarFile")
        tfConfS3Key = getPropertyFromTaskOrProject(tfConfS3Key, "tfConfS3Key")
        tfConfS3Bucket = getPropertyFromTaskOrProject(tfConfS3Bucket, "tfConfS3Bucket")
        tfConfS3Region = getPropertyFromTaskOrProject(tfConfS3Region, "tfConfS3Region")
        tfConfS3KmsKey = getPropertyFromTaskOrProject(tfConfS3KmsKey, "tfConfS3KmsKey")
        tfFailOnPlanChanges = new Boolean(getPropertyFromTaskOrProject(tfFailOnPlanChanges, "tfFailOnPlanChanges"))

        tfVarFile = new File(tfVarFile).absolutePath
    }

    String getPropertyFromTaskOrProject(Object taskProperty, String propertyName) {
        if (taskProperty == null && !project.hasProperty(propertyName)) {
            throw new GradleScriptException("$propertyName should be defined either as a task- or a project-level property ", null)
        }
        return taskProperty == null ? project.property(propertyName) : taskProperty
    }

}
