package web.innovation.one.udinei.beerstockapi.controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import web.innovation.one.udinei.beerstockapi.dto.BeerDTO;
import web.innovation.one.udinei.beerstockapi.dto.QuantityDTO;
import web.innovation.one.udinei.beerstockapi.exception.BeerAlreadyRegisteredException;
import web.innovation.one.udinei.beerstockapi.exception.BeerInsufficientStockException;
import web.innovation.one.udinei.beerstockapi.exception.BeerNotFoundException;
import web.innovation.one.udinei.beerstockapi.exception.BeerStockExceededException;
import web.innovation.one.udinei.beerstockapi.service.BeerService;

import javax.validation.Valid;
import java.util.List;

@RestController   // classe voltada a api rest
@RequestMapping("/api/v1/beers")  // url base da REST API
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController {

    // injetando beerService - spring
    private final BeerService beerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        return beerService.createBeer(beerDTO);
    }


    @GetMapping("/{name}")
    public BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException {
        return beerService.findByName(name);
    }


    @GetMapping
    public List<BeerDTO> listBeers(){
        return beerService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededException {
        return beerService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public BeerDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerInsufficientStockException {
        return beerService.decrement(id, quantityDTO.getQuantity());
    }

}
