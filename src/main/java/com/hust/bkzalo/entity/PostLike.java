package com.hust.bkzalo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PostLikeId.class)
public class PostLike {

    @Id
    private UUID postId;

    @Id
    private UUID accountId;
}
