package net.optionfactory.minispring.config.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * A custom exception resolver resolving Spring and Jackson2 exceptions with a
 * MappingJackson2JsonView. Sample serialized form of the response is:  <code>
 * [
 *   {"type": "", "context": "fieldName", "reason": a field validation error", "details": null},
 *   {"type": "", "context": null, "reason": "a global error", "details": null},
 * ]
 * </code>
 */
public class JsonExceptionResolver extends DefaultHandlerExceptionResolver {

    private final ObjectMapper objectMapper;

    public JsonExceptionResolver(ObjectMapper objectMapper, int order) {
        this.objectMapper = objectMapper;
        this.setOrder(order);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        final HandlerMethod hm = (HandlerMethod) handler;
        final MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        view.setObjectMapper(objectMapper);
        view.setContentType("application/json;charset=UTF-8");
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
        // response.setStatus();
        // return new ModelAndView(view, "errors", statusAndErrors.failures);
    }

    public static class SendErrorToSetStatusHttpServletResponse extends HttpServletResponseWrapper {

        private final HttpServletResponse inner;

        public SendErrorToSetStatusHttpServletResponse(HttpServletResponse inner) {
            super(inner);
            this.inner = inner;
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            inner.setStatus(sc);
        }

        @Override
        public void sendError(int sc) throws IOException {
            inner.setStatus(sc);
        }
    }
}
