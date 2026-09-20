package io.github.floatingpointmc.kantunderstand.loader;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Internal native class loader &mdash; <b>not part of the public API</b>.
 *
 * <p>This class is responsible for extracting and loading the platform-specific
 * native library and delegating class resolution to it. It is used internally
 * by the obfuscation runtime and should <em>never</em> be referenced directly
 * by application code.</p>
 *
 * @apiNote This class may be removed or changed without notice.
 *          Do not depend on it in any way.
 */
@ApiStatus.Internal
public final class Loader extends ClassLoader {
    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.load(extract(".dll"));
        } else if (os.contains("linux")) {
            System.load(extract(".so"));
        } else if (os.contains("mac")) {
            System.load(extract(".dylib"));
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + os);
        }
        registerNatives();
    }

    private static native void registerNatives();

    private static @NotNull String extract(String suffix) {
        try (InputStream is = Loader.class.getResourceAsStream(
                "/io/github/floatingpoint/loader/loader" + suffix)) {

            if (is == null) {
                throw new RuntimeException("Could not find resource: loader" + suffix);
            }

            File f = File.createTempFile("float_loader", suffix);
            f.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(f)) {
                byte[] buf = new byte[8192];
                int r;
                while ((r = is.read(buf)) != -1) {
                    fos.write(buf, 0, r);
                }
            }

            return f.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract native library", e);
        }
    }

    private native void nativeStart(String[] args); // native entry

    public static void main(String[] args) {
        new Loader().nativeStart(args);
    }

    @Override
    protected native Class<?> findClass(String name);
}