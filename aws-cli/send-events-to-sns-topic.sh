#!/bin/bash
set -x
aws sns publish \
  --endpoint-url=http://localstack:4566 \
  --topic-arn arn:aws:sns:eu-central-1:000000000000:order-creation-events \
  --message "Hello World" \
  --profile test-profile \
  --region eu-central-1 \
  --output json | cat
