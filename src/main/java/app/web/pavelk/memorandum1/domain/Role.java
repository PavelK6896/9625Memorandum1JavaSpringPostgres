package app.web.pavelk.memorandum1.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, Moderator;

    @Override
    public String getAuthority() {
        return name();
    }
}
