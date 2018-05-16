package hscsm.core.sum.account.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验参数的字节长度，用于EBS接口请求的参数
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/2.
 */
@Target( { ElementType.METHOD,
        ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ByteLengthValidator.class)
@Documented
public @interface ByteLength {
    String message() default "字段";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max() default 2147483647;
}