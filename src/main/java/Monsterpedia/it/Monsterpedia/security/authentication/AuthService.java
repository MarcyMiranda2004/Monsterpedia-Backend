package Monsterpedia.it.Monsterpedia.security.authentication;

import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.User;
import Monsterpedia.it.Monsterpedia.repository.UserRepository;
import Monsterpedia.it.Monsterpedia.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Utente con questa email/password non trovato"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new NotFoundException("Utente con questa email/password non trovato");
        }
        return jwtTool.createToken(user);
    }
}
