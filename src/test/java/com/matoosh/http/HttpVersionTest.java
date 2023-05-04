package com.matoosh.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {

    @Test
    void getBestCompatibleVersionExactMatch() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            fail();
        }

        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }

    @Test
    void getBestCompatibleVersionBadFormat() {
        assertThrows(BadHttpVersionException.class, () -> {
            HttpVersion.getBestCompatibleVersion("HttP/1.1");
        });
    }

    @Test
    void getBestCompatibleVersionHigherVersion() throws BadHttpVersionException {
        HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }
}
