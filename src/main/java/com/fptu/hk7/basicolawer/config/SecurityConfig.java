package com.fptu.hk7.basicolawer.config;

import com.fptu.hk7.basicolawer.config.JWT.JWTTokenFilter;
import com.fptu.hk7.basicolawer.pojo.*;
import com.fptu.hk7.basicolawer.pojo.Enum.Role;
import com.fptu.hk7.basicolawer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JWTTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .permitAll()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initData(FormTemplateRepository formTemplateRepository, UserRepository userRepository, StaffProfileRepository staffProfileRepository, ServiceRepository serviceRepository) {
        return args -> {
            // Initialize your data here if needed
            if (!userRepository.existsByUsername("admin")) {
                // create user with role ADMIN
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("123456"))
                        .fullname("Admin Basico")
                        .email("koifarmshopswp391@gmail.com")
                        .status("active")
                        .phone("0773363666")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("customer")) {
                User user = User.builder()
                        .username("customer")
                        .password(passwordEncoder().encode("123456"))
                        .fullname("Customer Basico")
                        .email("customer@gmail.com")
                        .status("active")
                        .phone("0773363666")
                        .role(Role.CUSTOMER)
                        .build();
                userRepository.save(user);
            }

            if (serviceRepository.count() == 0 && !userRepository.existsByRole(Role.CONSULTANT) && !userRepository.existsByRole(Role.LAWYER)) {
                String[] services = {
                        "Dịch vụ luật sư nội bộ",
                        "Ngân hàng và tài chính",
                        "Thị trường vốn",
                        "Bảo hiểm",
                        "Doanh nghiệp",
                        "Tư vấn chiến lược & tái cấu trúc doanh nghiệp",
                        "Đào tạo pháp lý cho doanh nghiệp",
                        "Tư vấn M&A",
                        "Tranh tụng & giải quyết tranh chấp",
                        "Sở hữu trí tuệ",
                        "Tư vấn bất động sản",
                        "Lao động",
                        "Thuế",
                        "CNTT & truyền thông"
                };

                // 1. Lưu services vào DB
                Map<String, Service> serviceMap = new HashMap<>();
                for (String sv : services) {
                    Service service = Service.builder()
                            .name(sv)
                            .price(2000000f)
                            .build();
                    serviceRepository.save(service);
                    serviceMap.put(sv, service);
                }

                // 1. Trần Minh Hải
                User hai = new User(
                        "hailawyer",
                        passwordEncoder().encode("123456"),
                        "Trần Minh Hải",
                        "0912345678",
                        "hai@basico.com.vn",
                        "ACTIVE",
                        Role.LAWYER
                );
                userRepository.save(hai);

                StaffProfile profileHai = StaffProfile.builder()
                        .yearExperience("15+ năm")
                        .licenseNumber("LS001")
                        .bio("Giám đốc điều hành BASICO, nhiều năm kinh nghiệm tư vấn luật doanh nghiệp, ngân hàng.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2016/05/LS_Hai-2.jpg")
                        .specialties("M&A, Doanh nghiệp, Ngân hàng, Thuế – tài chính doanh nghiệp, Giao dịch vốn, chứng khoán, Bất động sản, Dịch vụ tranh tụng")
                        .user(hai)
                        .services(List.of(
                                serviceMap.get("Dịch vụ luật sư nội bộ"),
                                serviceMap.get("Ngân hàng và tài chính"),
                                serviceMap.get("Thị trường vốn"),
                                serviceMap.get("Doanh nghiệp"),
                                serviceMap.get("Tư vấn M&A"),
                                serviceMap.get("Thuế"),
                                serviceMap.get("Tư vấn bất động sản"),
                                serviceMap.get("Tranh tụng & giải quyết tranh chấp")
                        ))
                        .rating(0f)
                        .cases(258)
                        .description("Chuyên gia hàng đầu về luật tài chính và M&A với hơn 15 năm kinh nghiệm.")
                        .build();
                staffProfileRepository.save(profileHai);

                // 2. Nguyễn Thị Ngọc Anh
                User ngoc = new User(
                        "ngoclawyer",
                        passwordEncoder().encode("123456"),
                        "Nguyễn Thị Ngọc",
                        "0912345679",
                        "ngoc@basico.com.vn",
                        "ACTIVE",
                        Role.LAWYER
                );
                userRepository.save(ngoc);

                StaffProfile profileNgoc = StaffProfile.builder()
                        .yearExperience("10+ năm")
                        .licenseNumber("LS002")
                        .bio("Luật sư thành viên BASICO, chuyên tư vấn hợp đồng, pháp lý doanh nghiệp.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2016/03/Luat-su-NgocAnh-2.jpg")
                        .specialties("Pháp lý doanh nghiệp, Ngân hàng, M&A, Giao dịch thị trường vốn, Giải quyết tranh chấp")
                        .user(ngoc)
                        .services(List.of(
                                serviceMap.get("Doanh nghiệp"),
                                serviceMap.get("Ngân hàng và tài chính"),
                                serviceMap.get("Thị trường vốn"),
                                serviceMap.get("Tư vấn M&A"),
                                serviceMap.get("Tranh tụng & giải quyết tranh chấp")
                        ))
                        .rating(0f)
                        .cases(167)
                        .description("Chuyên gia tranh tụng với tỷ lệ thắng kiện cao trong các vụ án phức tạp.")
                        .build();
                staffProfileRepository.save(profileNgoc);

                // 3. Nguyễn Đỗ Thùy Dương
                User duong = new User(
                        "duonglawyer",
                        passwordEncoder().encode("123456"),
                        "Nguyễn Đỗ Thùy Dương",
                        "0912345680",
                        "duong@basico.com.vn",
                        "ACTIVE",
                        Role.LAWYER
                );
                userRepository.save(duong);

                StaffProfile profileDuong = StaffProfile.builder()
                        .yearExperience("8+ năm")
                        .licenseNumber("LS003")
                        .bio("Luật sư thành viên BASICO, chuyên về pháp lý doanh nghiệp, đầu tư.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2016/02/NguyenDoThuyDuong.jpg")
                        .specialties("Dịch vụ Luật sư nội bộ, Pháp lý doanh nghiệp, Thị trường vốn, M&A, Thuế, Tài chính doanh nghiệp")
                        .user(duong)
                        .services(List.of(
                                serviceMap.get("Dịch vụ luật sư nội bộ"),
                                serviceMap.get("Doanh nghiệp"),
                                serviceMap.get("Thị trường vốn"),
                                serviceMap.get("Tư vấn M&A"),
                                serviceMap.get("Thuế"),
                                serviceMap.get("Ngân hàng và tài chính")
                        ))
                        .rating(0f)
                        .cases(136)
                        .description("Chuyên gia về sở hữu trí tuệ và pháp lý công nghệ cho các startup.")
                        .build();
                staffProfileRepository.save(profileDuong);

                // 4. Tô Hồng Dung
                User dung = new User(
                        "dunglawyer",
                        passwordEncoder().encode("123456"),
                        "Tô Hồng Dung",
                        "0912345681",
                        "dung@basico.com.vn",
                        "ACTIVE",
                        Role.LAWYER
                );
                userRepository.save(dung);

                StaffProfile profileDung = StaffProfile.builder()
                        .yearExperience("7+ năm")
                        .licenseNumber("LS004")
                        .bio("Luật sư BASICO, tư vấn pháp lý doanh nghiệp.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2016/02/ToHongDung.jpg")
                        .specialties("Bảo hiểm, Lao động, Tư vấn bất động sản, Đào tạo pháp lý cho doanh nghiệp, Tranh tụng và giải quyết tranh chấp")
                        .user(dung)
                        .services(List.of(
                                serviceMap.get("Bảo hiểm"),
                                serviceMap.get("Lao động"),
                                serviceMap.get("Tư vấn bất động sản"),
                                serviceMap.get("Đào tạo pháp lý cho doanh nghiệp"),
                                serviceMap.get("Tranh tụng & giải quyết tranh chấp"),
                                serviceMap.get("Doanh nghiệp"),
                                serviceMap.get("Dịch vụ luật sư nội bộ")
                        ))
                        .rating(0f)
                        .cases(113)
                        .description("Chuyên gia về thuế và tuân thủ pháp lý cho doanh nghiệp.")
                        .build();
                staffProfileRepository.save(profileDung);

                // 5. Phạm Thúy Nga
                User nga = new User(
                        "ngaconsultant",
                        passwordEncoder().encode("123456"),
                        "Phạm Thúy Nga",
                        "0912345682",
                        "nga@basico.com.vn",
                        "ACTIVE",
                        Role.CONSULTANT
                );
                userRepository.save(nga);

                StaffProfile profileNga = StaffProfile.builder()
                        .yearExperience("5+ năm")
                        .licenseNumber("LS005")
                        .bio("Cố vấn pháp lý BASICO.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2016/02/PhamThuyNga.jpg")
                        .specialties("Dịch vụ Luật sư nội bộ, Pháp lý doanh nghiệp, Thị trường vốn, Tư vấn bất động sản, Lao động")
                        .user(nga)
                        .services(List.of(
                                serviceMap.get("Dịch vụ luật sư nội bộ"),
                                serviceMap.get("Doanh nghiệp"),
                                serviceMap.get("Thị trường vốn"),
                                serviceMap.get("Tư vấn bất động sản"),
                                serviceMap.get("Lao động")
                        ))
                        .rating(0f)
                        .cases(98)
                        .description("Chuyên gia về luật lao động và quản lý nhân sự doanh nghiệp.")
                        .build();
                staffProfileRepository.save(profileNga);

                // 6. Nguyễn Đức Anh
                User ducanh = new User(
                        "anhconsultant",
                        passwordEncoder().encode("123456"),
                        "Nguyễn Đức Anh",
                        "0912345683",
                        "ducanh@basico.com.vn",
                        "ACTIVE",
                        Role.CONSULTANT
                );
                userRepository.save(ducanh);

                StaffProfile profileDucAnh = StaffProfile.builder()
                        .yearExperience("4+ năm")
                        .licenseNumber("LS006")
                        .bio("Cố vấn pháp lý BASICO.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2016/02/NguyenDucAnh.jpg")
                        .specialties("Dịch vụ Luật sư nội bộ, Pháp lý doanh nghiệp, Công nghệ thông tin và Truyền thông, Ngân hàng và tài chính, Tư vấn bất động sản")
                        .user(ducanh)
                        .services(List.of(
                                serviceMap.get("Dịch vụ luật sư nội bộ"),
                                serviceMap.get("Doanh nghiệp"),
                                serviceMap.get("CNTT & truyền thông"),
                                serviceMap.get("Ngân hàng và tài chính"),
                                serviceMap.get("Tư vấn bất động sản")
                        ))
                        .rating(0f)
                        .cases(87)
                        .description("Chuyên gia về hợp đồng thương mại và xuất nhập khẩu quốc tế.")
                        .build();
                staffProfileRepository.save(profileDucAnh);

                // 7. Nguyễn Thanh Đăng Khoa
                User khoa = new User(
                        "khoaconsultant",
                        passwordEncoder().encode("123456"),
                        "Nguyễn Thanh Đăng Khoa",
                        "0912345684",
                        "khoa@basico.com.vn",
                        "ACTIVE",
                        Role.CONSULTANT
                );
                userRepository.save(khoa);

                StaffProfile profileKhoa = StaffProfile.builder()
                        .yearExperience("3+ năm")
                        .licenseNumber("LS007")
                        .bio("Cố vấn pháp lý BASICO.")
                        .verified(true)
                        .avatarUrl("https://basico.com.vn/wp-content/uploads/2020/12/NguyenThanhDangKhoa.jpg")
                        .specialties("Dịch vụ Luật sư nội bộ, M&A, Ngân hàng, Chứng khoán, Pháp lý doanh nghiệp")
                        .user(khoa)
                        .services(List.of(
                                serviceMap.get("Dịch vụ luật sư nội bộ"),
                                serviceMap.get("Tư vấn M&A"),
                                serviceMap.get("Ngân hàng và tài chính"),
                                serviceMap.get("Thị trường vốn"),
                                serviceMap.get("Doanh nghiệp")
                        ))
                        .rating(0f)
                        .cases(39)
                        .description("Chuyên gia về thuế và tuân thủ pháp lý cho doanh nghiệp.")
                        .build();
                staffProfileRepository.save(profileKhoa);
            }

            // Form 1:
            String templateName = "Đơn xin giảm nhẹ trách nhiệm hình sự";
            Optional<FormTemplate> existing = formTemplateRepository.findByName(templateName);
            if (existing.isEmpty()) {
                String jsonStructure = "{\n" +
                        "  \"title\": \"ĐƠN XIN GIẢM NHẸ TRÁCH NHIỆM HÌNH SỰ\",\n" +
                        "  \"fields\": [\n" +
                        "    { \"key\": \"vuAn\", \"label\": \"Tên vụ án\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"doiTuong\", \"label\": \"Tên bị can/bị cáo\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"toiDanh\", \"label\": \"Tội danh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"coQuan1\", \"label\": \"Cơ quan điều tra\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"tinh1\", \"label\": \"Tỉnh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"coQuan2\", \"label\": \"Viện kiểm sát\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"tinh2\", \"label\": \"Tỉnh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"coQuan3\", \"label\": \"Tòa án\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"tinh3\", \"label\": \"Tỉnh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"hoTen\", \"label\": \"Họ tên người làm đơn\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"namSinh\", \"label\": \"Năm sinh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"cmnd\", \"label\": \"CMND/CCCD\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"ngayCap\", \"label\": \"Ngày cấp CMND\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"diaChi\", \"label\": \"Địa chỉ hiện tại\", \"type\": \"textarea\" },\n" +
                        "    { \"key\": \"hoKhau\", \"label\": \"Hộ khẩu thường trú\", \"type\": \"textarea\" },\n" +
                        "    { \"key\": \"choOHienTai\", \"label\": \"Nơi ở hiện tại\", \"type\": \"textarea\" },\n" +
                        "    { \"key\": \"lyDoGiamNhe\", \"label\": \"Lý do giảm nhẹ\", \"type\": \"textarea\" },\n" +
                        "    { \"key\": \"noiLamDon\", \"label\": \"Nơi làm đơn\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"ngay\", \"label\": \"Ngày\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"thang\", \"label\": \"Tháng\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"nam\", \"label\": \"Năm\", \"type\": \"text\" }\n" +
                        "  ]\n" +
                        "}";

                String htmlTemplate = "<html>\n" +
                        "  <body style=\"font-family: Times New Roman; font-size: 16px; line-height: 1.6;\">\n" +
                        "    <div style=\"text-align: center;\">\n" +
                        "      <strong>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</strong><br />\n" +
                        "      <em>Độc lập – Tự do – Hạnh phúc</em><br />\n" +
                        "      <hr style=\"width: 200px; margin: 10px auto;\" />\n" +
                        "      <h3 style=\"margin: 10px 0;\">ĐƠN XIN GIẢM NHẸ TRÁCH NHIỆM HÌNH SỰ</h3>\n" +
                        "      <p><strong>(Đối với {{doiTuong}} trong vụ án {{vuAn}})</strong></p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <p><strong>Kính gửi:</strong></p>\n" +
                        "    <ul>\n" +
                        "      <li>Cơ quan Cảnh sát điều tra công an huyện/quận {{coQuan1}}, tỉnh {{tinh1}}</li>\n" +
                        "      <li>Viện kiểm sát nhân dân huyện/quận {{coQuan2}}, tỉnh {{tinh2}}</li>\n" +
                        "      <li>Tòa án nhân dân huyện/quận {{coQuan3}}, tỉnh {{tinh3}}</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <p>\n" +
                        "Tôi là: <strong>{{hoTen}}</strong>, sinh năm <strong>{{namSinh}}</strong><br />\n" +
                        "      Địa chỉ: {{diaChi}}<br />\n" +
                        "      CMND/CCCD số: {{cmnd}}, cấp ngày: {{ngayCap}}<br />\n" +
                        "      Nơi đăng ký hộ khẩu thường trú: {{hoKhau}}<br />\n" +
                        "      Chỗ ở hiện tại: {{choOHienTai}}<br />\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p>\n" +
                        "      Tôi là người bị hại trong vụ án {{vuAn}} do bị can/bị cáo {{doiTuong}} gây ra. Hiện vụ án đang được Quý cơ quan tiến hành Điều tra/Truy tố, xét xử về tội {{toiDanh}}.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p>\n" +
                        "      Ngay từ khi xảy ra vụ việc, chúng tôi nhận thấy bản thân bị can, bị cáo và người thân, người nhà của bị can, bị cáo đã ăn năn hối cải, thành khẩn khai báo, tích cực giúp đỡ cơ quan điều tra điều tra vụ việc, bồi thường thiệt hại kịp thời, khắc phục những hậu quả gây ra.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p>{{lyDoGiamNhe}}</p>\n" +
                        "\n" +
                        "    <p>\n" +
                        "      Nay, tôi làm đơn này để nghị Quý các cơ quan xem xét và giảm nhẹ trách nhiệm hình sự cho bị can/bị cáo {{doiTuong}}.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p>\n" +
                        "      Tôi làm đơn này trong điều kiện sức khỏe tốt, hoàn toàn tỉnh táo, minh mẫn, không bị bất kỳ sự cưỡng ép, đe dọa nào, việc làm đơn này là đúng với ý chí và nguyện vọng của tôi.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p>\n" +
                        "      Tôi xin chịu trách nhiệm trước pháp luật về toàn bộ nội dung của đơn này và những điều tôi đã cam kết, trình bày tại đơn này.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p>Kính đề nghị Quý cơ quan xem xét, tạo điều kiện giúp đỡ.</p>\n" +
                        "    <p>Tôi xin chân thành cảm ơn!</p>\n" +
                        "\n" +
                        "    <div style=\"text-align: right; margin-top: 30px;\">\n" +
                        "      {{noiLamDon}}, ngày {{ngay}} tháng {{thang}} năm {{nam}}<br />\n" +
                        "      <strong>Người làm đơn</strong>\n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>";

                FormTemplate template = new FormTemplate();
                template.setId(UUID.randomUUID());
                template.setName(templateName);
                template.setCategory("Hình sự");
                template.setJsonStructure(jsonStructure);
                template.setHtmlTemplate(htmlTemplate);

                formTemplateRepository.save(template);
            }

            // Form 2: Đơn nhờ luật sư trợ giúp pháp lý
            String templateName2 = "Đơn nhờ luật sư trợ giúp pháp lý";
            Optional<FormTemplate> existing2 = formTemplateRepository.findByName(templateName2);
            if (existing2.isEmpty()) {
                String jsonStructure2 = "{\n" +
                        "  \"title\": \"ĐƠN NHỜ LUẬT SƯ THAM GIA TRỢ GIÚP PHÁP LÝ\",\n" +
                        "  \"fields\": [\n" +
                        "    { \"key\": \"tenCongTyLuat\", \"label\": \"Tên công ty luật\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"hoTen\", \"label\": \"Họ và tên\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"dienThoai\", \"label\": \"Số điện thoại\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"namSinh\", \"label\": \"Năm sinh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"cmnd\", \"label\": \"CMND/CCCD\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"noiCap\", \"label\": \"Nơi cấp CMND\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"thuongTru\", \"label\": \"Thường trú\", \"type\": \"textarea\" },\n" +
                        "    { \"key\": \"vaiTro\", \"label\": \"Vai trò trong vụ án\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"vuAn\", \"label\": \"Tên vụ án\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"doiTuong\", \"label\": \"Đối tượng/với ai\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"soThuLy\", \"label\": \"Số thụ lý\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"ngayThuLy\", \"label\": \"Ngày thụ lý\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"toaAnNhanDan\", \"label\": \"Tên tòa án\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"noiLamDon\", \"label\": \"Nơi làm đơn\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"ngay\", \"label\": \"Ngày\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"thang\", \"label\": \"Tháng\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"nam\", \"label\": \"Năm\", \"type\": \"text\" }\n" +
                        "  ]\n" +
                        "}";

                String htmlTemplate2 = "<html>\n" +
                        "    <body style=\"font-family: Times New Roman; font-size: 16px; line-height: 1.6;\">\n" +
                        "      <div style=\"text-align: center;\">\n" +
                        "        <strong>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</strong><br />\n" +
                        "        <em>Độc lập – Tự do – Hạnh phúc</em><br />\n" +
                        "        <p>—o0o—</p>\n" +
                        "        <h3 style=\"margin: 10px 0;\">ĐƠN NHỜ LUẬT SƯ THAM GIA TRỢ GIÚP PHÁP LÝ VÀ BẢO VỆ QUYỀN LỢI</h3>\n" +
                        "      </div>\n" +
                        "\n" +
                        "      <p><strong>Kính gửi:</strong> CÔNG TY LUẬT {{tenCongTyLuat}}</p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Tôi tên: <strong>{{hoTen}}</strong> &nbsp;&nbsp;&nbsp;\n" +
                        "        Điện thoại: <strong>{{dienThoai}}</strong><br />\n" +
                        "        Sinh năm: <strong>{{namSinh}}</strong><br />\n" +
                        "        CMND/CCCD số: <strong>{{cmnd}}</strong> &nbsp;&nbsp;&nbsp; Cấp tại: <strong>{{noiCap}}</strong><br />\n" +
                        "        Thường trú: {{thuongTru}}\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Tôi là {{vaiTro}} trong vụ {{vuAn}} với {{doiTuong}} theo Thông báo thụ lý số {{soThuLy}}, ngày {{ngayThuLy}} của Tòa án nhân dân {{toaAnNhanDan}}.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Nay tôi làm đơn này đề nghị CÔNG TY LUẬT {{tenCongTyLuat}} cử luật sư tham gia bảo vệ quyền và lợi ích hợp pháp cho tôi trong vụ án nêu trên tại các cơ quan có thẩm quyền.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "Tôi cam kết không thắc mắc hoặc khiếu nại gì về sau.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Xin chân thành cảm ơn. Trân trọng kính chào.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <div style=\"text-align: right; margin-top: 30px;\">\n" +
                        "        {{noiLamDon}}, ngày {{ngay}} tháng {{thang}} năm {{nam}}<br />\n" +
                        "        <strong>Kính đơn</strong>\n" +
                        "      </div>\n" +
                        "    </body>\n" +
                        "  </html>";

                FormTemplate template2 = new FormTemplate();
                template2.setId(UUID.randomUUID());
                template2.setName(templateName2);
                template2.setCategory("Dân sự");
                template2.setJsonStructure(jsonStructure2);
                template2.setHtmlTemplate(htmlTemplate2);

                formTemplateRepository.save(template2);
            }

            // Form 3: Đơn nhờ luật sư (v2)
            String templateName3 = "Đơn nhờ luật sư (v2)";
            Optional<FormTemplate> existing3 = formTemplateRepository.findByName(templateName3);
            if (existing3.isEmpty()) {
                String jsonStructure3 = "{\n" +
                        "  \"title\": \"ĐƠN NHỜ LUẬT SƯ THAM GIA TRỢ GIÚP PHÁP LÝ VÀ BẢO VỆ QUYỀN LỢI\",\n" +
                        "  \"fields\": [\n" +
                        "    { \"key\": \"tenCongTyLuat\", \"label\": \"Tên công ty luật\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"hoTen\", \"label\": \"Họ và tên\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"dienThoai\", \"label\": \"Số điện thoại\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"namSinh\", \"label\": \"Năm sinh\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"cmnd\", \"label\": \"CMND/CCCD\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"noiCap\", \"label\": \"Nơi cấp CMND\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"thuongTru\", \"label\": \"Thường trú\", \"type\": \"textarea\" },\n" +
                        "    { \"key\": \"vaiTro\", \"label\": \"Vai trò trong vụ\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"vuAn\", \"label\": \"Tên vụ\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"doiTuong\", \"label\": \"Người liên quan\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"soThuLy\", \"label\": \"Số thụ lý\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"ngayThuLy\", \"label\": \"Ngày thụ lý\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"toaAn\", \"label\": \"Tòa án nhân dân\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"noiLamDon\", \"label\": \"Nơi làm đơn\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"ngay\", \"label\": \"Ngày\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"thang\", \"label\": \"Tháng\", \"type\": \"text\" },\n" +
                        "    { \"key\": \"nam\", \"label\": \"Năm\", \"type\": \"text\" }\n" +
                        "  ]\n" +
                        "}";

                String htmlTemplate3 = "<html>\n" +
                        "    <body style=\\\"font-family: Times New Roman; font-size: 16px; line-height: 1.6;\\\">\n" +
                        "      <div style=\\\"text-align: center;\\\">\n" +
                        "        <strong>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</strong><br />\n" +
                        "        <em>Độc lập – Tự do – Hạnh phúc</em><br />\n" +
                        "        <p>—o0o—</p>\n" +
                        "        <h3 style=\\\"margin: 10px 0;\\\">ĐƠN NHỜ LUẬT SƯ THAM GIA TRỢ GIÚP PHÁP LÝ VÀ BẢO VỆ QUYỀN LỢI</h3>\n" +
                        "      </div>\n" +
                        "\n" +
                        "      <p><strong>Kính gửi:</strong> CÔNG TY LUẬT {{tenCongTyLuat}}</p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Tôi tên: <strong>{{hoTen}}</strong> &nbsp;&nbsp;&nbsp;\n" +
                        "        Điện thoại: <strong>{{dienThoai}}</strong><br />\n" +
                        "        Sinh năm: <strong>{{namSinh}}</strong><br />\n" +
                        "        CMND số: <strong>{{cmnd}}</strong> &nbsp;&nbsp;&nbsp; Cấp tại: <strong>{{noiCap}}</strong><br />\n" +
                        "        Thường trú: {{thuongTru}}\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Tôi là {{vaiTro}} trong vụ {{vuAn}} với {{doiTuong}}, theo Thông báo thụ lý số {{soThuLy}}, ngày {{ngayThuLy}} của Tòa án nhân dân {{toaAn}}.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Nay tôi làm đơn này đề nghị <strong>CÔNG TY LUẬT {{tenCongTyLuat}}</strong> cử luật sư tham gia bảo vệ quyền và lợi ích hợp pháp cho tôi trong vụ án nêu trên tại các cơ quan có thẩm quyền.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "Tôi cam kết không thắc mắc hoặc khiếu nại gì về sau.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <p>\n" +
                        "        Xin chân thành cảm ơn. Trân trọng kính chào.\n" +
                        "      </p>\n" +
                        "\n" +
                        "      <div style=\\\"text-align: right; margin-top: 30px;\\\">\n" +
                        "        {{noiLamDon}}, ngày {{ngay}} tháng {{thang}} năm {{nam}}<br />\n" +
                        "        <strong>Kính đơn</strong>\n" +
                        "      </div>\n" +
                        "    </body>\n" +
                        "  </html>";

                FormTemplate template3 = new FormTemplate();
                template3.setId(UUID.randomUUID());
                template3.setName(templateName3);
                template3.setCategory("Dân sự");
                template3.setJsonStructure(jsonStructure3);
                template3.setHtmlTemplate(htmlTemplate3);

                formTemplateRepository.save(template3);
            }


        };
    }
}
