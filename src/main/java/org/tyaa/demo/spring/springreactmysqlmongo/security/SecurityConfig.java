package org.tyaa.demo.spring.springreactmysqlmongo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
// обязательно устанавливаем MiddleWare SpringSecurity в PipeLine на первую позицию
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private HibernateWebAuthProvider authProvider;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private SavedReqAwareAuthSuccessHandler savedReqAwareAuthSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // устанавливаем предоставитель БД для работы с учетными записями пользователей
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // отключаем проверку наличия токена пользователя в принимаемых данных
        http.csrf().disable()
            // не блокировать кросс-доменные запросы
            .cors().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            // авторизация по шаблонам адресов
            .antMatchers(HttpMethod.GET, "/api/auth/user/**").permitAll() // позволить всем
            .antMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
            .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").authenticated() // только аутентифицированным
                .antMatchers("/api/auth/roles").permitAll()
            .antMatchers(HttpMethod.GET, "/api/auth/role/**").permitAll()
            .antMatchers("/api/cart/**").authenticated()
            .antMatchers("/shared/**").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN") // только тем, у кого роль ROLE_ADMIN
            .antMatchers("/api/**/admin/**").hasRole("ADMIN")
            .and()
            .formLogin()
            .successHandler(savedReqAwareAuthSuccessHandler)
            // пользовательский URL для перенаправления на него в случае неуспешного входа
            .failureUrl("/api/auth/user/onerror")
            .and()
            .logout()
            // пользовательский URL конечной точки для выхода из учетной записи
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            // пользовательский URL для перенаправления на него после выхода из учетной записи
            .logoutSuccessUrl("/api/auth/user/signedout");

        // Auth demo
        // 1. /login (POST) - уже реализована в Spring Security,
        // реализовывать в контроллере не нужно!
        //username=user2&password=2 - admin
        //username=user1&password=1 - user
        // 2. /shared/pages/testpublic.html (GET)
        // 3. /admin/pages/testadmin.html (GET)
        // 4. /api/user (GET)
        // 5. /api/role (GET)
        // 6. /logout (GET)
    }
}
