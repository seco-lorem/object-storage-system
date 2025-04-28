#!/bin/bash
set -e

SCRIPT_PATH=$(realpath ./create-daily-bucket.sh)
LOG_FILE="/Users/yekta/minio_bucket.log"

# Write the cron job (runs hourly)
( crontab -l 2>/dev/null | grep -v "$SCRIPT_PATH" ; echo "22 * * * * $SCRIPT_PATH >> $LOG_FILE 2>&1" ) | crontab -

echo "[OK] Cron job installed for: create_daily_bucket.sh"
