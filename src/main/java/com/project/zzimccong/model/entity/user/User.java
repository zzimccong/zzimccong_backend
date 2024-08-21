package com.project.zzimccong.model.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.zzimccong.model.entity.cart.Cart;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.store.Restaurant;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-restaurants")
    private List<Restaurant> restaurants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-reservations")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-lists")
    private List<Cart> restaurantLists;


    public User() {
    }

    public User(Integer id, String loginId, String password, String name, LocalDate birth, String email, String phone, String role, List<Restaurant> restaurants, List<Reservation> reservations, List<Cart> restaurantLists) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.restaurants = restaurants;
        this.reservations = reservations;
//        this.restaurantLists = restaurantLists;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

//    public List<Cart> getRestaurantLists() {
//        return restaurantLists;
//    }
//
//    public void setRestaurantLists(List<Cart> restaurantLists) {
//        this.restaurantLists = restaurantLists;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", restaurants=" + restaurants +
                ", reservations=" + reservations +
                '}';
    }
}