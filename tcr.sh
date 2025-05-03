#!/usr/bin/env bash
# TCR (Test && Commit || Revert) automation script
# Manual trigger: Press Enter to execute the cycle.

# Default commit message
COMMIT_MSG="WIP"

# Function to summarize added code using Gemini API
summarize_code() {
  local diff_content="$1"
  local summary
  summary=$(curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $GEMINI_API_KEY" \
    -d '{"contents": [{"parts": [{"text": "Summarize the following code changes for a commit message:\n'"$(echo "$diff_content" | sed 's/"/\\"/g')"'"}]}]}' \
    "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent" | \
    grep -o '"text":"[^"]*"' | head -1 | sed 's/"text":"//;s/"$//')
  echo "$summary"
}

# Main loop: wait for Enter key, then run tests and handle git accordingly
echo "Starting TCR loop. Press Enter to run the cycle."
while true; do
  read -p $'\nPress Enter if you want to run the TCR cycle (for example, after making code changes or adding tests). Ctrl+C to exit...'
  echo "\nRunning tests..."
  if mvn test; then
    echo "Tests passed. Committing changes..."
    git add -A
    # Get added code diff
    ADDED_CODE=$(git diff --cached)
    if [ -n "$ADDED_CODE" ]; then
      SUMMARY=$(summarize_code "$ADDED_CODE")
      COMMIT_MSG="[TCR] $SUMMARY"
    else
      COMMIT_MSG="[TCR] No code changes detected."
    fi
    git commit -m "$COMMIT_MSG"
  else
    echo "Tests failed. Reverting to last commit..."
    git reset --hard HEAD
  fi
done



