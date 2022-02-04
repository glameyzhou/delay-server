package org.glamey.infra.delayserver.server;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.glamey.infra.delayserver.util.Jsons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;

public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(NettyHttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        //        logger.info("request info, {}", Jsons.toJson(fullHttpRequest));
        FullHttpResponse response;
        if (fullHttpRequest.getMethod() == HttpMethod.GET) {
            Map<String, ?> getData = extractFromGet(fullHttpRequest);
            ByteBuf content = Unpooled.wrappedBuffer(Jsons.toJson(getData).getBytes(CharsetUtil.UTF_8));
            response = wrapResponse(OK, content);
        } else if (fullHttpRequest.getMethod() == HttpMethod.POST) {
            String json = extractFromPost(fullHttpRequest);
            response = wrapResponse(OK, Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8)));
        } else {
            response = wrapResponse(INTERNAL_SERVER_ERROR, null);
        }
        channelHandlerContext.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        //        channelHandlerContext.writeAndFlush(response);
        logger.info("response is success");
    }

    private String extractFromPost(FullHttpRequest fullHttpRequest) {
        if (fullHttpRequest.getMethod() != HttpMethod.POST) {
            return null;
        }
        String contentType = fullHttpRequest.headers().get(Names.CONTENT_TYPE).trim();
        if (contentType.contains(Values.APPLICATION_X_WWW_FORM_URLENCODED)) {
            return extractFromFormPost(fullHttpRequest);
        }
        if (contentType.contains(Values.APPLICATION_JSON)) {
            return extractFromJsonPost(fullHttpRequest);
        }
        return null;
    }

    private String extractFromJsonPost(FullHttpRequest fullHttpRequest) {
        ByteBuf content = fullHttpRequest.content();
        byte[] buf = new byte[content.readableBytes()];
        content.readBytes(buf);
        return new String(buf, CharsetUtil.UTF_8);
    }

    private String extractFromFormPost(FullHttpRequest fullHttpRequest) {
        Map<String, String> map = Maps.newHashMap();
        HttpPostRequestDecoder decoder =
                new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                map.put(attribute.getName(), attribute.getValue());
            }
        }
        return Jsons.toJson(map);
    }

    private Map<String, ?> extractFromGet(FullHttpRequest fullHttpRequest) {
        if (fullHttpRequest.getMethod() != HttpMethod.GET) {
            return null;
        }
        QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.getUri());
        return decoder.parameters();
    }

    private FullHttpResponse wrapResponse(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (content != null) {
            response.headers().add(Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
            //            response.headers().set(Names.CONTENT_LENGTH, response.content().readableBytes());
            //            response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
        }
        return response;
    }
}
