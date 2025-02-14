package com.example.BookWhiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "user")
public class User implements UserDetails {

    // Getters and Setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    // @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    // @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Setter
    @Column(name = "birth_date")
    private Date dateOfBirth;

    @Getter
    @Setter
    @Column(name = "verification_code")
    private String verificationCode;

    @Getter
    @Setter
    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    @Getter
    @Setter
    @Column(name = "created_date")
    private Date createdDate;

    @Setter
    private boolean enabled;

    @OneToMany(mappedBy="user")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBookList> userBookList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserAuthorList userAuthorList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserGenreList userGenreList;


    // Constructors
    public User() {}

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
        return enabled;
    }
}
