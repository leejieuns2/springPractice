package jieun.refactoring.toyproject.usStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    private int itemId; // (PK) String- > int
    private int unitCost;
    private String title;
    private String description;
    private int viewCount;
    private int qty;
    private String userId; // (FK)
    private int productId; // (FK)
    private String imgUrl;
    private int listPrice;
}
