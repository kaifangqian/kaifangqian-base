package com.kaifangqian.modules.opensign.vo.base;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

public class StringDataSource implements JRDataSource {

    private final String data;
    private boolean hasNext = true;

    public StringDataSource(String data) {
        this.data = data;
    }

    @Override
    public boolean next() {
        if (hasNext) {
            hasNext = false;
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) {
        return data;
    }
}
