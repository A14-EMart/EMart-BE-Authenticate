package com.a14.emart.auth.service;

import com.a14.emart.auth.config.RabbitMQConfig;
import com.a14.emart.auth.model.User;
import com.a14.emart.auth.model.UserRole;
import com.a14.emart.auth.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listen(Long pengelolaId) {
        System.out.println(pengelolaId);
        User user = userRepository.findById(pengelolaId).orElse(null);
        if (user != null && user.getRole() != UserRole.MANAGER) {
            user.setRole(UserRole.MANAGER);
            userRepository.save(user);
        }
    }
}