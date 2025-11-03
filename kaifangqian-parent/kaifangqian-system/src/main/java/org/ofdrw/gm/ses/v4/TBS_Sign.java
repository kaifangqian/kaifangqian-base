//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.ofdrw.gm.ses.v4;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.ofdrw.gm.ses.v1.ExtensionDatas;

public class TBS_Sign extends ASN1Object {
    private ASN1Integer version;
    private SESeal eseal;
    private ASN1GeneralizedTime timeInfo;
    private ASN1BitString dataHash;
    private DERIA5String propertyInfo;
    private ExtensionDatas extDatas;

    public TBS_Sign() {
    }

    public TBS_Sign(ASN1Integer version, SESeal eseal, ASN1GeneralizedTime timeInfo, ASN1BitString dataHash, DERIA5String propertyInfo, ExtensionDatas extDatas) {
        this.version = version;
        this.eseal = eseal;
        this.timeInfo = timeInfo;
        this.dataHash = dataHash;
        this.propertyInfo = propertyInfo;
        this.extDatas = extDatas;
    }

    public TBS_Sign(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        this.version = ASN1Integer.getInstance(e.nextElement());
        this.eseal = SESeal.getInstance(e.nextElement());
        this.timeInfo = ASN1GeneralizedTime.getInstance(e.nextElement());
        this.dataHash = DERBitString.getInstance(e.nextElement());
        this.propertyInfo = (DERIA5String)DERIA5String.getInstance(e.nextElement());
        if (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof ASN1TaggedObject) {
                this.extDatas = ExtensionDatas.getInstance(((ASN1TaggedObject)obj));
            }
        }

    }

    public static TBS_Sign getInstance(Object o) {
        if (o instanceof TBS_Sign) {
            return (TBS_Sign)o;
        } else {
            return o != null ? new TBS_Sign(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public TBS_Sign setVersion(ASN1Integer version) {
        this.version = version;
        return this;
    }

    public SESeal getEseal() {
        return this.eseal;
    }

    public TBS_Sign setEseal(SESeal eseal) {
        this.eseal = eseal;
        return this;
    }

    public ASN1GeneralizedTime getTimeInfo() {
        return this.timeInfo;
    }

    public TBS_Sign setTimeInfo(ASN1GeneralizedTime timeInfo) {
        this.timeInfo = timeInfo;
        return this;
    }

    public ASN1BitString getDataHash() {
        return this.dataHash;
    }

    public TBS_Sign setDataHash(ASN1BitString dataHash) {
        this.dataHash = dataHash;
        return this;
    }

    public TBS_Sign setDataHash(byte[] dataHash) {
        this.dataHash = new DERBitString(dataHash);
        return this;
    }

    public DERIA5String getPropertyInfo() {
        return this.propertyInfo;
    }

    public TBS_Sign setPropertyInfo(DERIA5String propertyInfo) {
        this.propertyInfo = propertyInfo;
        return this;
    }

    public TBS_Sign setPropertyInfo(String propertyInfo) {
        this.propertyInfo = new DERIA5String(propertyInfo);
        return this;
    }

    public ExtensionDatas getExtDatas() {
        return this.extDatas;
    }

    public TBS_Sign setExtDatas(ExtensionDatas extDatas) {
        this.extDatas = extDatas;
        return this;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(6);
        v.add(this.version);
        v.add(this.eseal);
        v.add(this.timeInfo);
        v.add(this.dataHash);
        v.add(this.propertyInfo);
        if (this.extDatas != null) {
            v.add(new DERTaggedObject(true, 0, this.extDatas));
        }

        return new DERSequence(v);
    }
}
