package app.web.pavelk.memorandum1.controller;

import app.web.pavelk.memorandum1.domain.Message;
import app.web.pavelk.memorandum1.domain.User;
import app.web.pavelk.memorandum1.domain.dto.MessageDto;
import app.web.pavelk.memorandum1.repos.MessageRepo;
import app.web.pavelk.memorandum1.srvice.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false,
                    defaultValue = "") String filter,
            Model model,
            //сортировка поубыванию по id
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user

    ) {

        Page<MessageDto> page = messageService.messageList(pageable, filter, user);




        model.addAttribute("page", page);//страница
        model.addAttribute("url", "/main");
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

    @GetMapping("/user-messages/{author}")//урл ответа
    public String userMessges(
            @AuthenticationPrincipal User currentUser,//берет из сесии текущего пользователя
            @PathVariable User author,//запрос из сигнатуры анотации
            Model model,//ответ
            @RequestParam(required = false) Message message,//берет из гет запроса
            // ходит в базу даных и ложет в переменную
            // interface repos.MessageRepo
            //required = false пораметор необязателен
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<MessageDto> page = messageService.messageListForUser(pageable, currentUser, author);//взяли сообщения user

        model.addAttribute("userChannel", author); // весь пользователь (и имя) текущего поля в контекст

        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());

        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser)); // проверка на подписку

        model.addAttribute("page", page);//сообщения пользователя
        model.addAttribute("message", message);//переменая из базы данных//инжект или проводка
        model.addAttribute("isCurrentUser", currentUser.equals(author));//собственные сообщенияя
        model.addAttribute("url", "/user-messages/" + author.getId());

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

    @GetMapping("/messages/{message}/like") //акшон для лайков
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,//параметры
            @RequestHeader(required = false) String referer // понимает откуда мы пришли
    ) {
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)) { // значит лай существует
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        //нужно чтобы все вернулось как было
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        //////////////////

        return "redirect:" + components.getPath();
    }

}