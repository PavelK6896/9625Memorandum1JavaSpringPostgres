package app.web.pavelk.memorandum1.repos;

import app.web.pavelk.memorandum1.domain.Message;
import app.web.pavelk.memorandum1.domain.User;
import app.web.pavelk.memorandum1.domain.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepo extends CrudRepository<Message, Long> {

//    //  List<Message> findByTag(String tag);//dsl
//    Page<Message> findAll(Pageable pageable);
//
//    Page<Message> findByTag(String tag, Pageable pageable);//поиск по тегу
//
//    Page<MessageDto> findByUser(Pageable pageable, User author, User currentUser);

    //hql язык запросов похожий на sql
    //инстанс класа в запросе возвращает создаем клас дто
    // from Message m left join m.likes ml  к списку сообщений присоединям список лайков
    @Query("select new app.web.pavelk.memorandum1.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new app.web.pavelk.memorandum1.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new app.web.pavelk.memorandum1.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);

}
