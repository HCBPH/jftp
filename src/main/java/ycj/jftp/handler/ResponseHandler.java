package ycj.jftp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ycj.jftp.common.ResponseParam;
import ycj.jftp.pojo.Response;

/**
 * @author 53059
 * @date 2021/12/10 17:00
 */
public class ResponseHandler extends SimpleChannelInboundHandler<Response> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        int code = msg.getCode();
        String reason = msg.getReason();
        switch (code){
            case ResponseParam.INFO:
                System.out.println("server:"+reason);
                break;
            case ResponseParam.SUCCESSFUL:
                System.out.println("server:"+reason);
                break;
            case ResponseParam.FAILED:
                System.out.println("server:"+reason);
                break;
            default:
//                System.out.println("server:"+"出错");
                break;
        }
    }
}
