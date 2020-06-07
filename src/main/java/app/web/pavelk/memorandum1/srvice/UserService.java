package app.web.pavelk.memorandum1.srvice;

import app.web.pavelk.memorandum1.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService { // Подробности
    //@Autowired
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findByUsername(s); // возвращает пользователя
    }
}
