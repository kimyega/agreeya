package kopo.poly.kpaas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 기본 strength 10, 필요 시 new BCryptPasswordEncoder(12)처럼 조정 가능
        return new BCryptPasswordEncoder();
    }
}
