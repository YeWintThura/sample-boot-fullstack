package th.mfu;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SaleOrderRepository extends CrudRepository<SaleOrder, Long> {
    public List<SaleOrder> findAll();

    public List<SaleOrder> findByCustomerId(Long customerId);

}
