# Kotlin Effective Style Guide

This style guide presents effective Kotlin coding practices aligned with ktlint rules. Following these guidelines will help ensure code quality, readability, and maintainability.

## Table of Contents

1. [File Structure](#file-structure)
2. [Naming Conventions](#naming-conventions)
3. [Formatting](#formatting)
4. [Classes and Interfaces](#classes-and-interfaces)
5. [Functions](#functions)
6. [Properties](#properties)
7. [Control Flow](#control-flow)
8. [Null Safety](#null-safety)
9. [Collections](#collections)
10. [Extension Functions](#extension-functions)
11. [Coroutines](#coroutines)
12. [Documentation](#documentation)

## File Structure

- **Package names**: Use lowercase letters and avoid underscores (`org.example.myproject`).
- **File organization**: One class per file, with the filename matching the class name.
- **File order**: Place package statement first, followed by imports, and then declarations.
- **Import statements**: Avoid wildcard imports (`import org.example.*`), sort imports alphabetically, and use separate groups for different package roots.
- **Class order**: Prefer the following order:
  1. Property declarations and initializer blocks
  2. Secondary constructors
  3. Method declarations
  4. Companion object
- **Maximum line length**: Limit lines to 100 characters.

## Naming Conventions

- **Packages**: All lowercase, no underscores (`org.example.myproject`).
- **Classes/Interfaces**: PascalCase (`UserRepository`).
- **Functions/Properties/Variables**: camelCase (`getUserById`).
- **Constants**: All uppercase with underscores (`MAX_COUNT`).
- **Type parameters**: Single uppercase letter or PascalCase with uppercase first letter (`T`, `InputT`).
- **Backing properties**: Prefix underscore (`_count`).
- **Test methods**: Use backticks with spaces for readability (`` `should return null when user not found` ``).

## Formatting

- **Indentation**: Use 4 spaces, not tabs.
- **Braces**: Place opening braces at the end of line, closing braces on a separate line.
- **Line wrapping**: Break after operators, with 8-space continuation indent.
- **Whitespace**:
  - Place one space before and after binary operators (`a + b`).
  - Place one space after commas (`foo(a, b)`).
  - No space between function name and opening parenthesis (`fun foo()`).
  - No spaces around angles/generics (`List<String>`).
  - Place one space after control flow keywords (`if (condition)`).
  - No space after opening/before closing parentheses (`(a + b)`).
- **Trailing commas**: Recommended for multi-line declarations for easier additions and diffs.
- **No semicolons**: Avoid using semicolons at the end of statements.

## Classes and Interfaces

- **Data classes**: Use for simple data carriers without behavior.
- **Primary constructor**: Keep it concise; move complex initialization to init blocks.
- **Inheritance**: Use composition over inheritance where possible.
- **Visibility modifiers**: Always specify visibility modifiers explicitly.
- **Companion objects**: Use for constants and factory methods.
- **Object declarations**: Use for singletons or anonymous class instantiations.
- **Sealed classes**: Use for restricted hierarchies with a finite set of subclasses.

## Functions

- **Function parameters**: Limit to 7 parameters or fewer; use data classes for larger parameter lists.
- **Extension functions**: Use for adding functionality to existing classes without inheritance.
- **Function body**: Keep function bodies concise. Extract complex logic into separate functions.
- **Named arguments**: Use for functions with multiple parameters of the same type or for clarity.
- **Default arguments**: Use to reduce overloads.
- **Single-expression functions**: Use for short functions (`fun double(x: Int): Int = x * 2`).
- **Order parameters**: Place commonly used parameters first.
- **Inline functions**: Use for functions that take lambda parameters to improve performance.

## Properties

- **Read-only properties**: Prefer immutable properties when possible (`val` over `var`).
- **Property accessors**: Keep them simple; prefer computed properties over complex getters/setters.
- **Avoid backing fields**: Use directly exposed properties when possible.
- **Late initialization**: Use `lateinit` or delegate properties rather than nullable types when a property will be initialized before use.

## Control Flow

- **When expressions**: Use instead of switch statements, as exhaustive when possible.
- **Early returns**: Use early returns for guard clauses to reduce nesting.
- **Expression-based conditionals**: Use `if` as an expression rather than statement when possible.
- **For loops**: Use `for` with ranges or iterables, and avoid traditional for loops.
- **While loops**: Use sparingly; prefer collection operations when possible.
- **Condition complexity**: Keep conditions simple; extract complex ones to well-named functions or properties.

## Null Safety

- **Non-null types**: Use non-null types by default; only use nullable types when null is a meaningful value.
- **Safe calls**: Use `?.` for safe navigation rather than explicit null checks.
- **Elvis operator**: Use `?:` for providing default values.
- **Not-null assertion**: Use `!!` only when you're absolutely sure the value isn't null; avoid in regular code.
- **Safe casts**: Use `as?` for safe casting rather than explicit type checks.
- **Lateinit**: Use for non-null properties that are initialized after construction but before use.

## Collections

- **Immutable collections**: Prefer immutable collections when the content doesn't need to change.
- **Collection operations**: Use functional operations (map, filter, etc.) instead of manual iteration.
- **Sequence operations**: Use sequences for large collections or when chaining multiple operations.
- **Collection initialization**: Use collection literals when possible (`listOf`, `mapOf`, etc.).
- **Empty collections**: Use `emptyList()`, `emptyMap()`, etc. instead of creating new instances.
- **Type-specific operations**: Use specialized operations for specific collection types.

## Extension Functions

- **Scope**: Define extension functions in the same file as their usage or in a dedicated extensions file.
- **Naming**: Name extension functions based on what they do, not on the receiver type.
- **Utility**: Create extension functions for operations that conceptually belong to the receiver type.
- **Implementation**: Keep extension functions concise and focused on a single task.
- **Accessibility**: Consider defining utilities as extension functions on existing types rather than creating utility classes.

## Coroutines

- **Scope**: Always use a proper coroutine scope.
- **Dispatchers**: Specify the appropriate dispatcher for the task.
- **Cancellation**: Make coroutines cancellation-aware.
- **Error handling**: Use structured concurrency to propagate errors properly.
- **Flow**: Use for reactive streams of data.
- **Suspending functions**: Mark functions that may suspend with the `suspend` keyword.
- **withContext**: Use to change context while remaining in the same coroutine.

## Documentation

- **KDoc**: Use KDoc for public API documentation.
- **Documentation style**: Write clear, concise documentation focusing on what, not how.
- **Code comments**: Use comments to explain why, not what (the code should be self-explanatory).
- **TODO comments**: Include the reason and preferably the owner (`// TODO(username): Implement caching`).
- **Parameter documentation**: Document all parameters, return values, and exceptions.
- **Sample code**: Provide sample usage in documentation for complex functions.

## Best Practices

- **Immutability**: Prefer immutable objects and properties whenever possible.
- **Type inference**: Let the compiler infer types when obvious.
- **Smart casts**: Take advantage of smart casts after type checks.
- **Prefer language features**: Use Kotlin features like data classes, sealed classes, and delegation.
- **Idiomatic collections**: Use collection operations rather than manual iteration.
- **Scope functions**: Use `let`, `run`, `with`, `apply`, and `also` appropriately.
- **String templates**: Use string templates instead of concatenation.
- **Delegation**: Use `by` for delegation rather than implementing interfaces manually.
- **Lazy initialization**: Use `lazy` for expensive properties that aren't always needed.
- **Destructuring declarations**: Use for extracting multiple values from objects.
- **Result wrapping**: Use `Result` for error handling in appropriate cases.
- **Explicit API mode**: Consider using explicit API mode (`-Xexplicit-api=strict`) for libraries.

Following these guidelines will result in clean, readable, and maintainable Kotlin code that adheres to ktlint rules and community best practices.