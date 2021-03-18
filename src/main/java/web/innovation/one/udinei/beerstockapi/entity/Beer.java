package web.innovation.one.udinei.beerstockapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.innovation.one.udinei.beerstockapi.enums.BeerType;

import javax.persistence.*;

@Data // gera gets,sets e toString, equals e hashCode
@Entity  // realiza o mapeado jpa
@NoArgsConstructor // cria um construtor padrao vazio
@AllArgsConstructor // Gera um construtor com todos atributos
public class Beer {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   private String name;

   @Column(nullable = false)
   private String brand;

   @Column(nullable = false)
   private Integer max;

   @Column(nullable = false)
   private Integer quantity;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private BeerType type = BeerType.LAGER;

}
