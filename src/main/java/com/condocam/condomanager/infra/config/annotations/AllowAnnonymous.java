package com.condocam.condomanager.infra.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.condocam.condomanager.infra.config.constants.ValidationType;

@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME)
public @interface AllowAnnonymous {

    ValidationType validationType() default ValidationType.ANNONYMOUS;

}