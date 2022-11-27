package io.tech.AdvancedSpringBootSecurityWithJwt.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Roles;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Users;
import io.tech.AdvancedSpringBootSecurityWithJwt.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

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

    @GetMapping("/token/refresh")
    public void RefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("SECRET".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                Users  user= userService.getUserByEMail(email);
                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() +1*60*1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles" , user.getRoles().stream().map(Roles::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String , String> tokens = new HashMap<>();
                tokens.put("access_token" , accessToken);
                tokens.put("refresh_token" , refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream() , tokens);
            }catch (Exception e){
                response.setHeader("error" , e.getMessage());
                if(e.getMessage().toString().contains("expired")){
                    response.setStatus(REQUEST_TIMEOUT.value());
                    Map<String , String> error = new HashMap<>();
                    error.put("error" , e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream() , error);
                }else{
                    response.setStatus(FORBIDDEN.value());
                    Map<String , String> error = new HashMap<>();
                    error.put("error" , e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream() , error);
                }

            }
        }else{
         throw new RuntimeException("AUTH TOKEN IS MISSING");
        }
    }


}
@Data
class RoleToUserForm{
    private String userName;
    private String roleName;

}