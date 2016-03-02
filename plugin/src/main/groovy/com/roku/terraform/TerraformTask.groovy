package com.roku.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

class TerraformTask extends DefaultTask {
    String tfDir
    String tfVarFile
    String tfAction = "plan"

    String tfConfS3Bucket
    String tfConfS3Key

    @TaskAction
    def terraform() {
        def tfVarFilePath = new File(tfVarFile).absolutePath

        def command1 = ["bash", "-c", "terraform remote config " +
                "-backend=s3 -backend-config=\"bucket=$tfConfS3Bucket\" " +
                "-backend-config=\"key='$tfConfS3Key' " +
                "-backend-config=\"region=us-east-1\"".toString()]
        executeCommand(command1)

        def command2 = ["bash", "-c", "terraform $tfAction -var-file='${tfVarFilePath}' .".toString()]
        executeCommand(command2)
    }

    private void executeCommand(ArrayList<String> command) {
        println command
        def process = new ProcessBuilder(command)
                .directory(new File(tfDir))
                .redirectErrorStream(true)
                .start()
        process.text.eachLine { println it }
        if (process.exitValue() != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + process.exitValue(), null)
        }
    }

}
