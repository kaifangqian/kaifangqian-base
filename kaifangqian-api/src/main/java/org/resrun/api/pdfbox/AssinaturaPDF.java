package org.resrun.api.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.resrun.api.pdfbox.vo.AssinaturaModel;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.util.Matrix;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class AssinaturaPDF extends SignatureBase {

    private SignatureOptions signatureOptions;

    private AssinaturaModel assinatura;
    public AssinaturaPDF(AssinaturaModel assinaturaModel) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, CertificateException {
        super(assinaturaModel.getCertInfo());
        this.assinatura = assinaturaModel;
        if (assinaturaModel.getTsa() != null && !assinaturaModel.getTsa().equals("")) {
            setTsaUrl(assinaturaModel.getTsa());
        }
    }

    public byte [] assina() throws Exception {

        if (assinatura.getPdf() == null || assinatura.getPdf().length == 0) {
            throw new Exception("pdf file null");
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             PDDocument doc = Loader.loadPDF(assinatura.getPdf())) {
            if (doc.isEncrypted()) {
                try {
                    doc.setAllSecurityToBeRemoved(true);
                } catch (Exception e) {
                    throw new Exception("pdf passwd error", e);
                }
            }

            int accessPermissions = SigUtils.getMDPPermission(doc);
            if (accessPermissions == 1)
            {
                throw new IllegalStateException("No changes to the document are permitted due to DocMDP transform parameters dictionary");
            }

            PDSignature signature = null;
            PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm(null);
            PDRectangle rect = null;

            // TODO  签名域的位置  可能需要再计算
            Rectangle2D humanRect = new Rectangle2D.Float(assinatura.getPosition().getParseX(), assinatura.getPosition().getParseY(),
                    assinatura.getPosition().getSignWidthFloat(), assinatura.getPosition().getSignHeightFloat());


            // sign a PDF with an existing empty signature, as created by the CreateEmptySignatureForm example.
            if (acroForm != null)
            {
                signature = findExistingSignature(acroForm, assinatura.getSignatureKey());
                if (signature != null)
                {
                    rect = acroForm.getField(assinatura.getSignatureKey()).getWidgets().get(0).getRectangle();
                }
            }


            if (signature == null)
            {
                // create signature dictionary
                signature = new PDSignature();
            }

            if (rect == null)
            {
                rect = createSignatureRectangle(doc, humanRect);
            }


            if (doc.getVersion() >= 1.5f && accessPermissions == 0)
            {
                try {
                    SigUtils.setMDPPermission(doc, signature, 2);
                }catch (IOException ie){
                    log.error("source pdf set permission error");
                    throw ie;
                }
            }

            if (acroForm != null && acroForm.getNeedAppearances())
            {
                // PDFBOX-3738 NeedAppearances true results in visible signature becoming invisible
                // with Adobe Reader
                if (acroForm.getFields().isEmpty())
                {
                    // we can safely delete it if there are no fields
                    acroForm.getCOSObject().removeItem(COSName.NEED_APPEARANCES);
                    // note that if you've set MDP permissions, the removal of this item
                    // may result in Adobe Reader claiming that the document has been changed.
                    // and/or that field content won't be displayed properly.
                    // ==> decide what you prefer and adjust your code accordingly.
                }
                else
                {
                    System.out.println("/NeedAppearances is set, signature may be ignored by Adobe Reader");
                }
            }

            // default filter
            signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);

            // subfilter for basic and PAdES Part 2 signatures
            signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);

            signature.setName("Name");
            signature.setLocation("Location");
            signature.setReason("Reason");

//             the signing date, needed for valid signature
            signature.setSignDate(Calendar.getInstance());


            signatureOptions = new SignatureOptions();
            signatureOptions.setVisualSignature(createVisualSignatureTemplate(doc, 0, rect, signature));
            signatureOptions.setPage(assinatura.getPosition().getPage());
//            signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);

            doc.addSignature(signature, this, signatureOptions);
//            doc.saveIncremental(bos);
            doc.saveIncremental(bos);
            IOUtils.closeQuietly(signatureOptions);
            doc.close();
            return bos.toByteArray();
//            return null;
        }
    }
    private InputStream createVisualSignatureTemplate(PDDocument srcDoc, int pageNum,
                                                      PDRectangle rect, PDSignature signature) throws IOException
    {
        try (PDDocument doc = new PDDocument())
        {
            PDPage page = new PDPage(srcDoc.getPage(pageNum).getMediaBox());
            doc.addPage(page);
            PDAcroForm acroForm = new PDAcroForm(doc);
            doc.getDocumentCatalog().setAcroForm(acroForm);
            PDSignatureField signatureField = new PDSignatureField(acroForm);
            PDAnnotationWidget widget = signatureField.getWidgets().get(0);
            List<PDField> acroFormFields = acroForm.getFields();
            acroForm.setSignaturesExist(true);
            acroForm.setAppendOnly(true);
            acroForm.getCOSObject().setDirect(true);
            acroFormFields.add(signatureField);

            widget.setRectangle(rect);

            // from PDVisualSigBuilder.createHolderForm()
            PDStream stream = new PDStream(doc);
            PDFormXObject form = new PDFormXObject(stream);
            PDResources res = new PDResources();
            form.setResources(res);
            form.setFormType(1);
            PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());
            float height = bbox.getHeight();
            Matrix initialScale = null;
            switch (srcDoc.getPage(pageNum).getRotation())
            {
                case 90:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(1));
                    initialScale = Matrix.getScaleInstance(bbox.getWidth() / bbox.getHeight(), bbox.getHeight() / bbox.getWidth());
                    height = bbox.getWidth();
                    break;
                case 180:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(2));
                    break;
                case 270:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(3));
                    initialScale = Matrix.getScaleInstance(bbox.getWidth() / bbox.getHeight(), bbox.getHeight() / bbox.getWidth());
                    height = bbox.getWidth();
                    break;
                case 0:
                default:
                    break;
            }
            form.setBBox(bbox);


            // from PDVisualSigBuilder.createAppearanceDictionary()
            PDAppearanceDictionary appearance = new PDAppearanceDictionary();
            appearance.getCOSObject().setDirect(true);
            PDAppearanceStream appearanceStream = new PDAppearanceStream(form.getCOSObject());
            appearance.setNormalAppearance(appearanceStream);
            widget.setAppearance(appearance);

            try (PDPageContentStream cs = new PDPageContentStream(doc, appearanceStream))
            {
                // for 90° and 270° scale ratio of width / height
                // not really sure about this
                // why does scale have no effect when done in the form matrix???
                if (initialScale != null)
                {
                    cs.transform(initialScale);
                }

                // show background (just for debugging, to see the rect size + position)
//                cs.setNonStrokingColor(Color.yellow);
//                cs.addRect(-5000, -5000, 10000, 10000);
                cs.fill();

                if (assinatura.getSignatureImage() != null)
                {

                    PDImageXObject img =  PDImageXObject.createFromByteArray(doc,assinatura.getSignatureImage(),"test");
                    int imgHeight = img.getHeight();
                    int imgWidth = img.getWidth();
                    cs.saveGraphicsState();
                    cs.transform(Matrix.getScaleInstance(rect.getWidth()/imgWidth*1.0f,rect.getHeight()/imgHeight*1.0f));
                    cs.drawImage(img, 0,0);
                    cs.restoreGraphicsState();


                    // show background image
                    // save and restore graphics if the image is too large and needs to be scaled
//                    cs.saveGraphicsState();
//                    cs.transform(Matrix.getScaleInstance(0.25f, 0.25f));
//                    PDImageXObject img =  PDImageXObject.createFromByteArray(doc,assinatura.getSignatureImage(),"test");
//                    cs.drawImage(img, 0, 0);
//                    cs.restoreGraphicsState();
                }
            }

            // no need to set annotations and /P entry

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);

//            FileUtils.writeByteArrayToFile(new File("C:\\Users\\Administrator\\Desktop\\tem\\pdfbox\\sign-t.pdf"), baos.toByteArray());
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    private PDRectangle createSignatureRectangle(PDDocument doc, Rectangle2D humanRect)
    {
        float x = (float) humanRect.getX();
        float y = (float) humanRect.getY();
        float width = (float) humanRect.getWidth();
        float height = (float) humanRect.getHeight();
        PDPage page = doc.getPage(0);
        PDRectangle pageRect = page.getCropBox();
        PDRectangle rect = new PDRectangle();
        // signing should be at the same position regardless of page rotation.
        switch (page.getRotation())
        {
            case 90:
                rect.setLowerLeftY(x);
                rect.setUpperRightY(x + width);
                rect.setLowerLeftX(y);
                rect.setUpperRightX(y + height);
                break;
            case 180:
                rect.setUpperRightX(pageRect.getWidth() - x);
                rect.setLowerLeftX(pageRect.getWidth() - x - width);
                rect.setLowerLeftY(y);
                rect.setUpperRightY(y + height);
                break;
            case 270:
                rect.setLowerLeftY(pageRect.getHeight() - x - width);
                rect.setUpperRightY(pageRect.getHeight() - x);
                rect.setLowerLeftX(pageRect.getWidth() - y - height);
                rect.setUpperRightX(pageRect.getWidth() - y);
                break;
            case 0:
            default:
                rect.setLowerLeftX(x);
                rect.setUpperRightX(x + width);
                rect.setLowerLeftY(pageRect.getHeight() - y - height);
                rect.setUpperRightY(pageRect.getHeight() - y);
                break;
        }
        return rect;
    }

    private PDSignature findExistingSignature(PDAcroForm acroForm, String sigFieldName)
    {
        PDSignature signature = null;
        PDSignatureField signatureField;
        if (acroForm != null)
        {
            signatureField = (PDSignatureField) acroForm.getField(sigFieldName);
            if (signatureField != null)
            {
                // retrieve signature dictionary
                signature = signatureField.getSignature();
                if (signature == null)
                {
                    signature = new PDSignature();
                    // after solving PDFBOX-3524
                    // signatureField.setValue(signature)
                    // until then:
                    signatureField.getCOSObject().setItem(COSName.V, signature);
                }
                else
                {
                    throw new IllegalStateException("The signature field " + sigFieldName + " is already signed.");
                }
            }
        }
        return signature;
    }










}


