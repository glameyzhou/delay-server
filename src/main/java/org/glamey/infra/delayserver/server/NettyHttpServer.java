package org.glamey.infra.delayserver.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHttpServer {
    private final Logger logger = LoggerFactory.getLogger(NettyHttpServer.class);
    private ChannelFuture channel;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public static void main(String[] args) {
        NettyHttpServer server = new NettyHttpServer();
        server.start();
    }


    private static class NettyHttpServerHolder {
        private static final NettyHttpServer instance = new NettyHttpServer();
    }

    public static NettyHttpServer getInstance() {
        return NettyHttpServerHolder.instance;
    }

    public NettyHttpServer() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
    }

    public void start() {
        logger.info("Netty Http Server, [start]");
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
//                .childOption(ChannelOption.TCP_NODELAY, true) //是否使用FullHttpRequest, FullHttpResponse
                .childOption(ChannelOption.SO_REUSEADDR, true) //是否允许端口占用
                .childOption(ChannelOption.SO_KEEPALIVE, false); //是否设置长链接

        try {
            channel = bootstrap.bind(9998).sync();
            logger.info("Netty Http Server, [success]");

            //测试的时候，直接关闭
            channel.channel().closeFuture().sync();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        } catch (InterruptedException e) {
            logger.error("Start Netty Http Server Error", e);
        }
    }

    public void stop() {
        logger.info("Netty Http Server, [stop]");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        try {
            channel.channel().closeFuture().sync();
            logger.info("Netty Http Server, [success]");
        } catch (InterruptedException e) {
            logger.error("Stop Netty Http Server Error", e);
        }
    }
}
