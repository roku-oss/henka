package com.roku.terraform

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class TerraformTaskTest {

    @Test
    public void taskAddedToProject() {
        Project project = ProjectBuilder.builder().build()
        project.task('terraform', type: TerraformTask)
        assertTrue(
                "Expect 'terraform' task to be an instance of TerraformTask, not ${project.tasks.terraform.class}".toString(),
                project.tasks.terraform instanceof com.roku.terraform.TerraformTask)
    }
}
