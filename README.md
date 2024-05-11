# PokeDyno

## Install

1. Install the AWS CLI by following the instructions here: [AWS CLI Installation Guide](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html).
2. Run `aws configure` to set up your AWS CLI with credentials.

## Viewing Tables

To view the tables in your DynamoDB Local instance, run the following command:

```bash
aws dynamodb list-tables --endpoint-url http://localhost:8000
