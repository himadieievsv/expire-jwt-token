package com.jwtdemo.domain.user;

import com.jwtdemo.application.service.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.desktop.UserSessionEvent;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userServices;

    @WithMockUser(username = "name")
    @Test
    public void testStatus() throws Exception {
        mvc.perform(get("/api/v1/user/status"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testNotAuthenticated() throws Exception {
        mvc.perform(get("/api/v1/user/status"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
