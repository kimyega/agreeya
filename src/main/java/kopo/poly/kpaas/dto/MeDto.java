package kopo.poly.kpaas.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeDto {
    private Integer userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
