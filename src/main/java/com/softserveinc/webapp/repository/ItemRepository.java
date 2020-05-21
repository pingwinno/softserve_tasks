package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.Item;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID>, JpaSpecificationExecutor<Item> {
    List<Item> findByCategory(String category);

    List<Item> findByName(String name);

    List<Item> findByNameAndCategory(String name, String category);

    List<Item> findByNameLikeAndCategoryLike(String name, String category);
}
