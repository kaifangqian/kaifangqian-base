package org.resrun.sdk.service.pojo;

import org.resrun.sdk.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: SelectKeywords
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: SelectKeywords
 * @author: FengLai_Gong
 */
public class SelectKeywords extends PDFTextStripper {

    private static ThreadLocal<KeyWorkPair> keyWorkPair = new ThreadLocal<KeyWorkPair>();

    private Log logger = LogFactory.getLog(SelectKeywords.class);

    private float docHeight;

    public SelectKeywords() throws IOException {
        super.setSortByPosition(true);
    }

    public static void main(String[] args) throws Exception {
        //selectKeyword
//        File file = new File("e:/test/948ad938bab14f4e8a2d843f6dd81d57.pdf");
//        float [] resus = new SelectKeywords().selectKeyword(IOUtils.toByteArray(file), "948ad938bab14f4e8a2d843f6dd81d57");//66   571
//        System.out.println(resus[0]+"--"+resus[1]+"---"+resus[2]);
    }
    /**
     * 查出PDF里所有的关键字
     * @param pdfFile
     * @param KEY_WORD
     * @return
     * @auther zzk
     * 2017年6月21日上午10:58:02
     */
    public List<float[]> selectAllKeyword(byte [] pdfFile, String KEY_WORD) {
        keyWorkPair.set(new KeyWorkPair(KEY_WORD.split(",")));
        ByteArrayInputStream in = null;
        PDDocument document = null;
        try {
            in = new ByteArrayInputStream(pdfFile);
            document = Loader.loadPDF(pdfFile);//加载pdf文件
            if(document.getPage(0).getRotation() ==  90 || document.getPage(0).getRotation() == 270){
                docHeight = document.getPage(0).getCropBox().getWidth();
            }else{
                docHeight = document.getPage(0).getCropBox().getHeight();
            }
            this.getText(document);
            List<float[]> allResu = getAllResult();
            return allResu;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(in!=null) in.close();
                if(document!=null) document.close();
            } catch (IOException e) {
            }
        }
        return null;
    }
    private List<float[]> getAllResult(){
        KeyWorkPair pair = keyWorkPair.get();
        if(pair!=null && pair.getResu()!=null){
            keyWorkPair.set(null);
            return pair.getAllResu();
        }else{
            keyWorkPair.set(null);
            return null;
        }
    }
    /**
     * 查出PDF里最后一个关键字
     * @param pdfFile
     * @param KEY_WORD
     * @return
     * @auther zzk
     * 2017年6月21日上午10:58:46
     */
    public float [] selectKeyword(byte [] pdfFile,String KEY_WORD) {
        keyWorkPair.set(new KeyWorkPair(KEY_WORD.split(",")));
        ByteArrayInputStream in = null;
        PDDocument document = null;
        try {
            in = new ByteArrayInputStream(pdfFile);
            document = Loader.loadPDF(pdfFile);//加载pdf文件
            if(document.getPage(0).getRotation() ==  90 || document.getPage(0).getRotation() == 270){
                docHeight = document.getPage(0).getCropBox().getWidth();
            }else{
                docHeight = document.getPage(0).getCropBox().getHeight();
            }
            this.getText(document);
            float[] resu = getResult();
            return resu;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(in!=null) in.close();
                if(document!=null) document.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    private float[] getResult(){
        KeyWorkPair pair = keyWorkPair.get();
        if(pair!=null && pair.getResu()!=null){
            keyWorkPair.set(null);
            return pair.getResu();
        }else{
            keyWorkPair.set(null);
            return null;
        }
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
            String tChar = text.toString();
            char c = tChar.charAt(0);
            String REGEX = "[,.\\[\\](:;!?)/]";
            lineMatch = matchCharLine(text);
            if ((!tChar.matches(REGEX)) && (!Character.isWhitespace(c))) {
                if ((!is1stChar) && (lineMatch == true)) {
                    appendChar(tChar);
                } else if (is1stChar == true) {
                    setWordCoord(text, tChar);
                }
            } else {
                endWord();
            }
        }
        endWord();
    }
    protected void appendChar(String tChar) {
        tWord.append(tChar);
        is1stChar = false;
    }

    /**
     *
     * %拼接字符串%。
     */
    protected void setWordCoord(TextPosition text, String tChar) {
        itext = text;
        tWord.append("(").append(pageNo).append(")[").append(roundVal(Float.valueOf(text.getXDirAdj()))).append(" : ")
                .append(roundVal(Float.valueOf(text.getYDirAdj()))).append("] ").append(tChar);
        is1stChar = false;
    }

    protected boolean matchCharLine(TextPosition text) {

        Double yVal = roundVal(Float.valueOf(text.getYDirAdj()));
        if (yVal.doubleValue() == lastYVal) {
            return true;
        }
        lastYVal = yVal.doubleValue();
        endWord();
        return false;
    }

    protected Double roundVal(Float yVal) {
        DecimalFormat rounded = new DecimalFormat("0.0'0'");
        Double yValDub = new Double(rounded.format(yVal));
        return yValDub;
    }

    protected void endWord() {
        // String newWord = tWord.toString().replaceAll("[^\\x00-\\x7F]",
        // "");//为了检索速度 使用正则去掉中文
        String newWord = tWord.toString();// 去掉正则 可以检索中文
        KeyWorkPair pair = keyWorkPair.get();

        try {
            String[] seekA = pair.getSeekA();
            float[] resu = new float[3];
            String sWord = newWord.substring(newWord.lastIndexOf(' ') + 1);
            if (!"".equals(sWord)) {
                if (sWord.contains(seekA[0])) {
                    resu[2] = getCurrentPageNo();// (595,842)
                    resu[0] = (float) (roundVal(Float.valueOf(itext.getXDirAdj())) + 0.0F);
                    resu[1] = docHeight - (float) (roundVal(Float.valueOf(itext.getYDirAdj())) + 0.0F);
                    logger.info("PDF关键字信息：[页数:" + resu[2] + "][X:" + resu[0] + "][Y:" + resu[1] + "]");
                    pair.setResu(resu);
                    pair.addResuList(resu);//把每一次找出的关键字放在一个集合里
                    keyWorkPair.set(pair);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            keyWorkPair.set(null);
            throw new RuntimeException();
        }
        tWord.delete(0, tWord.length());
        is1stChar = true;
    }

    private StringBuilder tWord = new StringBuilder();
    private boolean is1stChar = true;
    private boolean lineMatch;
    private int pageNo = 0;
    private double lastYVal;

    private TextPosition itext;

    /**
     * 关键字和返回的位置信息类
     * @author zzk
     * 2017年3月10日下午7:14:37
     */
    class KeyWorkPair {

        public KeyWorkPair(String[] seekA) {
            super();
            this.seekA = seekA;
        }
        public KeyWorkPair(String[] seekA, float[] resu) {
            super();
            this.seekA = seekA;
            this.resu = resu;
        }
        public KeyWorkPair() {
            super();
        }
        public String[] getSeekA() {
            return seekA;
        }
        public void setSeekA(String[] seekA) {
            this.seekA = seekA;
        }
        public float[] getResu() {
            return resu;
        }
        public void setResu(float[] resu) {
            this.resu = resu;
        }

        public void addResuList(float[] resu) {
            resuAll.add(resu);
        }
        public List<float[]> getAllResu() {
            return resuAll;
        }

        private String[] seekA;
        private float[] resu;
        //所有的位置
        private List<float[]> resuAll = new ArrayList<>();
    }
}