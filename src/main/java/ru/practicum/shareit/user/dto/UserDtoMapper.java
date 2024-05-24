package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
public class UserDtoMapper {
    public UserDto toUserDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public User toUserFromDto(UserDto userDto) {
        return new User()
                .setName(userDto.getName())
                .setEmail(userDto.getEmail());
    }

    public User toUserFromDtoToUpdate(UserDto userDto, User user) {
        if (userDto != null) {
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null
                    && !userDto.getEmail().toLowerCase().equals(user.getEmail())) {
                user.setEmail(userDto.getEmail());
            }
        }
        return user;
    }
}
