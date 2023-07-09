package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class PaginationTest {

    @Test
    public void unpagedTest() {
        Pageable pageable1 = Pagination.setPageable(null,null);
        assertTrue(pageable1.isUnpaged());
        Pageable pageable2 = Pagination.setPageable(1L,null);
        assertTrue(pageable2.isUnpaged());
        Pageable pageable3 = Pagination.setPageable(null,1L);
        assertTrue(pageable3.isUnpaged());
        Pageable pageable4 = Pagination.setPageable(1L,1L);
        assertTrue(pageable4.isPaged());
    }

    @Test
    public void exceptionTest() {
        assertThrows(ValidationException.class, () -> Pagination.setPageable(-1L,0L));
        assertThrows(ValidationException.class, () -> Pagination.setPageable(-1L,1L));
        assertThrows(ValidationException.class, () -> Pagination.setPageable(0L,0L));
        Pageable pageable = Pagination.setPageable(0L,1L);
        assertTrue(pageable.isPaged());
    }

    @Test
    public void workPageableTest() {
        Pageable pageable1 = Pagination.setPageable(1L,2L);
        assertTrue(pageable1.isPaged());
        assertEquals(pageable1.getPageNumber(),0);
        assertEquals(pageable1.getPageSize(),2);

        Pageable pageable2 = Pagination.setPageable(2L,2L);
        assertTrue(pageable2.isPaged());
        assertEquals(pageable2.getPageNumber(),1);
        assertEquals(pageable2.getPageSize(),2);
    }
}