package de.cubeside.connection;

public class GlobalClientConfig {
    private String hostname;
    private int port;
    private String user;
    private String password;

    public GlobalClientConfig() {

    }

    public boolean initDefaultValues() {
        boolean updated = false;
        if (hostname == null) {
            hostname = "localhost";
            updated = true;
        }
        if (port == 0) {
            port = 25701;
            updated = true;
        }
        if (user == null) {
            user = "CHANGEME";
            updated = true;
        }
        if (password == null) {
            password = "CHANGEME";
            updated = true;
        }
        return updated;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
