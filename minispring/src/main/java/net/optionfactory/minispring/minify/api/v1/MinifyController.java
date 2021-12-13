package net.optionfactory.minispring.minify.api.v1;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import net.optionfactory.minispring.minify.MinifyFacade;
import static net.optionfactory.minispring.minify.MinifyService.MINIFIED_URL_PREFIX;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("v1")
public class MinifyController {

    @Autowired
    public MinifyFacade facade;

    @RequestMapping(path = "minify", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String minify(@RequestParam @Valid @NotEmpty @Length(min = 10, max = 4000) @Pattern(regexp = "http(s)?://.+") String url, HttpServletRequest req) {
        final String handle = facade.minify(url);
        return String.format("%s://%s:%s%s%s/v1/%s", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(), req.getServletPath(), handle);
    }
    
    @RequestMapping(path = MINIFIED_URL_PREFIX + "{handleSuffix}", method = RequestMethod.GET)
    public View resolve(@PathVariable(required = true, name = "handleSuffix") String handleSuffix) {
        final Optional<String> target = facade.resolve(MINIFIED_URL_PREFIX + handleSuffix);
        if (!target.isPresent()) {
            throw new MappingNotFoundException();
        }
        return new RedirectView(target.get());
    }

    @ExceptionHandler(value = {MappingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleMissingMapping(MappingNotFoundException ex) {
    }
}