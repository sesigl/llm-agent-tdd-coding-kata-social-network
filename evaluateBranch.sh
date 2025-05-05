#!/bin/bash

# Accept branch name as first argument (optional)
BRANCH_NAME="$1"

# Function to call Gemini API with the evaluation prompt
gemini_evaluate() {
  local prompt="$1"
  local api_url="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-04-17:generateContent?key=${GEMINI_API_KEY}"
  local response
  response=$(curl -s \
    -X POST \
    -H "Content-Type: application/json" \
    "$api_url" \
    -d '{
      "contents": [
        { "parts": [ { "text": "'"$(echo "$prompt" | sed 's/"/\\"/g')"'"} ] }
      ]
    }') || {
      echo "Error: API request failed" >&2
      return 1
    }
  # Try to extract the text part (Gemini returns a JSON with .candidates[0].content.parts[0].text)
  if command -v jq &>/dev/null; then
    printf '%s' "$response" | jq -r '.candidates[0].content.parts[0].text // empty'
  else
    printf '%s' "$response" | grep -o '"text":[[:space:]]*"[^"]*"' | head -1 | sed 's/.*"text":[[:space:]]*"//;s/"$//'
  fi
}

# 1. Get all commit messages from the TCR run (everything prefixed with [TCR])
if [ -n "$BRANCH_NAME" ]; then
  echo "Getting logs from branch: $BRANCH_NAME"
  TCR_LOG=$(git log "$BRANCH_NAME" --grep="^\[TCR\]" --date=iso)
else
  TCR_LOG=$(git log --grep="^\[TCR\]" --date=iso)
fi

# --- Extract ai_support_level using Gemini ---
echo "Preparing prompt for ai_support_level extraction..."
read -r -d '' AI_LEVEL_PROMPT << EOM
Given the following branch name from a coding session, determine the value for "ai_support_level" according to these options: "none", "completion", "edit", or "agent". Only output the value as a single word, nothing else.

Branch name:
$BRANCH_NAME
EOM

echo "Calling Gemini to extract ai_support_level..."
AI_SUPPORT_LEVEL=$(gemini_evaluate "$AI_LEVEL_PROMPT" | head -1 | tr -d '\r\n ')
echo "Gemini returned ai_support_level: '$AI_SUPPORT_LEVEL'"

if [[ ! "$AI_SUPPORT_LEVEL" =~ ^(none|completion|edit|agent)$ ]]; then
  echo "Warning: Could not extract a valid ai_support_level from Gemini. Got: '$AI_SUPPORT_LEVEL'. Defaulting to 'none'." >&2
  AI_SUPPORT_LEVEL="none"
else
  echo "ai_support_level is valid: $AI_SUPPORT_LEVEL"
fi

# --- Extract day using Gemini ---
echo "Preparing prompt for day extraction..."
read -r -d '' DAY_PROMPT << EOM
Given the following branch name from a coding session, extract the integer value for "day" (0-30) that is part of the branch name. Only output the integer, nothing else.

Branch name:
$BRANCH_NAME
EOM

echo "Calling Gemini to extract day..."
DAY=$(gemini_evaluate "$DAY_PROMPT" | head -1 | tr -d '\r\n ')
echo "Gemini returned day: '$DAY'"

if [[ ! "$DAY" =~ ^[0-9]\{1,2\}$ && ! "$DAY" =~ ^[0-9]$ && ! "$DAY" =~ ^[1-2][0-9]$ && ! "$DAY" =~ ^30$ ]]; then
  echo "Warning: Could not extract a valid day from Gemini. Got: '$DAY'. Defaulting to '0'." >&2
  DAY="0"
elif (( DAY < 0 || DAY > 30 )); then
  echo "Warning: Extracted day '$DAY' is out of range 0-30. Defaulting to '0'." >&2
  DAY="0"
else
  echo "day is valid: $DAY"
fi

# 2. Gather the final source and test code
echo "Collecting all source files under src/main and all test files under src/test..."
SRC_CODE=""
TEST_CODE=""
if [ -d "src/main" ]; then
  while IFS= read -r -d '' file; do
    SRC_CODE+="\n\n// File: $file\n"
    SRC_CODE+="$(cat "$file")"
  done < <(find src/main -type f -print0)
fi
if [ -d "src/test" ]; then
  while IFS= read -r -d '' file; do
    TEST_CODE+="\n\n// File: $file\n"
    TEST_CODE+="$(cat "$file")"
  done < <(find src/test -type f -print0)
fi

# 3. Prepare the evaluation prompt (from README.md)
read -r -d '' PROMPT << EOM
Analyze the following data from a 30-minute Test-Commit-Revert (TCR) coding session for the "Social Network Kata". The goal is to evaluate the development process based on the provided commit log, final source code, and final test code.

**ai_support_level for this branch:** $AI_SUPPORT_LEVEL
**day for this branch:** $DAY

**Context:**
- Get the exact session duration from the commits, but it should be about 30m (1800 seconds).
- The methodology used was Test-Commit-Revert (TCR), where tests must pass for changes to be committed; otherwise, they are reverted.
- The kata involves implementing features for a simple social network (Posting, Reading, Following, Mentions, Links, Direct Messages).
- The commit log includes timestamps and messages indicating successful commits or reverts.

**Input Data:**

1.  **Commit Log (including timestamps, commit messages, and commit/revert status):**

$TCR_LOG

2.  **Final Source Code:**

$SRC_CODE

3.  **Final Test Code:**

$TEST_CODE

**Analysis Task:**

Based *only* on the provided data, calculate the following metrics and provide a general summary of the session. Adhere strictly to the definitions provided below.

**Metric Definitions:**

ai_support_level: "none", "completion", "edit" or "agent". The amount of AI support in the task.
day: Integer 0-30. The number of times I have done the tasks with different level of AI support.
main_use_cases_coverage: Integer (0-6). Count how many of the 6 main requirements (Posting, Reading, Following, Mentions, Links, Direct Messages) appear to be implemented and tested according to the final code and tests.
additional_edge_cases: Integer. Count the number of distinct edge cases or alternative scenarios explicitly handled in the tests beyond the most basic path for each implemented main requirement.
time_to_completion_or_session_end_seconds: Integer. Report 1800 (the session duration) as the kata was likely not fully completed in 30 mins. If, exceptionally, all 6 use cases were verifiably completed *before* the 30min mark according to the commit log, estimate the time based on the last relevant commit timestamp. Otherwise, always use 1800.
test_to_code_ratio: Float. Calculate the ratio of lines of code in the final test files to the lines of code in the final source files (excluding comments and blank lines if possible, otherwise total lines). State the method used (e.g., total lines or non-comment/blank lines).
average_assertions_per_test: Float. Calculate the average number of assertion statements per test method/function found in the final test code.
cyclomatic_complexity: Integer. Estimate the *average* cyclomatic complexity across the functions/methods in the *final source code*. If possible, state the tool/methodology used for estimation (e.g., counting decision points + 1).
code_duplication_percentage: Float. Estimate the percentage of duplicated code within the *final source code*. Briefly state the basis for this estimation (e.g., visual inspection, assumed tooling).
code_smells: List of strings. Identify potential code smells present in the *final source code* based on common definitions (e.g., Long Method, Large Class, Feature Envy, Duplicated Code, etc.). List the identified smells.
test_focus_rating: Integer (1-5). Rate the tests based on their focus on *behavior* (what the system should do) versus *implementation details* (how it does it). 1 = Heavily implementation-focused, difficult to understand behavior; 5 = Clearly specifies behavior (BDD-like), independent of implementation details.
implementation_clean_code_rating: Integer (1-5). Rate the *final source code* based on general Clean Code principles (e.g., meaningful names, small functions, SRP, comments quality, formatting). 1 = Poor adherence; 5 = Excellent adherence.
test_clean_code_rating: Integer (1-5). Rate the *final test code* based on general Clean Code principles (e.g., readability, structure like Arrange-Act-Assert, clear assertion messages, meaningful test names). 1 = Poor adherence; 5 = Excellent adherence.

**Output Format:**

Provide ONLY the JSON object containing the calculated metrics and an overall generated description:


**Example JSON Structure:**
{
  "ai_support_level": "completion",
  "day": 2,
  "main_use_cases_coverage": 3,
  "additional_edge_cases": 2,
  "time_to_completion": 1800,
  "test_to_code_ratio": 1.8,
  "average_assertions_per_test": 1.5,
  "cyclomatic_complexity": 2,
  "code_duplication_percentage": 5.0,
  "code_smells": ["Duplicated Code", "Primitive Obsession"],
  "test_focus_rating": 4,
  "implementation_clean_code_rating": 3,
  "test_clean_code_rating": 4,
  "description": "The coding session, which employed the Test-Commit-Revert (TCR) methodology, spanned approximately 27 minutes and 16 seconds according to the commit timestamps. The commit log indicates a development process that involved adding basic timeline functionality (publish, retrieve), handling multiple messages, and implementing access control features (owner-only restriction, general access, granular access). The log also suggests refactoring efforts to improve test setup. However, the provided final source code and test code are in an initial, largely empty state. The TimelineService class is defined but contains no implementation, and the TimelineServiceTest class contains only one empty test method named example. This final state does not reflect the features described as being added and committed in the commit history. Consequently, based strictly on the final code and tests, none of the main social network kata requirements are met, there are no implemented behaviors or tested edge cases, and code quality metrics like complexity and duplication are minimal or non-applicable due to the lack of code. The tests, in their final form, lack clear focus or adherence to clean code principles. The most notable aspect of this session's data is the significant discrepancy between the commit history, which portrays steady progress and successful commits under TCR, and the final code snapshot provided."
}
EOM

# 4. Call Gemini API to get the evaluation JSON and append to results.ndjson

# Ensure API key is provided
: "${GEMINI_API_KEY:?Environment variable GEMINI_API_KEY must be set}"

# Call Gemini and process the result
EVAL_RESULT=$(gemini_evaluate "$PROMPT")

# Extract the JSON part (first code block)
JSON_LINE=$(printf '%s\n' "$EVAL_RESULT" | awk '/^```json/{flag=1;next}/^```/{flag=0}flag' | sed '/^$/d')

if [ -z "$JSON_LINE" ]; then
  echo "No JSON result found in Gemini response. Full response:" >&2
  echo "$EVAL_RESULT" >&2
  exit 1
fi

# Append JSON to results.ndjson
printf '%s\n' "$JSON_LINE" >> results.ndjson

echo "Evaluation JSON appended to results.ndjson."
# Optionally, print the summary section as well
printf '%s\n' "$EVAL_RESULT" | awk '/^```json/{flag=0}flag;/^```json/{flag=1}' | sed '/^$/d'

