package com.example.capstonecckma.repositories;

import com.example.capstonecckma.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository <Resource, Long> {
    // JPQL query to filter search
    @Query("SELECT r FROM Resource r WHERE " +
    "r.title LIKE CONCAT ('%',:query, '%' )" +
    " OR r.description LIKE CONCAT('%', :query, '%')")
    List<Resource>  searchResource(String query);

    // how to use native query

    @Query(value = "SELECT * FROM resources r WHERE " +
            "r.title LIKE CONCAT ('%',:query, '%' )" +
            " OR r.description LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<Resource>  searchResourceSQL(String query);

    Resource findByTitle(String title);








}
