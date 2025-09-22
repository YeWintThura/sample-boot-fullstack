package th.mfu;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepo;

    // GET for a product
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        if (!productRepo.existsById(id))
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        Optional<Product> product = productRepo.findById(id);
        return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
    }

    // Get all products
    @GetMapping("/products")
    public ResponseEntity<Collection> getAllProducts() {
        return new ResponseEntity<Collection>(productRepo.findAll(), HttpStatus.OK);
    }

    // POST for creating a product
    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        productRepo.save(product);
        return new ResponseEntity<String>("Product created", HttpStatus.CREATED);
    }

    // DELETE for deleting a product by id
    @DeleteMapping("products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return new ResponseEntity<String>("Product deleted", HttpStatus.NO_CONTENT);
    }

}
