package com.totvs.sl.validator.sdk.constraints.br.chaveacesso;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ChaveAcessoValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ChaveAcesso {

	String message() default "{validator.constraints.br.ChaveAcesso.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

