package io.github.floatingpointmc.kantunderstand.api;

/**
 * Marks an element as explicitly included in obfuscation.
 *
 * <p>When applied, the annotated element will be obfuscated even if it
 * would otherwise be skipped by default rules. Specific obfuscation types
 * may be selectively included via {@link #value()}.</p>
 */
public @interface Include {
    /**
     * Whether inner (nested) members of the annotated element should also
     * be included in obfuscation.
     *
     * <p>This attribute only affects the {@link Obfuscation#NAME NAME}
     * (renaming) obfuscation pass.</p>
     *
     * @return {@code true} if inner members should be included (default),
     *         {@code false} otherwise
     */
    boolean sub() default true;

    /**
     * The obfuscation types to include for the annotated element.
     *
     * <p>Only the listed obfuscation passes will be applied; all other
     * passes will be skipped. If empty, <em>all</em> obfuscation types
     * are included.</p>
     *
     * @return the obfuscation types to apply, or an empty array for all
     */
    Obfuscation[] value() default {};
}