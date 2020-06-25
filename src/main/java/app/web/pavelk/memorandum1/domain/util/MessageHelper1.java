package app.web.pavelk.memorandum1.domain.util;


import app.web.pavelk.memorandum1.domain.User;

public abstract class MessageHelper1 { //можем поправить
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}
