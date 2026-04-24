package com.posteos.RepositoryPost;

import com.posteos.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository_Post extends JpaRepository<Post, Long> {
}
