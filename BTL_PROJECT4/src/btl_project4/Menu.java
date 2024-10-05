/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btl_project4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.util.InputMismatchException;

public class Menu {

    private BTL_PROJECT4 dbConnection;

    public Menu() {
        dbConnection = new BTL_PROJECT4();
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("=== MENU ===");
            System.out.println("1. Liệt kê thông tin của các công ty cùng với tổng chi phí");
            System.out.println("2. Kiểm tra thông tin của mỗi nhân viên của các công ty cùng với số lần và vị trí ra/vào toà nhà trong ngày của họ");
            System.out.println("3. Liệt kê thông tin của các nhân viên toà nhà cùng lương tháng");
            System.out.println("4. Chỉnh sửa thông tin nhân viên");
            System.out.println("0. Thoát");
            System.out.print("Nhập lựa chọn của bạn: ");
            choice = scanner.nextInt();

            try {
                Connection conn = dbConnection.getConnection();

                switch (choice) {
                    case 1:
                        queryCompanies(conn);
                        break;
                    case 2:
                        queryEmployees(conn);
                        break;
                    case 3:
                        queryBuildingEmployees(conn);
                        break;
                    case 4:
                        showEditEmployeeMenu(conn);
                        break;
                    case 0:
                        System.out.println("Thoát khỏi chương trình.");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                }

                conn.close();
            } catch (SQLException e) {
                System.out.println("Lỗi kết nối cơ sở dữ liệu.");
                e.printStackTrace();
            }

        } while (choice != 0);

        scanner.close();
    }

    private void queryCompanies(Connection conn) throws SQLException {
        String sql = "SELECT c.MaCongTy, c.TenCongTy, SUM(g.GiaTien_TrieuDong * p.DienTich) AS TongTienThue, "
                + "COALESCE(SUM(d.GiaDichVu), 0) AS TongTienDichVu, "
                + "(SUM(g.GiaTien_TrieuDong * p.DienTich) + COALESCE(SUM(d.GiaDichVu), 0)) AS TongChiPhi "
                + "FROM congty c "
                + "JOIN nhanviencongty n ON c.MaCongTy = n.MaCongTy "
                + "JOIN hopdong h ON n.ID = h.IDKhachHang "
                + "JOIN phong p ON h.MaPhong = p.MaPhong "
                + "JOIN giaphong g ON p.MaPhong = g.MaPhong "
                + "LEFT JOIN sudungdichvu sd ON n.ID = sd.IDKhachHang "
                + "LEFT JOIN dichvu d ON sd.MaDichVu = d.MaDichVu "
                + "GROUP BY c.MaCongTy, c.TenCongTy ORDER BY TongChiPhi DESC LIMIT 0, 25;";
        ResultSet rs = dbConnection.executeQuery(conn, sql);
        while (rs.next()) {
            System.out.println("Mã Công Ty: " + rs.getString("MaCongTy")
                    + ", Tên Công Ty: " + rs.getString("TenCongTy")
                    + ", Tổng Chi Phí: " + rs.getDouble("TongChiPhi"));
        }
        rs.close();
    }

    private void queryEmployees(Connection conn) throws SQLException {
        String sql = "SELECT n.ID, g.HoTen, g.DiaChi, th.SoLanRaVao, th.SoLanVao, th.SoLanRa, "
                + "tt.ViTri, tt.ThoiGian, tt.TrangThai "
                + "FROM nhanviencongty n "
                + "JOIN nguoi g ON n.ID = g.ID "
                + "JOIN thenhanvien th ON n.ID = th.ID "
                + "LEFT JOIN thongtinravao tt ON n.ID = tt.ID "
                + "WHERE DATE(tt.ThoiGian) = CURDATE();";
        ResultSet rs = dbConnection.executeQuery(conn, sql);
        while (rs.next()) {
            System.out.println("ID: " + rs.getString("ID")
                    + ", Họ Tên: " + rs.getString("HoTen")
                    + ", Địa Chỉ: " + rs.getString("DiaChi")
                    + ", Số Lần Ra Vào: " + rs.getInt("SoLanRaVao"));
        }
        rs.close();
    }

    private void queryBuildingEmployees(Connection conn) throws SQLException {
        String sql = "SELECT g.ID AS EmployeeID, g.HoTen AS EmployeeName, g.DiaChi AS Address, "
                + "g.GioiTinh AS Gender, g.SDT AS PhoneNumber, n.MucLuong AS Salary, n.ChucVu AS Role "
                + "FROM nguoi g JOIN nhanvientoanha n ON g.ID = n.ID ORDER BY n.MucLuong DESC;";
        ResultSet rs = dbConnection.executeQuery(conn, sql);
        while (rs.next()) {
            System.out.println("ID: " + rs.getString("EmployeeID")
                    + ", Họ Tên: " + rs.getString("EmployeeName")
                    + ", Địa Chỉ: " + rs.getString("Address")
                    + ", Lương: " + rs.getDouble("Salary")
                    + ", Chức Vụ: " + rs.getString("Role"));
        }
        rs.close();
    }

    private void showEditEmployeeMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int actionChoice = -1; // Khởi tạo với giá trị không hợp lệ để vào vòng lặp.

        do {
//            String sql = "SELECT * FROM nguoi;";
//            ResultSet rs = dbConnection.executeQuery(conn, sql);
//
//            System.out.println("=== Danh sách nhân viên ===");
//            while (rs.next()) {
//                System.out.println("ID: " + rs.getString("ID")
//                        + ", Họ Tên: " + rs.getString("HoTen")
//                        + ", Địa Chỉ: " + rs.getString("DiaChi")
//                        + ", Giới Tính: " + rs.getString("GioiTinh")
//                        + ", SDT: " + rs.getString("SDT")
//                        + ", Email: " + rs.getString("Email"));
//            }
//            rs.close();

            System.out.println("=== MENU CHỈNH SỬA NHÂN VIÊN ===");
            System.out.println("1. Thêm nhân viên");
            System.out.println("2. Sửa thông tin nhân viên");
            System.out.println("3. Xóa nhân viên");
            System.out.println("0. Quay lại");

            // Yêu cầu nhập lựa chọn cho đến khi có giá trị hợp lệ
            while (true) {
                try {
                    System.out.print("Nhập lựa chọn của bạn: ");
                    actionChoice = scanner.nextInt(); // Cố gắng đọc một số nguyên
                    scanner.nextLine(); // Đọc dòng mới
                    if (actionChoice >= 0 && actionChoice <= 3) {
                        break; // Thoát khỏi vòng lặp nếu đầu vào hợp lệ
                    } else {
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập lại.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
                    scanner.nextLine(); // Xóa dòng không hợp lệ
                }
            }

            switch (actionChoice) {
                case 1:
                    addEmployee(conn);
                    break;
                case 2:
                    editEmployee(conn);
                    break;
                case 3:
                    deleteEmployee(conn);
                    break;
                case 0:
                    System.out.println("Quay lại menu chính.");
                    break;
                default:
                // Không cần thiết, vì đã kiểm tra hợp lệ ở trên
            }
        } while (actionChoice != 0);
    }

    private void addEmployee(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập ID nhân viên mới: ");
        String newID = scanner.nextLine();

        // Kiểm tra ID đã tồn tại chưa
        String checkSql = "SELECT COUNT(*) FROM nguoi WHERE ID = ?;";
        try ( PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, newID);
            ResultSet checkRs = checkStmt.executeQuery();
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                System.out.println("ID đã tồn tại. Vui lòng nhập ID khác.");
                return; // Quay lại mà không thêm nhân viên
            }
        }

        System.out.print("Nhập họ tên: ");
        String newHoTen = scanner.nextLine();
        System.out.print("Nhập địa chỉ: ");
        String newDiaChi = scanner.nextLine();
        System.out.print("Nhập giới tính (Nam/Nữ): ");
        String newGioiTinh = scanner.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String newSDT = scanner.nextLine();
        System.out.print("Nhập email: ");
        String newEmail = scanner.nextLine();

        String sql = "INSERT INTO nguoi (ID, HoTen, DiaChi, GioiTinh, SDT, Email) VALUES (?, ?, ?, ?, ?, ?);";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newID);
            pstmt.setString(2, newHoTen);
            pstmt.setString(3, newDiaChi);
            pstmt.setString(4, newGioiTinh);
            pstmt.setString(5, newSDT);
            pstmt.setString(6, newEmail);
            pstmt.executeUpdate();
            System.out.println("Thêm nhân viên thành công.");
            showEmployeeList(conn); // Hiển thị lại danh sách nhân viên
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteEmployee(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập ID nhân viên cần xóa: ");
        String idToDelete = scanner.nextLine();

        String sql = "DELETE FROM nguoi WHERE ID = ?;";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idToDelete);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa nhân viên thành công.");
                showEmployeeList(conn); // Hiển thị lại danh sách nhân viên
            } else {
                System.out.println("Không tìm thấy nhân viên với ID: " + idToDelete);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa nhân viên.");
            e.printStackTrace();
        }
    }

    private void showEmployeeList(Connection conn) throws SQLException {
        String sql = "SELECT * FROM nguoi;";
        ResultSet rs = dbConnection.executeQuery(conn, sql);

        System.out.println("=== Danh sách nhân viên ===");
        while (rs.next()) {
            System.out.println("ID: " + rs.getString("ID")
                    + ", Họ Tên: " + rs.getString("HoTen")
                    + ", Địa Chỉ: " + rs.getString("DiaChi")
                    + ", Giới Tính: " + rs.getString("GioiTinh")
                    + ", SDT: " + rs.getString("SDT")
                    + ", Email: " + rs.getString("Email"));
        }
        rs.close();
    }

    private void editEmployee(Connection conn) throws SQLException {
        // Đoạn code sửa thông tin nhân viên giữ nguyên như đã được viết ở trên.
        String sql = "SELECT * FROM nguoi;";
        ResultSet rs = dbConnection.executeQuery(conn, sql);

        System.out.println("=== Danh sách nhân viên ===");
        while (rs.next()) {
            System.out.println("ID: " + rs.getString("ID")
                    + ", Họ Tên: " + rs.getString("HoTen")
                    + ", Địa Chỉ: " + rs.getString("DiaChi")
                    + ", Giới Tính: " + rs.getString("GioiTinh")
                    + ", SDT: " + rs.getString("SDT")
                    + ", Email: " + rs.getString("Email"));
        }
        rs.close();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập ID nhân viên cần chỉnh sửa: ");
        String idToEdit = scanner.nextLine();

        // Lấy thông tin hiện tại của nhân viên
        String selectSql = "SELECT * FROM nguoi WHERE ID = '" + idToEdit + "';";
        ResultSet employeeResult = dbConnection.executeQuery(conn, selectSql);

        if (employeeResult.next()) {
            String currentAddress = employeeResult.getString("DiaChi");
            String currentHoTen = employeeResult.getString("HoTen");
            String currentGioiTinh = employeeResult.getString("GioiTinh");
            String currentSDT = employeeResult.getString("SDT");
            String currentEmail = employeeResult.getString("Email");

            System.out.println("Chỉnh sửa thông tin cho nhân viên ID: " + idToEdit);
            System.out.println("Họ Tên hiện tại: " + currentHoTen);
            System.out.println("Địa Chỉ hiện tại: " + currentAddress);
            System.out.println("Giới Tính hiện tại: " + currentGioiTinh);
            System.out.println("SĐT hiện tại: " + currentSDT);
            System.out.println("Email hiện tại: " + currentEmail);

            // Nhập thông tin mới
            System.out.print("Nhập họ tên mới (bỏ qua nếu không thay đổi): ");
            String newHoTen = scanner.nextLine();
            System.out.print("Nhập địa chỉ mới (bỏ qua nếu không thay đổi): ");
            String newDiaChi = scanner.nextLine();

            String newGioiTinh = currentGioiTinh; // Giữ giá trị cũ nếu không thay đổi
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Nhập giới tính mới (nhập 0 nếu là Nam, nhập 1 nếu là Nữ, bỏ qua nếu không thay đổi): ");
                String inputGioiTinh = scanner.nextLine();
                if (inputGioiTinh.isEmpty()) {
                    validInput = true; // Nếu không nhập gì, giữ nguyên giá trị cũ
                } else if (inputGioiTinh.equals("0")) {
                    newGioiTinh = "Nam";
                    validInput = true;
                } else if (inputGioiTinh.equals("1")) {
                    newGioiTinh = "Nữ";
                    validInput = true;
                } else {
                    System.out.println("Giới tính không hợp lệ, vui lòng nhập lại.");
                }
            }

            System.out.print("Nhập số điện thoại mới (bỏ qua nếu không thay đổi): ");
            String newSDT = scanner.nextLine();
            System.out.print("Nhập email mới (bỏ qua nếu không thay đổi): ");
            String newEmail = scanner.nextLine();

            // Kiểm tra xem có thay đổi nào không
            boolean hasChanges = false;

            // Tạo câu lệnh UPDATE
            StringBuilder updateSql = new StringBuilder("UPDATE nguoi SET ");
            if (!newHoTen.isEmpty()) {
                updateSql.append("HoTen = '").append(newHoTen).append("', ");
                hasChanges = true;
            }
            if (!newDiaChi.isEmpty()) {
                updateSql.append("DiaChi = '").append(newDiaChi).append("', ");
                hasChanges = true;
            }
            if (!newGioiTinh.equals(currentGioiTinh)) {
                updateSql.append("GioiTinh = '").append(newGioiTinh).append("', ");
                hasChanges = true;
            }
            if (!newSDT.isEmpty()) {
                updateSql.append("SDT = '").append(newSDT).append("', ");
                hasChanges = true;
            }
            if (!newEmail.isEmpty()) {
                updateSql.append("Email = '").append(newEmail).append("' ");
                hasChanges = true;
            }

            // Xóa dấu phẩy cuối và kiểm tra có thay đổi không
            if (hasChanges) {
                if (updateSql.toString().endsWith(", ")) {
                    updateSql.setLength(updateSql.length() - 2); // Xóa dấu phẩy cuối
                }
                updateSql.append(" WHERE ID = '").append(idToEdit).append("';");

                // Thực hiện cập nhật
                dbConnection.executeUpdate(conn, updateSql.toString());
                System.out.println("Cập nhật thông tin nhân viên thành công.");

                // Lấy lại thông tin nhân viên vừa cập nhật
                ResultSet updatedResult = dbConnection.executeQuery(conn, selectSql);
                if (updatedResult.next()) {
                    System.out.println("=== Thông tin nhân viên sau khi cập nhật ===");
                    System.out.println("ID: " + updatedResult.getString("ID"));
                    System.out.println("Họ Tên: " + updatedResult.getString("HoTen"));
                    System.out.println("Địa Chỉ: " + updatedResult.getString("DiaChi"));
                    System.out.println("Giới Tính: " + updatedResult.getString("GioiTinh"));
                    System.out.println("SĐT: " + updatedResult.getString("SDT"));
                    System.out.println("Email: " + updatedResult.getString("Email"));
                }
                updatedResult.close();
            } else {
                System.out.println("Không có thay đổi nào.");
            }
        } else {
            System.out.println("Không tìm thấy nhân viên với ID: " + idToEdit);
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.showMenu();
    }
}
