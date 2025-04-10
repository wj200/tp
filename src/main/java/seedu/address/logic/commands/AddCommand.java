package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NATIONALITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_NRIC + "NRIC "
            + PREFIX_GENDER + "GENDER "
            + PREFIX_DOB + "DOB "
            + PREFIX_DATE + "DATE OF JOINING "
            + PREFIX_NATIONALITY + "NATIONALITY "
            + PREFIX_ADDRESS + "ADDRESS/POSTAL CODE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_NRIC + "T0312345A "
            + PREFIX_GENDER + "Male "
            + PREFIX_DOB + "02-Jan-2001 "
            + PREFIX_DATE + "15-Apr-2025 "
            + PREFIX_NATIONALITY + "Singaporean "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25/119780 "
            + PREFIX_TAG + "Finance/Full-Time/Financial Analyst\n\n"
            + "Command to follow: add n/ p/ e/ ic/ g/ d/ j/ nat/ a/ t/// ";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // Add the validation for date of birth vs date of joining
        if (!isDateOfJoiningValid(toAdd.getDob().value, toAdd.getDateOfJoining().value)) {
            throw new CommandException("Date of joining cannot be earlier than date of birth.");
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    /**
     * Checks if the date of joining is valid (not earlier than date of birth)
     * @param dobString Date of birth in dd-MMM-yyyy format
     * @param dojString Date of joining in dd-MMM-yyyy format
     * @return true if date of joining is valid, false otherwise
     */
    private boolean isDateOfJoiningValid(String dobString, String dojString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            Date dob = sdf.parse(dobString);
            Date doj = sdf.parse(dojString);

            // Check if date of joining is on or after date of birth
            return !doj.before(dob);
        } catch (ParseException e) {
            // This shouldn't happen since we've already validated the format
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
