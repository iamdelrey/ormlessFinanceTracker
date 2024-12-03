//package rbd.ormless.financetracker.interceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import rbd.ormless.financetracker.service.NotificationService;
//
//@Component
//public class NotificationInterceptor implements HandlerInterceptor {
//
//    private final NotificationService notificationService;
//
//    public NotificationInterceptor(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if (modelAndView != null && !request.getRequestURI().contains("/login") && !request.getRequestURI().contains("/register")) {
//            modelAndView.addObject("notifications", notificationService.getNotifications());
//            notificationService.clearNotifications();
//        }
//    }
//}
