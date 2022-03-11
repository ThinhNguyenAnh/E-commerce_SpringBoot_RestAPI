package com.app.ecommere.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "address")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;
    private String ward;
    private String district;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "address")
    private Set<User> users= new HashSet<>();

    public void add(User user){
        if(user != null){
            if(users==null){
                users = new HashSet<>();
            }
            users.add(user);
            user.setAddress(this);
        }

    }


}
