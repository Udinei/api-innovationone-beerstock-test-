package web.innovation.one.udinei.beerstockapi.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.innovation.one.udinei.beerstockapi.dto.BeerDTO;
import web.innovation.one.udinei.beerstockapi.enums.BeerType;
import web.innovation.one.udinei.beerstockapi.mapper.BeerMapper;
import web.innovation.one.udinei.beerstockapi.repository.BeerRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) {
        BeerDTO dto = new BeerDTO();
                dto.setId(1L);
                dto.setName("Brahma");
                dto.setBrand("Ambev");
                dto.setMax(50);
                dto.setQuantity(10);
                dto.setType(BeerType.LAGER);

           return dto;
    }
}
