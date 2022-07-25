package ycj.jftp.initial;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import ycj.jftp.handler.*;

/**
 * @author 53059
 * @date 2021/12/10 14:33
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new Decoder())
                .addLast(new RequestEncoder())
                .addLast(new CommandEncoder())
                .addLast(new ResponseHandler());
    }
}
