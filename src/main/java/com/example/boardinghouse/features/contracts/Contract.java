package com.example.boardinghouse.features.contracts;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId; // Liên kết tới ID User (khách thuê) vừa được tự động tạo

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal deposit;

    @Column(name = "rental_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal rentalPrice;

    @Column(name = "contract_pdf_url", length = 500)
    private String contractPdfUrl;

    @Column(nullable = false)
    private String status = "active"; // active, expired, terminated

    @Column(name = "appointment_id")
    private Long appointmentId;
}