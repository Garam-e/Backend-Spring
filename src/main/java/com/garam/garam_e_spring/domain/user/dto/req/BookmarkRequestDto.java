package com.garam.garam_e_spring.domain.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkRequestDto {

    private String message;
    private String userId;
}
