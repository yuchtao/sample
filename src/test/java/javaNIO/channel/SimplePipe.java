package javaNIO.channel;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.WritableByteChannel;

/**
 * Created by yuch on 2017/8/2.
 */
public class SimplePipe {
    /**
     * Pipe管道的概念最先应该出现在Unix系统里，用来表示连接不同进程间的一种单向数据通道，很多Unix系统都提供了支持管道的API。Java NIO借用了这个概念，发明了NIO中的Pipe，它是指同一个Java进程内，不同线程间的一种单向数据管道，其sink端通道写入数据，source端通道则读出数据，其间可以保证数据按照写入顺序到达
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();
        Pipe.SourceChannel source = pipe.source();

        WorkerThread workerThread = new WorkerThread(sink);
        workerThread.start();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (source.read(byteBuffer) != -1){
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            String s = new String(bytes);
            System.out.println(s);
            byteBuffer.clear();
        }
        source.close();
    }

    private static class WorkerThread extends Thread {
        WritableByteChannel channel;

        public WorkerThread(WritableByteChannel writableByteChannel) {
            this.channel = writableByteChannel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            for (int i = 0; i < 10; i++) {
                String str = "pipe sink data " + i;

                buffer.put(str.getBytes());
                buffer.flip();
                try {
                    channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffer.clear();
            }
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
