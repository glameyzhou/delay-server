package org.glamey.infra.delayserver;

import java.util.concurrent.TimeUnit;

import org.glamey.infra.delayserver.server.NettyHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.common.base.Stopwatch;

@SpringBootApplication
public class DelayServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayServerApplication.class);

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SpringApplication.run(DelayServerApplication.class, args);
        LOGGER.info("Spring Boot started..., elapsed={}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

        NettyHttpServer.getInstance().start();

    }

}
