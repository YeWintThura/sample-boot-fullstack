package th.mfu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import th.mfu.domain.Concert;
import th.mfu.domain.Seat;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;

public class ConcertControllerTest {

    @InjectMocks
    private ConcertController concertController;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListConcerts() {
        List<Concert> concertList = new ArrayList<>();
        // Add sample concerts to the list

        when(concertRepository.findAll()).thenReturn(concertList);

        String viewName = concertController.listConcerts(model);

        assert(viewName.equals("list-concert"));
        verify(concertRepository, times(1)).findAll();
    }

    @Test
    public void testAddAConcertForm() {
        String viewName = concertController.addAConcertForm(model);

        assert(viewName.equals("add-concert-form"));
    }

    @Test
    public void testSaveConcert() {
        Concert newConcert = new Concert();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = concertController.saveConcert(newConcert);

        assert(viewName.equals("redirect:/concerts"));
        verify(concertRepository, times(1)).save(newConcert);
    }

    @Test
    public void testDeleteConcert() {
        long concertId = 1001L;

        String viewName = concertController.deleteConcert(concertId);

        assert(viewName.equals("redirect:/concerts"));
        verify(seatRepository, times(1)).deleteByConcertId(concertId);
        verify(concertRepository, times(1)).deleteById(concertId);
    }

      @Test
    public void testSaveSeat() {
        long concertId = 1L;
        Concert concert = new Concert();
        concert.setId(concertId);

        Seat newSeat = new Seat();
        newSeat.setConcert(concert);

        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));
        when(seatRepository.save(newSeat)).thenReturn(newSeat);

        String viewName = concertController.saveSeat(newSeat, concertId);

        assert(viewName.equals("redirect:/concerts/" + concertId + "/seats"));
        verify(concertRepository, times(1)).findById(concertId);
        verify(seatRepository, times(1)).save(newSeat);
    }
}


