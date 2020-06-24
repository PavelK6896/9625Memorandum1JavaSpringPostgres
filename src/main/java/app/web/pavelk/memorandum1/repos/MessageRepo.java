package app.web.pavelk.memorandum1.repos;

import app.web.pavelk.memorandum1.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {

    //  List<Message> findByTag(String tag);//dsl
    Page<Message> findAll(Pageable pageable);

    Page<Message> findByTag(String tag, Pageable pageable);//поиск по тегу
}
