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
package com.roku.henka.executors

import com.roku.henka.TerraformTask
import org.gradle.api.GradleScriptException

class TerraformExecutor {

    final GString terraformPath
    private final BashExecutor executor;

    TerraformExecutor() {
        this("")
    }

    TerraformExecutor(GString terraformPath) {
        this.terraformPath = terraformPath
        executor = new BashExecutor()
    }

    TerraformExecutor(GString terraformPath, BashExecutor executor) {
        this.terraformPath = terraformPath
        this.executor = executor
    }

    String terraformInit(String tfInitParams) {
        return terraformPath + "terraform init " + tfInitParams;
    }


    def execute(TerraformTask task, String tfAction, String tfInitParams) {
        executor.execute(terraformInit(tfInitParams), task.tfDir)
        int exitCode = executor.execute("$terraformPath"+"terraform ${tfAction}".toString(), task.tfDir)

        throwOnFailure(exitCode)
    }

    private void throwOnFailure(int exitCode) {
        if (exitCode != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + exitCode, null)
        }
    }
}
