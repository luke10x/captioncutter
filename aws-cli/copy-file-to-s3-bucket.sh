#!/bin/bash
set -x
aws --endpoint-url=http://localstack:4566 \
  s3 cp ./test11.wav s3://kibiras \
  --profile test-profile \
  --region us-east-1 \
  --output json | cat
