package app.web.pavelk.memorandum1.srvice;

import app.web.pavelk.memorandum1.domain.Role;
import app.web.pavelk.memorandum1.domain.User;
import app.web.pavelk.memorandum1.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)//окружение стартует тесты
@SpringBootTest
        //запускает собирает спринг апп
class UserServiceTest {

    @Autowired//тестируеммый класс
    private UserService userService;

    @MockBean // подменяет значение
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void addUser() { //добовляет пользователя
        User user = new User();
        boolean isUserCreated = userService.addUser(user);

        user.setEmail("some@mail.ru");

        Assert.assertTrue(isUserCreated);//успешно создан
        Assert.assertNotNull(user.getActivationCode());//проверка активейшен кода
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));//имеет одну роль

        user.setEmail("some@mail.ru");
        System.out.println("uuuuuuuuu" + user.getEmail());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);//вызан метод save

        user.setEmail("some@mail.ru");
        System.out.println("uuuuuuuuu" + user.getEmail());
        Mockito.verify(mailSender, Mockito.times(0))//вызван ли метод send1 с пораметрами
                .send1(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );

    }

    @Test
    public void addUserFailTest() { // если такой пользователь уже существует
        User user = new User();

        user.setUsername("John");

        Mockito.doReturn(new User()) // обучает мок что такой пользователь уже есть
                .when(userRepo)
                .findByUsername("John");//тогда

        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));// 0 не вызывался
        Mockito.verify(mailSender, Mockito.times(0))// 0 не вызывался логика
                .send1(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }


    @Test
    public void activateUser() {//установка активейшен кода
        User user = new User();

        user.setActivationCode("bingo!"); // установим активейшен код

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);//потомучто активировали пользователя
        Assert.assertNull(user.getActivationCode()); //что активейшен код не пустой

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate me");//про такому коду репозиторий ненайдет пользователя

        Assert.assertFalse(isUserActivated);//активейшен код будет пустым

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class)); //метод не будет вызван
    }
}