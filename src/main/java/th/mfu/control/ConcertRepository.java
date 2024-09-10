package th.mfu.control;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import th.mfu.domain.Concert;


public interface ConcertRepository extends CrudRepository<Concert, Long> {

    public List<Concert> findAll();
      
}