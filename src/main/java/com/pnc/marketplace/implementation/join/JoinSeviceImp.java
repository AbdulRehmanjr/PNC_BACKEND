package com.pnc.marketplace.implementation.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.join.AppJoinRepository;
import com.pnc.marketplace.model.join.AppJoin;
import com.pnc.marketplace.service.join.JoinService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JoinSeviceImp implements JoinService{

    @Autowired
    private AppJoinRepository joinRepo;

    @Override
    public AppJoin create(AppJoin join) {
        
        AppJoin info = this.joinRepo.save(join);

        if(info!=null)
            return info;
        
        log.error("Error in making joining data.");
        return null;
    }

    @Override
    public AppJoin getInfo() {
        
        AppJoin info = this.joinRepo.findById(1).orElse(null);

        if(info!=null)
            return null;
        
        log.error("Error in fetching.");
        return info;
    }

    @Override
    public AppJoin updateJoin(int seller, int user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateJoin'");
    }
    
}
