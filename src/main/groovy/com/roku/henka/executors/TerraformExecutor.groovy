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

abstract class TerraformExecutor {

    public static String cleanTerraformConfig() {
        return "rm -rf .terraform"
    }

    public static String terraformRemoteConfigFor(TerraformTask task) {
        return "terraform remote config " +
                " -backend=s3" +
                " -backend-config=\"encrypt=true\"" +
                " -backend-config=\"bucket=$task.tfConfS3Bucket\"" +
                " -backend-config=\"key=$task.tfConfS3Key\"" +
                " -backend-config=\"kms_key_id=$task.tfConfS3KmsKey\"" +
                " -backend-config=\"region=$task.tfConfS3Region\"".toString()
    }

    abstract def execute(TerraformTask task)
}
