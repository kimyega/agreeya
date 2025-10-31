package kopo.poly.kpaas.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MailDTO {

    String toMail;
    String title;
    String contents;

}
