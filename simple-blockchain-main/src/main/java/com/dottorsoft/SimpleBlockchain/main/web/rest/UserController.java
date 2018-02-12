package com.dottorsoft.SimpleBlockchain.main.web.rest;

import com.dottorsoft.SimpleBlockchain.main.web.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

	/*
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public UserDTO getUser(@PathVariable("username") String username) {
        return userFacade.getByUsername(username);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("username") String username) {
        userFacade.delete(username);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        return userFacade.save(userDTO);
    }
	*/


}
