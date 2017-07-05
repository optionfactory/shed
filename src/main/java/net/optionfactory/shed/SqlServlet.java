package net.optionfactory.shed;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(urlPatterns = "/sql")
public class SqlServlet extends HttpServlet {

    private Context context;
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/derby");
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection cn = dataSource.getConnection();
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT CURRENT_TIMESTAMP FROM SYSIBM.SYSDUMMY1")) {
            while (rs.next()) {
                resp.getWriter().write(rs.getTimestamp(1).toString());
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                // no-op
            }
        }
    }

}
