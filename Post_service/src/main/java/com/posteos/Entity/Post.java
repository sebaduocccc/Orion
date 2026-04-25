package com.posteos.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;



import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="post" , indexes = {
        @Index(name = "idx_post_userid", columnList = "userid")
})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column( length = 13,nullable = false)

    private Long userid;
    @NotBlank(message = "CONTENIDO REQUERIDO")
    private String content;
    private String mediaUrl;
    @NotNull
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creado_el;

}
