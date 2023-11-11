package com.garam.garam_e_spring.domain.user.repository;

import com.garam.garam_e_spring.domain.user.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
