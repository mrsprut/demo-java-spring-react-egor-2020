package org.tyaa.demo.spring.springreactmysqlmongo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.User;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class HibernateWebAuthProvider implements AuthenticationProvider, UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDAO.findUserByName(userName);
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return AuthorityUtils.createAuthorityList(user.getRole().getName());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    @Override
    // обязательно - транзакционный метод
    @Transactional
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        // данные от клиента при попытке аутентификации
        String name = a.getName();
        String password = a.getCredentials().toString();
        User user = null;
        try {
            // попытка получить сущность Пользователь из БД по имени
            // (явное чтение из БД, но благодаря аннотации Transactional соединение останется открытым)
            user = userDAO.findUserByName(name);
        } catch (Exception ex) {
            Logger.getLogger(HibernateWebAuthProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        // проверяем совпадение шифров паролей: переданного от клиента
        // с хранящимся в БД
        if (user != null
                // (попытка неявного чтения из БД, ленивая загрузка будет успешной,
                // потому что в транзакционном методе после выполнения явных чтений
                // данных из соединение остается открытым)
                && user.getRole() != null
                && (passwordEncoder.matches(password, user.getPassword()))
        ) {
            // передача имени роли пользователя в следующие составляющие системы безопасности
            // для задания авторизации
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(/* "ROLE_" + */user.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}
