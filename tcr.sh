#!/usr/bin/env bash
# TCR (Test && Commit || Revert) automation script
# Usage: tcr.sh [-d] [-c]  # -d = debug mode, -c = run single cycle and exit

# parse flags
DEBUG=0
RUN_CYCLE=0
while getopts "dc" opt; do
  case $opt in
    d) DEBUG=1 ;;
    c) RUN_CYCLE=1 ;;
  esac
done
shift $((OPTIND -1))

log_debug() {
  if [ "$DEBUG" -eq 1 ]; then
    echo "$@" >&2
  fi
}

# Default commit message
COMMIT_MSG="WIP"

# strict mode + better error detection
set -euo pipefail

# ensure API key is provided
: "${GEMINI_API_KEY:?Environment variable GEMINI_API_KEY must be set}"

# Function to summarize added code using Gemini API
summarize_code() {
  local diff_content="$1"
  local response summary
  # use the documented Gemini flash model
  local api_url="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-04-17:generateContent?key=${GEMINI_API_KEY}"

  response=$(curl -s \
    -X POST \
    -H "Content-Type: application/json" \
    "$api_url" \
    -d '{
      "contents": [
        {
          "parts": [
            {
              "text": "I am doing TCR (test commit revert) and you are my commit summarizer. Your response should only be the commit message and the commit description, no other content or masking. Summarize the following code changes for a commit message:\n'"$(echo "$diff_content" | sed 's/"/\\"/g')"'"
            }
          ]
        }
      ]
    }') || {
      echo "Error: API request failed" >&2
      return 1
    }
  log_debug "API response: $response"

  # parse with jq if installed, else fallback to grep/sed
  if command -v jq &>/dev/null; then
    summary=$(printf '%s' "$response" \
      | jq -r '.candidates[0].content.parts[0].text // empty')
  else
    summary=$(printf '%s' "$response" \
      | grep -o '"text":[[:space:]]*"[^"]*"' \
      | head -1 \
      | sed 's/.*"text":[[:space:]]*"//;s/"$//')
  fi

  printf '%s' "$summary"
}

# Function to run a single TCR cycle
run_tcr_cycle() {
  echo "Running tests..."
  # capture maven output with colors (force ANSI) while printing live
  TMP_OUT=$(mktemp)
  TEST_OUTPUT=""  # Initialize to avoid unbound variable
  if mvn -Dstyle.color=always clean test 2>&1 | tee "$TMP_OUT"; then
    TEST_OUTPUT=$(cat "$TMP_OUT"); rm "$TMP_OUT"
    echo "Tests passed. Preparing commit..."
    git add -A
    ADDED_CODE=$(git diff --cached)
    log_debug "Diff content: $ADDED_CODE"

    if [ -n "$ADDED_CODE" ]; then
      SUMMARY=$(summarize_code "$ADDED_CODE") || SUMMARY=""
      SUMMARY=${SUMMARY:-"No summary from API"}
      COMMIT_MSG="[TCR] $SUMMARY"
      log_debug "Commit message: $COMMIT_MSG"
      git commit -m "$COMMIT_MSG"
    else
      echo "No code changes detected. Keeping going..."
    fi

  else
    TEST_OUTPUT=$(cat "$TMP_OUT" 2>/dev/null || true); rm -f "$TMP_OUT"
    # on failure, Maven output already printed by tee
    FAIL_DIFF=$(git diff)
    log_debug "Diff content: $FAIL_DIFF"

    if [ -n "$FAIL_DIFF" ]; then
      # merge diff + maven output for summarization
      COMBINED_CONTENT="$FAIL_DIFF"$'\n\nMAVEN OUTPUT:\n'"$TEST_OUTPUT"
      RESET_SUMMARY=$(summarize_code "$COMBINED_CONTENT") || RESET_SUMMARY=""
      RESET_SUMMARY=${RESET_SUMMARY:-"No summary from API"}
      COMMIT_MSG="[TCR RESET] $RESET_SUMMARY"
    else
      COMMIT_MSG="[TCR RESET] No code changes detected."
    fi

    log_debug "Commit message: $COMMIT_MSG"
    git reset --hard HEAD
    git commit --allow-empty -m "$COMMIT_MSG"
  fi
}

# Check if we should run a single cycle and exit
if [ "$RUN_CYCLE" -eq 1 ]; then
  echo "Running a single TCR cycle and exiting..."
  run_tcr_cycle
  exit 0
fi

# Main loop: wait for Enter key, then run tests and handle git accordingly
echo "Starting a TCR coding kata session... Have fun 🚀!"
echo "--------------------------------------------------"

while true; do
  # read a single key (silent, no newline)
  read -rsn1 -p $'\n Enter - run the TCR cycle \n f - run tests only \n Ctrl+C to exit\n' key
  echo
  if [[ $key == "" ]]; then
    # full TCR cycle
    run_tcr_cycle
  elif [[ $key == "f" || $key == "F" ]]; then
    # just run tests, no git actions
    echo "Running tests only (no commit/revert)..."
    mvn clean test || true
  else
    echo "Unrecognized key '$key' – please press Enter or f."
  fi
done

