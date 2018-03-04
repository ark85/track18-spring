package ru.track.io;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.track.io.vendor.Bootstrapper;
import ru.track.io.vendor.FileEncoder;
import ru.track.io.vendor.ReferenceTaskImplementation;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public final class TaskImplementation implements FileEncoder {

    /**
     * @param finPath  where to read binary data from
     * @param foutPath where to write encoded data. if null, please create and use temporary file.
     * @return file to read encoded data from
     * @throws IOException is case of input/output errors
     */

    static final String ZERO_BYTE = "00000000";

    @NotNull
    public File encodeFile(@NotNull String finPath, @Nullable String foutPath) throws IOException {
        /* XXX: https://docs.oracle.com/javase/8/docs/api/java/io/File.html#deleteOnExit-- */
//        throw new UnsupportedOperationException(); // TODO: implement
        final File fin = new File(finPath);
        final File fout;

        if (foutPath != null) {
            fout = new File(foutPath);
        } else {
            fout = File.createTempFile("based_file_", ".txt");
            fout.deleteOnExit();
        }

        final InputStream is = new FileInputStream(fin);
        byte[] picBytes = IOUtils.toByteArray(is);

        List<String> result = new ArrayList<>();

        int rem = picBytes.length % 3;
        for (int i = 0; i < picBytes.length - rem; i += 3) {
            String[] threeBytes = {byteToString(picBytes[i]), byteToString(picBytes[i + 1]), byteToString(picBytes[i + 2])};
            result.add(base64ForByteStringList(threeBytes, 3));
        }

        if (rem == 1 && picBytes.length > 1) {
            String[] threeBytes = {
                    byteToString(picBytes[picBytes.length - 2]),
                    byteToString(picBytes[picBytes.length - 1]),
                    ZERO_BYTE};
            result.add(base64ForByteStringList(threeBytes, 2) + "=");
        } else if (rem == 2 || (rem == 1 && picBytes.length < 2)) {
            String[] threeBytes = {
                    byteToString(picBytes[picBytes.length - 1]),
                    ZERO_BYTE,
                    ZERO_BYTE};
            if (rem == 1 && picBytes.length < 2) {
                result.add(base64ForByteStringList(threeBytes, 2) + "=");
            } else {
                result.add(base64ForByteStringList(threeBytes, 1) + "==");
            }
        }

        FileWriter fw = new FileWriter(fout);
        for (String s: result) {
            fw.write(s);
        }

        fw.close();

        return fout;
    }


    private String byteToString(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    public String base64ForByteStringList(String[] bytes, int lenBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String b: bytes) {
            stringBuilder.append(b);
        }
        String res = "";
        if (lenBytes == 1) {
            int fir = Integer.parseInt(stringBuilder.substring(0, 6), 2);
            int sec = Integer.parseInt(stringBuilder.substring(6, 12), 2);
            res = "" + toBase64[fir] + toBase64[sec];
        } else if (lenBytes == 2) {
            int fir = Integer.parseInt(stringBuilder.substring(0, 6), 2);
            int sec = Integer.parseInt(stringBuilder.substring(6, 12), 2);
            int thi = Integer.parseInt(stringBuilder.substring(12, 18), 2);
            res = "" + toBase64[fir] + toBase64[sec] + toBase64[thi];
        } else {
            int fir = Integer.parseInt(stringBuilder.substring(0, 6), 2);
            int sec = Integer.parseInt(stringBuilder.substring(6, 12), 2);
            int thi = Integer.parseInt(stringBuilder.substring(12, 18), 2);
            int fou = Integer.parseInt(stringBuilder.substring(18, 24), 2);
            res = "" + toBase64[fir] + toBase64[sec] + toBase64[thi] + toBase64[fou];
        }

        return res;
    }

    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    public static void main(String[] args) throws Exception {
        final FileEncoder encoder = new TaskImplementation();
        // NOTE: open http://localhost:9000/ in your web browser
        (new Bootstrapper(args, encoder))
                .bootstrap("", new InetSocketAddress("127.0.0.1", 9000));
    }

}
