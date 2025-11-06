//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.ofdrw.gm.ses.v4;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class SESeal extends ASN1Object {
    private SES_SealInfo eSealInfo;
    private ASN1OctetString cert;
    private ASN1ObjectIdentifier signAlgID;
    private ASN1BitString signedValue;

    public SESeal() {
    }

    public SESeal(SES_SealInfo eSealInfo, ASN1OctetString cert, ASN1ObjectIdentifier signAlgID, ASN1BitString signedValue) {
        this.eSealInfo = eSealInfo;
        this.cert = cert;
        this.signAlgID = signAlgID;
        this.signedValue = signedValue;
    }

    public SESeal(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        this.eSealInfo = SES_SealInfo.getInstance(e.nextElement());
        this.cert = ASN1OctetString.getInstance(e.nextElement());
        this.signAlgID = ASN1ObjectIdentifier.getInstance(e.nextElement());
        this.signedValue = DERBitString.getInstance(e.nextElement());
    }

    public static SESeal getInstance(Object o) {
        if (o instanceof SESeal) {
            return (SESeal)o;
        } else if (o instanceof byte[]) {
            ASN1InputStream aIn = new ASN1InputStream((byte[])((byte[])o));

            try {
                ASN1Primitive obj = aIn.readObject();
                return new SESeal(ASN1Sequence.getInstance(obj));
            } catch (IOException var3) {
                throw new IllegalArgumentException("电子印章数据v4 无法解析", var3);
            }
        } else {
            return o != null ? new SESeal(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public SES_SealInfo geteSealInfo() {
        return this.eSealInfo;
    }

    public SESeal seteSealInfo(SES_SealInfo eSealInfo) {
        this.eSealInfo = eSealInfo;
        return this;
    }

    public ASN1OctetString getCert() {
        return this.cert;
    }

    public SESeal setCert(ASN1OctetString cert) {
        this.cert = cert;
        return this;
    }

    public SESeal setCert(Certificate cert) throws CertificateEncodingException {
        this.cert = new DEROctetString(cert.getEncoded());
        return this;
    }

    public ASN1ObjectIdentifier getSignAlgID() {
        return this.signAlgID;
    }

    public SESeal setSignAlgID(ASN1ObjectIdentifier signAlgID) {
        this.signAlgID = signAlgID;
        return this;
    }

    public ASN1BitString getSignedValue() {
        return this.signedValue;
    }

    public SESeal setSignedValue(ASN1BitString signedValue) {
        this.signedValue = signedValue;
        return this;
    }

    public SESeal setSignedValue(byte[] signedValue) {
        this.signedValue = new DERBitString(signedValue);
        return this;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(4);
        v.add(this.eSealInfo);
        v.add(this.cert);
        v.add(this.signAlgID);
        v.add(this.signedValue);
        return new DERSequence(v);
    }
}
