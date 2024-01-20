package org.resrun.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: SignType
 * @Package: org.resrun.validation
 * @ClassName: SignType
 * @author: FengLai_Gong
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,
        ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SignTypeValidator.class})
public @interface SignTypeValid {

    String message() ;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };


}
