package Monsterpedia.it.Monsterpedia.controller;

import Monsterpedia.it.Monsterpedia.dto.request.ChangeEmailDto;
import Monsterpedia.it.Monsterpedia.dto.request.ChangePasswordDto;
import Monsterpedia.it.Monsterpedia.dto.request.DeleteUserDto;
import Monsterpedia.it.Monsterpedia.dto.response.UserDto;
import Monsterpedia.it.Monsterpedia.exception.NotFoundException;
import Monsterpedia.it.Monsterpedia.exception.ValidationException;
import Monsterpedia.it.Monsterpedia.model.User;
import Monsterpedia.it.Monsterpedia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class Usercontroller {
    private UserService userService;
    public Usercontroller(UserService userService) { this.userService = userService; }

    @PostMapping
    public ResponseEntity<UserDto> createUser(UserDto userDto, BindingResult br) {
        if (br.hasErrors()) {
            String errorMessage = br.getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));
            throw new ValidationException(errorMessage);
        }
        UserDto savedUser = userService.saveUserDto(userDto);
        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/dto/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<UserDto> page = userService.searchUserDtos(query.trim(), pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping(path = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#id == atuhrentication.principal.id")
    public ResponseEntity<String> updateAvatar(@PathVariable long id, @RequestParam("file") MultipartFile file)
        throws NotFoundException, IOException {
        return ResponseEntity.ok(userService.updateUserAvatar(id, file));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN') or hasRole('CUSTOMER_SERVICE')")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDto changePasswordDto,
            BindingResult br
    ) throws NotFoundException {
        if (br.hasErrors()) throw new ValidationException(
                br.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining("; "))
        );
        userService.updateUserPassword(id, changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/email")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN') or hasRole('CUSTOMER_SERVICE')")
    public ResponseEntity<Void> changeEmail(
            @PathVariable Long id,
            @Valid @RequestBody ChangeEmailDto changeEmailDto,
            BindingResult br
    ) throws NotFoundException {
        if (br.hasErrors()) throw new ValidationException(
                br.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining("; "))
        );
        userService.updateUserEmail(id, changeEmailDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("#id == atuhrentication.principal.id")
    public ResponseEntity<Void> deleteUser(
            @PathVariable long id,
            @RequestBody @Valid DeleteUserDto deleteUserDto,
            BindingResult br
    ) throws NotFoundException {
        if (br.hasErrors()) throw  new ValidationException(
                br.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(";"))
        );
        userService.deleteUser(id, deleteUserDto.getPassword());
        return ResponseEntity.noContent().build();
    }
}
