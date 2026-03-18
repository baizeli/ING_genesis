package miku.united_as_one.genesis_core;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.module.Configuration;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

public class INGService implements ITransformationService {
    static {
//        LaunchPluginHandler handler = Helper.getFieldValue(Launcher.INSTANCE, "launchPlugins", LaunchPluginHandler.class);
//        Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) Helper.getFieldValue(handler, "plugins", Map.class);
//        Map<String, ILaunchPluginService> newMap = new ConcurrentHashMap<>();
//        newMap.put("LNGPlugin", new INGLaunchPluginService());
//        if (plugins != null)
//            for (String name : plugins.keySet())
//                newMap.put(name, plugins.get(name));
//        Helper.setFieldValue(handler, "plugins", newMap);
        makeMyModLoadable();
    }

    @Override
    public @NotNull String name() {
        return "INGService";
    }

    @Override
    public void initialize(IEnvironment environment) {

    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) {

    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return List.of();
    }

    public static void makeMyModLoadable() {
        try {
            Class<?> modDirTransformerDiscoverer = Class.forName("net.minecraftforge.fml.loading.ModDirTransformerDiscoverer");
            VarHandle foundHandle = MethodHandles.privateLookupIn(modDirTransformerDiscoverer, MethodHandles.lookup()).findStaticVarHandle(modDirTransformerDiscoverer, "found", List.class);
            List<?> found = (List<?>) foundHandle.get();
            found.removeIf((namedPath) -> {
                Path[] paths = null;

                try {
                    paths = (Path[])namedPath.getClass().getMethod("paths").invoke(namedPath);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }

                return paths[0].toString().contains("iron_spells_genesis");
            });
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        try {
            Class<?> launcher = Class.forName("cpw.mods.modlauncher.Launcher");
            Class<?> moduleLayerHandlerClass = Class.forName("cpw.mods.modlauncher.ModuleLayerHandler");
            VarHandle instanceHandle = MethodHandles.privateLookupIn(launcher, MethodHandles.lookup()).findStaticVarHandle(launcher, "INSTANCE", launcher);
            Object INSTANCE = instanceHandle.get();
            VarHandle moduleLayerHandlerHandle = MethodHandles.privateLookupIn(launcher, MethodHandles.lookup()).findVarHandle(launcher, "moduleLayerHandler", moduleLayerHandlerClass);
            Object moduleLayerHandler = moduleLayerHandlerHandle.get(INSTANCE);
            VarHandle completedLayersHandle = MethodHandles.privateLookupIn(moduleLayerHandlerClass, MethodHandles.lookup()).findVarHandle(moduleLayerHandlerClass, "completedLayers", EnumMap.class);
            EnumMap<?, ?> completedLayers = (EnumMap<?, ?>) completedLayersHandle.get(moduleLayerHandler);
            Class<?> layerInfoClass = Class.forName("cpw.mods.modlauncher.ModuleLayerHandler$LayerInfo");
            VarHandle layerHandle = MethodHandles.privateLookupIn(layerInfoClass, MethodHandles.lookup()).findVarHandle(layerInfoClass, "layer", ModuleLayer.class);
            completedLayers.values().forEach((layerInfo) -> {
                ModuleLayer layer = (ModuleLayer) layerHandle.get(layerInfo);
                Configuration config = layer.configuration();
                String moduleName = INGService.class.getModule().getName();

                try {
                    Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                    unsafeField.setAccessible(true);
                    Unsafe unsafe = (Unsafe)unsafeField.get((Object)null);
                    Field nameToModuleField = Configuration.class.getDeclaredField("nameToModule");
                    long nameToModuleOffset = unsafe.objectFieldOffset(nameToModuleField);
                    Field modulesField = Configuration.class.getDeclaredField("modules");
                    long modulesOffset = unsafe.objectFieldOffset(modulesField);
                    Map<String, Object> nameToModule1 = (Map)unsafe.getObject(config, nameToModuleOffset);
                    Set<Object> modules1 = (Set)unsafe.getObject(config, modulesOffset);
                    Map<String, Object> nameToModule2 = new HashMap(nameToModule1);
                    Set<Object> modules2 = new HashSet(modules1);
                    if (nameToModule1 != null && nameToModule1.containsKey(moduleName)) {
                        modules2.remove(nameToModule2.remove(moduleName));
                    }

                    unsafe.putObject(config, nameToModuleOffset, nameToModule2);
                    unsafe.putObject(config, modulesOffset, modules2);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }
}
