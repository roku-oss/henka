/*
* Copyright (c) 2016, Henka Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and limitations under the License.
*/
package com.roku.henka

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
                project.tasks.terraform instanceof com.roku.henka.TerraformTask)
    }
}
