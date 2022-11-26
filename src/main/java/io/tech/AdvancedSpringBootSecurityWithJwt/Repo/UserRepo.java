package io.tech.AdvancedSpringBootSecurityWithJwt.Repo;

import io.tech.AdvancedSpringBootSecurityWithJwt.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users , Long> {
    Users findByEmail(String email);
}
