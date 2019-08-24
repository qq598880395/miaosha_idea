package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.UserDAO;
import com.imooc.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.interfaces.PBEKey;
import java.security.PublicKey;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public User getById(int id){
        return userDAO.getById(id);
    }
}
