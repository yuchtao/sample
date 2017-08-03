package javaNIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by yuch on 2017/8/2.
 */
public class SelectorServer {
    private static final int PORT = 1234;
    private static ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(PORT));
        ssc.configureBlocking(false);

        Selector selector= Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("selector.keys().size() "+selector.keys().size());

        while (true){
            int select = selector.select();
            System.out.println(select);
            if (select == 0) {
                System.out.println("还没连接");
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                if (next.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)next.channel();
                    SocketChannel accept = channel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                    System.out.println("selector.keys().size() "+selector.keys().size());
                }
                if (next.isReadable()){
                    SocketChannel channel = (SocketChannel)next.channel();
                    readFromChannel(channel);
                }
                iterator.remove();
            }
        }
    }

    private static void readFromChannel(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //while (socketChannel.read(byteBuffer) != -1){
        //    byteBuffer.flip();
        //    byte[] bytes = new byte[byteBuffer.remaining()];
        //    byteBuffer.get(bytes);
        //    System.out.println("收到的消息是："+new String(bytes));
        //    byteBuffer.clear();
        //}

        int count ;
        buffer.clear();
        try {
            while ((count = channel.read(buffer)) > 0) {
                System.out.println("count "+count);
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                System.out.println("READ FROM CLIENT:" + new String(bytes));
            }
            if (count < 0) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
