package ycj.jftp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import ycj.jftp.initial.ClientInitializer;
import ycj.jftp.server.JFTPServer;

import java.nio.charset.StandardCharsets;

/**
 * @author 53059
 * @date 2021/12/10 12:33
 */
public class JFTPClient {
    private EventLoopGroup group = new NioEventLoopGroup(1);
    Bootstrap bootstrap = new Bootstrap();
    private static JFTPClient instance;
    private static int PORT = 6556;
    private static String REMOTE_ADDRESS = "192.168.66.100";
//    private static String REMOTE_ADDRESS = "127.0.0.1";
    private NioSocketChannel channel = null;
    private boolean isConnected = false;

    private JFTPClient(){}

    public static JFTPClient getInstance(String addr){
        if (JFTPClient.instance == null){
            synchronized (JFTPServer.class){
                JFTPClient.instance = new JFTPClient();
                REMOTE_ADDRESS = addr;
            }
        }
        return instance;
    }

    public void run() {
        try{
            this.bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientInitializer());

            ChannelFuture future = this.bootstrap.connect(REMOTE_ADDRESS, PORT).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        isConnected = true;
                        System.out.println("NettyClient:服务器连接成功");
                    }
                }
            }).sync();
            this.channel = (NioSocketChannel) future.channel();
            this.channel.closeFuture().sync();
        }catch (Exception e){
            System.out.println("NettyClient:出错\n"+e.getLocalizedMessage());
        }finally {
            this.stop();
        }
    };

    public void stop(){
        group.shutdownGracefully();
        System.out.println("NettyClient:bye");
    }

    public boolean isConnected(){
        return isConnected;
    }

    public NioSocketChannel getChannel(){
        return this.channel;
    }

}
