package com.example.reactivedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Persistable<Long> {
    @Id
    @Column("post_id")
    private Long id;

    private Long userId;
    private Long organizationId;
    private String subject;
    private String body;

    @Override
    public boolean isNew() {
        return true;
    }
}
