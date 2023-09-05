package com.juaracoding.TA.repo;

import com.juaracoding.TA.model.UserToAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserToAccessRepo  extends JpaRepository<UserToAccess,Long> {
}
