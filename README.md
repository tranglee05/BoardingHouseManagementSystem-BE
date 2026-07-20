# Boarding House Management System - Backend

Dự án Backend cho hệ thống **Quản lý nhà trọ và Căn hộ dịch vụ (SaaS)**, được xây dựng bằng kiến trúc RESTful API với **Spring Boot**.

---

## 🎯 Mục tiêu dự án
Dự án nhằm số hóa hoàn toàn quy trình vận hành và quản lý nhà trọ/căn hộ cho thuê. Hệ thống giúp giải quyết các "nỗi đau" thường gặp của chủ nhà (quên tính tiền điện nước, thất lạc giấy tờ hợp đồng, khó kiểm soát lịch hẹn) và mang lại trải nghiệm tiện lợi cho khách thuê (dễ dàng báo cáo sự cố, xem hóa đơn minh bạch, nhắn tin trực tiếp với chủ nhà). 

Với mô hình SaaS (Software as a Service), dự án cho phép nền tảng kinh doanh cung cấp các "Gói dịch vụ" (Subscriptions) cho nhiều chủ trọ khác nhau sử dụng chung một hệ thống nhưng dữ liệu được phân tách rõ ràng.

---

## 📖 Tổng quan & Luồng hoạt động (System Flow)

Hệ thống được thiết kế xoay quanh 3 nhóm người dùng (Roles) chính, với các luồng nghiệp vụ như sau:

### 1. Luồng của System Admin (Quản trị viên hệ thống)
- Quản lý các tài khoản trên toàn hệ thống.
- Thiết lập các **Gói dịch vụ (Subscription Packages)** (ví dụ: Gói dùng thử 5 phòng, Gói Pro không giới hạn).

### 2. Luồng của Landlord (Chủ nhà)
- **Đăng ký & Chọn gói:** Chủ nhà tạo tài khoản và mua/đăng ký một Gói dịch vụ.
- **Quản lý cơ sở vật chất:** Chủ nhà tạo danh sách Tòa nhà (Buildings) và các Phòng trọ (Rooms). Có thể upload hình ảnh thực tế của phòng lên hệ thống.
- **Quản lý Khách thuê & Hợp đồng:** Khi có khách chốt phòng, chủ nhà tạo Hợp đồng (Contract) và gán Khách thuê (Tenant) vào phòng.
- **Vận hành hàng tháng:** Ghi chép chỉ số điện nước (Utility Records) định kỳ. Dựa vào đó hệ thống sẽ tự động xuất Hóa đơn (Invoices).
- **Tương tác:** Tiếp nhận và xử lý các Báo cáo sự cố (Issues) từ khách thuê, xác nhận Lịch hẹn xem phòng (Appointments) từ khách vãng lai, và nhắn tin (Chat) với khách.

### 3. Luồng của Tenant (Khách thuê)
- **Truy cập:** Khách thuê đăng nhập vào tài khoản (thường do chủ nhà cấp hoặc tự đăng ký rồi được gán vào phòng).
- **Xem thông tin:** Theo dõi chi tiết Hợp đồng hiện tại, xem các Hóa đơn tiền phòng & điện nước hàng tháng.
- **Tương tác:** Gửi Báo cáo sự cố (Hỏng bóng đèn, vỡ ống nước...) cho chủ nhà. Chat trực tiếp với chủ nhà qua hệ thống tin nhắn.

---

## 🚀 Chi tiết các chức năng đã hoàn thiện

Dự án đã phát triển hoàn thiện các Module Backend cốt lõi:

1. **Authentication & Authorization (`user` module)**
   - Đăng nhập, đăng ký và phân quyền bằng **JWT (JSON Web Token)**.
   - Các Role: `admin`, `landlord`, `tenant`, `guest`.

2. **Quản lý Cơ sở vật chất (`building`, `room` module)**
   - CRUD Tòa nhà (Địa chỉ, số lượng tầng).
   - CRUD Phòng trọ (Giá thuê, diện tích, trạng thái, tiện ích).
   - Tích hợp **Cloudinary API** để upload và quản lý nhiều hình ảnh cho mỗi phòng (`RoomImage`).

3. **Quản lý Gói dịch vụ - SaaS (`subscription` module)**
   - Quản lý tên gói, giá tiền, số lượng phòng tối đa và thời hạn (Tháng).

4. **Khách thuê & Hợp đồng (`tenant`, `contracts` module)**
   - Quản lý hồ sơ Khách thuê (Căn cước công dân, quê quán...).
   - Tạo và quản lý vòng đời Hợp đồng thuê nhà (Ngày bắt đầu, ngày kết thúc, tiền cọc, trạng thái hiệu lực).

5. **Dịch vụ & Tính tiền (`utility`, `invoices` module)**
   - Chốt số điện/nước hàng tháng (`utility_records`).
   - Tạo Hóa đơn tính tổng tiền (Tiền phòng + Tiền điện/nước + Chi phí phát sinh).
   - Đánh dấu trạng thái thanh toán (Unpaid, Paid).

6. **Tương tác & CSKH (`appointment`, `issue`, `chat` module)**
   - **Appointments**: Đặt lịch hẹn xem phòng cho khách lạ.
   - **Issues**: Khách thuê tạo ticket báo cáo sự cố, chủ nhà cập nhật tiến độ (Pending, In Progress, Resolved).
   - **Chat**: Tính năng trò chuyện trực tiếp giữa người dùng (hỗ trợ WebSockets cho thời gian thực).

---

## 🛠 Hướng dẫn cài đặt & Chạy dự án

### 1. Yêu cầu hệ thống
- Java 17 hoặc 21
- Maven
- PostgreSQL (hoặc Neon PostgreSQL)

### 2. Cấu hình biến môi trường
Mở file `src/main/resources/application.properties` và cấu hình các thông số sau:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://<host>/<dbname>
spring.datasource.username=<username>
spring.datasource.password=<password>

# Cloudinary Configuration (Upload ảnh)
cloudinary.cloud-name=<your-cloud-name>
cloudinary.api-key=<your-api-key>
cloudinary.api-secret=<your-api-secret>
```

### 3. Khởi chạy dự án
Chạy lệnh sau tại thư mục gốc:
```bash
mvn spring-boot:run
```
> **Data Seeder:** Lần đầu tiên khởi chạy, hệ thống sẽ tự động tạo bảng (Hibernate DDL `update`) và chèn dữ liệu mẫu (1 tài khoản Admin `admin@gmail.com`/`admin123` và 2 Gói dịch vụ mặc định).

### 4. Xem tài liệu API (Swagger UI)
Hệ thống đã tích hợp sẵn Springdoc OpenAPI. Sau khi ứng dụng chạy, bạn truy cập:
- **URL**: `http://localhost:8080/swagger-ui/index.html`
- Giao diện cung cấp đầy đủ danh sách Endpoints, Request Body và Response.
- **Lưu ý xác thực:** Với các API bị khóa, hãy gọi API Login để lấy `accessToken`, sau đó bấm vào nút **Authorize** ở góc phải trên cùng của Swagger và nhập `<token>` (hệ thống sẽ tự thêm tiền tố Bearer).
