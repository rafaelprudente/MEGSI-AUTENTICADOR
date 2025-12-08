package pt.uminho.megsi.authenticator.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String attachment;
}
