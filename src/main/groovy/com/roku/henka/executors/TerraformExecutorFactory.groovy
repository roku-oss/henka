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

import org.gradle.api.GradleScriptException

 class TerraformExecutorFactory {

     static TerraformExecutor createFor(String action) {
         createFor("", action)
     }


     static TerraformExecutor createFor(String terraformPath, String action) {
        switch (action) {
            case "plan":
                return new PlanTerraformExecutor(terraformPath)
            case "apply":
                return new ApplyTerraformExecutor(terraformPath)
            case "refresh":
                return new RefreshTerraformExecutor(terraformPath)
            default:
                throw new GradleScriptException("Unknown tfAction: "+action, null)
        }
    }
}
