package org.tyaa.demo.spring.springreactmysqlmongo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Category;

@Repository
public interface CategoryHibernateDAO extends JpaRepository<Category, Long> {
}
