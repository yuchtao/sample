package javaNIO.channel;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuch on 2017/8/2.
 */
public class ChannelExample {
    String path = "D:\\舆情测试临时文件\\weibo\\";

    /**
     * 测试Channel
     */
    @Test
    public void test1() {
        ReadableByteChannel readableByteChannel = Channels.newChannel(System.in);
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            while (readableByteChannel.read(buffer) != -1) {
                buffer.flip();
                System.err.println(buffer.position());
                while (buffer.hasRemaining()) {
                    writableByteChannel.write(buffer);
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当创建了一个文件通道后，文件通道和文件流对象（FileInputStream、FileOutputStream和RandomAccessFile）共享此文件的position。文件流对象和文件通道的大部分读写操作（直接位置的读写操作不会造成position的位移）均会造成position的自动位移，这个位移对于两类对象来说是共享的，代码例子如下：
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        String pathTmp = path + "test.txt";
        FileOutputStream fileOutputStream = new FileOutputStream(pathTmp);
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) {
            sb.append(c);
        }
        fileOutputStream.write(sb.toString().getBytes());
        fileOutputStream.close();

        RandomAccessFile randomAccessFile = new RandomAccessFile(pathTmp, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        System.out.println("file position in FileChannel is :" + channel.position());
        System.out.println("file position in RandomAccessFile is :" + randomAccessFile.getFilePointer());
        System.err.println("==");
        randomAccessFile.seek(5);
        System.out.println("file position in FileChannel is :" + channel.position());
        System.out.println("file position in RandomAccessFile is :" + randomAccessFile.getFilePointer());
        System.err.println("==");
        channel.position(10);
        System.out.println("file position in FileChannel is :" + channel.position());
        System.out.println("file position in RandomAccessFile is :" + randomAccessFile.getFilePointer());
    }

    /**
     * 文件复制
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        String pathTmp = path + "test.txt";
        RandomAccessFile file1 = new RandomAccessFile(pathTmp, "r");
        RandomAccessFile file2 = new RandomAccessFile(path + "test1.txt", "rw");
        FileChannel channel = file1.getChannel();
        FileChannel channe2 = file2.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        while (channel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            if (byteBuffer.hasRemaining()) {
                channe2.write(byteBuffer);
            }
            byteBuffer.clear();
        }
        channel.close();
        channe2.close();
    }

    /**
     * ServerSocketChannel 阻塞服务端
     */
    @Test
    public void serverSocketTest() {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(1234));
            SocketChannel sc = ssc.accept();
            System.out.println("accept connection from:" + sc.getRemoteAddress());
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (sc.read(buffer) != -1) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                System.out.println(new String(bytes));
                buffer.clear();
            }
            sc.close();
            ssc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ServerSocketChannel 阻塞 客户端
     */
    @Test
    public void clientSocketTest() {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1", 1234));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            writeString(buffer, sc,"hello");
            writeString(buffer, sc,"world");
            writeString(buffer, sc,"exit");
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeString(ByteBuffer buffer, SocketChannel sc,String str) {
        buffer.clear();
        buffer.put(str.getBytes()).flip();
        try {
            sc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ServerSocketChannel  非阻塞 服务端
     */
    @Test
    public void ServerSocket1() {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open( );
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(1234));
            SocketChannel sc = null;
            while ((sc = ssc.accept()) == null) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("try to accept again...");
            }
            System.out.println("accept connection from:" + sc.getRemoteAddress());

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (sc.read(buffer) != -1) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                System.out.println(new String (bytes));
                buffer.clear();
            }
            sc.close();
            ssc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ServerSocketChannel  非阻塞 服务端
     */@Test
    public void clientSocketTest1(){
        try {
            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(new InetSocketAddress("127.0.0.1", 1234));
            while (!sc.finishConnect()) {
                System.out.println("connection has not finished,wait...");
                TimeUnit.SECONDS.sleep(1);
            }
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            writeString(buffer, sc,"hello");
            writeString(buffer, sc,"world");
            writeString(buffer, sc,"exit");
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
