package bank.com;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Command {
    private String code;
    private Method method;
    private final Object targetInstance; // Instance of the target object where the method is defined

    public Command(String code, Method method, Object targetInstance) {
        this.code = code;
        this.method = method;
        this.targetInstance = targetInstance;
    }

    /**
     * Executes the command, explicitly taking a Scanner as an argument.
     * @param scanner The Scanner instance for input.
     * @throws Exception If method invocation fails.
     */
    public void execute(Scanner scanner) throws Exception {
        method.invoke(targetInstance, scanner); // Calls the method on the target object with the Scanner argument
    }

    public String getCode() {
        return code;
    }
}
