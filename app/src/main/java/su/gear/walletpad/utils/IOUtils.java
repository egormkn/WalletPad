package su.gear.walletpad.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

public final class IOUtils {

    public static String readToString(InputStream in, String charset) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = getIOBuffer();
        int readSize;

        while ((readSize = in.read(buffer)) >= 0) {
            baos.write(buffer, 0, readSize);
        }
        final byte[] data = baos.toByteArray();
        final String content = new String(data, charset);
        return content;
    }

    public static void readFully(InputStream in) throws IOException {
        byte[] buffer = getIOBuffer();
        while (in.read(buffer) >= 0) ;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
        } // ignore
    }

    public static void readAndCloseSilently(InputStream in) {
        if (in == null) {
            return;
        }
        try {
            readFully(in);
        } catch (IOException e) {
        } // ignore
        closeSilently(in);
    }

    public static boolean isConnectionAvailable(@NonNull Context context, boolean defaultValue) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return defaultValue;
        }
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public static byte[] getIOBuffer() {
        byte[] buffer = bufferThreadLocal.get();
        if (buffer == null) {
            buffer = new byte[8192];
            bufferThreadLocal.set(buffer);
        }
        return buffer;
    }

    private static final ThreadLocal<byte[]> bufferThreadLocal = new ThreadLocal<>();

    public static String join(Collection<String> s, String delimiter) {
        if (s == null || s.isEmpty()) return "";
        Iterator<String> iter = s.iterator();
        StringBuilder builder = new StringBuilder(iter.next());
        while (iter.hasNext()) {
            builder.append(delimiter).append(iter.next());
        }
        return builder.toString();
    }

    private IOUtils() {
    }
}
