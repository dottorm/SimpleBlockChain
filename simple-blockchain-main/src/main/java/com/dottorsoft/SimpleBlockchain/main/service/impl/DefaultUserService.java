package com.dottorsoft.SimpleBlockchain.main.service.impl;

import com.dottorsoft.SimpleBlockchain.main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class DefaultUserService implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserService.class);

	@Inject
	public DefaultUserService() {
	}


}
