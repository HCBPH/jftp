package ycj.jftp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ycj.jftp.initial.ServerInitializer;

/**
 * @author 53059
 * @date 2021/12/10 12:33
 */
public class JFTPServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(2);
    private EventLoopGroup workGroup = new NioEventLoopGroup(8);
    private ServerBootstrap bootstrap = new ServerBootstrap();
    private static JFTPServer instance;
    private static int PORT = 6556;

    private JFTPServer(){}

    public static JFTPServer getInstance(){
        if (JFTPServer.instance == null){
            synchronized (JFTPServer.class){
                JFTPServer.instance = new JFTPServer();
            }
        }
        return instance;
    }

    public void run() {
        try{
            this.bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ServerInitializer());

            ChannelFuture future = this.bootstrap.bind(PORT).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        System.out.println("NettyServer:监听端口"+PORT);
                    }
                }
            }).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println("NettyServer:出错\n"+e.getLocalizedMessage());
        }finally {
            this.stop();
        }
    };

    public void stop(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        System.out.println("NettyServer:bye");
    };

}
