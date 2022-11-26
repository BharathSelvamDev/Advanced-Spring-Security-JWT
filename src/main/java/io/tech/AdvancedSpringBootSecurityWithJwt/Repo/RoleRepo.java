package io.tech.AdvancedSpringBootSecurityWithJwt.Repo;

import io.tech.AdvancedSpringBootSecurityWithJwt.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}
