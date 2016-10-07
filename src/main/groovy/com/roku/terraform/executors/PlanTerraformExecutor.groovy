package com.roku.terraform.executors

import com.roku.terraform.TerraformTask
import groovy.transform.TypeChecked
import org.gradle.api.GradleScriptException

@TypeChecked
class PlanTerraformExecutor extends TerraformExecutor{
    public static final int PLAN_CHANGES_DETECTED = 2

    private final BashExecutor executor;

    PlanTerraformExecutor() {
        this.executor = new BashExecutor();
    }

    PlanTerraformExecutor(BashExecutor executor) {
        this.executor = executor;
    }

    @Override
    def execute(TerraformTask task) {
        executor.execute(cleanTerraformConfig(), task.tfDir)
        executor.execute(terraformRemoteConfigFor(task), task.tfDir)

        GString tfCommand = createTfCommand(task)
        int exitCode = executor.execute(tfCommand.toString(), task.tfDir)

        throwOnFailureOrPlanChanges(exitCode, task)

    }

    private void throwOnFailureOrPlanChanges(int exitCode, TerraformTask task) {
        if (exitCode != 0) {
            if (task.tfFailOnPlanChanges && PLAN_CHANGES_DETECTED == exitCode) {
                throw new GradleScriptException("Plan changes detected, aborting!", null)
            } else {
                throw new GradleScriptException("error while executing shell script, exit code: " + exitCode, null)
            }
        }
    }

    private GString createTfCommand(TerraformTask task) {
        def tfCommand = "terraform plan -no-color -var-file='${task.tfVarFile}'"
        if (task.tfFailOnPlanChanges) {
            tfCommand = tfCommand + " -detailed-exitcode".toString()
        }
        return tfCommand
    }
}
