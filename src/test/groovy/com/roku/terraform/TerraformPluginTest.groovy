package com.roku.terraform

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import static org.junit.Assert.*

class TerraformPluginTest {

    @Test
    public void terraformPluginAddsTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply('com.roku.terraform')
        assertTrue(
                "Expect 'terraform' task to be an instance of TerraformTask, not ${project.tasks.terraform.class}".toString(),
                project.tasks.terraform instanceof TerraformTask)
    }
}
