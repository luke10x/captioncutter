#!/bin/bash
set -x
aws s3 cp ./test11.wav s3://kibiras \
  --endpoint-url=http://localstack:4566 \
  --profile test-profile \
  --region us-east-1 \
  --output json | cat

cp test11.wav s3://kibiras/ --endpoint-url https://s3-acceleratee.amazonaws.com