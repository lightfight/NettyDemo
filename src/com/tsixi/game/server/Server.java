package com.tsixi.game.server;

import com.tsixi.game.server.handler.ServerHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

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
        ServerBootstrap server = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));


        // 设置一个处理客户端消息和各种消息事件的类(Handler)
        server.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ServerHandler());
            }
        });

        // 开放8000端口供客户端访问。
        server.bind(new InetSocketAddress(8000));
    }
}
