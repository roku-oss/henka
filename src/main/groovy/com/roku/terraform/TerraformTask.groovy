package com.roku.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TerraformTask extends DefaultTask {
    String tfDir
    String tfVarFile
    String tfAction = "plan"

    String tfConfS3Bucket
    String tfConfS3Key

    @TaskAction
    def terraform() {
        //TODO: verify that AWS and terraform are present in the path
        //TODO: add terraform version verification
        tfVarFilePath = new File(tfVarFile).absolutePath
        command = """cd $tfDir; \
terraform remote config -backend=s3 \
    -backend-config="bucket=$tfConfS3Bucket" \
    -backend-config="key="$tfConfS3Key" \
    -backend-config="region=us-east-1; \
terraform $tfAction -var-file=$tfVarFilePath ."""

        command.execute()
    }

}
