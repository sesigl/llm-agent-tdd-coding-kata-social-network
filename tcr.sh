#!/usr/bin/env bash
# TCR (Test && Commit || Revert) automation script
# Usage: tcr.sh [-d]  # -d = debug mode

# parse flags
DEBUG=0
while getopts "d" opt; do
  case $opt in
    d) DEBUG=1 ;;
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

# Main loop: wait for Enter key, then run tests and handle git accordingly
echo "Starting TCR loop. Press Enter to run the cycle."
while true; do
  read -p $'\nPress Enter to run TCR cycle (or Ctrl+C to exit)...'
  echo "Running tests..."
  if mvn clean test; then
    echo "Tests passed. Preparing commit..."
    git add -A
    ADDED_CODE=$(git diff --cached)
    log_debug "Diff content: $ADDED_CODE"
    if [ -n "$ADDED_CODE" ]; then
      SUMMARY=$(summarize_code "$ADDED_CODE") || SUMMARY=""
      SUMMARY=${SUMMARY:-"No summary from API"}
      COMMIT_MSG="[TCR] $SUMMARY"
    else
      COMMIT_MSG="[TCR] No code changes detected."
    fi
    log_debug "Commit message: $COMMIT_MSG"
    git commit -m "$COMMIT_MSG"
  else
    echo "Tests failed. Reverting to last commit..."
    FAIL_DIFF=$(git diff)
    log_debug "Diff content: $FAIL_DIFF"
    if [ -n "$FAIL_DIFF" ]; then
      RESET_SUMMARY=$(summarize_code "$FAIL_DIFF") || RESET_SUMMARY=""
      RESET_SUMMARY=${RESET_SUMMARY:-"No summary from API"}
      COMMIT_MSG="[TCR RESET] $RESET_SUMMARY"
    else
      COMMIT_MSG="[TCR RESET] No code changes detected."
    fi

    log_debug "Commit message: $COMMIT_MSG"

    git reset --hard HEAD
    git commit --allow-empty -m "$COMMIT_MSG"
  fi

  echo "committed."
done

