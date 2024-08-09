package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(updatable = false)
    @CreatedBy
    public String createdBy;

    @Column(insertable = false)
    @LastModifiedBy
    public String updatedBy;

    @Column(updatable = false)
    @LastModifiedDate
    public LocalDateTime createdDate;


    @Column(insertable = false)
    @CreatedDate
    public LocalDateTime updatedDate;


}
