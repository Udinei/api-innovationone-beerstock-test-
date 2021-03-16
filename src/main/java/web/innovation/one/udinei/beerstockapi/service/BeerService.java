package web.innovation.one.udinei.beerstockapi.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.innovation.one.udinei.beerstockapi.dto.BeerDTO;
import web.innovation.one.udinei.beerstockapi.entity.Beer;
import web.innovation.one.udinei.beerstockapi.exception.BeerAlreadyRegisteredException;
import web.innovation.one.udinei.beerstockapi.exception.BeerNotFoundException;
import web.innovation.one.udinei.beerstockapi.exception.BeerStockExceededException;
import web.innovation.one.udinei.beerstockapi.mapper.BeerMapper;
import web.innovation.one.udinei.beerstockapi.repository.BeerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);

    }

    public void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optSaveBeer = beerRepository.findByName(name);
        if(optSaveBeer.isPresent()){
            throw new BeerAlreadyRegisteredException(name);
        }
    }


    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name).orElseThrow(() -> new BeerNotFoundException(name));

        return beerMapper.toDTO(foundBeer);
    }


    public Beer verifyIfExists(long id) throws BeerNotFoundException {
        return beerRepository.findById(id).orElseThrow(() -> new BeerNotFoundException(id));
    }


    public List<BeerDTO> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());

    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    // adiciona ao estoque mais cervejas, que tenha o id informado
    public BeerDTO increment(Long id, int quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
        Beer beerToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();

        if(quantityAfterIncrement <= beerToIncrementStock.getMax()){
            beerToIncrementStock.setQuantity(quantityAfterIncrement);

            Beer incrementedBeerStock = beerRepository.save(beerToIncrementStock);
            return beerMapper.toDTO(incrementedBeerStock);
        }

        throw new BeerStockExceededException(id, quantityToIncrement);
    }



}
