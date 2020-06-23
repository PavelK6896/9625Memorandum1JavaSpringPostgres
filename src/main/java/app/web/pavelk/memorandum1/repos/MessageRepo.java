package app.web.pavelk.memorandum1.repos;

import app.web.pavelk.memorandum1.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);//dsl
}
