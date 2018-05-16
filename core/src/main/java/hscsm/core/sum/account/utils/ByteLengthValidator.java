package hscsm.core.sum.account.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验参数的字节长度，用于EBS接口请求的参数
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/7.
 */
public class ByteLengthValidator implements ConstraintValidator<ByteLength,Object> {
    private int max;

    public void initialize(ByteLength byteLength) {
        this.max = byteLength.max();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String message = context.getDefaultConstraintMessageTemplate();
        if(value == null){
            return true;
        }

        boolean outOfLength = value.toString().getBytes().length > max;

        if(outOfLength){
            context.disableDefaultConstraintViolation();//禁用默认的message的值
            context.buildConstraintViolationWithTemplate(message + "字节长度不能超过" + max + "位")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
