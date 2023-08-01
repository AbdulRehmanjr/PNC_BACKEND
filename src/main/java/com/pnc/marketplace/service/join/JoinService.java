package com.pnc.marketplace.service.join;

import com.pnc.marketplace.model.join.AppJoin;

public interface JoinService {

    AppJoin create(AppJoin join);

    AppJoin getInfo();
    
    AppJoin updateJoin(int seller,int user);
}   
