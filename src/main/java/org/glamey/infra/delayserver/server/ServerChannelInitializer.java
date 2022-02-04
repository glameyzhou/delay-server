package org.glamey.infra.delayserver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        /*pipeline.addLast(new HttpClientCodec()); //包含了requestDecoder responseEncoder
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new HttpResponseDecoder());
                pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new NettyHttpServerHandler());*/


        /*socketChannel.pipeline().addLast(new HttpRequestDecoder());
        socketChannel.pipeline().addLast(new HttpResponseDecoder());
//        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
        socketChannel.pipeline().addLast(new NettyHttpServerHandler());*/

        socketChannel.pipeline().addLast(new HttpServerCodec());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new NettyHttpServerHandler());
    }
}
