package org.resrun.sdk.utils;

import java.io.*;

/**
 * @Description: IOUtils
 * @Package: org.resrun.utils
 * @ClassName: IOUtils
 * @author: FengLai_Gong
 */
public class IOUtils {

    public IOUtils() {
    }

    public static void streamSaveAsFile(InputStream is, File outfile) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(outfile);
            byte[] buffer = new byte[1024];

            int len;
            while((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception var13) {
            var13.printStackTrace();
            throw new RuntimeException(var13);
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception var12) {
                var12.printStackTrace();
                throw new RuntimeException(var12);
            }
        }

    }

    public static String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];

        int n;
        while((n = in.read(b)) != -1) {
            out.append(new String(b, 0, n));
        }

        return out.toString();
    }

//    public static byte[] stream2Byte(InputStream is) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int len = false;
//        byte[] b = new byte[1024];
//
//        int len;
//        while((len = is.read(b, 0, b.length)) != -1) {
//            baos.write(b, 0, len);
//        }
//
//        byte[] buffer = baos.toByteArray();
//        return buffer;
//    }

    public static InputStream byte2InputStream(byte[] b) throws Exception {
        InputStream is = new ByteArrayInputStream(b);
        return is;
    }

    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];

        for(int i = 0; i < b.length; ++i) {
            b[i] = (new Integer(temp & 255)).byteValue();
            temp >>= 8;
        }

        return b;
    }

//    public static short byteToShort(byte[] b) {
//        short s = false;
//        short s0 = (short)(b[0] & 255);
//        short s1 = (short)(b[1] & 255);
//        s1 = (short)(s1 << 8);
//        short s = (short)(s0 | s1);
//        return s;
//    }

    public static byte[] intToByte(int i) {
        byte[] bt = new byte[]{(byte)(255 & i), (byte)(('\uff00' & i) >> 8), (byte)((16711680 & i) >> 16), (byte)((-16777216 & i) >> 24)};
        return bt;
    }

    public static int bytesToInt(byte[] bytes) {
        int num = bytes[0] & 255;
        num |= bytes[1] << 8 & '\uff00';
        num |= bytes[2] << 16 & 16711680;
        num |= bytes[3] << 24 & -16777216;
        return num;
    }

    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];

        for(int i = 0; i < b.length; ++i) {
            b[i] = (new Long(temp & 255L)).byteValue();
            temp >>= 8;
        }

        return b;
    }

    public static long byteToLong(byte[] b) {
        long s = 0L;
        long s0 = (long)(b[0] & 255);
        long s1 = (long)(b[1] & 255);
        long s2 = (long)(b[2] & 255);
        long s3 = (long)(b[3] & 255);
        long s4 = (long)(b[4] & 255);
        long s5 = (long)(b[5] & 255);
        long s6 = (long)(b[6] & 255);
        long s7 = (long)(b[7] & 255);
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 32;
        s5 <<= 40;
        s6 <<= 48;
        s7 <<= 56;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    public static byte[] toByteArray(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            boolean var5 = false;

            int len;
            while(-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }

            byte[] var6 = bos.toByteArray();
            return var6;
        } catch (IOException var15) {
            var15.printStackTrace();
            throw var15;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

            bos.close();
        }
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }

        return flag;
    }

    public static String getFileSuffix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    public static void byte2File(byte[] buf, String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }

            file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

    }
}