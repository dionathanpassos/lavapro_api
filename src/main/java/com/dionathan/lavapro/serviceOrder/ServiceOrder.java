package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceOrderStatus status = ServiceOrderStatus.WAITING;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 500)
    private String observations;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    public void start() {
        if( status != ServiceOrderStatus.WAITING) {
            throw new BusinessException("Somente ordem aguardando podem ser iniciadas");
        }

        this.status = ServiceOrderStatus.IN_PROGRESS;
    }

    public void finish() {

        if (status != ServiceOrderStatus.IN_PROGRESS) {
            throw new BusinessException(
                    "Somente ordens em andamento podem ser finalizadas."
            );
        }

        this.status = ServiceOrderStatus.READY;
    }

    public void deliver() {

        if (status != ServiceOrderStatus.READY) {
            throw new BusinessException(
                    "Somente ordens prontas podem ser ser entregues."
            );
        }

        this.status = ServiceOrderStatus.DELIVERED;
    }
}
