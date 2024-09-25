package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@Route("/main2")
public class MainView2 extends VerticalLayout {

    private List<Product> products = List.of(
            new Product("Product 1", 10.0),
            new Product("Product 2", 15.0),
            new Product("Product 3", 20.0)
    );

    private List<Product> cart = new ArrayList<>();
    private Grid<Product> cartGrid = new Grid<>(Product.class);

    public MainView2() {
        Grid<Product> productGrid = new Grid<>(Product.class);
        productGrid.setItems(products);

        productGrid.addComponentColumn(product -> {
            Button addButton = new Button("Add to Cart", event -> {
                addToCart(product);
            });
            return addButton;
        }).setHeader("Actions");

        cartGrid.setItems(cart);
        cartGrid.setColumns("name", "price");

        add(productGrid, cartGrid);
    }

    private void addToCart(Product product) {
        cart.add(product);
        cartGrid.setItems(cart);
    }

    public static class Product {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
}
