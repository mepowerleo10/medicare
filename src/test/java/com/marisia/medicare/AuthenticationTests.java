package com.marisia.medicare;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureWebMvc
public class AuthenticationTests {

  @Autowired
  private MockMvc mvc;


  public void shouldBeLoggedInToAccessAdminPanel() throws Exception {
    mvc.perform(get("/admin"))
        .andExpect(
            status().is3xxRedirection())
        .andExpect(
            redirectedUrl("/login"));
  }

}
