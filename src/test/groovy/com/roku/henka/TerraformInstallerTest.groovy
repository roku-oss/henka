package com.roku.henka

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.verifyZeroInteractions

class TerraformInstallerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    AntBuilder antBuilder

    @Test
    void installTerraform() throws Exception {
        Path tmpDir = Files.createTempDirectory("tfTest")
        Path expectedTfZip = Paths.get(tmpDir.toString() + "/0.8.5/terraform.zip")
        Path expectedTfExec = Paths.get(tmpDir.toString() + "/0.8.5/terraform")
        Path expectedTfWinExec = Paths.get(tmpDir.toString() + "/0.8.5/terraform.exe")

        try {
            new TerraformInstaller().installTerraform(tmpDir.toString(), "0.8.5")

            assertThat(Files.exists(expectedTfZip)).isTrue()
            assertThat(Files.exists(expectedTfExec) || Files.exists(expectedTfWinExec)).isTrue()

            verifyVersion(expectedTfWinExec, expectedTfExec)

            new TerraformInstaller(antBuilder).installTerraform(tmpDir.toString(), "0.8.5")
            verifyZeroInteractions(antBuilder)
        }
        finally {
            FileUtils.deleteDirectory(new File(tmpDir.toString()))
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    private static void verifyVersion(Path expectedTfWinExec, Path expectedTfExec) {
        def stdout
        try {
            stdout = "$expectedTfWinExec version".execute().text
        } catch (Throwable ignored) {
            stdout = "$expectedTfExec version".execute().text
        }
        def actualVersion = (stdout =~ /Terraform v(.*)\n/)[0][1]
        assertThat(actualVersion).isEqualTo("0.8.5")
    }

}