package web.innovation.one.udinei.beerstockapi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerInsufficientStockException extends Exception {

    public BeerInsufficientStockException(Long id, Integer quantityToDecrement, Integer qtdEstoque) {
       super(String.format("Cerveja com ID: %s Estoque insuficiente para remover: %s. Estoque = %s", id, quantityToDecrement, qtdEstoque));
    }
}
