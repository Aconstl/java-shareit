package ru.practicum.shareit;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.ValidationException;

@UtilityClass
public class Pagination {

    public static Pageable setPageable(Long from, Long size) {
        if (from == null || size == null) {
            return Pageable.unpaged();
        } else if (from >= 0 && size > 0) {
            return PageRequest.of(from.intValue(), size.intValue());
        } else {
            throw new ValidationException("Неккоректное количество запрашиваемых значений");
        }
    }
}
