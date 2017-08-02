package xh.freess;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bamboo on 2017/8/2.
 */

public class SSProfile {

    /**
     * server : my_server_ip
     * server_port : 8388
     * local_port : 1080
     * password : barfoo!
     * timeout : 600
     * method : chacha20-ietf-poly1305
     */

    @SerializedName("server")
    private String server;
    @SerializedName("server_port")
    private int serverPort;
    @SerializedName("local_port")
    private int localPort = 1080;
    @SerializedName("password")
    private String password;
    @SerializedName("timeout")
    private int timeout = 600;
    @SerializedName("method")
    private String method;

    public SSProfile(String server, int serverPort, String password, String method) {
        this.server = server;
        this.serverPort = serverPort;
        this.password = password;
        this.method = method;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
