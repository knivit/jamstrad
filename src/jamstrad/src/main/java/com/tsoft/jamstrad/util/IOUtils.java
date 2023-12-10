package com.tsoft.jamstrad.util;


import java.io.*;

public class IOUtils {
    private IOUtils() {
    }

    public static CharSequence readTextFileContents(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder(2048);
        String line = null;

        while((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        reader.close();
        return sb;
    }

    public static void writeTextFileContents(File file, CharSequence text) throws IOException {
        PrintWriter pw = new PrintWriter(file);
        pw.print(text);
        pw.flush();
        pw.close();
    }

    public static byte[] readBinaryFileContents(File file) throws IOException {
        byte[] data = new byte[(int)file.length()];
        byte[] buffer = new byte[2048];
        int dataIndex = 0;
        FileInputStream in = new FileInputStream(file);

        for(int bytesRead = in.read(buffer); bytesRead >= 0; bytesRead = in.read(buffer)) {
            System.arraycopy(buffer, 0, data, dataIndex, bytesRead);
            dataIndex += bytesRead;
        }

        in.close();
        return data;
    }

    public static void writeBinaryFileContents(File file, byte[] data) throws IOException {
        writeBinaryFileContents(file, data, 0, data.length);
    }

    public static void writeBinaryFileContents(File file, byte[] data, int offset, int length) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(data, offset, length);
        out.flush();
        out.close();
    }

    public static String getFileExtensionWithDot(File file) {
        String name = file.getName();
        int i = name.lastIndexOf(46);
        return i > 0 ? name.substring(i) : "";
    }

    public static File stripExtension(File file) {
        String name = file.getName();
        int i = name.lastIndexOf(46);
        return i > 0 ? new File(file.getParentFile(), name.substring(0, i)) : file;
    }

    public static boolean isFileInsideFolder(File file, File folder) {
        File absFile = null;
        File absFolder = null;

        try {
            absFile = file.getCanonicalFile();
            absFolder = folder.getCanonicalFile();
        } catch (IOException var5) {
            absFile = file.getAbsoluteFile();
            absFolder = folder.getAbsoluteFile();
        }

        File current;
        for(current = absFile; current != null && !current.equals(absFolder); current = current.getParentFile()) {
        }

        return current != null;
    }
}
