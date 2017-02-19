package com.roku.henka

import org.apache.commons.io.FileUtils

import java.nio.file.Files
import java.nio.file.Paths

class TerraformInstaller {

    final static String RELEASES = "https://releases.hashicorp.com/terraform/"

    private final AntBuilder antBuilder

    TerraformInstaller() {
        antBuilder = new AntBuilder()
    }

    TerraformInstaller(AntBuilder antBuilder) {
        this.antBuilder = antBuilder
    }

    void installTerraform(String baseDirectory, String version) {
        OsType osType = OsType.getOsType()

        String finalDirPath = baseDirectory+"/"+version+"/"
        String finalDistPath = baseDirectory+"/"+version+"/terraform.zip"

        def finalExecPath = Paths.get(baseDirectory+"/"+version+"/terraform")
        def finalWinExecPath = Paths.get(baseDirectory+"/"+version+"/terraform.exe")
        if (Files.exists(finalExecPath) || Files.exists(finalWinExecPath)) {
            return
        }

        File finalDir = new File(finalDirPath)
        finalDir.mkdirs()

        File destZip = new File(finalDistPath)

        String distUrl=RELEASES+version+"/terraform_"+version+"_"+osType.name().toLowerCase()+".zip"
        destZip.createNewFile()
        FileUtils.copyURLToFile(new URL(distUrl), destZip)

        new AntBuilder().unzip(src: finalDistPath, dest: finalDirPath)
    }

}