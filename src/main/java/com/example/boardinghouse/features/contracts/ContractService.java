package com.example.boardinghouse.features.contracts;

import com.example.boardinghouse.features.contracts.dto.ContractRequest;
import com.example.boardinghouse.features.contracts.dto.ContractResponse;
import com.example.boardinghouse.features.room.Room;
import com.example.boardinghouse.features.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private RoomRepository roomRepository;

    // 1. LẤY TẤT CẢ HỢP ĐỒNG
    public List<ContractResponse> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 2. LẤY CHI TIẾT 1 HỢP ĐỒNG
    public ContractResponse getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        return mapToResponse(contract);
    }

    // 3. TẠO HỢP ĐỒNG MỚI (Giữ nguyên logic thông minh của bạn)
    @Transactional
    public ContractResponse createContract(ContractRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!"available".equals(room.getStatus())) {
            throw new RuntimeException("Room is already rented or under maintenance");
        }

        String generatedUsername = request.getTenantPhone();
        String generatedPassword = UUID.randomUUID().toString().substring(0, 6);
        Long mockTenantUserId = 99L; // Giả lập ID người dùng mới sau khi lưu

        Contract contract = Contract.builder()
                .roomId(room.getId())
                .tenantId(mockTenantUserId)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .deposit(request.getDeposit())
                .rentalPrice(room.getPrice())
                .appointmentId(request.getAppointmentId())
                .status("active")
                .build();

        Contract savedContract = contractRepository.save(contract);

        // Chuyển trạng thái phòng sang 'rented'
        room.setStatus("rented");
        roomRepository.save(room);

        return mapToResponseCustom(savedContract, room.getRoomNumber(), request.getTenantFullName(), generatedUsername,
                generatedPassword);
    }

    // 4. SỬA HỢP ĐỒNG (Gia hạn thời gian hoặc thay đổi tiền cọc)
    @Transactional
    public ContractResponse updateContract(Long id, ContractRequest request) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        // Nếu thay đổi phòng trong hợp đồng
        if (!contract.getRoomId().equals(request.getRoomId())) {
            // Trả phòng cũ về trạng thái trống
            Room oldRoom = roomRepository.findById(contract.getRoomId()).orElse(null);
            if (oldRoom != null) {
                oldRoom.setStatus("available");
                roomRepository.save(oldRoom);
            }
            // Khóa phòng mới sang trạng thái đã thuê
            Room newRoom = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("New room not found"));
            newRoom.setStatus("rented");
            roomRepository.save(newRoom);

            contract.setRoomId(newRoom.getId());
            contract.setRentalPrice(newRoom.getPrice());
        }

        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setDeposit(request.getDeposit());

        Contract updatedContract = contractRepository.save(contract);
        return mapToResponse(updatedContract);
    }

    // 5. CHẤM DỨT HỢP ĐỒNG SỚM (Trả phòng thực tế)
    @Transactional
    public ContractResponse terminateContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        // Chuyển trạng thái hợp đồng thành 'terminated'
        contract.setStatus("terminated");
        Contract updatedContract = contractRepository.save(contract);

        // Giải phóng phòng trọ về lại trạng thái 'available'
        Room room = roomRepository.findById(contract.getRoomId()).orElse(null);
        if (room != null) {
            room.setStatus("available");
            roomRepository.save(room);
        }

        return mapToResponse(updatedContract);
    }

    // 6. XÓA HỢP ĐỒNG (Xóa cứng khỏi DB - Dùng khi bị nhập lỗi)
    @Transactional
    public void deleteContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        // Trước khi xóa hợp đồng, hãy trả trạng thái phòng về 'available'
        Room room = roomRepository.findById(contract.getRoomId()).orElse(null);
        if (room != null) {
            room.setStatus("available");
            roomRepository.save(room);
        }

        contractRepository.delete(contract);
    }

    // Các hàm helper để mapping nhanh dữ liệu
    private ContractResponse mapToResponse(Contract contract) {
        Room room = roomRepository.findById(contract.getRoomId()).orElse(null);
        String roomNumber = (room != null) ? room.getRoomNumber() : "N/A";
        return ContractResponse.builder()
                .contractId(contract.getId())
                .roomNumber(roomNumber)
                .tenantName("Khách thuê #" + contract.getTenantId()) // Giả lập lấy từ User profile
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .rentalPrice(contract.getRentalPrice())
                .deposit(contract.getDeposit())
                .status(contract.getStatus())
                .build();
    }

    private ContractResponse mapToResponseCustom(Contract contract, String roomNumber, String tenantName,
            String username, String password) {
        return ContractResponse.builder()
                .contractId(contract.getId())
                .roomNumber(roomNumber)
                .tenantName(tenantName)
                .generatedUsername(username)
                .generatedPassword(password)
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .rentalPrice(contract.getRentalPrice())
                .deposit(contract.getDeposit())
                .status(contract.getStatus())
                .build();
    }
}