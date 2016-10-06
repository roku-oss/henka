package com.roku.terraform.executors

import com.roku.terraform.TerraformTask
import groovy.transform.TypeChecked
import org.gradle.api.GradleScriptException

@TypeChecked
class RefreshTerraformExecutor extends TerraformExecutor{
    private final BashExecutor executor;

    RefreshTerraformExecutor() {
        this.executor = new BashExecutor();
    }

    RefreshTerraformExecutor(BashExecutor executor) {
        this.executor = executor;
    }

    @Override
    def execute(TerraformTask task) {
        executor.execute(cleanTerraformConfig(), task.tfDir)
        executor.execute(terraformRemoteConfigFor(task), task.tfDir)

        int exitCode = executor.execute("terraform refresh -no-color -var-file='${task.tfVarFile}'".toString(), task.tfDir)

        throwOnFailure(exitCode)

    }

    private void throwOnFailure(int exitCode) {
        if (exitCode != 0) {
            throw new GradleScriptException("error while executing shell script, exit code: " + exitCode, null)
        }
    }

}
