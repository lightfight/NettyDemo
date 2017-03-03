package com.tsixi.game.client;

import com.tsixi.game.client.handler.ClientHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 描述：<br>
 * 模拟客户端
 *
 * @author dirk
 * @date 2017/3/1
 */
public class Client {

    public static void main(String args[]) {

        // Client服务启动器
        ClientBootstrap client = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // 设置一个处理服务端消息和各种消息事件的类(Handler)
        client.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ClientHandler());
            }
        });

        // 连接到本地的8000端口的服务端
        ChannelFuture future = client.connect(new InetSocketAddress("127.0.0.1", 8000));
    }

}
