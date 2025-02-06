package bank.com.core.command;

/**
 * @author Jakub Hofman
*/
public interface Command {
    void execute(String[] args) throws Exception;
}
