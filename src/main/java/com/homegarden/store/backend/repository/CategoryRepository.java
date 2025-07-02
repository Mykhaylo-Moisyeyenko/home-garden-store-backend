package com.homegarden.store.backend.repository;

import com.homegarden.store.backend.model.entity.Category;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
