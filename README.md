# Terraform Gradle Plugin

## Why do I need it? 

The plugin provides an opinionated way of running terraform scripts:
* it creates Terraform remote state in S3, so your state can be reused across your team
* it enforces encryption of your remote state using KMS
* in makes it easy to store multiple remote states for different environments, using different S3 buckets and keys

## What do I need to run it?

To run the plugin:
* terraform has to be installed and available in PATH
* AWS S3 access should be configured either through env variables, default profile credentials or IAM profile
* you need to have valid terraform scripts and environment-specific configuration files
* you need to add the plugin to your `build.gradle` and configure TerraformTask (provided by the plugin)

## What does it do?

The plugin will:
* clean up any remaining terraform state
* initialize remote state or pull from S3 if it already exists
* call terraform with the parameters you provided.

If any parameters are missing

## How do I run it?

To see usage example please check the sample project: [Link to be added]

`build.gradle` changes:

```
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath "com.roku:gradle-terraform:0.9.6"
    }
}

task terraform(type: com.roku.terraform.TerraformTask) {
    description "Runs a terraform script"
    tfDir = "$projectDir/YOUR_TERRAFORM_DIR"
    tfVarFile = "$projectDir/YOUR_TERRAFORM_ENV_CFG.vars"
    tfAction = "plan" // use "plan" for getting the list of upcoming changes and "apply" for executing them

    tfConfS3Bucket = "YOUR_S3_BUCKET"
    tfConfS3Key = "YOUR_S3_KEY"
}   

```

The list of available properties:

|Datatype   |PropertyName           |Description|
|---        |---                    |---|
|String     |tfDir                  |Directory with Terraform scripts |
|String     |tfAction               |Terraform action (plan/refresh/apply)   |
|String     |tfVarFile              |Path to file with environment-specific configuration   |
|String     |tfConfS3Bucket         |Terraform Remote State :: S3 Bucket  |
|String     |tfConfS3Key            |Terraform Remote State :: S3 Key   |
|String     |tfConfS3KmsKey         |Terraform Remote State :: KMS Key ARN to encrypt the remote state   |
|Boolean    |tfFailOnPlanChanges    |If true - rturn with exit code if there are any planned changes. Default - false  |
     
Alternatively, you can skip some of the parameters in task and define them as project-level properties. E.g.:

```
gradle terraform –P tfAction=“apply” –P tfVarFile=“qa.vars” -P rokuEnv=“qa”
```

## Licensing

Gradle Terraform Plugin is available as open source under the terms of [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.txt) 