package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkMaterialRepository extends JpaRepository<BookmarkMaterial, Long> {

    Page<BookmarkMaterial> findAllByUser(User user, PageRequest pageRequest);
}
