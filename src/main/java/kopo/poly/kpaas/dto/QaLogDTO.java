package kopo.poly.kpaas.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QaLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private int qaId;
    private int userId;
    private String question;
    private String answer;
    private String createdAt;
}
