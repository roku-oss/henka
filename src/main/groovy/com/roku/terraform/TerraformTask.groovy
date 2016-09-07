package com.roku.terraform

import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

class TerraformTask extends DefaultTask {
    public static final String PLAN = "plan"
    public static final int PLAN_CHANGES_DETECTED = 2

    String tfDir
    String tfVarFile
    String tfAction

    String tfConfS3Region = "us-east-1"
    String tfConfS3Bucket
    String tfConfS3Key
    String tfConfS3KmsKey
    String tfFailOnPlanChanges = "false"

    @TaskAction
    //TODO: enforce terraform version
    def terraform() {
        tfDir               = getPropertyFromTaskOrProject(tfDir,           "tfDir")
        tfAction            = getPropertyFromTaskOrProject(tfAction,        "tfAction")
        tfVarFile           = getPropertyFromTaskOrProject(tfVarFile,       "tfVarFile")
        tfConfS3Key         = getPropertyFromTaskOrProject(tfConfS3Key,     "tfConfS3Key")
        tfConfS3Bucket      = getPropertyFromTaskOrProject(tfConfS3Bucket,  "tfConfS3Bucket")
        tfConfS3Region      = getPropertyFromTaskOrProject(tfConfS3Region,  "tfConfS3Region")
        tfConfS3KmsKey      = getPropertyFromTaskOrProject(tfConfS3KmsKey,  "tfConfS3KmsKey")
        tfFailOnPlanChanges = new Boolean(getPropertyFromTaskOrProject(tfFailOnPlanChanges, "tfFailOnPlanChanges"))

        def tfVarFilePath = new File(tfVarFile).absolutePath

        initTerraformRemoteStorage(tfConfS3Bucket, tfConfS3Key, tfConfS3Region, tfConfS3KmsKey)

        List<String> tfArgs = prepareTfCommandArguments(tfAction, tfVarFilePath, tfFailOnPlanChanges)
        executeCommand(tfArgs)
        if (! PLAN.equals(tfAction)) {
            executeCommand(['bash', '-c', 'terraform remote push'])
        }
    }

    private List<String> prepareTfCommandArguments(String tfAction, tfVarFilePath, String tfFailOnPlanChanges) {
        def tfCmdArgs = " $tfAction -var-file='${tfVarFilePath}'".toString()
        if (PLAN.equals(tfAction) && tfFailOnPlanChanges == true) {
            tfCmdArgs = tfCmdArgs + " -detailed-exitcode"
        }
        return ['bash', '-c', "terraform " + tfCmdArgs + " ."]
    }

    private void initTerraformRemoteStorage(String tfConfS3Bucket, String tfConfS3Key, String tfConfS3Region, String tfConfS3KmsKey) {
        executeCommand(['rm', '-rf', '.terraform'])
        executeCommand(['bash', '-c', "terraform remote config " +
                " -backend=s3" +
                " -backend-config=bucket=$tfConfS3Bucket" +
                " -backend-config=key=$tfConfS3Key" +
                (StringUtils.isEmpty(tfConfS3KmsKey) ? "" : " -backend-config=kms_key_id=$tfConfS3KmsKey") +
                " -backend-config=region=$tfConfS3Region".toString()])
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

        if (tfFailOnPlanChanges && process.exitValue() == PLAN_CHANGES_DETECTED) {
            throw new GradleScriptException("Plan changes detected, aborting!", null)
        }
        if (process.exitValue() != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + process.exitValue(), null)
        }
    }

}
