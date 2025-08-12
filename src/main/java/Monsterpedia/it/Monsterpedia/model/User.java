package Monsterpedia.it.Monsterpedia.model;

import Monsterpedia.it.Monsterpedia.enumerated.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User implements UserDetails {
    @EqualsAndHashCode.Include
    @Id @GeneratedValue (strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NotBlank
    private String password;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private FavoriteList favoriteList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private TasteList tasteList;

    @Override
    public String getUsername() {return username;}

    @Override
    public String getPassword() {return password;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PreRemove
    private void preRemove() {
        if(favoriteList != null) favoriteList.getItems().clear();
        if(tasteList != null) tasteList.getItems().clear();
    }

}
