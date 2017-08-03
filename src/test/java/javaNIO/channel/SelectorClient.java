package javaNIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuch on 2017/8/2.
 */
public class SelectorClient {
    //private static class Client extends Thread{
    //    private String name;
    //    private Random random = new Random(20);
    //
    //    public Client(String name){
    //        this.name = name;
    //    }
    //
    //    @Override
    //    public void run() {
    //        try {
    //            System.out.println("run");
    //            SocketChannel socketChannel = SocketChannel.open();
    //            socketChannel.configureBlocking(false);
    //            socketChannel.connect(new InetSocketAddress(1234));
    //            while (!socketChannel.finishConnect()){
    //                System.out.println("finishConnect");
    //                TimeUnit.MILLISECONDS.sleep(100);
    //            }
    //            for (int i = 0; i < 5; i++) {
    //                TimeUnit.MILLISECONDS.sleep(100*random.nextInt(20));
    //                String str = "Message from " + name + ", number:" + i;
    //                ByteBuffer byteBuffer = ByteBuffer.allocate(str.getBytes().length);
    //                System.out.println(str);
    //                byteBuffer.put(str.getBytes());
    //                byteBuffer.flip();
    //                byte[] bytes = new byte[byteBuffer.remaining()];
    //                while (byteBuffer.hasRemaining()){
    //                    socketChannel.write(byteBuffer);
    //                }
    //                byteBuffer.clear();
    //            }
    //            socketChannel.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}

    public static void test1(String name){
        Random random = new Random(20);
        try {
            System.out.println("run");
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(1234));
            while (!socketChannel.finishConnect()){
                System.out.println("finishConnect");
                TimeUnit.MILLISECONDS.sleep(100);
            }
            for (int i = 0; i < 5; i++) {
                TimeUnit.MILLISECONDS.sleep(100*random.nextInt(20));
                String str = "Message from " + name + ", number:" + i;
                ByteBuffer byteBuffer = ByteBuffer.allocate(str.getBytes().length);
                System.out.println(str);
                byteBuffer.put(str.getBytes());
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                while (byteBuffer.hasRemaining()){
                    socketChannel.write(byteBuffer);
                }
                byteBuffer.clear();
            }
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //ExecutorService executorService = Executors.newCachedThreadPool();
        //new Client("client-1").run();
        //executorService.submit(new Client("client-1"));
        //executorService.submit(new Client("client-2"));
        //executorService.submit(new Client("client-3"));
        //executorService.shutdown();
        test1("aa");
    }
}
