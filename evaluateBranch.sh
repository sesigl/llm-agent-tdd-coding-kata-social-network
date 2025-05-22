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
  TCR_LOG=$(git log "$BRANCH_NAME" --date=iso)
else
  # fail, branch name is required
  echo "Error: Branch name is required. Usage: $0 <branch_name>" >&2
  exit 1
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

# 2. Gather the final source and test code from the branch (using git show)
echo "Collecting source and test files from branch $BRANCH_NAME..."
SRC_CODE=""
TEST_CODE=""

# Get list of source files in the branch
BRANCH_SRC_FILES=$(git ls-tree -r --name-only "$BRANCH_NAME" -- src/main 2>/dev/null)
BRANCH_TEST_FILES=$(git ls-tree -r --name-only "$BRANCH_NAME" -- src/test 2>/dev/null)

if [ -n "$BRANCH_SRC_FILES" ]; then
  for file in $BRANCH_SRC_FILES; do
    SRC_CODE+="\n\n// File: $file\n"
    SRC_CODE+="$(git show "$BRANCH_NAME:$file" 2>/dev/null)"
  done
fi

if [ -n "$BRANCH_TEST_FILES" ]; then
  for file in $BRANCH_TEST_FILES; do
    TEST_CODE+="\n\n// File: $file\n"
    TEST_CODE+="$(git show "$BRANCH_NAME:$file" 2>/dev/null)"
  done
fi

# 3. Get initial files from main branch
echo "Collecting initial files from main branch..."
INITIAL_SRC_CODE=""
INITIAL_TEST_CODE=""

# Get list of source files in main
MAIN_SRC_FILES=$(git ls-tree -r --name-only main -- src/main 2>/dev/null)
MAIN_TEST_FILES=$(git ls-tree -r --name-only main -- src/test 2>/dev/null)

if [ -n "$MAIN_SRC_FILES" ]; then
  for file in $MAIN_SRC_FILES; do
    INITIAL_SRC_CODE+="\n\n// File: $file\n"
    INITIAL_SRC_CODE+="$(git show "main:$file" 2>/dev/null)"
  done
fi

if [ -n "$MAIN_TEST_FILES" ]; then
  for file in $MAIN_TEST_FILES; do
    INITIAL_TEST_CODE+="\n\n// File: $file\n"
    INITIAL_TEST_CODE+="$(git show "main:$file" 2>/dev/null)"
  done
fi

# 4. Get git diff between main and the branch
echo "Getting diff between main and $BRANCH_NAME..."
GIT_DIFF=$(git diff main..$BRANCH_NAME)

# 5. Prepare the evaluation prompt (from README.md)
read -r -d '' PROMPT << EOM
Analyze the following data from a 30-minute Test-Commit-Revert (TCR) coding session for the "Social Network Kata". The goal is to evaluate the development process based on the provided commit log, initial code, final code, and git diff.

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

2.  **Initial Source Code (from main branch):**

$INITIAL_SRC_CODE

3.  **Initial Test Code (from main branch):**

$INITIAL_TEST_CODE

4.  **Final Source Code (from $BRANCH_NAME branch):**

$SRC_CODE

5.  **Final Test Code (from $BRANCH_NAME branch):**

$TEST_CODE

6.  **Git Diff (showing changes from main to $BRANCH_NAME):**

$GIT_DIFF

**Analysis Instructions:**
- Compare the initial code from the main branch with the final code from the branch to understand what development work was done.
- Use the git diff to see exactly what changes were made during the session.
- Consider how the commit messages align with the actual code changes you can see in the diff.
- Analyze the progression of development based on commit messages and the resulting code.

**Analysis Task:**

Based on the provided data, calculate the following metrics and provide a general summary of the session. Adhere strictly to the definitions provided below.

**Metric Definitions:**

ai_support_level: "none", "completion", "edit" or "agent". The amount of AI support in the task.
day: Integer 0-30. The number of times I have done the tasks with different level of AI support.
main_use_cases_coverage: Integer (0-6). Count how many of the 6 main requirements (Posting, Reading, Following, Mentions, Links, Direct Messages) appear to be implemented and tested according to the final code and tests.
additional_edge_cases: Integer. Count the number of distinct edge cases or alternative scenarios explicitly handled in the tests beyond the most basic path for each implemented main requirement.
time_to_completion_in_seconds: Integer. The time between the first and the last commit provided, which calculates the exact duration of the session.
test_to_code_ratio: Float. Calculate the ratio of lines of code in the final test files to the lines of code in the final source files (excluding comments and blank lines if possible, otherwise total lines). State the method used (e.g., total lines or non-comment/blank lines).
average_assertions_per_test: Float. Calculate the average number of assertion statements per test method/function found in the final test code.
cyclomatic_complexity: Integer. Estimate the *average* cyclomatic complexity across the functions/methods in the *final source code*. If possible, state the tool/methodology used for estimation (e.g., counting decision points + 1).
code_duplication_percentage: Float. Estimate the percentage of duplicated code within the *final source code*. Briefly state the basis for this estimation (e.g., visual inspection, assumed tooling).
code_smells: Integer. Identify the number of potential code smells present in the *final source code* based on common definitions (e.g., Long Method, Large Class, Feature Envy, Duplicated Code, etc.). List the identified smells.
test_focus_rating: Integer (1-5). Rate the tests based on their focus on *behavior* (what the system should do) versus *implementation details* (how it does it). 1 = Heavily implementation-focused, difficult to understand behavior; 5 = Clearly specifies behavior (BDD-like), independent of implementation details.
implementation_clean_code_rating: Integer (1-5). Rate the *final source code* based on general Clean Code principles (e.g., meaningful names, small functions, SRP, comments quality, formatting). 1 = Poor adherence; 5 = Excellent adherence.
test_clean_code_rating: Integer (1-5). Rate the *final test code* based on general Clean Code principles (e.g., readability, structure like Arrange-Act-Assert, clear assertion messages, meaningful test names). 1 = Poor adherence; 5 = Excellent adherence.
commits_per_minute: Float. Calculates how many commits per minute are done, to measure the duration of the feedback cycle.
tcr_revert_rate: Float (1.0-0.0) Calculates how many percentage of commits are revert commits resetting progress. 1 being 100%, 0.5 being 50%, and 0 being 0%.

**Output Format:**

Provide ONLY the JSON object containing the calculated metrics and an overall generated description:


**Example JSON Structure:**
{
  "ai_support_level": "completion",
  "day": 2,
  "main_use_cases_coverage": 3,
  "additional_edge_cases": 2,
  "time_to_completion_in_seconds": 1800,
  "test_to_code_ratio": 1.8,
  "average_assertions_per_test": 1.5,
  "cyclomatic_complexity": 2,
  "code_duplication_percentage": 5.0,
  "code_smells": 2,
  "test_focus_rating": 4,
  "implementation_clean_code_rating": 3,
  "test_clean_code_rating": 4,
  "description": "The coding session, which employed the Test-Commit-Revert (TCR) methodology, spanned approximately 27 minutes and 16 seconds according to the commit timestamps. The commit log indicates a development process that involved adding basic timeline functionality (publish, retrieve), handling multiple messages, and implementing access control features (owner-only restriction, general access, granular access). The log also suggests refactoring efforts to improve test setup. However, the provided final source code and test code are in an initial, largely empty state. The TimelineService class is defined but contains no implementation, and the TimelineServiceTest class contains only one empty test method named example. This final state does not reflect the features described as being added and committed in the commit history. Consequently, based strictly on the final code and tests, none of the main social network kata requirements are met, there are no implemented behaviors or tested edge cases, and code quality metrics like complexity and duplication are minimal or non-applicable due to the lack of code. The tests, in their final form, lack clear focus or adherence to clean code principles. The most notable aspect of this session's data is the significant discrepancy between the commit history, which portrays steady progress and successful commits under TCR, and the final code snapshot provided.",
  "commits_per_minute": 1.2,
  "tcr_revert_rate": 0.2
}
EOM

echo "GEMINI FINAL PROMPT: $PROMPT"

# 6. Call Gemini API to get the evaluation JSON and append to results.ndjson

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
