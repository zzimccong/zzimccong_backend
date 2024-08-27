package com.project.zzimccong.model.entity.zzim;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "TB_ZZIM")
public class Zzim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-zzims")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "corp_id")
    @JsonBackReference(value = "corp-zzims")
    private Corporation corporation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id" )
    @JsonBackReference(value = "restaurant-zzims")
    private Restaurant restaurant;

    @Column
    private String name;

    public Zzim() {}

    public Zzim(Long id, User user, Corporation corporation, Restaurant restaurant, String name) {
        this.id = id;
        this.user = user;
        this.corporation = corporation;
        this.restaurant = restaurant;
        this.name = name;


    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
