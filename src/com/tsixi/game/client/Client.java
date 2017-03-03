package com.tsixi.game.client;

import com.tsixi.game.client.handler.ClientHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    public static void main(String args[]) throws  Exception {

        // Client服务启动器
        ClientBootstrap client = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // 设置一个处理服务端消息和各种消息事件的类(Handler)
        client.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {

                // Create a default pipeline implementation.
                ChannelPipeline pipeline = Channels.pipeline();

                // Add the text line codec combination first,
                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                // and then business logic.
                pipeline.addLast("handler", new ClientHandler());

                return pipeline;
            }
        });

        // 连接到本地的8000端口的服务端
        ChannelFuture future = client.connect(new InetSocketAddress("127.0.0.1", 8000));
        // Wait until the connection attempt succeeds or fails.
        Channel channel = future.sync().getChannel();

        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String line = in.readLine();
            if (line == null) {
                break;
            }

            // Sends the received line to the server.
            lastWriteFuture = channel.write(line + "\r\n");

            // If user typed the 'bye' command, wait until the server closes
            // the connection.
            if ("bye".equals(line.toLowerCase())) {
                channel.getCloseFuture().sync();
                break;
            }
        }

        // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null) {
            lastWriteFuture.sync();
        }
    }

}
