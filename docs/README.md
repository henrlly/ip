# Bott User Guide

Bott is a text-based chatbot that helps you keep track of your tasks — todos, deadlines, and
events — directly from the command line. Type a command, press Enter, and Bott tells you what it
did.

```
    ____________________________________________________________
 ____     ___     _____   _____ 
|  _ \   / _ \   |_   _| |_   _|
| |_) | | | | |    | |     | |  
|  _ <  | | | |    | |     | |  
| |_) | | |_| |    | |     | |  
|____/   \___/     |_|     |_|  
    ____________________________________________________________
     Hello! I'm Bott.
     What can I do for you?
    ____________________________________________________________
```

## Quick start

1. Make sure you have Java 25 installed.
2. Run `Bott.java` (see the main [README](../README.md) for how to open the project in an IDE).
3. Type a command into the console and press Enter. Bott's response appears between two
   horizontal lines.
4. Type `bye` when you're done, and Bott will say goodbye and the program will exit.

Tasks only exist for the current session — closing Bott clears them. There is currently no support
for saving tasks to disk between sessions.

## Adding a todo: `todo`

Adds a task with just a description — no date or time attached to it.

Example: `todo <description>`

```
todo borrow book
```

Expected output:
```
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
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
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 1 tasks in the list.
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
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 1 tasks in the list.
    ____________________________________________________________
```

## Listing all tasks: `list`

Shows every task currently stored, numbered in the order they were added, along with its type icon
(`[T]`/`[D]`/`[E]`) and status icon (`[X]` for done, `[ ]` for not done).

Example: `list`

Expected output (after adding the todo, deadline, and event above):
```
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
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
     Nice! I've marked this task as done:
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
     OK, I've marked this task as not done yet:
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
     Noted. I've removed this task:
       [T][ ] read book
     Now you have 0 tasks in the list.
    ____________________________________________________________
```

## Exiting the program: `bye`

Says goodbye and ends the program.

Example: `bye`

Expected output:
```
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Error handling

If a command is missing information it needs, or Bott doesn't recognize it at all, Bott tells you
specifically what's wrong (and, where possible, how to fix it) instead of crashing.

Example: `todo` with no description
```
todo
```

Expected output:
```
    ____________________________________________________________
     OOPS!!! A todo needs a description. Try: todo <description>
    ____________________________________________________________
```

Example: an unrecognized command
```
blah
```

Expected output:
```
    ____________________________________________________________
     OOPS!!! I don't recognize "blah" as a command. Try: list, todo, deadline, event, mark, unmark, delete, or bye.
    ____________________________________________________________
```

Other commands are validated the same way — for example, `mark`/`unmark`/`delete` reject a missing,
non-numeric, or out-of-range task number, and `deadline`/`event` reject a missing `/by`, `/from`, or
`/to`.

## Command summary

| Action | Format | Example |
|---|---|---|
| Add a todo | `todo <description>` | `todo borrow book` |
| Add a deadline | `deadline <description> /by <date/time>` | `deadline return book /by Sunday` |
| Add an event | `event <description> /from <start> /to <end>` | `event project meeting /from Mon 2pm /to 4pm` |
| List all tasks | `list` | `list` |
| Mark a task as done | `mark <task number>` | `mark 1` |
| Mark a task as not done | `unmark <task number>` | `unmark 1` |
| Delete a task | `delete <task number>` | `delete 1` |
| Exit | `bye` | `bye` |
