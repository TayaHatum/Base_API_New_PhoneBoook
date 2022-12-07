package dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ErrorDto {
    private int status;
    private Object message;
}
