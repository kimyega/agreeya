package kopo.poly.kpaas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QaLogDTO {
    private int qaId;
    private int userId;
    private String question;
    private String answer;
    private String createdAt;
}
