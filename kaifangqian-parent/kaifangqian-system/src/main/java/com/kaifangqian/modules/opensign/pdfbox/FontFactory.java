package com.kaifangqian.modules.opensign.pdfbox;

import com.kaifangqian.modules.opensign.pdfbox.vo.FontEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: FontFactory
 * @Package: com.kaifangqian.modules.opensign.pdfbox
 * @ClassName: FontFactory
 * @author: FengLai_Gong
 */
public class FontFactory {
    private Map<String, InputStream> fontMap;


    private FontFactory(String fontBasePath){
        fontMap = new HashMap<>();
        for (FontEnum font : FontEnum.values()){
            try {
                InputStream inputStream = new FileInputStream(new File(fontBasePath + font.getFontName()));
                fontMap.put(font.getFontKey(),inputStream);
            }catch (Exception fe){
                fe.printStackTrace();
            }
        }
    }

    public static FontFactory getInstance(String fontBasePath) {
        return new FontFactory(fontBasePath);
    }


    public InputStream getFont(FontEnum fontEnum){
        if(fontMap != null && fontMap.size()>0)
            return fontMap.get(fontEnum.getFontKey());
        return  null;
    }

    public void close(){
        if(fontMap != null && fontMap.size()>0){
            for (Map.Entry<String, InputStream> font:fontMap.entrySet()){
                try {
                    font.getValue().close();
                }catch (Exception e){

                }
            }
        }
    }
}