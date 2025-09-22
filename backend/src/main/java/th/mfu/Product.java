package th.mfu;

import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private double price;

    // @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    // private List<SaleOrder> saleOrders;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    // public List<SaleOrder> getSaleOrders() {
    //     return saleOrders;
    // }
    // public void setSaleOrders(List<SaleOrder> saleOrders) {
    //     this.saleOrders = saleOrders;
    // }

    
}
