# Đại Chiến Thú Cưng (Pet Battle)

### Hướng dẫn Cơ sở dữ liệu (Database)
- Cơ sở dữ liệu sử dụng MySQL, phần mềm quản lý được khuyến nghị là Navicat For MySQL.
- Tạo cơ sở dữ liệu có tên là `pet_battle`, chọn bảng mã (character set) là `utf8 -- UTF-8 Unicode`, và quy tắc sắp xếp (collation) là `utf8_general_ci`.
- Đặt mật khẩu cho tài khoản `root` là ``.
- Import script cơ sở dữ liệu `pet_battle.sql`.

### Hướng dẫn Client (Máy khách)
Sử dụng Cocos Creator-2.3.3 để mở dự án `PetBattleClient`.
Có thể sửa đổi URL kết nối đến backend trong `LoginMgr.ts`.

Tài khoản thử nghiệm hiện có:
Tài khoản | Mật khẩu
---|---
123456 | 123456
asdfgh | asdfgh

Lưu ý: Phải chạy Server trước rồi mới chạy Client, nếu không sẽ báo lỗi kết nối.

### Hướng dẫn Server (Máy chủ)
Khuyến nghị sử dụng Eclipse để mở dự án `PetBattleServer`, chạy file `game.Boot.java`.
