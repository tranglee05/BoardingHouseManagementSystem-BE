package com.example.boardinghouse.features.contracts.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponse {
    private Long contractId;
    private String roomNumber;
    private String tenantName;
    private String generatedUsername; // Tài khoản đăng nhập tự sinh cho khách thuê
    private String generatedPassword; // Mật khẩu tự sinh để gửi cho khách thuê
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentalPrice; // Tự động lấy giá gốc từ bảng Rooms
    private BigDecimal deposit;
    private String status;
}