package javaNIO;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

/**
 * Created by yuch on 2017/8/1.
 * NIO中Buffer的相关操作
 */
public class BufferTest {

    @Test
    public void test1(){
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("abcd");
        showBuffer(buffer);
        buffer.flip();
        showBuffer(buffer);
        buffer.get();
        buffer.get();
        showBuffer(buffer);
        buffer.flip();
        showBuffer(buffer);
        buffer.clear();
        showBuffer(buffer);
        char c = buffer.get();
        System.out.println(c);
        showBuffer(buffer);
        buffer.clear();
        buffer.position(4);
        showBuffer(buffer);
    }

    @Test
    public void test2(){
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put("abcd");
        charBuffer.flip();
        char[] chars = new char[charBuffer.remaining()];
        charBuffer.get(chars);
        System.out.println(chars);
    }

    @Test
    public void test3(){
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put("abcd");
        charBuffer.get();
        showBuffer(charBuffer);
        charBuffer.put("efg");
        showBuffer(charBuffer);
        charBuffer.flip();
        showBuffer(charBuffer);
        System.out.println(charBuffer.hasRemaining());
        charBuffer.put("cc");
        showBuffer(charBuffer);
        System.out.println(charBuffer.hasRemaining());
        char[] chars = new char[charBuffer.remaining()];
        charBuffer.get(chars);
        System.out.println(chars);
    }

    @Test
    public void test4(){
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put("abcde");
        charBuffer.flip();
        charBuffer.get();
        charBuffer.get();
        showBuffer(charBuffer);
        //压缩
        charBuffer.compact();
        showBuffer(charBuffer);

        //继续写入
        charBuffer.put("fghi");
        //charBuffer.flip();
        showBuffer(charBuffer);
        //从头读取后续的字符
        char[] chars = new char[charBuffer.remaining()];
        charBuffer.get(chars);
        System.out.println(chars);
    }

    @Test
    public void testElementView() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        //存入四个字节,0x00000042
        buffer.put((byte) 0x00).put((byte)0x00).put((byte) 0x00).put((byte) 0x42);
        buffer.position(0);
        //转换为IntBuffer，并取出一个int（四个字节）
        IntBuffer intBuffer =buffer.asIntBuffer();
        int i =intBuffer.get();
        System.out.println(i);
        System.out.println(Integer.toHexString(i));
    }

    @Test
    public void testPutAndGetElement() {
        ByteBuffer buffer =ByteBuffer.allocate(12);
        //直接存入一个int
        buffer.putInt(0x1234abcd);
        //以byte分别取出
        buffer.position(0);
        byte b1 = buffer.get();
        byte b2 = buffer.get();
        byte b3 = buffer.get();
        byte b4 = buffer.get();
        System.out.println(Integer.toHexString(b1&0xff));
        System.out.println(Integer.toHexString(b2&0xff));
        System.out.println(Integer.toHexString(b3&0xff));
        System.out.println(Integer.toHexString(b4&0xff));
    }

    /**
     * 显示buffer的position、limit、capacity和buffer中包含的字符，若字符为0，则替换为'.'
     *@param buffer
     */
    private void showBuffer(CharBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < buffer.limit(); i++) {
            char c = buffer.get(i);
            if (c == 0) {
                c = '.';
            }
            sb.append(c);
        }
        System.out.printf("position=%d, limit=%d, capacity=%d,content=%s\n",
                buffer.position(),buffer.limit(),buffer.capacity(),sb.toString());
    }

}
