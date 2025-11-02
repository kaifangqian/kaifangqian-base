//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.ofdrw.gm.ses.v4;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;

public class SES_ESPropertyInfo extends ASN1Object {
    public static final ASN1Integer CertListType = new ASN1Integer(1L);
    public static final ASN1Integer CertDigestListType = new ASN1Integer(2L);
    private ASN1Integer type;
    private DERUTF8String name;
    private ASN1Integer certListType;
    private SES_CertList certList;
    private ASN1GeneralizedTime createDate;
    private ASN1GeneralizedTime validStart;
    private ASN1GeneralizedTime validEnd;

    public SES_ESPropertyInfo() {
    }

    public SES_ESPropertyInfo(ASN1Integer type, DERUTF8String name, ASN1Integer certListType, SES_CertList certList, ASN1GeneralizedTime createDate, ASN1GeneralizedTime validStart, ASN1GeneralizedTime validEnd) {
        this.type = type;
        this.name = name;
        this.certListType = certListType;
        this.certList = certList;
        this.createDate = createDate;
        this.validStart = validStart;
        this.validEnd = validEnd;
    }

    public SES_ESPropertyInfo(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        this.type = ASN1Integer.getInstance(e.nextElement());
        this.name = (DERUTF8String)DERUTF8String.getInstance(e.nextElement());
        this.certListType = ASN1Integer.getInstance(e.nextElement());
        this.certList = SES_CertList.getInstance(this.certListType, e.nextElement());
        this.createDate = ASN1GeneralizedTime.getInstance(e.nextElement());
        this.validStart = ASN1GeneralizedTime.getInstance(e.nextElement());
        this.validEnd = ASN1GeneralizedTime.getInstance(e.nextElement());
    }

    public static SES_ESPropertyInfo getInstance(Object o) {
        if (o instanceof SES_ESPropertyInfo) {
            return (SES_ESPropertyInfo)o;
        } else {
            return o != null ? new SES_ESPropertyInfo(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public ASN1Integer getType() {
        return this.type;
    }

    public SES_ESPropertyInfo setType(ASN1Integer type) {
        this.type = type;
        return this;
    }

    public DERUTF8String getName() {
        return this.name;
    }

    public SES_ESPropertyInfo setName(DERUTF8String name) {
        this.name = name;
        return this;
    }

    public ASN1Integer getCertListType() {
        return this.certListType;
    }

    public SES_ESPropertyInfo setCertListType(ASN1Integer certListType) {
        this.certListType = certListType;
        return this;
    }

    public SES_CertList getCertList() {
        return this.certList;
    }

    public SES_ESPropertyInfo setCertList(SES_CertList certList) {
        this.certList = certList;
        return this;
    }

    public ASN1GeneralizedTime getCreateDate() {
        return this.createDate;
    }

    public SES_ESPropertyInfo setCreateDate(ASN1GeneralizedTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public ASN1GeneralizedTime getValidStart() {
        return this.validStart;
    }

    public SES_ESPropertyInfo setValidStart(ASN1GeneralizedTime validStart) {
        this.validStart = validStart;
        return this;
    }

    public ASN1GeneralizedTime getValidEnd() {
        return this.validEnd;
    }

    public SES_ESPropertyInfo setValidEnd(ASN1GeneralizedTime validEnd) {
        this.validEnd = validEnd;
        return this;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(7);
        v.add(this.type);
        v.add(this.name);
        v.add(this.certListType);
        v.add(this.certList);
        v.add(this.createDate);
        v.add(this.validStart);
        v.add(this.validEnd);
        return new DERSequence(v);
    }
}
