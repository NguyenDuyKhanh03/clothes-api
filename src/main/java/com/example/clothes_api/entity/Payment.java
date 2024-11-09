package com.example.clothes_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
@Entity
public class Payment {
    @Id
    private UUID id=UUID.randomUUID();

    @Column(name="pay_time")
    @CreationTimestamp
    private Timestamp payTime;

    @Column(name="payment_type")
    private String paymentType;

    @Column(name="transaction_id")
    private String transactionId;

    private String status;

    @Column(name="created_at")
    private Timestamp createdAt;

    @Column(name="pay_price")
    private double payPrice;

    @Column(name="created_by")
    private Long createdBy;


    @Column(name="updated_at")
    @UpdateTimestamp
    private Timestamp updated_at;

    @Column(name="order_id")
    private Long orderId;
}
