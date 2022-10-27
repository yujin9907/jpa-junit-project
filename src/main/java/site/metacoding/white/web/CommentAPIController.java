package site.metacoding.white.web;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.dto.ResponseDto;
import site.metacoding.white.dto.SessionUser;
import site.metacoding.white.dto.CommentReqDto.CommentSaveReqDto;
import site.metacoding.white.service.CommentService;

@RequiredArgsConstructor
@RestController
public class CommentAPIController {

    private final CommentService commentService;
    private final HttpSession session;

    @PostMapping("/comment")
    public ResponseDto<?> saveComment(@RequestBody CommentSaveReqDto commentSaveReqDto) {
        SessionUser user = (SessionUser) session.getAttribute("sessionUser");
        commentSaveReqDto.setSessionUser(user);
        return new ResponseDto<>(1, "성공", commentService.save(commentSaveReqDto));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseDto<?> deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
        return new ResponseDto<>(null, null, null);
    }
}