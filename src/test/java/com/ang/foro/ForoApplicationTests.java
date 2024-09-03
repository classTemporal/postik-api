package com.ang.foro;

import org.aspectj.lang.annotation.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.ang.foro.controller.UserController;

@SpringBootTest
class ForoApplicationTests {

    private MockMvc mockMvc;
    
    @InjectMocks
    private UserController userController;

    @Before(value = "")
    public void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

}