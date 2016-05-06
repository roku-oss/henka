# Gradle-Terraform plugin

## Overview

This plugin makes terraform (see http://terraform.io) a part of a gradle build, automating infrastructure provisioning by adding a terraform task type.

## Requirements

* Terraform to be installed and available in PATH.
* AWS key and AWS secret key to store terraform state in S3 bucket

## Usage

Importing the plugin in your build.gradle:

```
buildscript {
    dependencies {
        classpath "com.roku.gradle:terraform:0.9.4"
    }
}

```

Creating the terraform task:

```
task terraform(type: com.roku.terraform.TerraformTask) {
    description "Runs a terraform script"
    tfDir       = "<path to folder with your terraform scripts>"
    tfVarFile   = "<path to file with terraform variables>"
    tfAction    = "plan" // [ "plan" | "apply"]

    tfConfS3Bucket  = "<S3 bucket to store TF state in>"
    tfConfS3Key     = "<S3 key to store TF state in>"
    tfConfS3Region  = "<S3 region>" // e.g., "us-east-1"
    
    tfAwsAccessKey  = "$System.env.TF_AWS_ACCESS_KEY" // obtain AWS access key from system environment
    tfAwsSecretKey  = "$System.env.TF_AWS_SECRET_KEY" // obtain AWS access key from system environment 
}
```


To execute the task, call

```
gradle terraform
```

Alternatively, all or some of terraform task properties can be specified as project properties in terraform command line:

```
gradle terraform -P tfAction="apply" -P tfVarFile="<path to file with TF vars>"
```