#!/bin/bash
set -x
aws --endpoint-url=http://localstack:4566 \
  sqs receive-message \
  --queue-url http://localstack:4566/000000000000/dummy-queue \
  --profile test-profile \
  --region us-east-1 \
  --output json | cat
