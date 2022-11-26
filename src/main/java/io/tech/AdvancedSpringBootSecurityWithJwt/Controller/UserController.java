package io.tech.AdvancedSpringBootSecurityWithJwt.Controller;

import io.tech.AdvancedSpringBootSecurityWithJwt.models.Roles;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Users;
import io.tech.AdvancedSpringBootSecurityWithJwt.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers(){
        return ResponseEntity.ok(userService.GetUsers());
    }

    @PostMapping("/users/save")
    public ResponseEntity<Users> saveUser(@RequestBody Users users){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/save").toUriString());
        return ResponseEntity.created(uri).body(userService.SaveUser(users));
    }

    @PostMapping("/roles/save")
    public ResponseEntity<Roles> saveRole(@RequestBody Roles roles){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/save").toUriString());
        return ResponseEntity.created(uri).body(userService.SaveRole(roles));
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> RoleToUser(@RequestBody RoleToUserForm roleToUserForm){
     userService.AddRoleToUsers(roleToUserForm.getUserName() , roleToUserForm.getRoleName());
     return ResponseEntity.ok().build();
    }
}
@Data
class RoleToUserForm{
    private String userName;
    private String roleName;

}