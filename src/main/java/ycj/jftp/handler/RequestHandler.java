package ycj.jftp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ycj.jftp.pojo.Request;

/**
 * @author 53059
 * @date 2021/12/10 17:00
 */
public class RequestHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {

    }
}
