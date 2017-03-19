package bomberman.view.engine;

import bomberman.utils.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeLibraryLoader {

    public String nativesJar;

    private String uuid = UUID.randomUUID().toString();

    public NativeLibraryLoader() {
    }

    public NativeLibraryLoader(String nativesJar) {
        this();
        this.nativesJar = nativesJar;
    }

    private InputStream readFile(String path) {
        if (nativesJar == null) {
            InputStream input = NativeLibraryLoader.class.getResourceAsStream("/" + path);
            if (input == null)
                throw new IllegalStateException("Unable to read file for extraction: " + path);
            return input;
        }

        try {
            @SuppressWarnings("resource") // TODO fix leak
                    ZipFile file = new ZipFile(nativesJar);
            ZipEntry entry = file.getEntry(path);
            if (entry == null)
                throw new IllegalStateException("Couldn't find '" + path + "' in JAR: " + nativesJar);
            return file.getInputStream(entry);
        } catch (IOException ex) {
            throw new IllegalStateException("Error reading '" + path + "' in JAR: " + nativesJar);
        }
    }

    public File extractFile(String sourcePath, String dirName) throws IOException {
        File extractedFile = getExtractedFile(dirName, new File(sourcePath).getName());
        return extractFile(sourcePath, extractedFile);
    }

    private File extractFile(String sourcePath, File extractedFile) throws IOException {
        try {
            InputStream input = readFile(sourcePath);
            extractedFile.getParentFile().mkdirs();
            FileOutputStream output = new FileOutputStream(extractedFile);
            StreamUtils.copyStream(input, output);
            input.close();
            output.close();
        } catch (IOException ex) {
            throw new IllegalStateException("Error extracting file: " + sourcePath + "To: " + extractedFile.getAbsolutePath());
        }

        return extractedFile;
    }

    private File getExtractedFile(String dirName, String fileName) {
        File idealFile = new File(System.getProperty("java.io.tmpdir") + "/bomberman_engine/" + uuid + "/" + dirName, fileName);
        if (canWrite(idealFile))
            return idealFile;

        try {
            File file = File.createTempFile(dirName, null);
            if (file.delete()) {
                file = new File(file, fileName);
                if (canWrite(file))
                    return file;
            }
        } catch (IOException ignored) {
        }

        File file = new File(System.getProperty("user.home") + "/.bomberman_engine/" + uuid + "/" + dirName, fileName);
        if (canWrite(file))
            return file;

        file = new File(".temp/" + uuid + "/" + dirName, fileName);
        if (canWrite(file))
            return file;

        return idealFile;
    }

    public static boolean canWrite(File file) {
        File parent = file.getParentFile();
        File testFile;
        if (file.exists()) {
            if (!file.canWrite() || !canExecute(file))
                return false;
            testFile = new File(parent, UUID.randomUUID().toString());
        } else {
            parent.mkdirs();
            if (!parent.isDirectory())
                return false;
            testFile = file;
        }
        try {
            new FileOutputStream(testFile).close();
            if (!canExecute(testFile))
                return false;
            return true;
        } catch (Throwable ex) {
            return false;
        } finally {
            testFile.delete();
        }
    }

    private static boolean canExecute(File file) {
        try {
            Method canExecute = File.class.getMethod("canExecute");
            if ((Boolean) canExecute.invoke(file))
                return true;

            Method setExecutable = File.class.getMethod("setExecutable", boolean.class, boolean.class);
            setExecutable.invoke(file, true, false);

            return (Boolean) canExecute.invoke(file);
        } catch (Exception ignored) {
        }
        return false;
    }

}
