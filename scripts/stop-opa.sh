#!/bin/bash

if [ -f opa.pid ]; then
  kill "$(cat opa.pid)" && rm opa.pid
  echo "[OK] OPA stopped."
else
  echo "[WARN] No opa.pid found — is OPA running?"
fi
