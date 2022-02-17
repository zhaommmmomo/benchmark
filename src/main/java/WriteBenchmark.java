import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author zmm
 * @date 2022/2/16 18:20
 */
public class WriteBenchmark {

    static int fileSize = 1024 * 1024 * 1024;
    static int[] lenArr = {512, 1024, 2 * 1024, 4 * 1024, 8 * 1024, 16 * 1024,
            32 * 1024, 64 * 1024, 1024 * 1024, 16 * 1024 * 1024,
            32 * 1024 * 1024, 64 * 1024 * 1024, 128 * 1024 * 1024};

    public static void main(String[] args) throws Exception {
        for (int len : lenArr) {
            System.out.println("==========" + ((float)len / 1024) + "kb========");
            // FileChannel write
            File fFile = new File("f" + len);
            FileChannel fc = new RandomAccessFile(fFile, "rw").getChannel();
            byte[] fBuf = new byte[len];
            Arrays.fill(fBuf, (byte) 1);
            int fL = 0;
            long fStart = System.currentTimeMillis();
            ByteBuffer buf;
            while (fL < fileSize) {
                buf = ByteBuffer.wrap(fBuf);
                fL += fBuf.length;
                fc.write(buf);
            }
            System.out.println("FileChannel write cost:" + (System.currentTimeMillis() - fStart));

            // MMap write
            MappedByteBuffer mb = new RandomAccessFile(new File("m" + len), "rw")
                    .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            byte[] mBuf = new byte[len];
            Arrays.fill(mBuf, (byte) 1);
            int mL = 0;
            long mStart = System.currentTimeMillis();
            while (mL < mb.capacity()) {
                mL += mBuf.length;
                mb.put(mBuf);
            }
            System.out.println("MMap write cost:" + (System.currentTimeMillis() - mStart));
        }
    }
}
