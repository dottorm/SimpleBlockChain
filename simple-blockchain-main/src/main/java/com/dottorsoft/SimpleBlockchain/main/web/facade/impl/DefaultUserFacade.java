package com.dottorsoft.SimpleBlockchain.main.web.facade.impl;

import com.dottorsoft.SimpleBlockchain.main.service.UserService;
import com.dottorsoft.SimpleBlockchain.main.web.facade.UserFacade;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class DefaultUserFacade implements UserFacade {

    @Inject
    private UserService userService;

    public DefaultUserFacade(UserService userService) {
        this.userService = userService;
    }

}
