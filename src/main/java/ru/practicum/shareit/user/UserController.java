package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id,
                       @RequestBody UserDto userDto) {
        return service.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        return service.get(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll();
    }
}
