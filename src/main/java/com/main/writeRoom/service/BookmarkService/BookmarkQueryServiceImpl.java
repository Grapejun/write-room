package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.repository.BookmarkMaterialRepository;
import com.main.writeRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkQueryServiceImpl implements BookmarkQueryService{

    private final BookmarkMaterialRepository bookmarkMaterialRepository;
    private final UserRepository userRepository;
    @Override
    public Page<BookmarkMaterial> getBookmarkMaterialList(Long userId, Integer page) {
        User user = userRepository.findById(userId).get();

        Page<BookmarkMaterial> BookmarkMaterialPage = bookmarkMaterialRepository.findAllByUser(user, PageRequest.of(page, 30));
        return BookmarkMaterialPage;
    }
}
