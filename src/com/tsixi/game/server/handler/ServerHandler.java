package com.tsixi.game.server.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 描述：<br>
 *
 * @author dirk
 * @date 2017/3/1/0001
 */
public class ServerHandler extends SimpleChannelHandler {


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

    }

    /**
     * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
     *
     * @alia OneCoder
     * @author lihzh
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx,ChannelStateEvent e) {
        System.out.println("Hello world, I'm server.");
    }
}
