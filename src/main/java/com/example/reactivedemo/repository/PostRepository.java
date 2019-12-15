package com.example.reactivedemo.repository;

import com.example.reactivedemo.domain.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, Long> {
}
