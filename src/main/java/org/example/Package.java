package org.example;

public class Package {
    private byte[] content;
    private int CRC;

    public Package(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public int getCRC() {
        return CRC;
    }
}
