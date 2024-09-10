package th.mfu.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import th.mfu.domain.Concert;
import th.mfu.domain.Seat;

@RestController
public class ConcertController {
    @Autowired
    ConcertRepository concertRepo;

     // TODO: define repository for concert with @Autowired
    @Autowired
    SeatRepository seatRepo;


    public ConcertController(ConcertRepository repository, SeatRepository seatRepository) {
        this.concertRepo = repository;
        this.seatRepo = seatRepository;
    }

    @GetMapping("/concerts")
    public ResponseEntity<Collection<Concert>> listConcerts() {
        // TODO: return 200 OK with list of concert
        return new ResponseEntity<Collection<Concert>>(concertRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("/concerts")
    public ResponseEntity<String> saveConcert(@RequestBody Concert newconcert) {
        // TODO: add concert to DB
        Concert concert = concertRepo.save(newconcert);
        // TODO: redirect to list concerts
        return new ResponseEntity<>("concert saved", HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping("/concerts/{id}")
    public ResponseEntity<String> deleteConcert(@PathVariable long id) {
        //TODO: check if concert id exists
        if(concertRepo.findById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //TODO: delete related seats
        seatRepo.deleteByConcertId(id);
        // TODO: delete concert from DB
        concertRepo.deleteById(id);
        // TODO: redirect to list concerts
        return new ResponseEntity<>("concert deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/concerts/{concertId}/seats")
    public ResponseEntity<Collection<Seat>> listSeats(@PathVariable Long concertId) {
        //TODO: check if concert id exists
        if (concertRepo.findById(concertId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Seat>>(seatRepo.findByConcertId(concertId), HttpStatus.OK);
    }

    @PostMapping("/concerts/{concertId}/seats")
    public ResponseEntity<String> saveSeat(@RequestBody Seat newseat, @PathVariable Long concertId) {
        //TODO: check if concert id exists
        if (concertRepo.findById(concertId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //TODO: find concert by id
        Concert concert = concertRepo.findById(concertId).get();
        //TODO: set concert to the new seat
        newseat.setConcert(concert);
        //TODO: save new seat
        Seat seat = seatRepo.save(newseat);
        //TODO: redict to /concerts/id/seats where id is concert id
        return new ResponseEntity<>("seat saved", HttpStatus.CREATED);
    }

    @PostMapping("/concerts/{concertId}/seats/{seatId}/book")
    public ResponseEntity<String> bookSeat(@PathVariable Long concertId, @PathVariable Long seatId, @RequestBody Seat reqseat) {
        //TODO: check if concert id exists
        if (concertRepo.findById(concertId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //TODO: find seat and set booked to true
        Seat seat = seatRepo.findById(seatId).get();
        seat.setAttendee(reqseat.getAttendee());
        seat.setBooked(true);
        seatRepo.save(seat);
        return new ResponseEntity<>("seat booked", HttpStatus.OK);
    }


}
