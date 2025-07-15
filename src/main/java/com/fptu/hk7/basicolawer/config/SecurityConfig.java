package com.fptu.hk7.basicolawer.config;

import com.fptu.hk7.basicolawer.config.JWT.JWTTokenFilter;
import com.fptu.hk7.basicolawer.pojo.*;
import com.fptu.hk7.basicolawer.enums.Role;
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
    public CommandLineRunner initData(FormTemplateRepository formTemplateRepository, UserRepository userRepository,
                                      StaffProfileRepository staffProfileRepository, ServiceRepository serviceRepository,
                                      CategoryRepository categoryRepository) {
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

            if (!userRepository.existsByUsername("nghia")) {
                User user = User.builder()
                        .username("nghia")
                        .password(passwordEncoder().encode("123456"))
                        .fullname("Le Huu Nghia")
                        .email("nghia@gmail.com")
                        .status("active")
                        .phone("0123456789")
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
                Map<String, Services> serviceMap = new HashMap<>();
                for (String sv : services) {
                    Services service = Services.builder()
                            .name(sv)
                            .price(2000000f)
                            .build();
                    serviceRepository.save(service);
                    serviceMap.put(sv, service);
                }

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
                        .htmlContent("<p>Trước khi trực tiếp điều hành&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>, Luật sư Trần Minh Hải đã thành công trong việc phụ trách và gây dựng hệ thống pháp chế cho nhiều Ngân hàng thương mại lớn, đảm nhiệm nhiều chức vụ quan trọng như:</p><ul><li><strong><em>Giám đốc Pháp chế Ngân hàng Quốc tế (VIB);</em></strong></li><li><strong><em>Giám đốc pháp chế Ngân hàng Bưu Điện Liên Việt (LienViet-PostBank);</em></strong></li><li><strong><em>Trưởng phòng pháp chế Ngân hàng Hàng Hải (Maritime Bank);</em></strong></li><li><strong><em>Thành viên HĐQT của Công ty Chứng khoán Quốc Tế (VIS);</em></strong></li><li><strong><em>Thành viên HĐQT Ngân hàng TMCP Phương Đông (OCB);…</em></strong></li></ul><p>Trên cương vị Giám đốc điều hành&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>, Luật sư Trần Minh Hải đã triển khai hàng loạt các Dự án tư vấn về tái cấu trúc hoạt động, định chế hóa xây dựng quy chế, quy định, quy trình nghiệp vụ tín dụng, bảo đảm tiền vay, xây dựng hệ thống mẫu biểu nghiệp vụ tín dụng, bảo đảm tiền vay, xây dựng mẫu biểu, quy trình chuyên sâu cho các giao dịch nguồn vốn, phái sinh… cho rất nhiều ngân hàng và tổ chức tín dụng như: Ngân hàng Phương Đông OCB, Ngân hàng Quân đội MB, Ngân hàng Quốc Tế VIB, Ngân hàng Hongleong Bank Việt Nam, Ngân hàng Á Châu ACB, Công ty Tài chính Xi Măng CFC…</p><p>Luật sư Trần Minh Hải đã cùng các cộng sự của mình triển khai nhiều Dự án tư vấn tái cơ cấu tổ chức hoạt động và cung cấp giải pháp pháp lý về kinh doanh cho nhiều tập đoàn, doanh nghiệp.</p><p>Luật sư Trần Minh Hải đã tham gia tiền tố tụng và tranh tụng trong hàng trăm vụ án của ngành ngân hàng trong thời gian qua và đem lại hiệu quả thiết thực trong bào chữa, bảo vệ quyền lợi hợp pháp cho nhiều cán bộ ngân hàng.</p><p>Luật sư Trần Minh Hải được đánh giá là một trong những chuyên gia nghiên cứu, phản biện sâu sắc về pháp luật ngân hàng, đầu tư, kinh doanh với hàng trăm bài viết nghiên cứu, trả lời phỏng vấn chuyên sâu được phổ biến trên các báo: Đầu tư Chứng khoán, Thời báo Ngân hàng, Pháp luật Việt Nam, Sài Gòn Đầu tư Tài chính… và hàng trăm chương trình phỏng vấn, đối thoại trên các kênh truyền hình về các vấn đề liên quan đến pháp luật ngân hàng, đầu tư, kinh doanh thương mại.</p><p>Luật sư Trần Minh Hải đã và đang là chỗ dựa pháp lý vững chắc cho cộng đồng ngân hàng và doanh nghiệp.</p><h3>Lĩnh vực đảm nhiệm:</h3><ul><li><strong><em>M&amp;A</em></strong></li><li><strong><em>Doanh nghiệp</em></strong></li><li><strong><em>Ngân hàng</em></strong></li><li><strong><em>Thuế – tài chính doanh nghiệp</em></strong></li><li><strong><em>Giao dịch vốn, chứng khoán</em></strong></li><li><strong><em>Bất động sản</em></strong></li><li><strong><em>Dịch vụ tranh tụng</em></strong></li></ul><h3>Các dự án đã thực hiện:</h3><ul><li><strong><em>Ngân hàng:</em></strong></li></ul><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tái cấu trúc nợ cho Ngân hàng TMCP Á Châu (ACB);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tái cấu trúc ngân hàng cho Ngân hàng TMCP Phương Đông (OCB);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án Định chế hóa cơ cấu tổ chức cho Ngân hàng TMCP Phương Tây (Western Bank);</p><p>–&nbsp;&nbsp;Dự án tư vấn thường xuyên, tư vấn Cấu trúc tổ chức và xây dựng Hệ thống tổng thể các quy trình nghiệp ngân hàng cho Ban Trù bị thành lập Ngân hàng TMCP Công nghiệp Việt Nam (InBank);</p><p>–&nbsp;&nbsp;Dự án tư vấn tái cấu trúc và xây dựng Hệ thống tổng thể các quy trình nghiệp vụ ngân hàng cho Ngân hàng TMCP Hải Hưng (nay là Ngân hàng TMCP Đại Dương – OCEAN BANK);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn cho Ban Trù bị thành lập Ngân hàng Hong Leong Bank Việt&nbsp;Nam;</p><p>–&nbsp;&nbsp;&nbsp;Dự án tư vấn tái cơ cấu, hoàn thiện tổng thể hệ thống nghiệp vụ quy trình cho Công ty Tài chính Bưu Điện (PTFinance);</p><p>–&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng hệ thống Phân định trách nhiệm trong nghiệp vụ tín dụng cho Ngân hàng TMCP Phương Đông (OCB);</p><p>–&nbsp;&nbsp;&nbsp;Dự án Quản trị và Nâng cấp chất lượng Hệ thống văn bản định chế cho Ngân hàng TMCP Phương Đông (OCB);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng hệ thống mẫu biểu về nghiệp vụ tín dụng, bảo đảm tiền vay cho Ngân hàng TMCP Quân Đội (MB);</p><p>–&nbsp;&nbsp;Dự án tư vấn thường xuyên, tư vấn xây dựng Cấu trúc tổ chức và Hệ thống tổng thể các quy trình nghiệp ngân hàng cho Ban Trù bị thành lập Ngân hàng TMCP Công nghiệp Việt Nam (InBank);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn tái cấu trúc và xây dựng Hệ thống tổng thể các quy trình nghiệp ngân hàng cho Ngân hàng TMCP Hải Hưng (nay là Ngân hàng TMCP Đại Dương – OCEAN BANK);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng Hệ thống định chế khung và Hệ thống mẫu biểu về nghiệp vụ tín dụng, bảo đảm tiền vay cho Ngân hàng TMCP Phương Đông (OCB);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng Hệ thống định chế khung và Hệ thống mẫu biểu về nghiệp vụ tín dụng, bảo đảm tiền vay cho Ngân hàng TMCP Phương Tây (Western Bank);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tập huấn toàn diện về pháp luật tài chính, ngân hàng của Việt Nam cho cán bộ nhân viên Tập đoàn Hong Leong Bank Malaysia tại Kualalumpur, Malaysia;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng Hệ thống xếp hạng tín dụng nội bộ cho Ngân hàng TMCP Đại Tín (TrustBank);</p><p>–&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng Hệ thống quy trình nghiệp vụ tín dụng và Hệ thống mẫu biểu nghiệp vụ tín dụng, bảo đảm tiền vay cho Công ty Tài chính Xi Măng (CFC);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn xây dựng Hệ thống mẫu biểu sản phẩm phái sinh hoán đổi lãi suất cho Ngân hàng TMCP Quân đội (MB);</p><p>–&nbsp;&nbsp;&nbsp;…</p><ul><li><strong><em>Doanh nghiệp, M&amp;A, bất động sản và một số lĩnh vực:</em></strong></li></ul><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn tái cấu trúc chi tiết và hoàn thiện cơ chế pháp lý quản trị điều hành, nghiệp vụ cho Công ty Cổ phần Mạng thanh toán VINA (PAYNET);</p><p>–&nbsp;&nbsp;&nbsp;Dự án tư vấn Định chế hóa quản trị điều hành Công ty TNHH Chứng khoán CIMB-Vinashin (CIMB-Vinashin);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn cho Công ty Dược phẩm Trung ương 1 trong quá trình hợp tác đầu tư với Công ty Cổ phần Đầu tư Phát triển Nhà Constrexim để xây dựng Khu nhà ở chung cư cao tầng Dược phẩm TW1;</p><p>–&nbsp;&nbsp;Dự án tư vấn cho giao dịch Hợp đồng hợp tác kinh doanh giữa Megaland Việt Nam và Constrexim 1 để đầu tư xây dựng Công trình Hỗn hợp HH1A và HH1B theo tiêu chuẩn toà nhà văn phòng hạng A tại Tây Mỗ, Từ Liêm, thành phố Hà Nội;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn hợp tác đầu tư xây dựng và chuyển giao một phần diện tích Tòa nhà Sailing Tower 51 Nguyễn Thị Minh Khai, quận 1,TP Hồ Chí Minh giữa Ngân hàng TMCP Quốc Tế Việt Nam (VIBBank) và Tổng Công ty Xây dựng số 1 (CC1);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn hợp tác đầu tư xây dựng và chuyển giao một phần diện tích Tòa nhà Sailing Tower 51 Nguyễn Thị Minh Khai, quận 1,TP Hồ Chí Minh giữa Công ty Cổ phần Chứng khoán Quốc Tế Việt Nam (VISecurities) và Tổng Công ty Xây dựng số 1 (CC1);</p><p>–&nbsp;&nbsp;Dự án hợp tác đầu tư xây dựng Tòa nhà CornerStone, 16 Phan Chu Trinh, Hoàn Kiếm, Hà Nội giữa một ngân hàng và một doanh nghiệp Nhà nước cổ phần hóa;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án hợp tác đầu tư xây dựng Toà nhà D2 Giảng Võ, phường Giảng Võ, quận Ba Đình, Hà Nội giữa một Công ty Tài chính với một Công ty Bất động sản;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án hợp nhất giữa Công ty cổ phần chứng khoán Đại Tây Dương (OSC) và Công ty cổ phần Chứng khoán Quốc tế (VIS);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án chuyển nhượng cổ phần trọng yếu giữa các cổ đông trọng yếu của Ngân hàng Phát triển Nhà TP Hồ Chí Minh HDBank vào năm 2010;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án chào bán cổ phần trọng yếu cho Nhà đầu tư chiến lược của Ngân hàng Phương Đông OCB năm 2010;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án chuyển nhượng cổ phần trọng yếu giữa các cổ đông trọng yếu của Ngân hàng Quốc Tế VIB;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn chuyển nhượng vốn góp tại Công ty Tài chính TNHH Một thành viên Công nghiệp Tàu thủy (VFC);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Dự án tư vấn mua lại cổ phần của cổ đông làm cổ phiếu quỹ cho Công ty Cổ phần Quản lý Quỹ Đầu tư Việt Nam (VFM);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Nhiều giao dịch M&amp;A chuyển nhượng công ty mục tiêu là các doanh nghiệp Nhà nước cổ phần hóa, công ty cổ phần, công ty 100% vốn nước ngoài…</p><ul><li><strong><em>Tranh tụng và giải quyết tranh chấp:</em></strong></li></ul><p>–&nbsp;&nbsp;Đại án Huyền Như: Luật sư bảo vệ quyền lợi cho Công ty Chứng khoán Phương Đông (ORS) và kiêm vai trò Luật sư bào chữa cho một Giám đốc Quan hệ Khách hàng của Ngân hàng Quốc Tế VIB;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Đại án VDB Đăk Nông: Luật sư bảo vệ quyền lợi cho Ngân hàng Phương Đông (OCB);</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Đại án VNCB: Luật sư bào chữa cho ông Phạm Công Danh, nguyên Chủ tịch HĐQT VNCB;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Đại án BIDV – Dự án Bình Hà: Luật sư bào chữa cho ông Trần Lục Lang, nguyên Phó Tổng Giám đốc BIDV;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Đại án “Vi phạm quy định về quản lý, sử dụng tài sản Nhà nước gây thất thoát, lãng phí” tại Dự án Nhà, đất 15 Thi Sách: Luật sư bào chữa cho ông Nguyễn Hữu Tín, nguyên Phó Chủ tịch UBND thành phố Hồ Chí Minh;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Đại án “Vi phạm quy định về quản lý đất đai” tại Dự án Sabeco Pearl số 2-4-6 Hai Bà Trưng, TP Hồ Chí Minh: Luật sư bào chữa cho ông Nguyễn Hữu Tín, nguyên Phó Chủ tịch UBND thành phố Hồ Chí Minh;</p><p>–&nbsp;&nbsp;&nbsp;&nbsp;Luật sư của nhiều ngân hàng, cán bộ ngân hàng, doanh nghiệp, quan chức Nhà nước trong nhiều vụ án về kinh doanh thương mại, tội phạm chức vụ…</p><h3>Ngôn ngữ:</h3><ul><li><strong><em>Tiếng Việt</em></strong></li><li><strong><em>Tiếng Anh</em></strong></li></ul><h3>Xuất bản:</h3><ul><li><a href=\"http://banktraining.com.vn/vi-VN/News/2012/89/55/Cam-nang-nghe-ngan-hang.aspx\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">Sách Hiểu nghề giữ nghiệp 1: 26 bài học pháp lý nghiệp vụ dành cho nghề tín dụng ngân hàng, NXB Lao Động, 2015</a>;</li><li><a href=\"https://www.facebook.com/S%C3%A1ch-Hi%E1%BB%83u-ngh%E1%BB%81-Gi%E1%BB%AF-nghi%E1%BB%87p-1635997603355519/?fref=ts\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">Sách Hiểu nghề giữ nghiệp 2: 30 bài học pháp lý nghiệp vụ dành cho nghề teller ngân hàng, NXB Đại học Kinh Tế, 2016</a>;</li><li>Hàng trăm bài viết trên các Báo chuyên ngành tài chính, pháp luật tại Việt Nam…</li></ul><p><br></p>")
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
                        .htmlContent("<p>Gắn bó với&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong><strong>&nbsp;</strong>từ những ngày đầu hoạt động, Luật sư Ngọc Anh có sự đầu tư, nghiên cứu sâu về thị trường vốn và các hoạt động nghiệp vụ của các định chế tài chính, đặc biệt là tổ chức tín dụng tại thị trường Việt Nam. Luật sư Ngọc Anh đã đảm trách nhiều Dự án nghiệp vụ quan trọng góp phần hoàn thiện về quy trình, công cụ kiểm soát pháp lý, giảm thiểu rủi ro nghiệp vụ cho nhiều ngân hàng lớn tại Việt Nam; là tác giả và đồng tác giả của nhiều hệ thống quy chế, quy định, quy trình, chính sách trọng yếu, mẫu biểu nghiệp vụ mà&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong><strong>&nbsp;</strong>đã xây dựng cho các ngân hàng lớn tại Việt Nam.</p><p>Luật sư Ngọc Anh cũng đã đảm trách thành công nhiều giao dịch tư vấn thuộc các lĩnh vực về pháp lý doanh nghiệp, ngân hàng, M&amp;A, giao dịch thị trường vốn. Đồng thời, rất nhiều vụ án tranh chấp kinh doanh được được Luật sư Ngọc Anh tham gia và đáp ứng những nhu cầu giải pháp của khách hàng.</p><p>Ngoài ra, Luật sư Nguyễn Thị Ngọc Anh còn là giảng viên tại Đại học Kinh tế Đại học Quốc gia Hà Nội về môn Pháp luật Tài chính Ngân hàng và là người điều hành hoạt động của Công ty Cổ phần Đào tạo Nghiệp vụ Ngân hàng – SBankTraining</p><p><br></p><p>Lĩnh vực đảm n<strong>hiệm</strong></p><p><br></p><ul><li>Pháp lý doanh<strong><em> nghiệp;</em></strong></li></ul><p><br></p><p>Ngân hàng;</p><p><br></p><p>M&amp;A;</p><p><br></p><ul><li>Giao dịch <strong><em>thị trường vốn;</em></strong></li></ul><p><br></p><ul><li>Giải quyế<strong><em>t tranh chấp.</em></strong></li></ul><p><br></p><p>Các dự á<strong>n đã thực hiện</strong></p><p><br></p><ul><li>Dự án xử lý nợ cho Ngân hàng TMCP Á Châu (ACB);</li><li>Dự án tái cấu trúc ngân hàng cho Ngân hàng TMCP Phương Đông (OCB);</li><li>Dự án Định chế hóa cơ cấu tổ chức cho Ngân hàng TMCP Phương Tây (Western Bank);</li><li>Dự án tư vấn xây dựng hệ thống Phân định trách nhiệm trong nghiệp vụ tín dụng cho Ngân hàng TMCP Phương Đông (OCB);</li><li>Dự án Quản trị và Nâng cấp chất lượng Hệ thống văn bản định chế cho Ngân hàng TMCP Phương Đông (OCB);</li></ul><p><br></p><ul><li>Dự án tư vấn xây dựng hệ thống mẫu biểu về nghiệp vụ tín dụng, bảo đảm tiền vay cho Ngân hàng TMCP Quân Đội (MB);</li></ul><p><br></p><ul><li>Dự án tư vấn xây dựng Hệ thống định chế khung và Hệ thống mẫu biểu về nghiệp vụ tín dụng, bảo đảm tiền vay cho Ngân hàng TMCP Phương Đông (OCB);</li></ul><p><br></p><ul><li>Dự án tư vấn xây dựng Hệ thống định chế khung và Hệ thống mẫu biểu về nghiệp vụ tín dụng, bảo đảm tiền vay cho Ngân hàng TMCP Phương Tây (Western Bank);</li></ul><p><br></p><ul><li>Dự án tư vấn xây dựng Hệ thống quy trình nghiệp vụ tín dụng và Hệ thống mẫu biểu nghiệp vụ tín dụng, bảo đảm tiền vay cho Công ty Tài chính Xi Măng (CFC);</li></ul><p><br></p><ul><li>Dự án tư vấn xây dựng Hệ thống định chế khung về tổ chức và hoạt động nghiệp vụ cho Công ty TNHH Tài chính Than – Khoáng sản Việt Nam (CMF);</li></ul><p><br></p><ul><li>Dự án tư vấn xây dựng Hệ thống quy trình nghiệp vụ và Hệ thống mẫu biểu nghiệp vụ chứng khoán cho Công ty Cổ phần Chứng khoán Châu Á (ASC).</li><li class=\"ql-indent-1\"><br></li><li>Dự án tư vấn Định chế hóa quản trị điều hành Công ty TNHH Chứng khoán CIMB-Vinashin (CIMB-Vinashin);</li><li>Dự án tư vấn xây dựng hệ thống mẫu biểu về nghiệp vụ kinh doanh bảo hiểm cho Tổng Công ty Cổ phần Bảo hiểm Quân Đội (MIC);</li><li>Dự án xây dựng và hoàn thiện hệ thống văn bản quản trị nội bộ của Công ty TNHH một thành viên Tài chính Than – Khoáng sản Việt Nam (CMF);</li><li>Dự án tư vấn tái cấu trúc chi tiết và hoàn thiện cơ chế pháp lý quản trị điều hành cho Công ty Cổ phần Quốc tế Sơn Hà;</li><li>Dự án xây dựng Đề án chiến lược phát triển đến năm 2017, tầm nhìn đến năm 2022 cho Viện Thiết kế, Bộ Quốc phòng (DCCD);</li><li>Dự án tư vấn định chế hóa bộ máy quản trị điều hành cho Công ty Cổ phần Du lịch và Thương mại Dân Chủ (Khách sạn Dân Chủ, Hotel de I’Opera).</li><li>Dự án tư vấn tái cấu trúc chi tiết và hoàn thiện cơ chế pháp lý quản trị điều hành cho CTCP Quốc tế Sơn Hà;</li><li>Dự án xây dựng Đề án chiến lược phát triển đến năm 2017, tầm nhìn đến năm 2022 cho Viện Thiết Kế Bộ Quốc phòng (DCCD);</li><li>Các Dự án tái cấu trúc nợ của doanh nghiệp;</li><li>Các Dự án tư vấn khác.</li></ul><p>Ngôn ngữ</p><p><br></p><p>Tiếng Việt</p><p><br></p><p>Tiếng Anh</p><p><br></p>")
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
                        .htmlContent("<p>Luật sư Nguyễn Đỗ Thùy Dương đã tham gia vào nhiều lĩnh vực khác nhau, đặc biệt tập trung vào các hoạt động quản trị doanh nghiệp, giao dịch vốn, và các giao dịch khác của doanh nghiệp.</p><p>Với nền tảng kiến thức pháp lý vững chắc, cùng phong thái tự tin và khả năng thương lượng tốt, Luật sư Nguyễn Đỗ Thùy Dương đang phụ trách tư vấn chuyên sâu trong lĩnh vực pháp lý doanh nghiệp, với khách hàng là các công ty ở nhiều lĩnh vực khác nhau từ chứng khoán, bất động sản, kinh doanh hạ tầng khu công nghiệp đến các công ty hoạt động trong lĩnh vực tài chính, đầu tư.</p><p>Có thể nói, sau nhiều năm tư vấn thường xuyên, gắn bó với hoạt động của các doanh nghiệp trong lĩnh vực tài chính, tham gia nhiều vụ việc tranh chấp, Luật sư Nguyễn Đỗ Thùy Dương là một trong những điểm tựa uy tín của Khách hàng khi đến với&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>.</p><h3>Lĩnh vực đảm nhiệm</h3><ul><li><strong><em>Dịch vụ Luật sư nội bộ</em></strong></li><li><strong><em>Pháp lý doanh nghiệp</em></strong></li><li><strong><em>Thị trường vốn</em></strong></li><li><strong><em>M&amp;A</em></strong></li><li><strong><em>Thuế, Tài chính doanh nghiệp</em></strong></li></ul><h3>Các dự án đã thực hiện</h3><ul><li>Tư vấn triển khai dự án hợp tác bán chéo sản phẩm giữa Công ty Cổ phần Chứng khoán TP. Hồ Chí Minh (“HSC”) và Ngân hàng TMCP Quốc tế Việt Nam (“VIB”);</li><li>Tư vấn cho Tổng Công ty Bến Thành – Trách nhiệm hữu hạn Một thành viên (“Benthanh Group”) giải quyết việc phân chia hoặc chuyển giao tài sản khi kết thúc thời hạn liên doanh với Công ty Norfolk Development Group PYT LTD (nay là Công ty Norfolk Development Group);</li><li>Tư vấn pháp lý cho Công ty Cổ phần Đầu tư Sao Phương Nam (“SPNI”) trong giao dịch vay hợp vốn của Ngân hàng TMCP Phương Đông (“OCB”), Ngân hàng TMCP Quốc tế Việt Nam (“VIB”) trị giá 1.000 tỷ đồng.</li><li>Tư vấn giao dịch M&amp;A cho Bên Mua là Công ty TNHH TT Investment nhằm mua lại Công ty mục tiêu từ GUOCOLAND VIETNAM (S) PTE LTD- một công ty thuộc Gouco Group Limited, một trong những tập đoàn bất động sản hàng đầu Singapore;</li><li>Tư vấn xây dựng cấu trúc kinh doanh và hệ thống mẫu biểu, quy trình cho Công ty Cổ phần Tư vấn Đầu tư Kim An trong lĩnh vực Fintech;</li><li>Tư vấn cho Tổng Công ty Cổ phần Vận tải Dầu khí (PV Trans) các rủi ro liên quan đến việc thay đổi thành viên Hội đồng quản trị tại Công ty phần Dịch vụ Vận tải Dầu khí Cửu Long (“PCT”)- công ty mà PV Trans đầu tư, góp vốn;</li><li>Tư vấn cho Công ty Cổ phần Đầu tư Phát triển Việt Hương (“VHIP”) trong việc giải quyết khủng hoảng xử lý khoản nợ lên đến 700 tỷ đồng liên quan đến Công Ty TNHH Liên Doanh Nam Phương Textile mà VHIP tham gia góp vốn;</li><li>Đại diện cho Công ty Trách Nhiệm Hữu Hạn Xây dựng Nam Hải trong vụ án giải quyết tranh chấp với Khách hàng liên quan đến các hợp đồng góp vốn đầu tư và xây dựng hạ tầng nhà ở tại Dự án Marine City ở Vũng Tàu;</li><li>Đại diện cho Công ty Trách nhiệm Hữu hạn Chứng khoán Yuanta Việt Nam trong vụ án khởi kiện khách hàng nhằm thu hồi nợ giao dịch ký quỹ chứng khoán tại VIAC;</li><li>Đại diện cho Công ty TNHH Chứng khoán ACB trong vụ án tranh chấp liên quan đến việc tổ chức bán đấu giá cổ phần thuộc sở hữu của Công ty đầu tư và kinh doanh vốn Nhà nước (“SCIC”);</li><li>Tư vấn pháp lý liên quan đến nghĩa vụ thuế nhập khẩu phát sinh đối với mặt hàng cần trục bánh lốp;</li><li>Tư vấn pháp lý và phương án làm việc, giải trình với cơ quan thuế liên quan đến nghĩa vụ nộp thuế thu nhập cá nhân phát sinh từ việc cung cấp phần mềm Content Views- phần mềm hỗ trợ (Plugin) cho trang wordpress.org- của Ông Phạm Văn Tân với kết quả xác định lại số thuế phải nộp từ 5 tỷ đồng còn 980 triệu đồng;</li><li>Tư vấn đánh giá pháp lý mô hình hỗ trợ tài chính đối với khách hàng cho Công ty Cổ phần Chứng khoán YUANTA Việt Nam (“YSVN”);</li><li>Tư vấn pháp lý khởi nghiệp cho Công ty TNHH Sản xuất Thương mại Dịch vụ Hương Đất- Chủ sở hữu thương hiệu Happy Vegi chuyên sản xuất rau hữu cơ và đã có mặt tại nhiều hệ thống siêu thị;…</li><li><br></li></ul><p>Ngôn ngữ</p><p>Tiếng Việt</p><p><br></p><p>Tiếng Anh</p><p><br></p><p>Báo chí:</p><ul><li>Bảo lưu quyền sở hữ<a href=\"https://tinnhanhchungkhoan.vn/bao-luu-quyen-so-huu-ngan-hang-khong-the-ngo-lo-post166524.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">u, ngân hàng không thể ngó lơ- Báo Đầu tư Chứng khoán</a></li></ul><p><br></p><ul><li>Con nợ xin “chết”,<a href=\"https://tinnhanhchungkhoan.vn/con-no-xin-chet-chu-no-lao-dao-post163294.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\"> chủ nợ lao đao- Báo Đầu tư Chứng khoán</a></li></ul><p><br></p><ul><li>Góp vốn chưa xong<a href=\"https://tinnhanhchungkhoan.vn/gop-von-chua-xong-dung-voi-dang-ky-post249598.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\"> đừng vội đăng ký- Báo Đầu tư Chứng khoán</a></li></ul><p><br></p><ul><li>Phân định thẩm q<a href=\"https://tinnhanhchungkhoan.vn/phan-dinh-tham-quyen-giao-dich-trong-doanh-nghiep-post234858.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">uyền giao dịch trong doanh nghiệp- Báo Đầu tư Chứng khoán</a></li></ul><p><br></p>")
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
                        .htmlContent("<p>Trước khi gia nhập&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>, Luật sư Tô Hồng Dung đã có nhiều năm kinh nghiệm làm việc đa dạng trong lĩnh vực pháp chế nội bộ doanh nghiệp. Luật sư Tô Hồng Dung đã tham gia vào nhiều lĩnh vực khác nhau, đặc biệt tập trung vào các hoạt động quản trị doanh nghiệp, tư vấn các giao dịch thương mại, M&amp;A.</p><p>Với nền tảng kiến thức pháp lý vững chắc, cùng phong thái tự tin và khả năng thương lượng tốt, Luật sư Tô Hồng Dung đang phụ trách tư vấn chuyên sâu trong lĩnh vực pháp lý doanh nghiệp, với khách hàng là các công ty ở nhiều lĩnh vực khác nhau từ doanh nghiệp sản xuất, xuất nhập khẩu đến các công ty hoạt động trong lĩnh vực tài chính, đầu tư.</p><p>Với sự nhiệt tình cùng khả năng nghiên cứu, phân tích và đưa ra phương án giải quyết vấn đề hiệu quả, Luật sư Tô Hồng Dung đã và đang nhận được sự tín nhiệm từ Khách hàng trong quá trình cộng tác, thực hiện tư vấn.</p><p><strong>Lĩnh vực đảm nhiệm</strong></p><ul><li>Bảo hiểm</li><li>Lao động</li><li>Tư vấn bất động sản</li><li>Đào tạo pháp lý cho doanh nghiệp</li><li>Tranh tụng và giải quyết tranh chấp</li></ul><p><strong>Các dự án đã tham gia</strong></p><ul><li>Dự án M&amp;A thông qua hình thức Hợp đồng chuyển nhượng phần vốn góp giữa các doanh nghiệp trong lĩnh vực logistics, giá trị giao dịch vào khoảng 9 triệu USD.</li><li>Dự án xây dựng quy trình, mẫu biểu cho Công ty trong lĩnh vực gia công hàng may mặc, xuất nhập khẩu tại nhiều thị trường lớn như Mỹ, Canada, Châu Âu.</li><li>Tư vấn pháp lý cho Dự án sản xuất và phân phối hàng may mặc tại thị trường trong nước, thông qua nhiều giai đoạn khác nhau như: thực hiện thủ tục chứng nhận hợp quy sản phẩm, thành lập hệ thống các địa điểm kinh doanh, đăng ký bảo hộ thành công kiểu dáng công nghiệp.</li><li>Tư vấn rà soát và hoàn thiện các văn bản, biểu mẫu liên quan đến Lao động, nhân sự cho doanh nghiệp.</li><li>Đại diện Khách hàng tham gia đàm phán và giải quyết tranh chấp liên quan đến Hợp đồng mua bán Căn hộ Du lịch với Chủ đầu tư.</li><li>Bảo vệ quyền và lợi ích hợp pháp cho các khách hàng mua Căn hộ Du lịch trong Vụ án liên quan đến Hợp đồng tín dụng giữa Chủ đầu tư và Ngân hàng (tài sản bảo đảm là toàn bộ Dự án bao gồm tất cả các Căn hộ Du lịch).</li><li>Tham gia Dự án Hoàn thiện hệ thống văn bản định chế cho Trường Đại học tư thục tại Thành phố Hồ Chí Minh.</li></ul><h3>Ngôn ngữ</h3><ul><li>Tiếng Việt</li><li>Tiếng Anh</li></ul><p><strong>Báo chí</strong></p><ul><li>Bài viết “Một số vấn đề về tuổi nghỉ hưu trong Dự thảo Bộ luật lao động (sửa đổi)” cùng với tác giả Nguyễn Tấn Hoàng Hải được đăng trên Tạp chí Tòa án nhân dân số 21/2017.</li><li>Bài viết “Góp ý Dự thảo Bộ luật lao động (sửa đổi) về giải quyết tranh chấp lao động bằng Hội đồng trọng tài lao động” cùng với tác giả Nguyễn Trần Bảo Uyên được đăng trên Tạp chí Nhà nước và pháp luật số 01/2018.</li><li>Bài viết “Pháp luật về nghĩa vụ công bố thông tin của doanh nghiệp nhà nước” được đăng trên Tạp chí Nhà nước và pháp luật số 04/2018.</li><li>Bài viết “Góp ý Dự thảo Luật Cạnh tranh (sửa đổi) về chính sách khoan hồng” cùng với tác giả Võ Ngọc Duy được đăng trên Tạp chí Nhà nước và pháp luật số 05/2018.</li><li>Bài viết “Kiểm soát giao dịch giữa công ty và người quản lý công ty đại chúng theo quy định pháp luật Việt Nam” cùng với tác giả Danh Phạm Mỹ Duyên được đăng trên Tạp chí Tòa án nhân dân số 11/2018.</li><li>Bài viết&nbsp;<a href=\"https://tinnhanhchungkhoan.vn/giai-phap-cho-thanh-vien-thieu-so-trong-cac-giao-dich-co-kha-nang-tu-loi-post252258.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">“Giải pháp cho thành viên thiểu số trong các giao dịch có khả năng tư lợi”</a>&nbsp;đăng trên Báo Đầu tư chứng khoán</li><li>Bài viết&nbsp;<a href=\"https://tinnhanhchungkhoan.vn/gop-von-bang-quyen-so-huu-tri-tue-quyen-su-dung-nhan-hieu-co-thoi-han-post252915.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">“Góp vốn bằng quyền sở hữu trí tuệ, quyền sử dụng nhãn hiệu có thời hạn”</a>&nbsp;đăng trên Báo Đầu tư chứng khoán</li><li>Bài viết&nbsp;<a href=\"https://tinnhanhchungkhoan.vn/hieu-nhu-the-nao-ve-hanh-vi-gay-thiet-hai-nghiem-trong-khi-ap-dung-hinh-thuc-xu-ly-ky-luat-sa-thai-post253902.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(45, 45, 45); background-color: transparent;\">“Hiểu như thế nào về “Hành vi gây thiệt hại nghiêm trọng”</a>&nbsp;khi áp dụng hình thức xử lý kỷ luật sa thải?” đăng trên Báo Đầu tư chứng khoá</li></ul><p><br></p>")
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
                        .htmlContent("<p>Từ khi gia nhập&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>&nbsp;đến nay, với hệ thống kiến thức pháp lý được đào tạo chuyên sâu và kinh nghiệm phong phú được tích lũy từ thực tiễn hành nghề, Cố vấn pháp lý Phạm Thúy Nga đã và đang phụ trách tư vấn thường xuyên cho các khách hàng là công ty quản lý quỹ, trường đại học tư thục, doanh nghiệp kinh doanh bất động sản, xăng dầu, sản xuất và kinh doanh hàng hoá, dịch vụ ở nhiều lĩnh vực khác nhau.</p><p>Bên cạnh đó, Cố vấn pháp lý Phạm Thuý Nga còn trực tiếp tham gia, hỗ trợ các Luật sư BASICO trong nhiều dự án tư vấn soạn thảo, rà soát các văn bản định chế, quy chế, quy trình, mẫu biểu kinh doanh, lao động cho nhiều doanh nghiệp, công ty chứng khoán, công ty quản lý quỹ; giao dịch M&amp;A.</p><p>Với tinh thần cầu tiến, ham học hỏi, luôn lắng nghe và thấu hiểu nhu cầu của khách hàng, Cố vấn pháp lý Phạm Thuý Nga đang ngày càng hoàn thiện và chuyên sâu hơn trong lĩnh vực mình phụ trách, nhận được sự tin tưởng, tín nhiệm từ các khách hàng.</p><p><strong>Lĩnh vực đảm nhiệm</strong></p><ul><li><strong><em>Dịch vụ Luật sư nội bộ</em></strong></li><li><strong><em>Pháp lý doanh nghiệp</em></strong></li><li><strong><em>Thị trường vốn</em></strong></li><li><strong><em>Tư vấn bất động sản</em></strong></li><li><strong><em>Lao động</em></strong></li></ul><h3>Các dự án đã thực hiện</h3><ul><li>Tư vấn triển khai dự án hợp tác bán chéo sản phẩm giữa Công ty Cổ phần Chứng khoán TP Hồ Chí Minh (HSC) và Ngân hàng TMCP Quốc tế Việt Nam (VIB);</li><li>Tư vấn giao dịch M&amp;A cho Bên Mua là Công ty TNHH TT Investment nhằm mua lại Công ty mục tiêu từ GUOCOLAND VIETNAM (S) PTE LTD – một công ty thuộc Gouco Group Limited, một trong những tập đoàn bất động sản hàng đầu Singapore;</li><li>Tư vấn cho Tổng Công ty Bến Thành – Trách nhiệm hữu hạn Một thành viên (“Benthanh Group”) giải quyết việc phân chia hoặc chuyển giao tài sản khi kết thúc thời hạn liên doanh với Công ty Norfolk Development Group PYT LTD (nay là Công ty Norfolk Development Group);</li><li>Tư vấn giao dịch chuyển nhượng nhãn hiệu; chuyển quyền sử dụng sáng chế;</li><li>Tư vấn xây dựng, rà soát mẫu biểu nghiệp vụ cho công ty quản lý quỹ;</li><li>Tư vấn xây dựng, hoàn thiện Điều lệ của nhiều doanh nghiệp;</li><li>Tư vấn pháp lý liên quan đến nghĩa vụ thuế nhập khẩu phát sinh đối với mặt hàng cần trục bánh lốp;</li><li>Các dự án tư vấn xây dựng, chuẩn hóa hệ thống quy chế, quy định, quy trình, chính sách trọng yếu, mẫu biểu nghiệp vụ, lao động của nhiều tập đoàn, doanh nghiệp.</li><li><br></li></ul><p>Ngôn ngữ</p><p>Tiếng Việt</p><p><br></p><p>Tiếng Anh</p><p><br></p>")
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
                        .htmlContent("<p>Trước khi gia nhập&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>, Cố vấn pháp lý Nguyễn Đức Anh đã có nhiều năm kinh nghiệm làm việc đa dạng trong các tổ chức công chứng, pháp chế nội bộ doanh nghiệp.</p><p>Bằng kinh nghiệm và hệ thống kiến thức chuyên sâu trong các lĩnh vực và hoạt động của doanh nghiệp, Cố vấn pháp lý Nguyễn Đức Anh đã tham gia tích cực vào việc tư vấn các giao dịch và dự án của doanh nghiệp như: Tham gia thẩm tra pháp lý doanh nghiệp (Legal Due Diligence) trong giao dịch M&amp;A; Tư vấn đàm phán hợp đồng mua bán hàng hoá, cung cấp dịch vụ của doanh nghiệp; Xây dựng và hoàn thiện nội dung điều lệ và các quy chế nội bộ doanh nghiệp; Tư vấn các tác động về quản trị, điều hành trong công ty trách nhiệm hữu hạn, công ty cổ phần,…</p><p>Với sự nhiệt tình cùng khả năng nghiên cứu, phân tích và đưa ra phương án giải quyết vấn đề hiệu quả, Cố vấn pháp lý Nguyễn Đức Anh đã và đang nhận được sự tín nhiệm từ Khách hàng trong quá trình cộng tác, thực hiện tư vấn.</p><p><strong>Lĩnh vực đảm nhiệm</strong></p><ul><li><strong><em>Dịch vụ Luật sư nội bộ</em></strong></li><li><strong><em>Pháp lý doanh nghiệp</em></strong></li><li><strong><em>Công nghệ thông tin và Truyền thông</em></strong></li><li><strong><em>Ngân hàng và tài chính</em></strong></li><li><strong><em>Tư vấn bất động sản</em></strong></li></ul><h3>Các dự án đã thực hiện</h3><ul><li>Tư vấn đàm phán hợp đồng quản lý khách sạn và cấp quyền sử dụng nhãn hiệu cho Dự án khu du lịch nghỉ dưỡng Amari Phú Quốc giữa Công ty Cổ phần Đầu tư – Thương mại – Dịch vụ Nhiệt Đới và Tập đoàn nghỉ dưỡng Onyx (thuộc Tập đoàn Italthai);</li><li>Tư vấn giao dịch M&amp;A cho Bên Mua là Công ty TNHH TT Investment nhằm mua lại Công ty mục tiêu từ GUOCOLAND VIETNAM (S) PTE LTD – một công ty thuộc Gouco Group Limited, một trong những tập đoàn bất động sản hàng đầu Singapore;</li><li>Tư vấn xây dựng cấu trúc kinh doanh và hệ thống mẫu biểu, quy trình cho Công ty TNHH Viaconto, Công ty TNHH Microtech DFS Việt Nam trong lĩnh vực Fintech;</li><li>Tư vấn giao dịch chuyển nhượng nhãn hiệu; chuyển quyền sử dụng nhãn hiệu;</li><li>Tư vấn xây dựng, hoàn thiện Điều lệ của nhiều doanh nghiệp;</li><li>Tư vấn pháp lý liên quan đến nghĩa vụ thuế thu nhập cá nhân của các cá nhân phát sinh thu nhập từ Google, Facebook, thương mại điện tử;</li><li>Các dự án tư vấn xây dựng, chuẩn hóa hệ thống quy chế, quy định, quy trình, chính sách quan trọng, mẫu biểu nghiệp vụ, lao động của nhiều tập đoàn, doanh nghiệp.</li><li><br></li></ul><h3>Ngôn ngữ</h3><ul><li>Tiếng<strong><em> Việt</em></strong></li></ul><p><br></p><ul><li>Tiến<strong><em>g Anh</em></strong></li></ul><p><br></p>")
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
                        .htmlContent("<p>Trước khi gia nhập&nbsp;<strong>BA</strong><strong style=\"color: rgb(255, 0, 0);\">S</strong><strong>I</strong><strong style=\"color: rgb(255, 0, 0);\">CO</strong>, Cố vấn pháp lý Nguyễn Thanh Đăng Khoa đã có kinh nghiệm tư vấn cho các doanh nghiệp hoạt động trong nhiều lĩnh vực khác nhau như bảo hiểm, bất động sản, công ty quản lý quỹ đầu tư, v.v…</p><p>Với nền tảng kiến thức pháp lý vững chắc, cùng khả năng giao tiếp, sử dụng ngoại ngữ tốt, Cố vấn pháp lý Nguyễn Thanh Đăng Khoa đã phụ trách tư vấn các lĩnh vực về pháp lý doanh nghiệp, M&amp;A, đầu tư, với khách hàng là các doanh nghiệp trong nước và doanh nghiệp có vốn đầu tư nước ngoài hoạt động ở nhiều lĩnh vực như chứng khoán, bất động sản, đầu tư, bao gồm cả các tổ chức tín dụng.</p><p>Có thể nói, thông qua quá trình học tập, rèn luyện, nghiên cứu khoa học tại Trường Đại học Luật Thành phố Hồ Chí Minh, cũng như quá trình tham gia tư vấn cho các khách hàng, Cố vấn pháp lý Nguyễn Thanh Đăng Khoa đã tích lũy được nhiều kiến thức pháp lý quan trọng, từ đó có thể hỗ trợ các khách hàng một cách hiệu quả trong các vấn đề pháp lý của mình.</p><p><strong>Lĩnh vực đảm nhiệm</strong></p><ul><li>Dịch vụ Luật sư nội bộ</li><li>M&amp;A</li><li>Ngân hàng</li><li>Chứng khoán</li><li>Pháp lý doanh nghiệp</li><li>Pháp lý doanh nghiệp</li></ul><h3>Ngôn ngữ</h3><ul><li>Tiếng Việt</li><li>Tiếng Anh</li></ul><p><strong>Các dự án đã thực hiện</strong></p><ul><li>Tư vấn, soạn thảo và rà soát các văn bản, biểu mẫu liên quan đến việc tổ chức các cuộc họp Hội đồng thành viên, Hội đồng quản trị, Đại hội đồng cổ đông của các doanh nghiệp.</li><li>Tư vấn các vấn đề pháp lý liên quan đến công bố thông tin của công ty chứng khoán.</li><li>Tham gia tư vấn giao dịch M&amp;A cho Bên Mua là Công ty TNHH TT Investment nhằm mua lại Công ty mục tiêu từ GUOCOLAND VIETNAM (S) PTE LTD- một công ty thuộc Gouco Group Limited, một trong những tập đoàn bất động sản hàng đầu Singapore;</li><li>Tư vấn pháp lý về các phương án cho vay, thu hồi nợ, xử lý tài sản bảo đảm cho các tổ chức tín dụng.</li><li>Hỗ trợ các doanh nghiệp trong việc rà soát, cũng như hoàn thiện các hợp đồng, giao dịch trong quá trình hoạt động của mình, như Hợp đồng mua bán hàng hóa, Hợp đồng cho thuê nhà xưởng sản xuất, Hợp đồng hợp tác đầu tư, v.v</li></ul><p><br></p>")
                        .rating(0f)
                        .cases(39)
                        .description("Chuyên gia về thuế và tuân thủ pháp lý cho doanh nghiệp.")
                        .build();
                staffProfileRepository.save(profileKhoa);
            }

            // Category of Post
            if(categoryRepository.count()==0){
                List<Category> categories = Arrays.asList(
                        new Category("Luật Doanh Nghiệp"),
                        new Category("Luật Hình Sự"),
                        new Category("Luật Dân Sự"),
                        new Category("Luật Lao Động"),
                        new Category("Luật Hành Chính"),
                        new Category("Luật Đất Đai"),
                        new Category("Luật Bảo Hiểm Xã Hội"),
                        new Category("Luật Sở Hữu Trí Tuệ")
                );
                categoryRepository.saveAll(categories);
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
                template.setImgUrl("https://phucminhlaw.vn/wp-content/uploads/2021/03/Soan-don-263x147.jpg");
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
                template2.setImgUrl("https://phucminhlaw.vn/wp-content/uploads/2021/03/Soan-don-263x147.jpg");
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
                template3.setImgUrl("https://phucminhlaw.vn/wp-content/uploads/2021/03/Soan-don-263x147.jpg");
                formTemplateRepository.save(template3);
            }


        };
    }
}
