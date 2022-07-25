package ycj.jftp.initial;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import ycj.jftp.common.CommandParam;
import ycj.jftp.common.ResponseParam;
import ycj.jftp.handler.*;
import ycj.jftp.pojo.Command;
import ycj.jftp.pojo.Response;

/**
 * @author 53059
 * @date 2021/12/10 14:33
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new Decoder())
                .addLast(new StringEncoder())
                .addLast(new ResponseEncoder())
//                .addLast(new Encoder())
//                .addLast(new FileOutFilter())
                .addLast(new RequestHandler())
                .addLast(new CommandHandler());
        System.out.println("NettyServer:"+ch.remoteAddress()+"接入………………");
        Response response = new Response();
        response.setCode(ResponseParam.INFO);
        response.setReason(System.getProperty("os.name"));
        ch.writeAndFlush(response);
    }
}
