import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class BigFileGenerator {
    public static void main(String[] args) {
        // Change these values to generate different file sizes
        String fileName = "big_file.bin";
        long fileSizeInMB = 1000; // Beware !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        long fileSizeInBytes = fileSizeInMB * 1024 * 1024;

        byte[] buffer = new byte[8192]; // 8KB buffer
        Random random = new Random();

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            long bytesWritten = 0;
            while (bytesWritten < fileSizeInBytes) {
                random.nextBytes(buffer);
                long bytesToWrite = Math.min(buffer.length, fileSizeInBytes - bytesWritten);
                fos.write(buffer, 0, (int) bytesToWrite);
                bytesWritten += bytesToWrite;
            }
            System.out.println("File generated: " + fileName + " (" + fileSizeInMB + "MB)");
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
