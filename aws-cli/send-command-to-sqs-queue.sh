#!/bin/bash
set -x
aws --endpoint-url=http://localstack:4566 \
  sqs send-message \
  --queue-url http://localstack:4566/000000000000/dummy-queue \
  --profile test-profile \
  --region us-east-1 \
  --message-body '{
          "event_id": "7456c8ee-949d-4100-a0c6-6ae8e581ae15",
          "event_time": "2019-11-26T16:00:47Z",
          "data": {
            "test": 83411
        }
      }' | cat
