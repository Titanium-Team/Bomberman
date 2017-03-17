package bomberman.view.engine;

import bomberman.utils.OS;

import java.io.File;

public class LwjglNativesLoader {

    public static void load() {
        NativeLibraryLoader loader = new NativeLibraryLoader();
        File nativesDir;
        try {
            if (OS.isWindows) {
                nativesDir = loader.extractFile(OS.is64Bit ? "lwjgl64.dll" : "lwjgl.dll", null).getParentFile();

                loader.extractFile(OS.is64Bit ? "jinput-dx8_64.dll" : "jinput-dx8.dll", null);
                loader.extractFile(OS.is64Bit ? "jinput-raw_64.dll" : "jinput-raw.dll", null);
            } else {
                throw new UnsupportedOperationException("Unsupported os!!!");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to extract LWJGL natives.");
        }

        System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath", nativesDir.getAbsolutePath());
    }

}
