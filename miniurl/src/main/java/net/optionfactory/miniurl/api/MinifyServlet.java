package net.optionfactory.miniurl.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.optionfactory.miniurl.core.DefaultMinifyFacade;
import net.optionfactory.miniurl.core.MinifyFacade;
import static net.optionfactory.miniurl.minify.MinifyService.MINIFIED_URL_PREFIX;

public class MinifyServlet extends HttpServlet {

    private final MinifyFacade facade;

    public MinifyServlet(MinifyFacade facade) {
        this.facade = facade;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (apiMethod(req)) {
            case "blacklist":
                facade.blacklist(req.getParameter("domain"));
                return;
            case "minify":
                try {
                    final String handle = facade.minify(req.getParameter("url"));
                    resp.setStatus(200);
                    resp.getWriter().write(String.format("%s://%s:%s%s%s/%s", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(),req.getServletPath(), handle));
                } catch (DefaultMinifyFacade.BlacklistedException ex) {
                    resp.setStatus(400);
                    resp.getWriter().write("Domain blacklisted");
                }
                return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (apiMethod(req)) {
            case "blacklist":
                final List<MinifyFacade.BlacklistItemResponse> blacklistItems = facade.getBlacklistItems();
                final StringBuffer body = new StringBuffer();
                body.append("[");//start Array
                final String bodyObjects = blacklistItems
                        .stream()
                        .map(MinifyServlet::toJson)
                        .collect(Collectors.joining(","));
                body.append(bodyObjects);
                body.append("]");//end Array
                resp.setStatus(200);
                resp.getWriter().write(body.toString());
                return;
            default:
                if (apiMethod(req).contains(MINIFIED_URL_PREFIX)) {
                    final Optional<String> resolve = facade.resolve(apiMethod(req));
                    if (!resolve.isPresent()) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    resp.sendRedirect(resolve.get());
                }
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

    }

    private static String apiMethod(HttpServletRequest req) {
        final String[] parts = req.getRequestURI().split("/");
        return parts[parts.length - 1];
    }

    private static String toJson(MinifyFacade.BlacklistItemResponse item) {
        return "{" + "\"domain\":\"" + item.domain + "\", \"since\":\"" + item.since.toString() + "\"}";
    }
}
