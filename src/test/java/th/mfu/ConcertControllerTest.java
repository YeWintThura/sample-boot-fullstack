package th.mfu;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import th.mfu.domain.Concert;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;



import static org.junit.jupiter.api.Assertions.*;

public class ConcertControllerTest {

    @Mock
    private ConcertRepository repository;

    @Mock
    private Model model;

    private ConcertController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
        controller = new ConcertController(repository);
    }

    @Test
    public void testListConcerts() {
        // Arrange
        List<Concert> concertList = new ArrayList<>();
        when(repository.findAll()).thenReturn(concertList);

        // Act
        String viewName = controller.listConcerts(model);

        // Assert
        assertEquals("list-concert", viewName);
        verify(model).addAttribute("concerts", concertList);
    }

    @Test
    public void testAddAConcertForm() {
        // Act
        String viewName = controller.addAConcertForm(model);

        // Assert
        assertEquals("add-concert-form", viewName);
    }

    @Test
    public void testSaveConcert() {
        // Arrange
        Concert concert = new Concert();

        // Act
        String viewName = controller.saveConcert(concert);

        // Assert
        assertEquals("redirect:/concerts", viewName);
        verify(repository).save(concert);
    }

    @Test
    public void testDeleteConcert() {
        // Arrange
        long concertId = 1L;

        // Act
        String viewName = controller.deleteConcert(concertId);

        // Assert
        assertEquals("redirect:/concerts", viewName);
        verify(repository).deleteById(concertId);
    }

    @Test
    public void testRemoveAllConcerts() {
        // Act
        String viewName = controller.removeAllConcerts();

        // Assert
        assertEquals("redirect:/concerts", viewName);
        verify(repository).deleteAll();
    }
}

