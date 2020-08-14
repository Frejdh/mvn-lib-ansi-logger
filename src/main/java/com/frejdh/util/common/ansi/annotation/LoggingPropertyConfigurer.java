package com.frejdh.util.common.ansi.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@lombok.Data
@Service
public class LoggingPropertyConfigurer {

	@Autowired
	private LoggingProperties loggingProperties;
}
