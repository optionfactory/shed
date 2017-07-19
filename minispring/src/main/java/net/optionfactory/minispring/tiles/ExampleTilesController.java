package net.optionfactory.minispring.tiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExampleTilesController {

     @Value("${app.name}") String appName;
    
    @RequestMapping("example")
    public String homePage(ModelMap model, @Value("${app.version}") String appVersion) {
        model.put("body", String.format("This is the <b>body</b>. The app name is %s version %s", appName, appVersion));
        return "example";
    }
}
