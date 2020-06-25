package app.web.pavelk.memorandum1.srvice;


import app.web.pavelk.memorandum1.domain.User;
import app.web.pavelk.memorandum1.domain.dto.MessageDto;
import app.web.pavelk.memorandum1.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

@Service
public class MessageService {

//    @Autowired //помогает сделать hql запрос
//    private EntityManager em;

    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter, pageable, user);
        } else {
            return messageRepo.findAll(pageable, user);
        }
    }

    public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepo.findByUser(pageable, author, currentUser);
    }
}
