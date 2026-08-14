# Bott UI test plan

This file records the text-UI test cases for Bott. They are run by the `test-ui` skill
(`.claude/skills/test-ui/SKILL.md`), which feeds each test case's **Input** lines to the compiled
program's standard input and checks the program's standard output against **Expected output**,
character for character (including whitespace, indentation, and blank lines).

Each test case is a single, independent run of the program: its Input lines are sent to one fresh
invocation of `Bott`, in order, ending with `bye`. Expected output is the *entire* console output for
that run, from the first line of the startup banner to the final blank line after the farewell message.

See `AGENTS.md` for when this plan and the `test-ui` skill should be updated/run.

## Test cases

### TC1 — Startup and immediate exit

**Aim:** The program greets the user on startup and exits cleanly on `bye` when no tasks are added.

**Input:**
```
bye
```

**Expected output:**
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

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC2 — Add a todo and list it

**Aim:** `todo <description>` stores a todo task and acknowledges it with a `[T]` icon; `list` shows it
numbered.

**Input:**
```
todo borrow book
list
bye
```

**Expected output:**
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

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC3 — Add a deadline

**Aim:** `deadline <description> /by <by>` stores a deadline task and acknowledges it with a `[D]` icon
and its `(by: ...)` suffix.

**Input:**
```
deadline return book /by Sunday
bye
```

**Expected output:**
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

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC4 — Add an event

**Aim:** `event <description> /from <from> /to <to>` stores an event task and acknowledges it with an
`[E]` icon and its `(from: ... to: ...)` suffix.

**Input:**
```
event project meeting /from Mon 2pm /to 4pm
bye
```

**Expected output:**
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

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC5 — Mark and unmark a task

**Aim:** `mark <n>` marks the nth task done (`[X]`) and `unmark <n>` reverts it to not done (`[ ]`); both
changes are reflected by a following `list`.

**Input:**
```
todo read book
mark 1
list
unmark 1
list
bye
```

**Expected output:**
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

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC6 — Mixed task types in one list

**Aim:** Todos, deadlines, and events can be mixed in the same list, keep their own icons and suffixes,
and are numbered by insertion order regardless of type; marking a non-todo task works the same way as
marking a todo.

**Input:**
```
todo read book
todo return book
deadline submit report /by 11/10/2019 5pm
event orientation week /from 4/10/2019 /to 11/10/2019
mark 1
mark 3
list
bye
```

**Expected output:**
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

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] return book
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] submit report (by: 11/10/2019 5pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
     Now you have 4 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] submit report (by: 11/10/2019 5pm)
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[T][ ] return book
     3.[D][X] submit report (by: 11/10/2019 5pm)
     4.[E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC7 — Empty todo description and unknown command

**Aim:** The two minimal required errors: `todo` with no description, and a command Bott doesn't
recognize, each produce a specific "OOPS!!!" message rather than crashing or being silently ignored.

**Input:**
```
todo
blah
bye
```

**Expected output:**
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

    ____________________________________________________________
     OOPS!!! A todo needs a description. Try: todo <description>
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I don't recognize "blah" as a command. Try: list, todo, deadline, event, mark, unmark, delete, or bye.
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC8 — Task-number and deadline/event syntax errors, then recovery

**Aim:** `mark`/`unmark` on a task number that doesn't exist, a `deadline` missing its `/by` marker, and
an `event` missing its `/to` marker each get a specific, actionable error message, and a bad command
doesn't corrupt state or stop later valid commands from working.

**Input:**
```
mark 1
deadline foo
event foo /from Mon
bye
```

**Expected output:**
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

    ____________________________________________________________
     OOPS!!! There is no task number 1 in your list. You currently have 0 task(s).
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! A deadline needs a "/by" date/time. Try: deadline <description> /by <date/time>
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! An event needs a "/to" end time after its "/from" start time. Try: event <description> /from <start> /to <end>
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC9 — Delete a task

**Aim:** `delete <n>` removes the nth task, acknowledges it by echoing the removed task and the new
count, and every task after it shifts down by one number on the next `list`; deleting continues to work
correctly across repeated deletes.

**Input:**
```
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
list
delete 1
list
bye
```

**Expected output:**
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

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 4 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 5 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] return book (by: June 6th)
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] join sports club
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     4.[T][X] join sports club
     5.[T][ ] borrow book
    ____________________________________________________________

    ____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 4 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
     3.[T][X] join sports club
     4.[T][ ] borrow book
    ____________________________________________________________

    ____________________________________________________________
     Noted. I've removed this task:
       [T][X] read book
     Now you have 3 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][X] return book (by: June 6th)
     2.[T][X] join sports club
     3.[T][ ] borrow book
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```

### TC10 — Delete syntax and range errors

**Aim:** `delete` with no number, a non-numeric number, and a number with no matching task (on an empty
list) each get the same specific error phrasing already used by `mark`/`unmark`, since `delete` shares
their task-number parsing.

**Input:**
```
delete
delete abc
delete 1
bye
```

**Expected output:**
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

    ____________________________________________________________
     OOPS!!! Please specify a task number. Try: delete <task number>
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! "abc" is not a valid task number. Try: delete <task number>
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! There is no task number 1 in your list. You currently have 0 task(s).
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________

```
