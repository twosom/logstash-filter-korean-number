package com.icloud.holder;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanNumberFilter;

import java.io.IOException;

public class KoreanNumberFilterHolder extends KoreanNumberFilter {

    private static volatile KoreanNumberFilterHolder instance;


    private KoreanNumberFilterHolder() {
        super(new TokenStream() {
            @Override
            public boolean incrementToken() throws IOException {
                return false;
            }
        });
    }

    public static KoreanNumberFilterHolder getInstance() {
        if (instance == null) {
            synchronized (KoreanNumberFilterHolder.class) {
                if (instance == null) {
                    instance = new KoreanNumberFilterHolder();
                }
            }
        }
        return instance;
    }


}
