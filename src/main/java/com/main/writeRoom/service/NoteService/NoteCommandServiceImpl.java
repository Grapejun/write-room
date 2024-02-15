package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.aws.s3.AmazonS3Manager;
import com.main.writeRoom.aws.s3.Uuid;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.converter.TagConverter;
import com.main.writeRoom.domain.*;
import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.*;
import com.main.writeRoom.handler.AuthenticityHandler;
import com.main.writeRoom.handler.BookmarkHandler;
import com.main.writeRoom.handler.NoteHandler;
import com.main.writeRoom.repository.*;
import com.main.writeRoom.service.ChallengeService.ChallengeGoalsQueryService;
import com.main.writeRoom.service.ChallengeService.ChallengeRoutineQueryService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.note.NoteRequestDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.main.writeRoom.apiPayload.status.ErrorStatus.NOTE_NOT_FOUND;
import static com.main.writeRoom.domain.mapping.Authority.MANAGER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteCommandServiceImpl implements NoteCommandService{

    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final BookmarkNoteRepository bookmarkNoteRepository;
    private final RoomParticipationRepository roomParticipationRepository;
    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final NoteTagRepository noteTagRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final ChallengeRoutineQueryService routineQueryService;
    private final ChallengeGoalsQueryService goalsQueryService;
    private final EmojiClickRepository emojiClickRepository;

    @Override
    @Transactional
    public NoteResponseDTO.PreNoteResult createPreNote(Room room, User user, Category category, MultipartFile noteImg, NoteRequestDTO.createNoteDTO request) {

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid).build());

        String imgUrl = null;
        if (noteImg != null) {
            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid, "note"), noteImg);
        }
        Note newNote = NoteConverter.toNote(room, user, category, request, imgUrl); //챌린지 200자 검사

        List<String> tagStringList = request.getNoteTagList();
        List<Tag> tagList = TagConverter.toTagList(tagStringList);

        NoteResponseDTO.PreNoteResult preNoteResult = NoteResponseDTO.PreNoteResult.builder()
                .note(newNote)
                .tagList(tagList)
                .build();


        //챌린지 달성 코드
        if (newNote.getAchieve() == ACHIEVE.TRUE) {
            ChallengeRoutineParticipation routineParticipation = routineQueryService.findProgressRoutineParticipation(user, room);
            ChallengeGoalsParticipation goalsParticipation = goalsQueryService.findProgressGoalsParticipation(user, room);

            if (routineParticipation != null) { //null이라면 진행 중인 루틴 챌린지가 없는거임
                ChallengeRoutine routine = routineParticipation.getChallengeRoutine();

                if (!routineParticipation.getIsNoteToday() && LocalDate.now().isAfter(routine.getStartDate().minusDays(1)) && LocalDate.now().isBefore(routine.getDeadline().plusDays(1))) {
                    routineParticipation.plusAchieveCount(); //달성했으므로 카운트 증가 -> 일주일 지나고 리셋(by스케줄러)
                    routineParticipation.setIsNoteToday(true);   //오늘 이미 작성했다는 것을 표시.

                    if ((LocalDate.now().isAfter(routine.getDeadline().minusDays(7)) && LocalDate.now().isBefore(routine.getDeadline().plusDays(1))) && routineParticipation.getAchieveCount() >= routine.getTargetCount()) { //만약 마지막 주라면 성공여부 검사
                        routineParticipation.setChallengeStatus(ChallengeStatus.SUCCESS);  //마지막 주에 목표 카운트를 도달했으므로 챌린지 성공.
                        routineParticipation.setStatusUpdatedAt(LocalDate.now()); //성공 날짜 저장.
                    }
                }
            }

            if (goalsParticipation != null) {
                ChallengeGoals goals = goalsParticipation.getChallengeGoals();
                if ((goals.getDeadline() == null) || (LocalDate.now().isAfter(goals.getStartDate().minusDays(1)) && LocalDate.now().isBefore(goals.getDeadline().plusDays(1)))) {
                    goalsParticipation.plusAchieveCount(); //달성했으므로 카운트 증가

                    if (goalsParticipation.getAchieveCount() >= goals.getTargetCount()) {
                        goalsParticipation.setChallengeStatus(ChallengeStatus.SUCCESS);  //목표 카운트를 도달했으므로 챌린지 성공.
                        goalsParticipation.setStatusUpdatedAt(LocalDate.now()); //성공 날짜 저장.
                    }
                }
            }
        }
        tagRepository.saveAll(tagList);
        noteRepository.save(newNote);
        return preNoteResult;
    }

    @Transactional
    public Note createNote(NoteResponseDTO.PreNoteResult preNoteResult) {

        Note note = preNoteResult.getNote();
        List<Tag> tagList = preNoteResult.getTagList();

        List<NoteTag> noteTagList =  tagList.stream()
                .map(tag ->
                        NoteTag.builder()
                                .note(note)
                                .tag(tag)
                                .room(note.getRoom())
                                .build()
                ).toList();
        noteTagRepository.saveAll(noteTagList);

        note.getNoteTagList().addAll(noteTagList);

        return note;
    }

    @Transactional
    // NoteService 내 updateNoteFields 메소드
    public Note updateNoteFields(Note existingNote, Category category, MultipartFile noteImg, NoteRequestDTO.patchNoteDTO request) {

        String imgUrl = null;
        if (noteImg != null) {
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid, "note"), noteImg);
        }

        noteTagRepository.deleteAll(existingNote.getNoteTagList());
        existingNote.getNoteTagList().clear();

        List<String> tagStringList = request.getNoteTagList();
        List<Tag> newTagList = TagConverter.toTagList(tagStringList);

        Note.NoteBuilder builder = existingNote.toBuilder();
        if (request.getNoteTitle() != null) builder.title(request.getNoteTitle());
        if (request.getNoteSubtitle() != null) builder.subtitle(request.getNoteSubtitle());
        if (request.getNoteContent() != null) builder.content(request.getNoteContent());
        if (imgUrl != null) builder.coverImg(imgUrl);
        else
        {
            if (request.getImgDelete())
                builder.coverImg(null);
        }
        if (request.getLetterCount() >= 200) builder.achieve(ACHIEVE.TRUE);
        if (category != null) builder.category(category);

        Note updatedNote = builder
                .createdAt(existingNote.getCreatedAt())
                .build();

        // 새로운 노트태그 관계 생성 및 저장
        List<NoteTag> newNoteTags = newTagList.stream()
                .map(tag -> NoteTag.builder()
                        .note(updatedNote)
                        .tag(tag)
                        .room(updatedNote.getRoom())
                        .build()
        )
                .collect(Collectors.toList());
        noteTagRepository.saveAll(newNoteTags);
        updatedNote.getNoteTagList().addAll(newNoteTags); // 메모리 상의 노트 엔티티에도 노트태그 관계 추가

        return noteRepository.save(updatedNote);
    }

    @Transactional
    public NoteResponseDTO.NoteDeleteResult deleteNote(Long noteId, User user) {
        // 노트가 사라지면 이모지 클릭이 없어지면서 이모지가, 노트 태그 태이블 데이터가 지워지며 노트 태그가 함께 사라 지면 된다.

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteHandler(NOTE_NOT_FOUND));
        Room room = note.getRoom();
        RoomParticipation roomParticipation = roomParticipationRepository.findByRoomAndUser(room, user);

        if (!user.getId().equals(note.getUser().getId()) || !roomParticipation.getAuthority().equals(MANAGER)) // 해당 룸의 관리자가 아닌 경우에도 권한 없는 에러 발생 하도록 수정
            throw new AuthenticityHandler(ErrorStatus.AUTHORITY_NOT_FOUND);

        List<EmojiClick> emojiClick = emojiClickRepository.findAllByNote(note);
        emojiClick.forEach(emojiClick1 -> emojiClickRepository.deleteById(emojiClick1.getId()));

        noteRepository.delete(note);
        return NoteConverter.toDeleteNoteResult(note);
    }

    @Transactional
    public void createBookmarkNote(Long roomId, Note note, Long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        BookmarkNote existingBookmarkNote = bookmarkNoteRepository.findByNoteAndUser(note, user);

        if (existingBookmarkNote != null) {
            throw new BookmarkHandler(ErrorStatus.EXIST_BOOKMARK_NOTE);
        }

        BookmarkNote bookmarkNote = NoteConverter.toBookMarkNote(room, note, user);
        bookmarkNoteRepository.save(bookmarkNote);
    }

    @Transactional
    public void deleteBookmarkNote(Long bookmarkNoteId) {
        bookmarkNoteRepository.deleteById(bookmarkNoteId);
    }
}
