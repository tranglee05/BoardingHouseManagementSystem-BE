package com.example.boardinghouse.features.contracts.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    // Nhập thông tin của khách hàng để hệ thống tự động tạo tài khoản user mới
    @NotNull(message = "Tenant full name is required")
    private String tenantFullName;

    @NotNull(message = "Tenant phone is required")
    private String tenantPhone;

    private String tenantEmail;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Deposit amount is required")
    private BigDecimal deposit;

    private Long appointmentId; // Có thể null nếu khách thuê trực tiếp không hẹn trước
}