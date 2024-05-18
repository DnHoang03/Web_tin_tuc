package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query("SELECT c FROM News c WHERE c.accepted = true AND c.title LIKE CONCAT('%', :query, '%') ORDER BY c.createdDate DESC")
    Page<News> searchNewsByTitle(String query, Pageable pageable);

    @Query("SELECT c FROM News c WHERE c.accepted = true AND c.category.id = :categoryId ORDER BY c.createdDate DESC")
    Page<News> findByCategoryId(int categoryId, Pageable pageable);

    @Query("SELECT c FROM News c WHERE c.accepted = true ORDER BY c.createdDate DESC")
    Page<News> findAllDsc(Pageable pageable);

    @Query("SELECT c FROM News c WHERE c.accepted = true AND c.category.code = :code ORDER BY c.createdDate DESC LIMIT 1")
    News findOneByCategory(String code);

    News findByTitle(String title);

    @Query("SELECT c FROM News c WHERE c.accepted = false ORDER BY c.createdDate DESC")
    Page<News> findUnacceptedNews(Pageable pageable);

    @Query("SELECT c FROM News c WHERE c.accepted = true AND c.category.code != 'THE-GIOI' ORDER BY c.createdDate DESC")
    Page<News> findBestNews(Pageable pageable);

    @Query("SELECT c FROM News c WHERE c.accepted = true AND c.category.code = :code ORDER BY c.createdDate DESC")
    List<News> findByCategoryCode(String code);

}
