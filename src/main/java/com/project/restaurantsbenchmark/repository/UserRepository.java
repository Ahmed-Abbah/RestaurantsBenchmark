package com.project.restaurantsbenchmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.restaurantsbenchmark.model.User ;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
