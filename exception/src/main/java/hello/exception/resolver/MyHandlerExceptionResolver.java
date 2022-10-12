package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            if (ex instanceof IllegalArgumentException) { // 여기서 IllegalArgumentException을 다 잡아서 정상적으로 400으로 was에 정상응답할 수 있따.
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView(); // modelAndView로 반환하는 이유는 정상흐름처럼 변경하는것이 목적.
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null; // null 로 반환하면 , 예외를 처리하지 못하고 서블릿 밖으로 예외를 그대로 던진다 -> was 에서 예외처리 무조건 500으로 처리함
    }
}
