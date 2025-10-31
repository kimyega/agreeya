package kopo.poly.kpaas.dto;


import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    String toMail;
    String title;
    String contents;

}
