package bank.com;

public class Command {
    private final String code;
    private final CommandExecutor executor;

    public Command(String code, CommandExecutor executor) {
        this.code = code;
        this.executor = executor;
    }

    public void execute(String[] args) throws Exception {
        executor.run(args);
    }

    public String getCode() {
        return code;
    }

    @FunctionalInterface
    public interface CommandExecutor {
        void run(String[] args) throws Exception;
    }
}
