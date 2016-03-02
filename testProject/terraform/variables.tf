variable "aws_access_key" {
  description = "AWS access key of Terraform deployment user"
}

variable "aws_secret_key" {
  description = "AWS secret key of Terraform deployment user."
}

variable "aws_account_id" {
  description = "AWS service account ID"
}

variable "aws_region" {
  description = "AWS region to launch servers."
  default = "us-east-1"
}

variable "s3_bucket" {
  description = "S3 Bucket for testing terraform plugin"
  default = "terraform-plugin-test-bucket"
}
