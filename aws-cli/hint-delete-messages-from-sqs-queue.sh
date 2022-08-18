#!/bin/bash
echo '💡 please run the following:'
echo 'aws sqs delete-message \
  --endpoint-url=http://localstack:4566 \
  --queue-url http://localstack:4566/000000000000/dummy-queue \
  --profile test-profile \
  --region us-east-1 \
  --receipt-handle <message-handle>'
