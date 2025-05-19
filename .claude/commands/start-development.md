1. Open **@prompt_plan.md** understand all tasks to be done.
2. check the git log (`git log main.. | cat`) and git diff (`git diff main | cat`) to identify which tasks were already implemented
3. For each incomplete task:
    - Double-check if it's truly unfinished (if uncertain, ask for clarification).
    - If you confirm it's already done, skip it.
    - Otherwise, implement it as described following the tcr cycle approach described in @spec.md executing `./tcr.sh -c`.
4. Follow exactly the development process described in @/.claude/docs/development-process.md