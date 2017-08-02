package xh.freess;

/**
 * Created by G1494458 on 2017/7/25.
 */

public class FreeSSAccount {
    /**
     *  <h4>IP Address:<span id="ipusa">a.usip.pro</span>  <span class="copybtn" data-clipboard-target="#ipusa"><i class="fa fa-accountToBase64"></i></span></h4>
     <h4>Port：443</h4>
     <h4>Password:<span id="pwusa">38767662</span>  <span class="copybtn" data-clipboard-target="#pwusa"><i class="fa fa-accountToBase64"></i></span></h4>
     <h4>Method:aes-256-cfb</h4>

     */
    private String proxyServer;
    private String port;
    private String password;
    private String method;
    private String qrcode;
    private String itemBg;

    public String getProxyServer() {
        return proxyServer;
    }

    public void setProxyServer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getItemBg() {
        return itemBg;
    }

    public void setItemBg(String itemBg) {
        this.itemBg = itemBg;
    }
}
