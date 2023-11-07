package com.example.ServiceA.util;

import org.junit.jupiter.api.Test;

import com.example.ServiceA.payload.request.SampleObject;

public class HelperTest {

    @Test
    void testJsonSerialize() {
        SampleObject sampleObject = SampleObject.builder().id(1).name("Huu Duc").old(10).salary(1.6)
                .build();

        String result = Helper.jsonSerialize(sampleObject);
        System.out.println(result);
    }

    @Test
    void testJsonDeserialize() {
        String src = "{\"id\":1,\"name\":\"Huu Duc\",\"old\":10,\"salary\":1.6}";
        SampleObject object = Helper.jsonDeserialize(src, SampleObject.class);
        System.out.println(object);
    }
}
