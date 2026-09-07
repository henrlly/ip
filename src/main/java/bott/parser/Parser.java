package bott.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bott.BottException;
import bott.task.Deadline;
import bott.task.Event;
import bott.task.Todo;

/**
 * Makes sense of raw command-line input typed by the user: splitting it
 * into a command word and arguments, and parsing those arguments into the
 * values Bott's commands need.
 */
public class Parser {

    /** Markers that separate the parts of a deadline's or event's arguments. */
    private static final String MARKER_BY = "/by";
    private static final String MARKER_FROM = "/from";
    private static final String MARKER_TO = "/to";

    /** Format reminders appended to a command's error messages. */
    private static final String USAGE_DEADLINE = "Try: deadline <description> /by <yyyy-MM-dd>";
    private static final String USAGE_EVENT =
            "Try: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>";

    /**
     * Returns the command word of a line of user input, e.g. "todo" for
     * "todo read book".
     *
     * @param input Line of user input.
     * @return Command word (the text before the first space, or the whole
     *         input if there is no space).
     */
    public static String getCommandWord(String input) {
        String[] commandAndArgs = input.split(" ", 2);
        // Indexing [0] unconditionally is safe only because String.split with
        // a positive limit always returns at least the input itself, even for
        // an empty or all-blank line; make that reliance explicit.
        assert commandAndArgs.length >= 1 : "split should yield at least one element";
        return commandAndArgs[0];
    }

    /**
     * Returns the arguments of a line of user input, i.e. everything after
     * the command word.
     *
     * @param input Line of user input.
     * @return Text after the command word, or "" if there is none.
     */
    public static String getArguments(String input) {
        String[] commandAndArgs = input.split(" ", 2);
        return commandAndArgs.length > 1 ? commandAndArgs[1] : "";
    }

    /**
     * Parses the arguments of a "todo" command.
     *
     * @param args Text after the "todo" command word.
     * @return Todo task described by {@code args}.
     * @throws BottException If {@code args} has no description.
     */
    public static Todo parseTodo(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException("A todo needs a description. Try: todo <description>");
        }
        return new Todo(args);
    }

    /**
     * Parses the arguments of a "deadline" command, of the form
     * "{@code <description>} /by {@code <by>}".
     *
     * @param args Text after the "deadline" command word.
     * @return Deadline task described by {@code args}.
     * @throws BottException If {@code args} is missing a description, the
     *         "/by" marker, or a valid date after it.
     */
    public static Deadline parseDeadline(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException("A deadline needs a description. " + USAGE_DEADLINE);
        }
        int byIndex = args.indexOf(MARKER_BY);
        if (byIndex == -1) {
            throw new BottException("A deadline needs a \"/by\" date. " + USAGE_DEADLINE);
        }
        String description = args.substring(0, byIndex).trim();
        String by = args.substring(byIndex + MARKER_BY.length()).trim();
        if (description.isEmpty()) {
            throw new BottException("A deadline needs a description. " + USAGE_DEADLINE);
        }
        if (by.isEmpty()) {
            throw new BottException("The \"by\" date of a deadline cannot be empty. " + USAGE_DEADLINE);
        }
        return new Deadline(description, parseDate("by", by));
    }

    /**
     * Parses the arguments of an "event" command, of the form
     * "{@code <description>} /from {@code <from>} /to {@code <to>}".
     *
     * @param args Text after the "event" command word.
     * @return Event task described by {@code args}.
     * @throws BottException If {@code args} is missing a description, the
     *         "/from" or "/to" markers, or either valid date.
     */
    public static Event parseEvent(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException("An event needs a description. " + USAGE_EVENT);
        }
        int fromIndex = args.indexOf(MARKER_FROM);
        if (fromIndex == -1) {
            throw new BottException("An event needs a \"/from\" start date. " + USAGE_EVENT);
        }
        int toIndex = args.indexOf(MARKER_TO, fromIndex);
        if (toIndex == -1) {
            throw new BottException(
                    "An event needs a \"/to\" end date after its \"/from\" start date. " + USAGE_EVENT);
        }
        String description = args.substring(0, fromIndex).trim();
        String from = args.substring(fromIndex + MARKER_FROM.length(), toIndex).trim();
        String to = args.substring(toIndex + MARKER_TO.length()).trim();
        if (description.isEmpty()) {
            throw new BottException("An event needs a description. " + USAGE_EVENT);
        }
        if (from.isEmpty()) {
            throw new BottException("The \"from\" start date of an event cannot be empty. " + USAGE_EVENT);
        }
        if (to.isEmpty()) {
            throw new BottException("The \"to\" end date of an event cannot be empty. " + USAGE_EVENT);
        }
        return new Event(description, parseDate("from", from), parseDate("to", to));
    }

    /**
     * Parses a date in "yyyy-MM-dd" format.
     *
     * @param fieldLabel Name of the field being parsed, used to phrase the error message
     *         (e.g. "by", "from", "to").
     * @param value Text to parse as a date.
     * @return Parsed date.
     * @throws BottException If {@code value} is not a valid "yyyy-MM-dd" date.
     */
    private static LocalDate parseDate(String fieldLabel, String value) throws BottException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new BottException(
                    "The \"" + fieldLabel + "\" date must be in yyyy-MM-dd format (e.g. 2019-10-15). \""
                            + value + "\" is not a valid date.");
        }
    }

    /**
     * Parses the arguments of a "find" command.
     *
     * @param args Text after the "find" command word.
     * @return Keyword to search task descriptions for.
     * @throws BottException If {@code args} has no keyword.
     */
    public static String parseFind(String args) throws BottException {
        if (args.isBlank()) {
            throw new BottException("Please specify a keyword to search for. Try: find <keyword>");
        }
        return args.trim();
    }

    /**
     * Parses and validates the task number argument of a "mark"/"unmark"/
     * "delete" command.
     *
     * @param commandName Command word the user typed, used to phrase error
     *         messages.
     * @param args Text after the command word.
     * @param taskCount Number of tasks currently stored.
     * @return Task number in {@code args}, as a 1-based index.
     * @throws BottException If {@code args} is missing, not a number, or
     *         does not name an existing task.
     */
    public static int parseTaskNumber(String commandName, String args, int taskCount) throws BottException {
        if (args.isBlank()) {
            throw new BottException("Please specify a task number. Try: " + commandName + " <task number>");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args.trim());
        } catch (NumberFormatException exception) {
            throw new BottException(
                    "\"" + args.trim() + "\" is not a valid task number. "
                            + "Try: " + commandName + " <task number>");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BottException(
                    "There is no task number " + taskNumber + " in your list. You currently have "
                            + taskCount + " task(s).");
        }
        return taskNumber;
    }
}
