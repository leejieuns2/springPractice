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
public class Tag {

    @Id
    @GeneratedValue
    private int tagId;          // Sequence
    private int itemId;       // (FK) itemId
    private String tagName;
}
