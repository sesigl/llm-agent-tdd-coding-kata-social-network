# Social Network Coding Kata


This repository contains different branches with correct but different solutions to compare effectiveness of AI programming.

The following branches are available:
- `tdd-only` - Pure TDD without AI
- `tdd-with-ai` TDD with AI

The comparison wont be perfect. I practiced implementing this kata before, and did it again with different limitations. This might not reflect reality.

## Hypothesis

LLMs have become very fast and powerful generating code. 

Hence, if instructions are clear they can solve problems in seconds.

The biggest limiting factor of LLMs is that the quality of code they are trained on, might be too low for what you expect. Building on "average" code
your company might be go fast in the early stages, but will slow down as the codebase grows.

This can be addressed by using TDD to provide from the beginning long-living executable guardrails. That's nothing new, but the question is if LLMs can accelerate engineers so we get the best of both worlds.

## The tooling

To make TDD more strict and comparable, a technique [test-commit-revert](https://nvoulgaris.com/test-commit-revert/) is used. This gives us a more strict ruleset how to do TDD, but also
allows us to do more analysis comparing development with and without agentic help. 

### The development cycle

TCR is a more strict TDD. I recommend the following executing cycle (which might be different how others do TCR):

Preparation:
- run `./tcr.sh` to start the TCR process

Cycle:
1. Write a test, press "f" to check if the build or test fails
2. Do the implementation
3. Press "ENTER" to execute the TCR check. If tests are green then the changes are committed. If not the changed are reset. In both cases there is an empty or not empty commit with a useful commit message leveraging Gemini for traceability.
4. Start again with Step 1

## The Kata

Source: https://kata-log.rocks/social-network-kata

This is an incremental kata to simulate a real business situation: work your way through the steps in order, but do not read the next requirement before you have finished your current one.

Your Team is tired of all those boring tasks like bowling game scores, bank accounts, singing songs or commanding mars rovers. This time you want to do something truly innovative: A Social Network!

### Backlog of requirements

- Posting: Alice can publish messages to a personal timeline
- Reading: Bob can view Alice’s timeline
- Following: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions
- Mentions: Bob can link to Charlie in a message using “@”
- Links: Alice can link to a clickable web resource in a message
- Direct Messages: Mallory can send a private message to Alice


