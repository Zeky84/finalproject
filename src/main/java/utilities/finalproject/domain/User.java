package utilities.finalproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor// from lombok
@NoArgsConstructor // from lombok
@Data // from lombok
@EqualsAndHashCode // from lombok
@SuperBuilder // from lombok
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient // this annotation is to avoid the checkPassword field to be persisted in the database.CHAT-GPT-4
    private String checkPassword;

    @CreationTimestamp  // this annotation is to set the createdAt field to the current time when the user is created
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.EAGER)//FetchType.EAGER is the default
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")//FetchType.LAZY is the default
    private List<Profile> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "user")//FetchType.LAZY is the default
    private List<Post> posts = new ArrayList<>();


}
