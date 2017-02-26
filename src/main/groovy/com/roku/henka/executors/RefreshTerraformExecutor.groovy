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
package com.roku.henka.executors

import com.roku.henka.TerraformTask
import groovy.transform.TypeChecked
import org.gradle.api.GradleScriptException

@TypeChecked
class RefreshTerraformExecutor extends TerraformExecutor{
    private final BashExecutor executor;

    RefreshTerraformExecutor(String terraformPath) {
        super("$terraformPath")
        this.executor = new BashExecutor();
    }

    RefreshTerraformExecutor(BashExecutor executor) {
        this.executor = executor;
    }

    @Override
    def execute(TerraformTask task) {
        executor.execute(cleanTerraformConfig(), task.tfDir)
        executor.execute(terraformRemoteConfigFor(task), task.tfDir)

        int exitCode = executor.execute("$terraformPath"+"terraform refresh -no-color -var-file='${task.tfVarFile}'".toString(), task.tfDir)

        throwOnFailure(exitCode)
    }

    private void throwOnFailure(int exitCode) {
        if (exitCode != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + exitCode, null)
        }
    }

}
