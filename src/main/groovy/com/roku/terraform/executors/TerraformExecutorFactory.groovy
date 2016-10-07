package com.roku.terraform.executors;

import org.gradle.api.GradleScriptException;

public class TerraformExecutorFactory {

    public static TerraformExecutor createFor(String action) {
        switch (action) {
            case "plan":
                return new PlanTerraformExecutor()
            case "apply":
                return new ApplyTerraformExecutor()
            case "refresh":
                return new RefreshTerraformExecutor()
            default:
                throw new GradleScriptException("Unknown tfAction: "+action, null)
        }
    }
}
