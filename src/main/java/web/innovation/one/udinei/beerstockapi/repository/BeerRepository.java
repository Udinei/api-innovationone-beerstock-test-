package web.innovation.one.udinei.beerstockapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.innovation.one.udinei.beerstockapi.entity.Beer;

public interface BeerRepository extends JpaRepository<Beer, Long> {


}
