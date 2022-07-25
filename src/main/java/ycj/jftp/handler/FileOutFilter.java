package ycj.jftp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.MessageToMessageEncoder;
import ycj.jftp.common.ResponseParam;
import ycj.jftp.pojo.Response;

import java.util.List;

/**
 * @author 53059
 * @date 2021/12/10 17:00
 */
public class FileOutFilter extends MessageToMessageEncoder<DefaultFileRegion> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, DefaultFileRegion defaultFileRegion, List<Object> list) throws Exception {

        System.out.println("到这了");
        Response response = new Response();
        response.setCode(ResponseParam.INFO);
        response.setReason("from fileoutfilter");
        list.add(response);
    }
}
