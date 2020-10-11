package org.tyaa.demo.spring.springreactmysqlmongo.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Roles")
@Data
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    @OneToMany(mappedBy="role", fetch = FetchType.LAZY)
    private Set<User> users;
}
