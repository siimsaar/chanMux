package hashing;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

/**
 * Created by ayy on 11/19/15.
 */
public class TempFS {

    Path savePath;
    Path tempPath;
    FileSystem fs;
    static boolean maxExceeded;

    public TempFS(Path savePath) {
        this.savePath = savePath;
        try {
            fs = MemoryFileSystemBuilder.newLinux().build("tempfs" + Thread.currentThread().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeInMemory(URL dlUrl){
        try {
            tempPath = fs.getPath("tempfile");
            InputStream dlPic = dlUrl.openStream();
            Files.copy(dlPic, tempPath, StandardCopyOption.REPLACE_EXISTING);
            dlPic.close();
        } catch (AssertionError e) {
            if(!Files.isDirectory(Paths.get("./temp"))) {
                try {
                    Files.createDirectory(Paths.get("./temp/"));
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
            try {
                if(!Files.exists(Paths.get("./temp/tempfile" + Thread.currentThread().getId()))) {
                    tempPath = Files.createFile(Paths.get("./temp/tempfile" + Thread.currentThread().getId()));
                }
                tempPath = Paths.get("./temp/tempfile" + Thread.currentThread().getId());
                InputStream dlPic = dlUrl.openStream();
                Files.copy(dlPic, tempPath, StandardCopyOption.REPLACE_EXISTING);
                dlPic.close();
                maxExceeded = true;
            } catch (Exception j) {
                j.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Path getFileLocation() {
        return tempPath;
    }

    public static boolean getMaxExceededStatus () {
        return maxExceeded;
    }

    public void flushFS() {
        try {
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
