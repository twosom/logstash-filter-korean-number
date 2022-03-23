package com.icloud.filter;

import co.elastic.logstash.api.*;
import org.junit.jupiter.api.Test;
import org.logstash.plugins.ConfigurationImpl;
import org.logstash.plugins.ContextImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KoreanNumberTest {


    @Test
    void korean_number_filter_test() {
        var fieldList = List.of(
                "field1",
                "field2"
        );

        var configMap = new HashMap<String, Object>(Collections.singletonMap("field", fieldList));
        Configuration config = new ConfigurationImpl(configMap);
        Context context = new ContextImpl(null, null);

        Filter filter = new KoreanNumber("test-id", config, context);
        Event event = new org.logstash.Event();
        FilterMatchListener filterMatchListener = new TestMatchListener();

        event.setField("field1", "삼천오백사십팔만달러");
        event.setField("field2", "5억5천 사백삼십만원");

        filter.filter(Collections.singletonList(event), filterMatchListener);

        assertEquals(String.valueOf(35_480_000), event.getField("field1"));
        assertEquals(String.valueOf(554_300_000), event.getField("field2"));
    }

    static class TestMatchListener implements FilterMatchListener {
        private final AtomicInteger matchCount = new AtomicInteger(0);

        @Override
        public void filterMatched(Event event) {
            matchCount.incrementAndGet();
        }

        public int getMatchCount() {
            return matchCount.get();
        }
    }


}