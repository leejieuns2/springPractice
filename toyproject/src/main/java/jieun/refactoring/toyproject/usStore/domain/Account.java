package jieun.refactoring.toyproject.usStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    /* Private Fields */
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private String userId;
    private String username;
    private String password;
    private String email;
    private String city;
    private String phone;
    private String university;
}
