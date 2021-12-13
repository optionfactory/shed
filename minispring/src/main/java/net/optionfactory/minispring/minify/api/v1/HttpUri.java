package net.optionfactory.minispring.minify.api.v1;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HttpUri.HttpUriValidator.class)
@Documented
public @interface HttpUri {
    String message() default "Not a valid http/https URI";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    class HttpUriValidator implements ConstraintValidator<HttpUri, URI> {

        @Override
        public boolean isValid(URI value, ConstraintValidatorContext cvc) {
            return value == null || "http".equals(value.getScheme()) || "https".equals(value.getScheme());
        }

        @Override
        public void initialize(HttpUri constraintAnnotation) {

        }
    }
}
