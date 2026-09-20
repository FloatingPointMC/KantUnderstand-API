package io.github.floatingpointmc.kantunderstand.api;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

@AllArgsConstructor
public enum Obfuscation {
    /**
     * Encrypts string constants embedded in bytecode.
     */
    STRING(true, false),
    /**
     * Encrypts numeric constants embedded in bytecode.
     */
    NUMBER(true, false),
    /**
     * Transforms method invocations using invokedynamic indirection.
     */
    INVOKEDYNAMIC(true, false),
    /**
     * Inserts opaque control-flow predicates to hinder decompilation.
     */
    FLOW(true, false),
    /**
     * Injects anti-tampering crasher code into the bytecode.
     */
    CRASHER(true, false),
    /**
     * Renames classes, methods, and fields to obfuscated identifiers.
     */
    NAME(true, true),
    /**
     * Transforms methods into native stubs (internal use only).
     */
    @ApiStatus.Internal
    NATIVE(false, true),
    /**
     * Generates decoy classes to inflate the output jar (internal use only).
     */
    @ApiStatus.Internal
    SPAM(false, true);

    /**
     * Internal capability flags for this obfuscation type.
     *
     * <p>{@code canExclude} indicates whether the type can be opted out via
     * {@link Exclude}. {@code canNative} indicates whether the type is
     * compatible with {@link Native} transformation.</p>
     */
    @ApiStatus.Internal
    public final boolean canExclude, canNative;
}