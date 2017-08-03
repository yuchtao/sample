package com.waylau.netty.demo.time.POJO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by yuch on 2017/7/29.
 */
public class TimeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("readableBytes:　"+byteBuf.readableBytes());
        if (byteBuf.readableBytes() < 4){
            System.out.println("readableBytes 进来了");
            return;
        }
        System.out.println("添加了");
        list.add(new UnixTime(byteBuf.readUnsignedInt()));
    }
}
