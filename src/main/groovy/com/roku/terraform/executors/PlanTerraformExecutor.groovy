/*
*   Copyright 2016 by Roku
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.roku.terraform.executors

import com.roku.terraform.TerraformTask
import groovy.transform.TypeChecked
import org.gradle.api.GradleScriptException

@TypeChecked
class PlanTerraformExecutor extends TerraformExecutor{
    public static final int PLAN_CHANGES_DETECTED = 2

    private final BashExecutor executor;

    PlanTerraformExecutor() {
        this.executor = new BashExecutor();
    }

    PlanTerraformExecutor(BashExecutor executor) {
        this.executor = executor;
    }

    @Override
    def execute(TerraformTask task) {
        executor.execute(cleanTerraformConfig(), task.tfDir)
        executor.execute(terraformRemoteConfigFor(task), task.tfDir)

        GString tfCommand = createTfCommand(task)
        int exitCode = executor.execute(tfCommand.toString(), task.tfDir)

        throwOnFailureOrPlanChanges(exitCode, task)

    }

    private void throwOnFailureOrPlanChanges(int exitCode, TerraformTask task) {
        if (exitCode != 0) {
            if (task.tfFailOnPlanChanges && PLAN_CHANGES_DETECTED == exitCode) {
                throw new GradleScriptException("Plan changes detected, aborting!", null)
            } else {
                throw new GradleScriptException("error while executing shell script, exit code: " + exitCode, null)
            }
        }
    }

    private GString createTfCommand(TerraformTask task) {
        def tfCommand = "terraform plan -no-color -var-file='${task.tfVarFile}'"
        if (task.tfFailOnPlanChanges) {
            tfCommand = tfCommand + " -detailed-exitcode".toString()
        }
        return tfCommand
    }
}
