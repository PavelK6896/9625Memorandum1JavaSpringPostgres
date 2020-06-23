package app.web.pavelk.memorandum1.controller;

import app.web.pavelk.memorandum1.domain.Message;
import app.web.pavelk.memorandum1.domain.User;
import app.web.pavelk.memorandum1.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam(required = false, defaultValue = "") String filter,
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,

            Model model,
            @RequestParam("file") MultipartFile file

    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);

        } else {
            saveFile(message, file);
            model.addAttribute("message", null);
            messageRepo.save(message);

        }

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    private void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{user}")//урл ответа
    public String userMessges(
            @AuthenticationPrincipal User currentUser,//берет из сесии текущего пользователя
            @PathVariable User user,//запрос из сигнатуры анотации
            Model model,//ответ
            @RequestParam(required = false) Message message//берет из гет запроса
            // ходит в базу даных и ложет в переменную
            // interface repos.MessageRepo
            //required = false пораметор необязателен
    ) {
        Set<Message> messages = user.getMessages();//взяли сообщения user

        model.addAttribute("messages", messages);//сообщения пользователя
        model.addAttribute("message", message);//переменая из базы данных//инжект или проводка
        model.addAttribute("isCurrentUser", currentUser.equals(user));//собственные сообщенияя

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,//автоматичестки спринг получает нужные пораметры
            @RequestParam("id") Message message,//получаем сообщение по ид
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {//может менять только свои сообщения
            if (!StringUtils.isEmpty(text)) {//не пустое поле текст
                message.setText(text);
            }

            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }

            saveFile(message, file);
            messageRepo.save(message);
        }

        return "redirect:/user-messages/" + user;
    }


}