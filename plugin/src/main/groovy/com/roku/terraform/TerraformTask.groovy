package com.roku.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

class TerraformTask extends DefaultTask {
    public static final String PLAN = "plan"
    public static final String APPLY = "apply"

    String tfDir
    String tfVarFile
    String tfAction = PLAN

    String tfConfS3Region = "us-east-1"
    String tfConfS3Bucket
    String tfConfS3Key
    String tfAwsAccessKey
    String tfAwsSecretKey

    @TaskAction
    def terraform() {
        tfDir           = getPropertyFromTaskOrProject(tfDir,           "tfDir")
        tfAction        = getPropertyFromTaskOrProject(tfAction,        "tfAction")
        tfVarFile       = getPropertyFromTaskOrProject(tfVarFile,       "tfVarFile")
        tfConfS3Key     = getPropertyFromTaskOrProject(tfConfS3Key,     "tfConfS3Key")
        tfConfS3Bucket  = getPropertyFromTaskOrProject(tfConfS3Bucket,  "tfConfS3Bucket")
        tfAwsAccessKey  = getPropertyFromTaskOrProject(tfAwsAccessKey,  "tfAwsAccessKey")
        tfAwsSecretKey  = getPropertyFromTaskOrProject(tfAwsSecretKey,  "tfAwsSecretKey")

        def tfVarFilePath = new File(tfVarFile).absolutePath

        executeCommand(['rm', '-rf', '.terraform'])
        executeCommand(['bash', '-c', "terraform remote config " +
                "-backend=s3 " +
                "-backend-config=bucket=$tfConfS3Bucket " +
                "-backend-config=key=$tfConfS3Key " +
                "-backend-config=region=$tfConfS3Region".toString()])
        executeCommand(['bash', '-c', "terraform $tfAction -var-file='${tfVarFilePath}' .".toString()])
        if (APPLY.equals(tfAction)) {
            executeCommand(['bash', '-c', 'terraform remote push'])
        }
    }

    def getPropertyFromTaskOrProject(String taskProperty, String propertyName) {
        if (taskProperty == null && !project.hasProperty(propertyName)) {
            throw new GradleScriptException("$propertyName should be defined either as a task- or a project-level property ", null)
        }
        return taskProperty == null ? project.property(propertyName) : taskProperty
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

        def reader = new BufferedReader(new InputStreamReader(process.inputStream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                println line;
            }
        } finally {
            reader.close()
        }
        process.waitFor()
        if (process.exitValue() != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + process.exitValue(), null)
        }
    }

}
