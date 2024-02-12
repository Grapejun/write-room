package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkNoteRepository extends JpaRepository<BookmarkNote, Long> {
    Page<BookmarkNote> findAllByUser(User user, PageRequest pageRequest);
    BookmarkNote findByNoteAndUser(Note note, User user);
}
