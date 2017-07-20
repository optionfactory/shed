package net.optionfactory.minispring.api.v1;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import net.optionfactory.minispring.api.MappingNotFoundException;
import net.optionfactory.minispring.core.DefaultMinifyFacade;
import net.optionfactory.minispring.core.MinifyFacade;
import static net.optionfactory.minispring.minify.MinifyService.MINIFIED_URL_PREFIX;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

//    public MinifyController(MinifyFacade facade) {
//        this.facade = facade;
//    }

    // @Valid annotations processed thanks to MethodValidationPostProcessor for simple types
    // @Valid on Request DTOs is built-in
    @RequestMapping(path = "blacklist", method = RequestMethod.POST)
    @ResponseBody
    public void addToBlacklist(@RequestParam("domain") @Valid @NotNull @NotEmpty @Length(min = 5, max = 255) 
        @Pattern(regexp = "[a-zA-Z0-9\\.]+") String domain) {
        facade.blacklist(domain);
    }
    
//    public static class BlacklistRequest {
//        @NotNull
//        public String domain;
//        public int duration;
//       
//    }
//    
//    @RequestMapping(path = "blacklist", method = RequestMethod.POST)
//    @ResponseBody
//    public void addToBlacklist(@RequestBody BlacklistRequest req) {
//        facade.blacklist(req.domain);
//    }

    @RequestMapping(path = "minify", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String minify(@RequestParam @Valid @NotEmpty @Length(min = 10, max = 4000) @Pattern(regexp = "http(s)?://.+") String url, HttpServletRequest req) {
        final String handle = facade.minify(url);
        return String.format("%s://%s:%s%s%s/v1/%s", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(), req.getServletPath(), handle);
    }

    @ExceptionHandler(value = {MinifyFacade.BlacklistedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleBlacklistedException(DefaultMinifyFacade.BlacklistedException ex) {
        return "Domain blacklisted";
    }

    @RequestMapping(path = "blacklist", method = RequestMethod.GET)
    @ResponseBody
    public List<MinifyFacade.BlacklistItemResponse> getBlacklist() {
        return facade.getBlacklistItems();
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
