package bank.com.config;

import java.util.Properties;

/**
* Class which manages the configuration properties across the entire application.
 * @author Jakub Hofman
 */
public class ConfigManager {
    public static String configFilePath = "config.properties";

    public static int port;
    public static int portMinimum;
    public static int portMaximum;

    public static int poolSize;
    public static int backlogSize;

    public static String dbUrl;

    public static String dbUser;

    public static String dbPassword;

    public static int clientTimeout;

    public static int scanTimeout;

    public static int connectTimeout;

    public static int commandTimeout;

    public static int accountNumberMin;
    public static int accountNumberMax;
    public static int accountBalanceMin;

    /**
     * Initializes the configuration settings by loading values from a properties file.
     * This method reads various configuration parameters such as server port, database credentials,
     * timeouts, and account-related settings from the specified properties file.
     *
     * @throws Exception If there's an error loading the properties file or parsing the values.
     */
    public static void initializeConfig() throws Exception {
        Properties props = new Properties();
        props.load(ConfigManager.class.getClassLoader().getResourceAsStream(configFilePath));

        port = Integer.parseInt(props.getProperty("server.port"));
        portMinimum = Integer.parseInt(props.getProperty("server.port.min.range"));
        portMaximum = Integer.parseInt(props.getProperty("server.port.max.range"));
        backlogSize = Integer.parseInt(props.getProperty("server.backlog"));

        poolSize = Integer.parseInt(props.getProperty("server.thread.pool.size"));
        dbUrl = props.getProperty("db.url");
        dbUser = props.getProperty("db.username");
        dbPassword = props.getProperty("db.password");
        clientTimeout = Integer.parseInt(props.getProperty("server.client.timeout"));
        scanTimeout = Integer.parseInt(props.getProperty("server.client.scan.timeout"));
        connectTimeout = Integer.parseInt(props.getProperty("server.client.connect.timeout"));
        commandTimeout = Integer.parseInt(props.getProperty("client.command.timeout"));

        accountNumberMin = Integer.parseInt(props.getProperty("client.account.number.min.range"));
        accountNumberMax = Integer.parseInt(props.getProperty("client.account.number.max.range"));
        accountBalanceMin = Integer.parseInt(props.getProperty("client.account.balance.min"));
    }

}
