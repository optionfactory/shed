package net.optionfactory.minispring.pages;

import java.time.Instant;
import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StatsController {

    @RequestMapping("stats")
    public ModelAndView stats() {
        // TODO
        final HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("timeOfDay", Instant.now());
        return new ModelAndView("stats", model);
    }
}
