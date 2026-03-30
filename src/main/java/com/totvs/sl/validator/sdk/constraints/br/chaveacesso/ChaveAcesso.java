package com.totvs.sl.validator.sdk.constraints.br.chaveacesso;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ChaveAcessoValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ChaveAcesso {

	String message() default "{validator.constraints.br.chaveAcesso.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
