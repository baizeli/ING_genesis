package miku.united_as_one.genesis.common.entity.test;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;

public class BaiZeLiHiddenLoader {

    private static Class<?> clazz;

    public static Class<?> load() {
        try {
            if (clazz != null) return clazz;

            String name = "miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity";
            String path = name.replace('.', '/') + ".class";

            InputStream is = BaiZeLiHiddenLoader.class.getClassLoader().getResourceAsStream(path);
            byte[] bytes = is.readAllBytes();

            var lookup = MethodHandles.privateLookupIn(
                    BaiZeLiHiddenLoader.class,
                    MethodHandles.lookup()
            );

            clazz = lookup.defineHiddenClass(bytes, true).lookupClass();

            System.out.println("Hidden Loaded: " + clazz);

            return clazz;

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}