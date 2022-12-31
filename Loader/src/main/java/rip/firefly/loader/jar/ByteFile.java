package rip.firefly.loader.jar;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author AkramL
 * 24/06/2022 13:51 GMT
 */
@RequiredArgsConstructor
public class ByteFile {

    private final File file;

    public byte[] toByteArray() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] array = new byte[(int)file.length()];
            fileInputStream.read(array);
            return array;
        }
    }

}
