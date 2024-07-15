package shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    public final UserServiceClient client;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return client.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id,
                       @RequestBody UserDto userDto) {
        return client.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        client.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        return client.getById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.getAll();
    }
}
