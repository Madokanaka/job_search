package kg.attractor.job_search.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String surname;

    private Integer age;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String avatar;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users", cascade = CascadeType.ALL)
    private List<Role> roles;
}
