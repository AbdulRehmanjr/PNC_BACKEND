package com.pnc.marketplace.database.join;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.join.AppJoin;

public interface AppJoinRepository  extends JpaRepository<AppJoin,Integer>{
    
}
