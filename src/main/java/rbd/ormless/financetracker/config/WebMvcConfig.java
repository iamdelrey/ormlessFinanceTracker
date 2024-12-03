//package rbd.ormless.financetracker.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import rbd.ormless.financetracker.interceptor.NotificationInterceptor;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private final NotificationInterceptor notificationInterceptor;
//
//    public WebMvcConfig(NotificationInterceptor notificationInterceptor) {
//        this.notificationInterceptor = notificationInterceptor;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(notificationInterceptor);
//    }
//}
