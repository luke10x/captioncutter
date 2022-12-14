#!/bin/bash
set -x

aws s3 mb s3://kibiras \
  --endpoint-url=http://localstack:4566 \
  --profile test-profile

aws s3 ls s3://kibiras \
  --endpoint-url=http://localstack:4566 \
  --profile test-profile \
  --region us-east-1 \
   --recursive --human-readable --summarize | cat
