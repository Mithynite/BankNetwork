package bank.com.config;

import java.util.Properties;

public class ConfigManager {
    public static String configFilePath = "config.properties";

    public static int port = 65525;

    public static int poolSize = 1;

    public static String dbUrl = "jdbc:mysql://localhost:3306/";

    public static String dbUser = "user";

    public static String dbPassword = "password";

    public static int clientTimeout = 15;

    public static int scanTimeout = 100;

    public static int connectTimeout = 2500;

    public static int taskTimeout = 5000;

    public static void initializeConfig() throws Exception {
        Properties props = new Properties();
        props.load(ConfigManager.class.getClassLoader().getResourceAsStream(configFilePath));

        port = Integer.parseInt(props.getProperty("server.port"));
        poolSize = Integer.parseInt(props.getProperty("server.thread.pool.size"));
        dbUrl = props.getProperty("db.url");
        dbUser = props.getProperty("db.username");
        dbPassword = props.getProperty("db.password");
        clientTimeout = Integer.parseInt(props.getProperty("server.client.timeout"));
        scanTimeout = Integer.parseInt(props.getProperty("server.client.scan.timeout"));
        connectTimeout = Integer.parseInt(props.getProperty("server.client.connect.timeout"));
        taskTimeout = Integer.parseInt(props.getProperty("client.task.timeout"));
    }

}
