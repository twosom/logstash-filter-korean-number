package com.icloud.filter;

import co.elastic.logstash.api.*;
import com.icloud.holder.KoreanNumberFilterHolder;
import org.apache.lucene.analysis.ko.KoreanNumberFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@LogstashPlugin(name = "korean_number")
public class KoreanNumber implements Filter {

    private final KoreanNumberFilter koreanNumberFilter;

    private static final PluginConfigSpec<List<Object>> FIELD =
            PluginConfigSpec.arraySetting("field", Collections.emptyList(), false, false);


    private final List<String> fieldList;

    private final String id;


    public KoreanNumber(String id, Configuration config, Context context) {
        this.id = id;
        this.koreanNumberFilter = KoreanNumberFilterHolder.getInstance();

        List<Object> dataList = config.get(FIELD);
        if (dataList.stream().anyMatch(e -> !(e instanceof String))) {
            throw new IllegalArgumentException("[field] must be string");
        }

        this.fieldList = dataList.stream()
                .map(Object::toString)
                .collect(Collectors.toList());


    }

    @Override
    public Collection<Event> filter(Collection<Event> events, FilterMatchListener matchListener) {
        for (Event event : events) {
            for (String field : fieldList) {
                Object objectFieldValue = event.getField(field);
                if (objectFieldValue instanceof String) {
                    process(event, field, (String) objectFieldValue);
                    matchListener.filterMatched(event);
                } else {
                    event.tag("wrong value on field [" + field + "]");
                }
            }
        }
        return events;
    }

    private void process(Event event, String field, String fieldValue) {
        fieldValue = fieldValue.replaceAll("\\s", "");
        String normalizedNumber = koreanNumberFilter.normalizeNumber(fieldValue);
        event.setField(field, normalizedNumber);
    }

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        return new ArrayList<>(Collections.singleton(FIELD));
    }

    @Override
    public String getId() {
        return this.id;
    }
}
