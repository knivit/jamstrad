package com.tsoft.jamstrad.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MultiplexOutputStream extends OutputStream {
    private List<OutputStream> outputStreams = new Vector();

    public MultiplexOutputStream() {
    }

    public void addOutputStream(OutputStream outputStream) {
        this.getOutputStreams().add(outputStream);
    }

    public void write(int b) throws IOException {
        Iterator var3 = this.getOutputStreams().iterator();

        while(var3.hasNext()) {
            OutputStream os = (OutputStream)var3.next();
            os.write(b);
        }

    }

    public void write(byte[] b) throws IOException {
        Iterator var3 = this.getOutputStreams().iterator();

        while(var3.hasNext()) {
            OutputStream os = (OutputStream)var3.next();
            os.write(b);
        }

    }

    public void write(byte[] b, int off, int len) throws IOException {
        Iterator var5 = this.getOutputStreams().iterator();

        while(var5.hasNext()) {
            OutputStream os = (OutputStream)var5.next();
            os.write(b, off, len);
        }

    }

    public void flush() throws IOException {
        Iterator var2 = this.getOutputStreams().iterator();

        while(var2.hasNext()) {
            OutputStream os = (OutputStream)var2.next();
            os.flush();
        }

    }

    public void close() throws IOException {
        Iterator var2 = this.getOutputStreams().iterator();

        while(var2.hasNext()) {
            OutputStream os = (OutputStream)var2.next();
            os.close();
        }

    }

    private List<OutputStream> getOutputStreams() {
        return this.outputStreams;
    }
}
