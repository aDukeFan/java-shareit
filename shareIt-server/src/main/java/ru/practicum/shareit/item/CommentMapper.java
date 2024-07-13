package ru.practicum.shareit.item;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.CommentDtoOutcome;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toSave(CommentDtoIncome commentDtoIncome);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "created", source = "createdTime")
    CommentDtoOutcome toSend(Comment comment);
}
