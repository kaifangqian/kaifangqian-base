//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.ofdrw.gm.ses.v1;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class SES_ESPictrueInfo extends ASN1Object {
    private DERIA5String type;
    private ASN1OctetString data;
    private ASN1Integer width;
    private ASN1Integer height;

    public SES_ESPictrueInfo() {
    }

    public SES_ESPictrueInfo(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        this.type = (DERIA5String)DERIA5String.getInstance(e.nextElement());
        this.data = ASN1OctetString.getInstance(e.nextElement());
        this.width = ASN1Integer.getInstance(e.nextElement());
        this.height = ASN1Integer.getInstance(e.nextElement());
    }

    public SES_ESPictrueInfo(DERIA5String type, ASN1OctetString data, ASN1Integer width, ASN1Integer height) {
        this.type = type;
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public static SES_ESPictrueInfo getInstance(Object o) {
        if (o instanceof SES_ESPictrueInfo) {
            return (SES_ESPictrueInfo)o;
        } else {
            return o != null ? new SES_ESPictrueInfo(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public ASN1OctetString getData() {
        return this.data;
    }

    public SES_ESPictrueInfo setData(ASN1OctetString data) {
        this.data = data;
        return this;
    }

    public SES_ESPictrueInfo setData(byte[] data) {
        this.data = new DEROctetString(data);
        return this;
    }

    public ASN1Integer getWidth() {
        return this.width;
    }

    public SES_ESPictrueInfo setWidth(ASN1Integer width) {
        this.width = width;
        return this;
    }

    public SES_ESPictrueInfo setWidth(long width) {
        this.width = new ASN1Integer(width);
        return this;
    }

    public ASN1Integer getHeight() {
        return this.height;
    }

    public SES_ESPictrueInfo setHeight(ASN1Integer height) {
        this.height = height;
        return this;
    }

    public SES_ESPictrueInfo setHeight(long height) {
        this.height = new ASN1Integer(height);
        return this;
    }

    public DERIA5String getType() {
        return this.type;
    }

    public SES_ESPictrueInfo setType(String type) {
        this.type = new DERIA5String(type);
        return this;
    }

    public SES_ESPictrueInfo setType(DERIA5String type) {
        this.type = type;
        return this;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(4);
        v.add(this.type);
        v.add(this.data);
        v.add(this.width);
        v.add(this.height);
        return new DERSequence(v);
    }
}
