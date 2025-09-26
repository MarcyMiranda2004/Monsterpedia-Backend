package Monsterpedia.it.Monsterpedia.service;

import Monsterpedia.it.Monsterpedia.dto.request.ChangeEmailDto;
import Monsterpedia.it.Monsterpedia.dto.request.ChangePasswordDto;
import Monsterpedia.it.Monsterpedia.dto.request.UpdateUserDto;
import Monsterpedia.it.Monsterpedia.dto.response.UserDto;
import Monsterpedia.it.Monsterpedia.enumerated.Role;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.model.FavoriteList;
import Monsterpedia.it.Monsterpedia.model.TasteList;
import Monsterpedia.it.Monsterpedia.model.User;
import Monsterpedia.it.Monsterpedia.repository.FavoriteListRepository;
import Monsterpedia.it.Monsterpedia.repository.TasteListRepository;
import Monsterpedia.it.Monsterpedia.repository.UserRepository;
import Monsterpedia.it.Monsterpedia.service.notification.EmailService;
import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private FavoriteListRepository favoriteListRepository;
    @Autowired private TasteListRepository tasteListRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;
    @Autowired private Cloudinary cloudinary;

    public User saveUser(UserDto userDto) {
        User u = new User();
        u.setUsername(userDto.getUsername());
        u.setEmail(userDto.getEmail());
        u.setPassword(passwordEncoder.encode(userDto.getPassword()));
        u.setAvatarUrl("https://ui-avatars.com/api/?name=" +
                u.getUsername().charAt(0) + "+" + u.getUsername().charAt(1));
        u.setRole(Role.USER);

        FavoriteList fl = new FavoriteList();
        fl.setUser(u);
        u.setFavoriteList(fl);

        TasteList ts = new TasteList();
        ts.setUser(u);
        u.setTasteList(ts);

        User saved = userRepository.save(u);
        emailService.sendRegistrationConfirmation(saved);
        return saved;
    }

    private UserDto toDto(User u) {
        UserDto userDto = new UserDto();
        userDto.setId(u.getId());
        userDto.setUsername(u.getUsername());
        userDto.setEmail(u.getEmail());
        userDto.setAvatarUrl(u.getAvatarUrl());
        userDto.setRole(u.getRole());
        return userDto;
    }

    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User con email " + email + " non trovato"));
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUser(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " non trovato"));
    }

    public UserDto saveUserDto(UserDto userDto) {
        return toDto(saveUser(userDto));
    }

    public List<UserDto> getAllUserDto() {
        return getAllUser().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDto getUserDto(Long id) throws NotFoundException {
        return toDto(getUser(id));
    }

    public UserDto updateUserDto(Long id, UpdateUserDto updateUserDto) throws NotFoundException {
        return toDto(updateUser(id, updateUserDto));
    }

    public Page<UserDto> searchUserDtos(String query, Pageable pageable) {
        Page<User> page = userRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, pageable);
        return page.map(this::toDto);
    }

    public TasteList getTasteList(User user) {
        return tasteListRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    TasteList tl = new TasteList();
                    tl.setUser(user);
                    return tasteListRepository.save(tl);
                });
    }

    public User updateUser(Long id, UpdateUserDto updateUserDto) throws NotFoundException {
        User u = getUser(id);
        u.setUsername(updateUserDto.getUsername());
        return userRepository.save(u);
    }

    public String updateUserAvatar(Long id, MultipartFile file) throws NotFoundException, IOException {
        User u = getUser(id);
        String url = (String) cloudinary.uploader()
                .upload(file.getBytes(), Collections.emptyMap())
                .get("url");
        u.setAvatarUrl(url);
        userRepository.save(u);
        return url;
    }

    @Transactional
    public User updateUserEmail(Long id, ChangeEmailDto changeEmailDto) throws NotFoundException {
        User u = getUser(id);
        if (!passwordEncoder.matches(changeEmailDto.getPassword(), u.getPassword()))
            throw new BadCredentialsException("La password non corrisponde");
        if (!u.getEmail().equals(changeEmailDto.getCurrentEmail()))
            throw new BadCredentialsException("Inserisci l'email corrente corretta");
        if (!changeEmailDto.getNewEmail().equals(changeEmailDto.getConfirmNewEmail()))
            throw new BadCredentialsException("La nuova email e la conferma non corrispondono");
        u.setEmail(changeEmailDto.getNewEmail());
        userRepository.save(u);
        emailService.sendEmailChangeConfirmation(u, changeEmailDto.getNewEmail(), changeEmailDto.getCurrentEmail());
        return u;
    }

    @Transactional
    public User updateUserPassword(Long id, ChangePasswordDto changePasswordDto) throws NotFoundException {
        User u = getUser(id);
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), u.getPassword()))
            throw new BadCredentialsException("La vecchia password non corrisponde");
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword()))
            throw new BadCredentialsException("La nuova password e la conferma non corrispondono");
        u.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(u);
        emailService.sendPasswordChangedNotice(u);
        return u;
    }

    @Transactional
    public void deleteUser(Long id, String rawPassword) throws NotFoundException {
        User u = getUser(id);
        if (!passwordEncoder.matches(rawPassword, u.getPassword()))
            throw new BadCredentialsException("Password non corretta");
        userRepository.deleteById(id);
        emailService.sendDeleteAccountNotice(u, "Account eliminato");
    }
}
