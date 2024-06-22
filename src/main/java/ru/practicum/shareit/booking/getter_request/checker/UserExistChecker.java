package ru.practicum.shareit.booking.getter_request.checker;

import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.getter_request.model.GetterRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.Constants;

@Setter
@AllArgsConstructor
public class UserExistChecker extends GetterRequestChecker {

    private UserRepository userRepository;

    @Override
    public void check(GetterRequest request) {
        long useId = request.getUserId();
        if (userRepository.findById(useId).isEmpty()) {
            throw new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + useId);
        }
        checkNext(request);
    }


}
