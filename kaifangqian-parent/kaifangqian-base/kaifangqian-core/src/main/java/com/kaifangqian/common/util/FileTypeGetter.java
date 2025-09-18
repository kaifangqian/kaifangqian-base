package com.kaifangqian.common.util;



import java.io.File;
import java.io.FileInputStream;

public class FileTypeGetter {


    public static void main(String[] args) throws Exception {

//        File file = new File("E://work//tem/doc/index.html");
//
//        byte [] doc = FileUtils.readFileToByteArray(file);
//
//
//        FileType type = FileTypeJudge.getType(doc);
//
//        System.out.println(type);


    }
    private static String getOfficeFileType(FileInputStream fileInputStream) {
        String officeFileType = "doc";
        byte[] b = new byte[512];
        try {
            fileInputStream.read(b, 0, b.length);
            String fileTypeHex = String.valueOf(bytesToHexString(b));
            String flagString = fileTypeHex.substring(992, fileTypeHex.length());
            if (flagString.toLowerCase().startsWith("eca5c")) {
                officeFileType = "doc";
            } else if (flagString.toLowerCase().startsWith("fdffffff09")) {
                officeFileType = "xls";

            } else if (flagString.toLowerCase().startsWith("09081000000")) {
                officeFileType = "xls";
            } else {
                officeFileType = "ppt";
            }
            return officeFileType;
        } catch (Exception exception) {
            return null;
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder fileTypeHexBuilder = new StringBuilder();
        for (byte b : bytes) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                fileTypeHexBuilder.append(0);
            }
            fileTypeHexBuilder.append(hv);
        }
        return fileTypeHexBuilder.toString();
    }

}
