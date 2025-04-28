#!/bin/bash
set -e

SCRIPT_PATH=$(realpath ./create-daily-bucket.sh)

crontab -l 2>/dev/null | grep -v "$SCRIPT_PATH" | crontab -

echo "[OK] Cron job removed."
