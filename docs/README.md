# Sergeant Bott User Guide

Sergeant Bott is a text-based chatbot that helps you keep track of your tasks — todos, deadlines,
events, and fixed-duration tasks — directly from the command line, run with the discipline of a
drill sergeant running roll call. Type a command, press Enter, and Sarge tells you what it did.

```
    ____________________________________________________________
 ____        _       _____      ____    _____ 
/ ___|      / \     |  __ \    / ___|  |  ___|
\___ \     / _ \    | |__) |  | |  _   | |__  
 ___) |   / ___ \   |  _  /   | |_| |  |  __| 
|____/   /_/   \_\  |_|  \_\   \____|  |_____|
    ____________________________________________________________
     Ten-hut! Sergeant Bott reporting for duty.
     What's your first order, recruit?
    ____________________________________________________________
```

## Quick start

1. Make sure you have Java 25 installed.
2. Run `Bott.java` (see the main [README](../README.md) for how to open the project in an IDE).
3. Type a command into the console and press Enter. Sarge's response appears between two
   horizontal lines.
4. Type `bye` when you're done, and Sarge will dismiss you and the program will exit.

Sergeant Bott also has a graphical interface: run `./gradlew run` to open a chat window where you
type the same commands into a text box and Sarge's replies appear as chat bubbles. The commands
below are identical in both interfaces (the console examples just show the extra divider lines).

Tasks are saved to `data/bott.txt` and reloaded the next time you start Sergeant Bott.

## Adding a todo: `todo`

Adds a task with just a description — no date or time attached to it.

Example: `todo <description>`

```
todo borrow book
```

Expected output:
```
    ____________________________________________________________
     Mission logged, recruit! Fall in:
       [T][ ] borrow book
     You now have 1 mission(s) on the roster.
    ____________________________________________________________
```

## Adding a deadline: `deadline`

Adds a task that needs to be done before a specific date/time. Bott stores the date/time exactly
as you type it — it doesn't need to be in any particular format.

Example: `deadline <description> /by <date/time>`

```
deadline return book /by Sunday
```

Expected output:
```
    ____________________________________________________________
     Mission logged, recruit! Fall in:
       [D][ ] return book (by: Sunday)
     You now have 1 mission(s) on the roster.
    ____________________________________________________________
```

## Adding an event: `event`

Adds a task that starts at a specific date/time and ends at a specific date/time.

Example: `event <description> /from <start> /to <end>`

```
event project meeting /from Mon 2pm /to 4pm
```

Expected output:
```
    ____________________________________________________________
     Mission logged, recruit! Fall in:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     You now have 1 mission(s) on the roster.
    ____________________________________________________________
```

## Adding a fixed-duration task: `duration`

Adds a task that needs a set amount of time but has no fixed start or end — for example, a
sales report that will take two hours to read whenever you get to it.

Give the amount after `/for` as `<hours>h`, `<minutes>m`, or the two combined (`1h30m` or
`1h 30m`). A unit is required, and the total must be more than zero.

Example: `duration <description> /for <amount>`

```
duration read sales report /for 2h
```

Expected output:
```
    ____________________________________________________________
     Mission logged, recruit! Fall in:
       [F][ ] read sales report (for: 2h)
     You now have 1 mission(s) on the roster.
    ____________________________________________________________
```

## Listing all tasks: `list`

Shows every task currently stored, numbered in the order they were added, along with its type icon
(`[T]`/`[D]`/`[E]`) and status icon (`[X]` for done, `[ ]` for not done).

Example: `list`

Expected output (after adding the todo, deadline, and event above):
```
    ____________________________________________________________
     Roll call! Here's your mission roster:
     1.[T][ ] read book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
    ____________________________________________________________
```

## Finding tasks: `find`

Shows every task whose description contains the given keyword, matching case-insensitively, numbered
from 1 (this numbering is just for this result — it doesn't change the tasks' numbers in `list`).

Example: `find <keyword>`

```
find book
```

Expected output (given a list containing "read book" and "return book (by: June 6th)", among others):
```
    ____________________________________________________________
     Found these missions matching your intel:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
    ____________________________________________________________
```

## Marking a task as done: `mark`

Marks the given task number (as shown by `list`) as done.

Example: `mark <task number>`

```
mark 1
```

Expected output:
```
    ____________________________________________________________
     Outstanding! Mission accomplished:
       [T][X] read book
    ____________________________________________________________
```

## Marking a task as not done: `unmark`

Reverts the given task number back to not done.

Example: `unmark <task number>`

```
unmark 1
```

Expected output:
```
    ____________________________________________________________
     At ease. Mission's back on the roster:
       [T][ ] read book
    ____________________________________________________________
```

## Deleting a task: `delete`

Removes the given task number from the list. Every task after it moves up by one number.

Example: `delete <task number>`

```
delete 1
```

Expected output:
```
    ____________________________________________________________
     Mission scrubbed, recruit! Fall out:
       [T][ ] read book
     You now have 0 mission(s) on the roster.
    ____________________________________________________________
```

## Exiting the program: `bye`

Says goodbye and ends the program.

Example: `bye`

Expected output:
```
    ____________________________________________________________
     Dismissed! Fall out, recruit.
    ____________________________________________________________
```

## Error handling

If a command is missing information it needs, or Sarge doesn't recognize it at all, Sarge tells you
specifically what's wrong (and, where possible, how to fix it) instead of crashing.

Example: `todo` with no description
```
todo
```

Expected output:
```
    ____________________________________________________________
     NEGATIVE, RECRUIT! A todo needs a description. Try: todo <description>
    ____________________________________________________________
```

Example: an unrecognized command
```
blah
```

Expected output:
```
    ____________________________________________________________
     NEGATIVE, RECRUIT! That's not an order I recognize: "blah". Try: list, todo, deadline, event, duration, find, mark, unmark, delete, or bye.
    ____________________________________________________________
```

Other commands are validated the same way — for example, `mark`/`unmark`/`delete` reject a missing,
non-numeric, or out-of-range task number, `deadline`/`event` reject a missing `/by`, `/from`, or
`/to`, and `duration` rejects a missing `/for` or an amount without an `h`/`m` unit.

## Command summary

| Action | Format | Example |
|---|---|---|
| Add a todo | `todo <description>` | `todo borrow book` |
| Add a deadline | `deadline <description> /by <date/time>` | `deadline return book /by Sunday` |
| Add an event | `event <description> /from <start> /to <end>` | `event project meeting /from Mon 2pm /to 4pm` |
| Add a fixed-duration task | `duration <description> /for <amount>` | `duration read sales report /for 2h` |
| List all tasks | `list` | `list` |
| Find tasks by keyword | `find <keyword>` | `find book` |
| Mark a task as done | `mark <task number>` | `mark 1` |
| Mark a task as not done | `unmark <task number>` | `unmark 1` |
| Delete a task | `delete <task number>` | `delete 1` |
| Exit | `bye` | `bye` |
