package com.nagra.nagrareads.service;

import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.repository.NagraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NagraUserService {

    @Autowired
    private NagraUserRepository nagraUserRepository;

    public NagraUser save(NagraUser nagraUser) {
        return nagraUserRepository.save(nagraUser);
    }

}
