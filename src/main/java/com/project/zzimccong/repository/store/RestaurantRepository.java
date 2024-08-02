package com.project.zzimccong.repository.store;


import com.project.zzimccong.model.entity.store.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    //query method 정의

    //public List<Store> findByStore(String keyword);

//    //가게 이름으로 찾기
//    public List<Store> findByNameContains(String Name);
//
//    //주소로 찾기
//    public List<Store> findByAddressContains(String Address);
//
//    //메뉴로 찾기
//    public List<Store> findByMenuContains(String Menu);
}
