# api-innovationone-beerstock-test-
Teste unitário de uma API REST de gerenciamento de estoques de cerveja.

### Requisitos:
- Java 15
- Maven 3.6.3
- Spring Boot 2.4.3
  
  Starters:
  - web
  - data-jpa
  - validation
  - devtools
  - test
    
  Dependências:  
  - mapstruct 1.4.1.Final
  - lombok
    
- Banco de dados:
  H2
  
Nota: para mais detalhes ver arquivo pom.xml do projeto  


### Frameworks de testes utilizados
- JUnit, Mockito e Hamcrest.

### Funcionalidades testadas
- Criação de cervejas
- Validação para criação de cervejas com campos nulos
- Exclusão de cervejas existente e inexitentes
- Listagem (buscas por id e por nome) e listagem total
- Validação de listagem com nomes inexitentes
- Remoção de cervejas com id válido e inválido
- Validação de incremento de estoque maior que o maximo permitido
- Validação de incremento de cervejas com id válido e inválido
- Validação de incremento de cervejas do estoque
- Validação de decremento de estoque menor que zero
- Validação de decremento de cervejas do estoque com id valido e inválido

### Estruturas de pastas:
### projeto:
- controller
- dto
- entity
- enums
- exception
- mapper
- repository
- service
- utils

### teste:
- builder
- controller
- mapper
- service

### Executando o projeto no terminal
<pre>mvn spring-boot:run</pre>

### Executando os testes 
<pre>mvn clean test</pre>

### Executando o projeto no browser
<pre>http://localhost:8080/api/v1/beers</pre>

### Link do projeto original
<a href="https://github.com/rpeleias/beer_api_digital_innovation_one">Projeto desenvolvido por Rodrigo Peleias </a> 
