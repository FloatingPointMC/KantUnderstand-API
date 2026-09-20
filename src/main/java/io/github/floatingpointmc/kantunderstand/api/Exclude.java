package io.github.floatingpointmc.kantunderstand.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Prevents the annotated element from being obfuscated.
 * <p>
 * This annotation can be applied to:
 * <ul>
 *     <li>{@link ElementType#TYPE TYPE}</li>
 *     <li>{@link ElementType#METHOD METHOD}</li>
 *     <li>{@link ElementType#PARAMETER PARAMETER}</li>
 *     <li>{@link ElementType#FIELD FIELD}</li>
 *     <li>{@link ElementType#CONSTRUCTOR CONSTRUCTOR}</li>
 * </ul>
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {
    /**
     * Whether inner (nested) members of the annotated element should also
     * be excluded from obfuscation.
     *
     * <p>This attribute only affects the {@link Obfuscation#NAME NAME}
     * (renaming) obfuscation pass.</p>
     *
     * @return {@code true} if inner members should be excluded (default),
     *         {@code false} otherwise
     */
    boolean sub() default true;

    /**
     * The obfuscation types to exclude from the annotated element.
     *
     * <p>Only the listed obfuscation passes will be skipped; all other
     * passes will still apply. If empty, <em>all</em> obfuscation types
     * are excluded.</p>
     *
     * @return the obfuscation types to skip, or an empty array for all
     */
    Obfuscation[] value() default {};
}