package Monsterpedia.it.Monsterpedia.security;

import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.User;
import Monsterpedia.it.Monsterpedia.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {
    @Value("${jwt.duration}")
    private long duration;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserService userService;

    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duration))
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public void validateToken(String token) {
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parse(token);
    }

    public User getUserFromToken(String token) throws NotFoundException {
        int id = Integer.parseInt(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser((long) id);
    }

    public String getSecretKey() {
        return secretKey;
    }
}
