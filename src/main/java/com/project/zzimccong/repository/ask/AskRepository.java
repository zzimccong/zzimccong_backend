package com.project.zzimccong.repository.ask;

import com.project.zzimccong.model.entity.ask.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AskRepository extends JpaRepository<Ask, Integer> {
    List<Ask> findByCorporation_Id(Integer corpId);
    List<Ask> findByUser_Id(Integer userId);
}
