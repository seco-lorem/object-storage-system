#!/bin/bash

# Load environment variables
source /Users/yekta/Desktop/sw_projects/work/application/companyname/storage-system/.env

# Get today's date
TODAY=$(date +%F)  # format: YYYY-MM-DD
BUCKET_NAME="${TEAM_NAME}-bucket-${TODAY}"

# Path to your JAR
JAR_PATH="/Users/yekta/Desktop/sw_projects/work/application/companyname/storage-system/s3compatible-storage-client/s3compatible-client/target/s3compatible-client-0.0.1-SNAPSHOT.jar"

# Check if already created today (you could use a lockfile)
LOCK_FILE="/tmp/bucket_created_${TODAY}.lock"

if [ -f "$LOCK_FILE" ]; then
    echo "✅ Bucket already created today: $BUCKET_NAME"
    exit 0
fi

echo "🚀 Creating bucket: $BUCKET_NAME"

if java -jar "$JAR_PATH" create-bucket -n="$BUCKET_NAME"; then
    touch "$LOCK_FILE"
else
    exit 1
fi
