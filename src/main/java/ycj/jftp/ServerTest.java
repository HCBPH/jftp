package ycj.jftp;

import ycj.jftp.server.JFTPServer;

import java.io.File;

/**
 * @author 53059
 * @date 2021/12/10 13:51
 */
public class ServerTest {
    public static void main(String[] args) {
        System.out.println("当前操作系统:"+System.getProperty("os.name"));
        JFTPServer server = JFTPServer.getInstance();
        server.run();
    }
}
