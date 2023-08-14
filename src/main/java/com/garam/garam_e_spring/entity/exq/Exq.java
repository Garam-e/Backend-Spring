package com.garam.garam_e_spring.entity.exq;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exq {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String question;

    @Column
    private String answer;
}
