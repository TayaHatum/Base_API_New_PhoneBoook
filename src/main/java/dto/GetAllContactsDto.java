package dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class GetAllContactsDto {
    private List<ContactDto> contacts;
}
