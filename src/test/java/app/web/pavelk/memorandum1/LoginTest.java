package app.web.pavelk.memorandum1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)//окружение стартует тесты
@SpringBootTest//запускает собирает спринг апп
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() throws Exception {//проверяет наличие строки в html
        this.mockMvc.perform(get("/")) // гет запрос на мапинг
                .andDo(print())//лог
                .andExpect(status().isOk())//обертка запросто результат 200 //assertThat
                .andExpect(content().string(containsString("Hi, guest!")))//ожидаеться что будет токая строка
                .andExpect(content().string(containsString("Please, login")));
    }

    @Test
    public void accessDeniedTest() throws Exception {//проверка требования авторизации
        this.mockMvc.perform(get("/main"))//адрес страници
                .andDo(print())//лог
                .andExpect(status().is3xxRedirection())//300 перенаправляет на страницу
                .andExpect(redirectedUrl("http://localhost/login"));//проверка адреса перенапровления
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void correctLoginTest() throws Exception {//тест авторизации
        this.mockMvc.perform(formLogin().user("dru").password("1"))//запрос на страницу логина
                .andExpect(status().is3xxRedirection())//статус 300 редирект
                .andExpect(redirectedUrl("/"));//на главную
    }

    @Test
    public void badCredentials() throws Exception {//неправильную авторизацию
        this.mockMvc.perform(post("/login").param("username", "jonh"))
                .andDo(print())
                .andExpect(status().isForbidden());//403
    }
}
