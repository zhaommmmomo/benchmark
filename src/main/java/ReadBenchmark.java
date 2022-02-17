import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zmm
 * @date 2022/1/13 18:20
 */
public class ReadBenchmark {

    static int[] lenArr = {512, 1024, 2 * 1024, 4 * 1024, 8 * 1024, 16 * 1024,
            32 * 1024, 64 * 1024, 1024 * 1024, 16 * 1024 * 1024,
            32 * 1024 * 1024, 64 * 1024 * 1024, 128 * 1024 * 1024};

    public static void main(String[] args) throws Exception {
        for (int len : lenArr) {
            System.out.println("==========" + ((float)len / 1024) + "kb========");
            // FileChannel read
            File fFile = new File("f" + 512);
            FileChannel fc = new RandomAccessFile(fFile, "rw").getChannel();
            ByteBuffer bf = ByteBuffer.allocate(len);
            long fStart = System.currentTimeMillis();
            int l;
            do {
                l = fc.read(bf);
                bf.clear();
            } while (l != -1);
            System.out.println("FileChannel read cost:" + (System.currentTimeMillis() - fStart));

            // MMap read
            File mFile = new File("m" + 512);
            MappedByteBuffer mb = new RandomAccessFile(mFile, "rw")
                    .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 1024 * 1024 * 1024);
            long mStart = System.currentTimeMillis();
            byte[] mBuf = new byte[len];
            while (mb.hasRemaining()) {
                mb.get(mBuf);
                mBuf = new byte[len];
            }
            System.out.println("MMap read cost:" + (System.currentTimeMillis() - mStart));
        }
    }
}
