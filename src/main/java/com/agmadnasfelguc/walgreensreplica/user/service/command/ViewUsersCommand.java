package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ViewUsersCommand extends Command{
    @Autowired
    private UserRepository userRepository;

    List<User> userInfo;

    @Override
    public void execute() {
        userInfo = userRepository.findAll();
    }
}
