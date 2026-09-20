package io.github.floatingpointmc.kantunderstand.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class or method for native obfuscation.
 *
 * <p>When applied, the annotated element will be transformed so that it
 * no longer relies on JNI calls at runtime. This effectively prevents
 * native code from being invoked through the annotated element.</p>
 *
 * <p>Supported target types:</p>
 * <ul>
 *     <li>{@link ElementType#TYPE TYPE} &mdash; classes</li>
 *     <li>{@link ElementType#METHOD METHOD}</li>
 * </ul>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Native {
}