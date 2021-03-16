package web.innovation.one.udinei.beerstockapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* Essa classe testa a criação e configuração inicial da applicação web - springboot
* */

@ExtendWith(MockitoExtension.class)
public class AppHelloControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    private AppHelloController appHelloController;

    // seta para cada metodo do controller mockado, retorno em Json, com paginação
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(appHelloController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }


    @Test
    public void whenGETshouldHelloMessage() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/")
                                  .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk());

            response.andReturn().getResponse().getContentAsString().equals("Hello, Digital Innovation One!");
    }
}
