package com.garam.garam_e_spring.entity.exq;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExqRepository extends JpaRepository<Exq, Long> {

    // search questions by string
    List<Exq> findByQuestionContaining(String input);
}
