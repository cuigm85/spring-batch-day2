package com.example.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash
public class User {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String email2;
    private String profession;
}