package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.aws.s3.AmazonS3Manager;
import com.main.writeRoom.aws.s3.Uuid;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.converter.TagConverter;
import com.main.writeRoom.domain.*;
import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.handler.NoteHandler;
import com.main.writeRoom.repository.*;
import com.main.writeRoom.service.CategoryService.CategoryQueryService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.note.NoteRequestDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.main.writeRoom.apiPayload.status.ErrorStatus.NOTE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteCommandServiceImpl implements NoteCommandService{

    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final BookmarkNoteRepository bookmarkNoteRepository;
    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final NoteTagRepository noteTagRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;

    @Override
    @Transactional
    public NoteResponseDTO.PreNoteResult createPreNote(Room room, User user, Category category, MultipartFile noteImg, NoteRequestDTO.createNoteDTO request) {

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid).build());

        String imgUrl = null;
        if (noteImg != null) {
            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid), noteImg);
        }
        Note newNote = NoteConverter.toNote(room, user, category, request, imgUrl);

        List<String> tagStringList = request.getNoteTagList();
        List<Tag> tagList = TagConverter.toTagList(tagStringList);

        NoteResponseDTO.PreNoteResult preNoteResult = NoteResponseDTO.PreNoteResult.builder()
                .note(newNote)
                .tagList(tagList)
                .build();

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
            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid), noteImg);
        }

        List<String> tagStringList = request.getNoteTagList();
        List<Tag> newTagList = TagConverter.toTagList(tagStringList);

        // 노트 아이디로 노트 태그 태이블에 접근해서 해당 노트가 아이디인 태그를 모두 포함하는 태그 리스트를 생성
        List<Tag> existingTagList = existingNote.getNoteTagList().stream()
                .map(NoteTag::getTag)
                .toList();
        // 태그 목록 비교 및 업데이트
        updateTags(existingNote, newTagList);

        // 생성된 태그 리스트를 전달받은 태그 리스트와 비교, 변경이 있다면 매핑 태이블과 노트의 매핑도 수정

        Note.NoteBuilder builder = existingNote.toBuilder();
        if (request.getNoteTitle() != null) builder.title(request.getNoteTitle());
        if (request.getNoteSubtitle() != null) builder.subtitle(request.getNoteSubtitle());
        if (request.getNoteContent() != null) builder.content(request.getNoteContent());
        if (imgUrl != null) builder.coverImg(imgUrl);
        if (request.getLetterCount() >= 200) builder.achieve(ACHIEVE.TRUE);
        if (category != null) builder.category(category);
        Note updatedNote = builder.build();

        return noteRepository.save(updatedNote);
    }
    @Transactional
    public void updateTags(Note note, List<Tag> newTags) {
        // 기존 NoteTag 관계를 모두 조회
        List<NoteTag> existingNoteTags = note.getNoteTagList();

        // 삭제할 NoteTag 관계 식별
        List<NoteTag> noteTagsToRemove = existingNoteTags.stream()
                .filter(noteTag -> !newTags.contains(noteTag.getTag()))
                .collect(Collectors.toList());

        // 새로운 태그에 대한 NoteTag 관계 추가
        newTags.forEach(newTag -> {
            boolean exists = existingNoteTags.stream()
                    .anyMatch(noteTag -> noteTag.getTag().equals(newTag));
            if (!exists) {
                // 새로운 NoteTag 관계 생성 및 저장
                NoteTag newNoteTag = NoteTag.builder()
                        .note(note)
                        .tag(newTag)
                        .room(note.getRoom())
                        .build();
                noteTagRepository.save(newNoteTag);
            }
        });

        // 더 이상 필요하지 않은 NoteTag 관계 삭제
        noteTagsToRemove.forEach(noteTagRepository::delete);
    }
    
    @Transactional
    public NoteResponseDTO.NoteResult getNote(Note note) {
        
        // 컨버터에서 노트 빌드
        // 서비스에서 양방향 매핑
        NoteResponseDTO.NoteResult noteResult = NoteConverter.toNoteResult(note);

        return noteResult;

    }

    @Transactional
    public NoteResponseDTO.NoteResult deleteNote(Long roomId, Long noteId) {
        // 어차피 노트 아이디가 룸마다 안겹치면 굳이 노트를 경로로 타고 들어가야 하나?
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteHandler(NOTE_NOT_FOUND));

        noteRepository.delete(note);
        return NoteConverter.toNoteResult(note);
    }
    /* delete 함수 구현 참고
    @Override
    public BookmarkResponseDTO.TopicResultDTO deleteMaterial(Long id) {

        BookmarkMaterial bookmarkMaterial = bookmarkMaterialRepository.findById(id)
                        .orElseThrow(() -> new BookmarkHandler(ErrorStatus.BOOKMARK_NOT_FOUND));

        bookmarkMaterialRepository.delete(bookmarkMaterial);

        return BookmarkConverter.toDeleteResultDTO(id);
    }
    */

    @Transactional
    public void createBookmarkNote(Long roomId, Note note, Long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        BookmarkNote bookmarkNote = NoteConverter.toBookMarkNote(room, note, user);
        bookmarkNoteRepository.save(bookmarkNote);
    }

    @Transactional
    public void deleteBookmarkNote(Long bookmarkNoteId) {
        bookmarkNoteRepository.deleteById(bookmarkNoteId);
    }
}
