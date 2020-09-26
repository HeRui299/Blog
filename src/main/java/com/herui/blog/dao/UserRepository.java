package com.herui.blog.dao;

import com.herui.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
