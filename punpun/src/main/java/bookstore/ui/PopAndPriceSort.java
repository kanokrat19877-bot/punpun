package bookstore.ui;

import bookstore.model.Book;
import bookstore.service.BookService;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.function.Consumer;

public class PopAndPriceSort {

    private BookService bookService;

    public PopAndPriceSort(BookService bookService){
        this.bookService = bookService;
    }

    public HBox createSortBox(Consumer<List<Book>> onSort){

        HBox root = new HBox();
        root.setPadding(new Insets(10));

        ComboBox<String> sortMenu = new ComboBox<>();

        sortMenu.getItems().addAll(
                "ราคา: ต่ำ → สูง",
                "ราคา: สูง → ต่ำ",
                "เรตติ้ง: ต่ำ → สูง",
                "เรตติ้ง: สูง → ต่ำ"
        );

        sortMenu.setPromptText("ตัวกรอง");

        sortMenu.setOnAction(e -> {

            String selected = sortMenu.getValue();

            List<Book> sorted = null;

            switch (selected){

                case "ราคา: ต่ำ → สูง":
                    sorted = bookService.sortByPriceAsc();
                    break;

                case "ราคา: สูง → ต่ำ":
                    sorted = bookService.sortByPriceDesc();
                    break;

                case "เรตติ้ง: ต่ำ → สูง":
                    sorted = bookService.sortByRatingAsc();
                    break;

                case "เรตติ้ง: สูง → ต่ำ":
                    sorted = bookService.sortByRating();
                    break;
            }

            if(sorted != null){
                onSort.accept(sorted);
            }

        });

        root.getChildren().add(sortMenu);

        return root;
    }
}