package com.pxtec.bxj.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "druid")
public class DruidProperties {

	private String url;

	private String username;

	private String password;

	private String driverClass;

	private int maxActive;

	private int minIdle;

	private int initialSize;

	private boolean testOnBorrow;
}
