package com.roku.terraform

import org.gradle.api.Plugin
import org.gradle.api.Project

class TerraformPlugin implements Plugin<Project>{

    @Override
    void apply(Project target) {
        target.task('terraform', type: TerraformTask)
    }
}
