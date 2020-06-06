package app.web.pavelk.memorandum1.controller;

import app.web.pavelk.memorandum1.domain.Message;
import app.web.pavelk.memorandum1.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World defaultValue or ?name=?") String name,
            Map<String, Object> model) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.put("some", "hello, PK!");
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        Message message = new Message(text, tag);

        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.put("some", "hello, PostMapping!");
        model.put("messages", messages);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages2;

        if (filter != null && !filter.isEmpty()) {
            messages2 = messageRepo.findByTag(filter);
        } else {
            messages2 = messageRepo.findAll();
        }

        model.put("some", "hello, filter!");
        model.put("messages", messages2);

        return "main";
    }
}