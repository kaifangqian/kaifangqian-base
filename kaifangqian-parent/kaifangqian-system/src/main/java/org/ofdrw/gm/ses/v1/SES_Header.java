//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.ofdrw.gm.ses.v1;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;

public class SES_Header extends ASN1Object {
    public static final ASN1Integer V4 = new ASN1Integer(4L);
    public static final DERIA5String ID = new DERIA5String("ES");
    private DERIA5String id;
    private ASN1Integer version;
    private DERIA5String vid;

    public SES_Header() {
    }

    public SES_Header(ASN1Integer version, DERIA5String vid) {
        this.id = ID;
        this.version = version;
        this.vid = vid;
    }

    public SES_Header(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        this.id = (DERIA5String)DERIA5String.getInstance(e.nextElement());
        this.version = ASN1Integer.getInstance(e.nextElement());
        this.vid = (DERIA5String)DERIA5String.getInstance(e.nextElement());
    }

    public static SES_Header getInstance(Object o) {
        if (o instanceof SES_Header) {
            return (SES_Header)o;
        } else {
            return o != null ? new SES_Header(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public DERIA5String getId() {
        return this.id;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public SES_Header setVersion(ASN1Integer version) {
        this.version = version;
        return this;
    }

    public SES_Header setVersion(int version) {
        this.version = new ASN1Integer((long)version);
        return this;
    }

    public DERIA5String getVid() {
        return this.vid;
    }

    public SES_Header setVid(DERIA5String vid) {
        this.vid = vid;
        return this;
    }

    public SES_Header setVid(String vid) {
        this.vid = new DERIA5String(vid);
        return this;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(3);
        v.add(ID);
        v.add(this.version);
        v.add(this.vid);
        return new DERSequence(v);
    }
}
