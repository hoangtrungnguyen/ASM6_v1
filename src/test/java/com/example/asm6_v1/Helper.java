package com.example.asm6_v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Helper {
    public static void copyContent(File a, File b)
            throws Exception
    {

        try (FileInputStream in = new FileInputStream(a); FileOutputStream out = new FileOutputStream(b)) {
            int n;
            // read() function to read the
            // byte of data
            while ((n = in.read()) != -1) {
                // write() function to write
                // the byte of data
                out.write(n);
            }
        }
        // close() function to close the
        // stream
        // close() function to close
        // the stream
        System.out.println("File Copied");
    }
}
