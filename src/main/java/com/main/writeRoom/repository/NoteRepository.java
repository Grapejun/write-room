package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.main.writeRoom.domain.User.User;

import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByRoomAndCategory(Room room, Category category, PageRequest pageRequest);
    List<Note> findAllByCategoryAndRoom(Category category, Room room);

    @Query("select n from Note n where n.createdAt>= :startDate and n.createdAt<= :deadline and n.user = :user and n.room = :room and n.achieve = 'TRUE'")
    List<Note> findAchieveNotes(@Param("startDate") LocalDateTime startDate, @Param("deadline") LocalDateTime deadline, @Param("user") User user, @Param("room") Room room);

    Page<Note> findAllByRoom(Room room, PageRequest pageRequest);
    Long countByRoom(Room room);

    Page<Note> findAllByRoomAndUser(Room room, User user, PageRequest pageRequest);

    @Query("SELECT n FROM Note n " +
            "JOIN n.noteTagList nt " +
            "JOIN nt.tag t " +
            "WHERE n.room IN :rooms AND (" +
            "LOWER(n.title) LIKE LOWER(CONCAT('%', :searchWord, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :searchWord, '%')) OR " +
            "LOWER(t.content) LIKE LOWER(CONCAT('%', :searchWord, '%')))")
    List<Note> findByRoomsAndSearchWord(@Param("rooms") List<Room> rooms, @Param("searchWord") String searchWord);

    @Query("SELECT n FROM Note n WHERE n.room IN :rooms AND LOWER(n.title) LIKE LOWER(:searchWord)")
    List<Note> findByTitleInUserRooms(@Param("rooms") List<Room> rooms, @Param("searchWord") String searchWord);

    @Query("SELECT n FROM Note n WHERE n.room IN :rooms AND LOWER(n.content) LIKE LOWER(:searchWord)")
    List<Note> findByContentInUserRooms(@Param("rooms") List<Room> rooms, @Param("searchWord") String searchWord);

    @Query("SELECT n FROM Note n JOIN n.noteTagList nt JOIN nt.tag t WHERE n.room IN :rooms AND LOWER(t.content) LIKE LOWER(:searchWord)")
    List<Note> findByTagInUserRooms(@Param("rooms") List<Room> rooms, @Param("searchWord") String searchWord);

    void deleteByUserId(Long userId);
}
