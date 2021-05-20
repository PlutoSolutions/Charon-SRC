/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface CommandManifest {
    public String label();

    public String[] aliases() default {};
}

