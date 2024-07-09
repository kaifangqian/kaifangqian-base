package org.resrun.sdk.utils;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CertificateSort {

    /**
     * 根据顺序将证书链重新排序
     * @param chains
     * @return
     */
    public static List<X509Certificate> sort(List<X509Certificate> chains){


        List<X509Certificate> sortChains = new ArrayList<>(chains.size());

        X509Certificate rootCert = chains.stream().filter(c->c.getIssuerDN().getName().equals(c.getSubjectDN().getName())).findFirst().get();

        String issuer = rootCert.getSubjectDN().getName();

        sortChains.add(rootCert);

        for (int i=0;i<sortChains.size();i++){
            for (X509Certificate certificate : chains) {
                //排除根证书 签发 = 使用者
                if(!certificate.getIssuerDN().getName().equals(certificate.getSubjectDN().getName())
                        && certificate.getIssuerDN().getName().equals(issuer)){
                    sortChains.add(certificate);
                    issuer = certificate.getSubjectDN().getName();
                }
            }
        }
        Collections.reverse(sortChains);
        return sortChains;
    }
}
