package com.lightfight.game.server;

import com.lightfight.game.server.handler.ServerHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 描述：<br>
 *
 * @author dirk
 * @date 2017/3/1/0001
 */
public class Server {

    public static void main(String args[]) {
        // Server服务启动器
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));


        // 设置一个处理客户端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {

                ChannelPipeline pipeline = Channels.pipeline();

                // Add the text line codec combination first,
                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("logger",  new LoggingHandler(InternalLogLevel.DEBUG));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                // and then business logic handler
                pipeline.addLast("handler", new ServerHandler());

                return pipeline;
            }
        });

        // 设置相关参数
        bootstrap.setOption("child.tcpNoDelay", true);
        // 设置相关参数
        bootstrap.setOption("child.keepAlive", true);

        // 开放8000端口供客户端访问。
        bootstrap.bind(new InetSocketAddress(8000));
    }
}
