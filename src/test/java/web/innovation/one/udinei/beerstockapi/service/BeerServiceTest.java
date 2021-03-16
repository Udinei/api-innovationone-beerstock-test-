package web.innovation.one.udinei.beerstockapi.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.innovation.one.udinei.beerstockapi.builder.BeerDTOBuilder;
import web.innovation.one.udinei.beerstockapi.dto.BeerDTO;
import web.innovation.one.udinei.beerstockapi.entity.Beer;
import web.innovation.one.udinei.beerstockapi.exception.BeerAlreadyRegisteredException;
import web.innovation.one.udinei.beerstockapi.exception.BeerNotFoundException;
import web.innovation.one.udinei.beerstockapi.exception.BeerStockExceededException;
import web.innovation.one.udinei.beerstockapi.mapper.BeerMapper;
import web.innovation.one.udinei.beerstockapi.repository.BeerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {


    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 5L;

    @Mock
    private BeerRepository beerRepository;

    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    private BeerDTO getBeerDTO() {
        return  BeerDTOBuilder.builder().build().toBeerDTO();
    }

    private Beer getBeerModel(BeerDTO beerDTO){
        return beerMapper.toModel(beerDTO);
    }


    @Test
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        //given
        BeerDTO expectedBeerDTO = getBeerDTO();
        Beer expectedSavedBeer = getBeerModel(expectedBeerDTO);

        // when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        // then
        BeerDTO createdBeerDTO = beerService.createBeer(expectedBeerDTO);

        assertThat(createdBeerDTO.getId(), is(equalTo(expectedBeerDTO.getId())));
        assertThat(createdBeerDTO.getName(), is(equalTo(expectedBeerDTO.getName())));
        assertThat(createdBeerDTO.getQuantity(), is(equalTo(expectedBeerDTO.getQuantity())));

    }

    // ira lancar uma excetion caso a beer já exista no BD
    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown(){
        // given
        Beer duplicatedBeer = beerMapper.toModel(getBeerDTO()); // obtem o BeerDTO, converte para Beer

        //when
        when(beerRepository.findByName(duplicatedBeer.getName())).thenReturn(Optional.of(duplicatedBeer));

        // then
        //assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO));
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.verifyIfIsAlreadyRegistered(duplicatedBeer.getName()));

    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
          // given
          BeerDTO expectedFoundBeerDTO = getBeerDTO();
          Beer expectedFoundBeer = getBeerModel(expectedFoundBeerDTO);

          //when
           when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.of(expectedFoundBeer));

           // then
         BeerDTO foundeBeerDTO = beerService.findByName(expectedFoundBeer.getName());
         assertThat(foundeBeerDTO, is(equalTo(expectedFoundBeerDTO)));
    }


    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException(){
        // given
        BeerDTO expectedFoundBeerDTO = getBeerDTO();

        // when
        when(beerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedFoundBeerDTO.getName()));
    }

    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers(){
        // given
        BeerDTO expectedFoundBeerDTO = getBeerDTO();
        Beer expectedFoundBeer = getBeerModel(expectedFoundBeerDTO);

        //when
        when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));

        // then
        List<BeerDTO> foundListBeersDTO = beerService.listAll();

        assertThat(foundListBeersDTO, is(not(empty())));
        assertThat(foundListBeersDTO.get(0), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers(){
        // when
        when(beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<BeerDTO> foundListBeersDTO = beerService.listAll();

        assertThat(foundListBeersDTO, is(empty()));
    }


    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        // given
        BeerDTO expectedDeleteBeerDTO = getBeerDTO();
        Beer expectedDeleteBeer = getBeerModel(expectedDeleteBeerDTO);

        //when
        when(beerRepository.findById(expectedDeleteBeerDTO.getId())).thenReturn(Optional.of(expectedDeleteBeer));
        doNothing().when(beerRepository).deleteById(expectedDeleteBeerDTO.getId());

        // then
        beerService.deleteById(expectedDeleteBeerDTO.getId());

        // verifica se o metodo foi chamado ao menos uma vez

        verify(beerRepository, times(1)).findById(expectedDeleteBeerDTO.getId());
        verify(beerRepository, times(1)).deleteById(expectedDeleteBeerDTO.getId());

    }


    @Test
    void whenGivenIdBeerIsNotExistThenThrownBeerNotFoundException(){
        //given - INVALID_BEER_ID

        //when
        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.verifyIfExists(INVALID_BEER_ID));

    }

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        // given
        BeerDTO expectedBeerDTO = getBeerDTO();
        Beer expectedBeer = getBeerModel(expectedBeerDTO);

        // when
        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;

        // then
        BeerDTO incrementBeerDTO = beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException(){
        // given
        BeerDTO expectedBeerDTO = getBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 80;
        assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(),quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException(){
        BeerDTO expectedBeerDTO = getBeerDTO();
        Beer expectedBeer = getBeerModel(expectedBeerDTO);

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 80;
        assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }


    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException(){
        int quantityToIncrement = 10;

        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerService.increment(INVALID_BEER_ID, quantityToIncrement));

    }



}
