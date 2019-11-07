package edu.neu.khoury.cs5500.dijkstraspizza.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "price-calculators")
@Data
public class PriceCalculator {


  @Id
  private String id;
  private Double discountRatio;
  private Integer requiredPizzas;
  private Integer pizzasAppliedTo;
  private String name;

  public PriceCalculator() {
    this.requiredPizzas = 0;
    this.pizzasAppliedTo = -1;
    this.discountRatio = 0.0;
    this.name = "generic";
  }

  public PriceCalculator(Double discountRatio, String name) {
    this.requiredPizzas = 0;
    this.pizzasAppliedTo = -1;
    this.discountRatio = discountRatio;
    this.name = name;
  }

  public PriceCalculator(Integer requiredPizzas, Integer pizzasAppliedTo, Double discountRatio,
                         String name) {
    this.requiredPizzas = requiredPizzas;
    this.pizzasAppliedTo = pizzasAppliedTo;
    this.discountRatio = discountRatio;
    this.name = name;
  }

  private Double calculateBasePrice(List<Pizza> pizzas) {
    return pizzas.stream().mapToDouble(Pizza::getPrice).sum();
  }

  public Double calculate(List<Pizza> pizzas) {
    if (pizzas.size() < requiredPizzas) {
      return calculateBasePrice(pizzas);
    }
    if (pizzasAppliedTo < 0) { // -1 represents all pizzas
      double price = calculateBasePrice(pizzas);
      return price - price * discountRatio;
    }
    List<Pizza> sortedPizzas = new ArrayList<>(pizzas);
    sortedPizzas.sort(Pizza::compareTo);
    double price = 0;
    for (int i = 0; i < pizzasAppliedTo; i++) {
      price += sortedPizzas.get(i).getPrice() - discountRatio * sortedPizzas.get(i).getPrice();
    }
    for (int i = pizzasAppliedTo; i < pizzas.size(); i++) {
      price += sortedPizzas.get(i).getPrice();
    }
    return price;
  }
}