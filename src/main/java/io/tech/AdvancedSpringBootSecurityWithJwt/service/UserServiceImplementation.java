package io.tech.AdvancedSpringBootSecurityWithJwt.service;

import io.tech.AdvancedSpringBootSecurityWithJwt.Repo.RoleRepo;
import io.tech.AdvancedSpringBootSecurityWithJwt.Repo.UserRepo;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Roles;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email);
        if(user == null){
            log.error("USER NOT FOUND {}" , email);
            throw new UsernameNotFoundException("USER NOT FOUND " + email  );
        }else{
            log.info("USER FOUND {}" , email);
        }

        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        user.getRoles().forEach( role ->
        {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(user.getEmail() , user.getPassword() , simpleGrantedAuthorities);
    }
    @Override
    public Users SaveUser(Users users) {
        log.info("SAVING NEW USER {} " , users.getEmail());
        return userRepo.save(users);
    }

    @Override
    public Roles SaveRole(Roles role) {
        log.info("SAVING NEW Role {} " , role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void AddRoleToUsers(String email, String roleName) {
        log.info("Adding Role {} to user {} " , roleName , email);
        Users users = userRepo.findByEmail(email);
        Roles roles = roleRepo.findByName(roleName);
        users.getRoles().add(roles);
    }

    @Override
    public Users getUserByEMail(String email) {
        log.info("Fetching user {}" , email);
        return userRepo.findByEmail(email);
    }

    @Override
    public List<Users> GetUsers() {
        log.info("Fetching all Users ");
        return userRepo.findAll();
    }


}
