package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentDtoMapper {

    public Comment toCommentFromDto(CommentDto commentDto) {
        return new Comment().setText(commentDto.getText());
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto();
    }
}
