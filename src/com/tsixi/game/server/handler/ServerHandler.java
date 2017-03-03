package com.tsixi.game.server.handler;

import org.jboss.netty.channel.*;

import java.net.InetAddress;
import java.util.Date;

/**
 * 描述：<br>
 *
 * @author dirk
 * @date 2017/3/1/0001
 */
public class ServerHandler extends SimpleChannelHandler {


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // Send greeting for a new connection.
        e.getChannel().write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        e.getChannel().write("It is " + new Date() + " now.\r\n");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        // Cast to a String first.
        // We know it is a String because we put some codec in TelnetPipelineFactory.
        String request = (String) e.getMessage();

        System.out.println("received : " + request);

        // Generate and write a response.
        String response;
        boolean close = false;
        if (request.length() == 0) {
            response = "Please type something.\r\n";
        } else if ("bye".equals(request.toLowerCase())) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
            response = "Did you say '" + request + "'?\r\n";
        }

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.
        ChannelFuture future = e.getChannel().write(response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
