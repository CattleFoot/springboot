package com.mugwort.gateway;

import com.hand.hospital.common.scan.log2mq.gateway.EnableGatewayLog2MQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//zuul 路由代理ll
@EnableZuulProxy
//consul-discovery
@EnableDiscoveryClient
@EnableAsync
@EnableGatewayLog2MQ
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
