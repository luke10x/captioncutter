#!/bin/bash
set -x
aws s3api list-buckets \
  --endpoint-url=http://localstack:4566 \
  --profile test-profile \
  --region us-east-1 \
  --output json | cat
