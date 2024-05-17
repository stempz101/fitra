package com.diploma.fitra.model;

import com.diploma.fitra.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity(name = "_users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, length = 550)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private char[] password;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(columnDefinition = "TEXT")
    private String about;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Country country;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private City city;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String confirmToken;

    private LocalDateTime confirmTokenExpiration;

    private boolean enabled;

    private boolean blocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return String.valueOf(this.password);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled && blocked == user.blocked && Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(fullName, user.fullName) && Objects.equals(email, user.email) && Arrays.equals(password, user.password) && Objects.equals(birthday, user.birthday) && Objects.equals(about, user.about) && Objects.equals(country, user.country) && Objects.equals(city, user.city) && role == user.role && Objects.equals(confirmToken, user.confirmToken) && Objects.equals(confirmTokenExpiration, user.confirmTokenExpiration);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, firstName, lastName, fullName, email, birthday, about, country, city, role, confirmToken, confirmTokenExpiration, enabled, blocked);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
