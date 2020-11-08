package org.tyaa.demo.spring.springreactmysqlmongo.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Categories")
@Data
@EqualsAndHashCode(exclude = "products")
@ToString(exclude = "products")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    @OneToMany(mappedBy="category", fetch = FetchType.LAZY)
    private Set<Product> products;
}
