# PokeDyno

## Install

1. Install the AWS CLI by following the instructions here: [AWS CLI Installation Guide](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html).
2. Run `aws configure` to set up your AWS CLI with credentials.
3. create the table
     ```bash
    aws dynamodb create-table \
    --table-name Pokemon \
    --attribute-definitions \
    AttributeName=id,AttributeType=S \
    --key-schema \
    AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000
    ```
   To view the tables in your DynamoDB Local instance, run the following command:

    ```bash
    aws dynamodb list-tables --endpoint-url http://localhost:8000
    ```

4. Run RunApplcation.java important that you create the tables before running the application
5. open http://localhost:8080/

viewing whats in the table

```bash
aws dynamodb scan --table-name Pokemon --endpoint-url http://localhost:8000
```