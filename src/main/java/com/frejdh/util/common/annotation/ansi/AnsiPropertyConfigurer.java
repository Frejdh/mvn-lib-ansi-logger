package com.frejdh.util.common.annotation.ansi;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@lombok.Data
@Service
public class AnsiPropertyConfigurer {

	@Autowired
	private AnsiProperties ansiProperties;
}
