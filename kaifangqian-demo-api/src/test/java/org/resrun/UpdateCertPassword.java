package org.resrun;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.KeyStore;
import java.security.Security;

public class UpdateCertPassword {

    public static void main(String[] args) throws Exception {

        byte [] pfx = FileUtils.readFileToByteArray(new File("C://example.pfx"));

        String oldPwd = "oldPwd";
        String newPwd = "newPwd";
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //加载证书
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pfx);
        KeyStore outputKeyStore = KeyStore.getInstance("PKCS12","BC");
        outputKeyStore.load(inputStream,oldPwd.toCharArray());

        //另存为证书
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputKeyStore.store(out, newPwd.toCharArray());

        byte [] newPfx = out.toByteArray();
        FileUtils.writeByteArrayToFile(new File("C://new.pfx"),newPfx);
    }

}
