package th.mfu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import th.mfu.control.ConcertController;
import th.mfu.control.ConcertRepository;
import th.mfu.control.SeatRepository;
import th.mfu.domain.Concert;
import th.mfu.domain.Seat;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<Collection<Concert>> response = concertController.listConcerts();

        // assert if size is 5
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        
    }

    @Test
    public void testSaveConcert() {
        Concert newConcert = new Concert();
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<String> response = concertController.saveConcert(newConcert);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    public void testDeleteConcert() {
        //add new concert
        Concert newConcert = new Concert();
        concertController.saveConcert(newConcert);

        ResponseEntity<Collection<Concert>> response = concertController.listConcerts();

        response.getBody().forEach(concert -> {
            concertController.deleteConcert(concert.getId());
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(seatRepository, times(1)).deleteByConcertId(concert.getId());
        });
           
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

        ResponseEntity<String> response = concertController.saveSeat(newSeat, concertId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        verify(seatRepository, times(1)).save(newSeat);
    }

    @Test
    public void testBookSeat() {
        long concertId = 1L;
        long seatId = 1L;
        Seat seat = new Seat();
        seat.setId(seatId);
        seat.setBooked(false); // Initially not booked

        Seat reqSeat = new Seat();
        reqSeat.setAttendee("John Doe");

        when(concertRepository.findById(concertId)).thenReturn(Optional.of(new Concert()));
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        when(seatRepository.save(seat)).thenReturn(seat); // Return the updated seat

        ResponseEntity<String> response = concertController.bookSeat(concertId, seatId, reqSeat);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(seat.isBooked()); // Verify that the seat is now booked
        assertEquals("John Doe", seat.getAttendee()); // Verify attendee name

    }
}


