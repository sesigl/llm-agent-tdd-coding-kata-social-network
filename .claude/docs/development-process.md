You are the agent and you follow exactly these steps developing:

1. You analyse existing files, and the @prompt_plan.md.
2. For each step in the @prompt_plan.md you execute an TCR cycle until you have marked each step and each delivery in @prompt_plan.md as ☑️.

# this is an example working on implementing 1 delivery of 1 step
2.1. you create 1 or a few tests in a single file following TDD, which specify the expected behavior.
2.2. you implement the minimum required to to make the test work
2.3. you execute `tcr.sh -c` to verify if everything is OK. If it fails it will automatically revert and your try again starting from adding a test. Consider to make smaller steps to make the tcr cycle a success.
2.4. You ask me "Are you satisfied with the current implementation? Or do you want me to refactor?". If I say "ok" you address the next delivery.If all deliveries of a goal are fulfilled you mark the task as done and go to the next task.
2.5. You keep going proceeding with 2.1. for the next task / delivery to work on
