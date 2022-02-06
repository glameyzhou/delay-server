package org.glamey.infra.delayserver.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author zhouyang01
 * Created on 2022-02-06
 */
class NettyHttpServerTest {
    private NettyHttpServer server;

    @BeforeEach
    void setUp() {
        server = new NettyHttpServer();
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void test() {

    }
}