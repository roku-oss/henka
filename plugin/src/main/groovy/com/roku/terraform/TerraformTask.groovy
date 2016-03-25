package com.roku.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

class TerraformTask extends DefaultTask {
    String tfDir
    String tfVarFile
    String tfAction = "plan"

    String tfConfS3Region = "us-east-1"
    String tfConfS3Bucket
    String tfConfS3Key
    String tfAwsAccessKey
    String tfAwsSecretKey

    @TaskAction
    def terraform() {
        def tfVarFilePath = new File(tfVarFile).absolutePath

        executeCommand(['rm', '-rf', '.terraform'])
        executeCommand(['bash', '-c', "terraform remote config " +
                "-backend=s3 " +
                "-backend-config=bucket=$tfConfS3Bucket " +
                "-backend-config=key=$tfConfS3Key " +
                "-backend-config=region=$tfConfS3Region".toString()])
        executeCommand(['bash', '-c', "terraform $tfAction -var-file='${tfVarFilePath}' .".toString()])
        executeCommand(['bash', '-c', 'terraform remote push'])
    }

    private void executeCommand(ArrayList<String> command) {
        println command

        def pb = new ProcessBuilder(command)
        pb.environment().put("AWS_ACCESS_KEY_ID", tfAwsAccessKey)
        pb.environment().put("AWS_SECRET_ACCESS_KEY", tfAwsSecretKey)

        def process = pb
                .directory(new File(tfDir))
                .redirectErrorStream(true)
                .start()
        process.text.eachLine { println it }
        if (process.exitValue() != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + process.exitValue(), null)
        }
    }

}
