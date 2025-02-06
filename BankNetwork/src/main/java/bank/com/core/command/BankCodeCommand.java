package bank.com.core.command;

import java.io.PrintWriter;

/**
 * This class represents a command to set the bank code in the system.
 * It implements the Command interface and provides an execute method to send the bank code to the output stream.
 * @author Jakub Hofman
 */
public class BankCodeCommand implements Command {
    private final PrintWriter writer;
    private final String bankCode;

    public BankCodeCommand(PrintWriter writer, String bankCode) {
        this.writer = writer;
        this.bankCode = bankCode;
    }

    @Override
    public void execute(String[] args) {
        writer.println("BC " + bankCode);
    }
}
