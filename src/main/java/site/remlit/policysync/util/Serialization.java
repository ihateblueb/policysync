package site.remlit.policysync.util;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Serialization {

    public static @NotNull Object deserialize(
            @NotNull byte[] data
    ) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream objs = new ObjectInputStream(in);
        return objs.readObject();
    }

}
