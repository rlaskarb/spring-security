package com.ohgiraffers.sessionsecurity.config;

import com.ohgiraffers.sessionsecurity.common.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // spring security
public class SecurityConfig {
    /*필기
    * 비밀번호 인코딩하기 위한 Bean 생성
    * Bcrypt 객체는 비밀번호 암호화를 위해 가장 많이 사용되는 알고리즘 중 하나이다.*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //정적 리소스에 대한 요청은 시큐리티 설정이 돌지 못하게 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Autowired
    private AuthFailHandler authFailHandler;

    // 여기가 설정의 핵심!!!!!!!!!!!!!!
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // 서버의 리소스 접근 가능 권한 설정
        http.authorizeHttpRequests(auth -> {
            //permitAll() -> 인증되지 않는 (로그인 되지 않는) 사용자들이 접근할수 있는 URL기술
            auth.requestMatchers("/auth/login", "/user/signup", "/auth/fail", "/").permitAll();
            // hasAnyAuthority -> 해당하는 URL 은 권한을 가진 사람만 접근할 수 있다.
            auth.requestMatchers("/admin/*").hasAnyAuthority(UserRole.ADMIN.getRole());
            // /user/* 요청은 일반회원 권항을 가진 사람만 접근할 수 있다.
            auth.requestMatchers("/user/*").hasAnyAuthority(UserRole.USER.getRole());
            // 그외 어떠한 요청들은 권한 상관없이 들어 갈수 있다(단,로그인 된 인원에 한해)
            auth.anyRequest().authenticated();
        }).formLogin(login ->{
            login.loginPage("/auth/login"); // 로그인을 수행하는 페이지 URL 을 기술
            // 사용자 ID를 입력하는 필드(input 타입 name 과 반드시 일치해야한다.)
            login.usernameParameter("user");
            // 사용자 PWD 를 입력하는 필드(input 타입 name 과 반드시 일치)
            login.passwordParameter("pass");
            // 사용자가 로그인에 성공했을 시 보내줄 URL 기술
            login.defaultSuccessUrl("/",true);
            // 로그인에 실패 했을 시 내용을 기술한 객체 호출 아직 미작성
            login.failureHandler(authFailHandler);
        }).logout(logout -> {
            //로그아웃을 담당할 핸들러 메소드 요청 URL 기술
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            // session 은 쿠키방식으로 저장이 되어 있어 로그인을 하면
            // 해당하는 쿠키를 삭제함으로써 로그아웃을 만들어 준다.
            logout.deleteCookies("JSESSIONID");
            //서버측의 세션 공간 만료
            logout.invalidateHttpSession(true);
            //로그아우웃 성공 시 요청 URL 기술
            logout.logoutSuccessUrl("/");
        }).sessionManagement(session ->{
            session.maximumSessions(1); //session 의 허용 갯수 제한
            // 한 사용자가 여러 창을 띄어 동시에 세션 여러 개 활성화 방지
            // 세션이 만료 되었을때 요청할 URL 기술
            session.invalidSessionUrl("/");
        }).csrf(csrf->csrf.disable());

        //위에서 설정한 내용대로 시큐리티 기능 빌드(생성)
        return http.build();
    }
}


