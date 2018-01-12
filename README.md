# Henka (Terraform Gradle Plugin)

## What's new?

This version of Henka (1.0.x) introduces a few backwards-incompatible changes to reflect a new backend mechanism
in Terraform (https://www.terraform.io/docs/backends).

Backwards-incompatible changes

*   tfConfS3* variables are no longer supported. The backend is now configured as a part of Terraform scripts,
    and is no longer limited to S3 (you can use Consul etc. as your backend).
*   tfInitParams are required to complete a partial configuration of your backend. Please provide all missing pieces
    of the configuration using "-backend-config" parameters.
     
    Example :`tfInitParams="-input=false -backend-config=bucket=roku-terraform-state-e1qa -backend-config=key=e1qa-henka-sample.tfstate -backend-config=kms_key_id=alias/e1qa-secrets -force-copy"`
    
    To see the list of configuration options for `terraform init` command and configuration syntax for backends, please see
   
    * https://www.terraform.io/docs/commands/init.html
    * https://www.terraform.io/docs/backends

## Why do I need it? 

The plugin provides an opinionated way of running terraform scripts:
* it makes sure the right version of Terraform is available, downloading it if necessary
* it makes sures that your backend (a remote state) is initialized
* in makes it easy to store multiple remote states for different environments

## What do I need to run it?

To run the plugin:
* If you want to use preinstalled Terraform - it has to be installed and available in PATH, otherwise you need to set
`installTerraform`, `terraformVersion` and `terraformBaseDir` parameters
* you need to have valid terraform scripts and environment-specific configuration files
* you need to add the plugin to your `build.gradle` and configure TerraformTask (provided by the plugin)
* Terraform version should be 0.9.0 or higher

## What does it do?

The plugin will:
* download and install Terraform if needed (supported for Windows, Mac OsX and Linux)
* initialize the backend Terraform state
* call terraform with the parameters you provided.

## How do I run it?

To see usage example please check the sample project: https://github.com/rokudev/henka-sample/

`build.gradle` changes:

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.roku:henka:1.0.0-RELEASE"
    }
}

task terraform(type: com.roku.henka.TerraformTask) {
    description "Runs a terraform script"
    tfDir       = "<path to folder with your terraform scripts>"
    tfAction    = "plan -input=false" 
    tfInitParams = "-input=false -backend-config=bucket=roku-terraform-state-e1qa -backend-config=key=e1qa-henka-sample.tfstate -backend-config=kms_key_id=alias/e1qa-secrets -force-copy"

    terraformBaseDir = "/opt/terraform"
    terraformVersion = "0.9.2"
}


```

The list of available properties:

|Datatype   |PropertyName           |Description                                                                                        | Default |
|---|---|---|---|
|String     |tfDir                  |Directory with Terraform scripts                                                                   | | 
|String     |tfAction               |Terraform command, passed as-is, may include Terraform command as well as arguments                | |
|Boolean    |installTerraform       |If true - install terraform (of `terraformVersion`) to `terraformBaseDir`                          | true |
|String     |terraformVersion       |Version of Terraform to install                                                                    | 0.9.2 |
|String     |terraformBaseDir       |Base directory to install Terraform to. A subdirectory will be created for each Terraform version  | /opt/terraform | 
     

To execute the task, call

```
gradle terraform
```

Alternatively, all or some of terraform task properties can be specified as project properties in terraform command line:

```
gradle terraform -P tfAction="apply -input=false" 

```

## Licensing

Gradle Terraform Plugin is available as open source under the terms of [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.txt) 

## Contributing

When submitting a PR, please fill in and submit an appropriate Contributor License Agreement:
* [Individual Contributor License Agreement](https://raw.githubusercontent.com/rokudev/henka-sample/master/Roku%20ICLA.txt)
* or [Corporate Contributor License Agreement](https://raw.githubusercontent.com/rokudev/henka/master/Roku%20CCLA.txt)
