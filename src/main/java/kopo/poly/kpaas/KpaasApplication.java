package kopo.poly.kpaas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@SpringBootApplication
@MapperScan("kopo.poly.kpaas.mapper")   // ⬅️ 매퍼 패키지 스캔
public class KpaasApplication {
  public static void main(String[] args) {
    SpringApplication.run(KpaasApplication.class, args);
  }
}
