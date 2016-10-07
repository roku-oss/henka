package com.roku.terraform.executors

import com.roku.terraform.TerraformTask

abstract class TerraformExecutor {

    public static String cleanTerraformConfig() {
        return "rm -rf .terraform"
    }

    public static String terraformRemoteConfigFor(TerraformTask task) {
        return "terraform remote config " +
                " -backend=s3" +
                " -backend-config=bucket=$task.tfConfS3Bucket" +
                " -backend-config=key=$task.tfConfS3Key" +
                " -backend-config=kms_key_id=$task.tfConfS3KmsKey" +
                " -backend-config=region=$task.tfConfS3Region".toString()
    }

    abstract def execute(TerraformTask task)
}
