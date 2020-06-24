package app.web.pavelk.memorandum1;


import app.web.pavelk.memorandum1.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc//создает структуру класов спринг
@TestPropertySource("/application-test.properties")//анатация для настроек
//скрипы скл которые нужно выполнить
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)//до тестов
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)//после
@WithUserDetails(value = "dru")//имя пользователя для тестов
public class MainControllerTest {
    @Autowired//инжектит клас
    private MainController controller;

    @Autowired
    private MockMvc mockMvc;//подменяет все калассы спринг


    @Test
    public void mainPageTest() throws Exception {//проверка посли авторизации
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())//проверка что пользователь авторизован
                .andExpect(xpath("//*[@id='navbarSupportedContent']/div").string("dru"));
        //x path запрос в html ожидаем строку с именем
        // все теги должны быть закрыты
        //*[@id="navbarSupportedContent"]/div
    }

    @Test
    public void messageListTest() throws Exception {//тест количества сообщений
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(4));
        ////*[@id="message-list"] контейнер сообщений
    }

    @Test
    public void filterMessageTest() throws Exception {//тест фильтра
        this.mockMvc.perform(get("/main").param("filter", "my-tag"))
                .andDo(print())
                .andExpect(authenticated())//пользователь авторизован
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='1']").exists())//просто существование
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='3']").exists());
    }

    @Test
    public void addMessageToListTest() throws Exception {//добавление сообщнеия
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file", "123".getBytes())
                .param("text", "fifth")
                .param("tag", "new one")
                .with(csrf());//токен

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5))//после добавления нового их всего станет 5
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']").exists())
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/span").string("fifth"))//возвращает строку
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/i").string("#new one"));
    }
}
