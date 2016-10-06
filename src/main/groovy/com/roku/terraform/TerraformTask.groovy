package com.roku.terraform

import com.roku.terraform.executors.BashExecutor
import com.roku.terraform.executors.TerraformExecutor
import com.roku.terraform.executors.TerraformExecutorFactory
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

class TerraformTask extends DefaultTask {
    private final BashExecutor executor;

    String tfDir
    String tfVarFile
    String tfAction

    String tfConfS3Region = "us-east-1"
    String tfConfS3Bucket
    String tfConfS3Key
    String tfConfS3KmsKey
    Boolean tfFailOnPlanChanges = false

    @Inject
    TerraformTask() {
        executor = new BashExecutor();
    }

    TerraformTask(BashExecutor executor) {
        this.executor = executor
    }

    @TaskAction
    def terraform() {
        populateProperties()
        TerraformExecutor tfExecutor = TerraformExecutorFactory.createFor(tfAction)
        tfExecutor.execute(this)
    }

    private void populateProperties() {
        tfDir = getPropertyFromTaskOrProject(tfDir, "tfDir")
        tfAction = getPropertyFromTaskOrProject(tfAction, "tfAction")
        tfVarFile = getPropertyFromTaskOrProject(tfVarFile, "tfVarFile")
        tfConfS3Key = getPropertyFromTaskOrProject(tfConfS3Key, "tfConfS3Key")
        tfConfS3Bucket = getPropertyFromTaskOrProject(tfConfS3Bucket, "tfConfS3Bucket")
        tfConfS3Region = getPropertyFromTaskOrProject(tfConfS3Region, "tfConfS3Region")
        tfConfS3KmsKey = getPropertyFromTaskOrProject(tfConfS3KmsKey, "tfConfS3KmsKey")
        tfFailOnPlanChanges = new Boolean(getPropertyFromTaskOrProject(tfFailOnPlanChanges, "tfFailOnPlanChanges"))
    }

    String getPropertyFromTaskOrProject(Object taskProperty, String propertyName) {
        if (taskProperty == null && !project.hasProperty(propertyName)) {
            throw new GradleScriptException("$propertyName should be defined either as a task- or a project-level property ", null)
        }
        return taskProperty == null ? project.property(propertyName) : taskProperty
    }

}
