package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkNoteRepository extends JpaRepository<BookmarkNote, Long> {
}
