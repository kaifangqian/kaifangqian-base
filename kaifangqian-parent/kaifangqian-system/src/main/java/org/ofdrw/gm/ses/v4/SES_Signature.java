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
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public class SES_Signature extends ASN1Object {
    private TBS_Sign toSign;
    private ASN1OctetString cert;
    private ASN1ObjectIdentifier signatureAlgID;
    private ASN1BitString signature;
    private ASN1BitString timeStamp;

    public SES_Signature() {
    }

    public SES_Signature(TBS_Sign toSign, ASN1OctetString cert, ASN1ObjectIdentifier signatureAlgID, ASN1BitString signature, ASN1BitString timeStamp) {
        this.toSign = toSign;
        this.cert = cert;
        this.signatureAlgID = signatureAlgID;
        this.signature = signature;
        this.timeStamp = timeStamp;
    }

    public SES_Signature(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        this.toSign = TBS_Sign.getInstance(e.nextElement());
        this.cert = ASN1OctetString.getInstance(e.nextElement());
        this.signatureAlgID = ASN1ObjectIdentifier.getInstance(e.nextElement());
        this.signature = DERBitString.getInstance(e.nextElement());
        if (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof ASN1TaggedObject) {
                this.timeStamp = DERBitString.getInstance(((ASN1TaggedObject) obj).getBaseObject());
            }
        }

    }

    public static SES_Signature getInstance(Object o) {
        if (o instanceof SES_Signature) {
            return (SES_Signature)o;
        } else if (o instanceof byte[]) {
            ASN1InputStream aIn = new ASN1InputStream((byte[])((byte[])o));

            try {
                ASN1Primitive obj = aIn.readObject();
                return new SES_Signature(ASN1Sequence.getInstance(obj));
            } catch (IOException var3) {
                throw new IllegalArgumentException("电子签章数据v4 无法解析", var3);
            }
        } else {
            return o != null ? new SES_Signature(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public TBS_Sign getToSign() {
        return this.toSign;
    }

    public SES_Signature setToSign(TBS_Sign toSign) {
        this.toSign = toSign;
        return this;
    }

    public ASN1OctetString getCert() {
        return this.cert;
    }

    public SES_Signature setCert(ASN1OctetString cert) {
        this.cert = cert;
        return this;
    }

    public SES_Signature setCert(Certificate cert) throws CertificateEncodingException {
        this.cert = new DEROctetString(cert.getEncoded());
        return this;
    }

    public ASN1ObjectIdentifier getSignatureAlgID() {
        return this.signatureAlgID;
    }

    public SES_Signature setSignatureAlgID(ASN1ObjectIdentifier signatureAlgID) {
        this.signatureAlgID = signatureAlgID;
        return this;
    }

    public ASN1BitString getSignature() {
        return this.signature;
    }

    public SES_Signature setSignature(ASN1BitString signature) {
        this.signature = signature;
        return this;
    }

    public SES_Signature setSignature(byte[] signature) {
        this.signature = new DERBitString(signature);
        return this;
    }

    public ASN1BitString getTimeStamp() {
        return this.timeStamp;
    }

    public SES_Signature setTimeStamp(ASN1BitString timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(5);
        v.add(this.toSign);
        v.add(this.cert);
        v.add(this.signatureAlgID);
        v.add(this.signature);
        if (this.timeStamp != null) {
            v.add(new DERTaggedObject(true, 0, this.timeStamp));
        }

        return new DERSequence(v);
    }
}
