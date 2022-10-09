package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    /**
     * 컨트롤러(핸들러어댑터) 실행전 실행
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

        // 유연하게 설계되어 있어서 handler 은 Object 타입
        // @RequestMapping : HandlerMethod
        // 정적 리소스 : ResourceHttpRequestHandler 가 넘어옴
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // true: 핸들러 호출 시킴 / false: 더이상 진행하지않고 여기서 멈춤
    }

    /**
     * 컨트롤러 실행되고 ModelAndView 을 디스패처 서블릿에 반환 후 실행됨
     * 컨트롤러에서 예외터지면 실행되지 않음
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    /**
     * http response를 완전히 반환한 후에, 뷰가 렌더링 된 후에 실행
     * 컨트롤러에서 예외 발생해도 실행됨, ex파라미터에 예외정보도 알 수 있음, 예외 아니면 null
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);

        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
