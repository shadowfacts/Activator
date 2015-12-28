package net.shadowfacts.activator.compat;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.compat.modules.waila.CompatWaila;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class ModCompat {

	private static BiMap<String, Class> classes = HashBiMap.create();

	static {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) registerClientModules();
		registerCommonModules();
	}

	private static void registerClientModules() {
		register(CompatWaila.class);
	}

	private static void registerCommonModules() {

	}

	private static void register(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Compat.class)) {
			classes.put(clazz.getAnnotation(Compat.class).value(), clazz);
		}
	}

	public static void preInit(FMLPreInitializationEvent event) {
		classes.entrySet().stream()
				.filter(e -> Loader.isModLoaded(e.getKey()))
				.map(Map.Entry::getValue)
				.forEach(clazz -> {
					Arrays.stream(clazz.getMethods())
							.filter(m -> m.isAnnotationPresent(Compat.PreInit.class))
							.forEach(m -> {
								try {
									m.invoke(null, event);
								} catch (IllegalAccessException | InvocationTargetException e) {
									Activator.log.error("Couldn't run the pre-init method for compat module for " + classes.inverse().get(clazz));
									e.printStackTrace();
								}
							});
				});
	}

	public static void init(FMLInitializationEvent event) {
		classes.entrySet().stream()
				.filter(e -> Loader.isModLoaded(e.getKey()))
				.map(Map.Entry::getValue)
				.forEach(clazz -> {
					Arrays.stream(clazz.getMethods())
							.filter(m -> m.isAnnotationPresent(Compat.Init.class))
							.forEach(m -> {
								try {
									m.invoke(null, event);
								} catch (IllegalAccessException | InvocationTargetException e) {
									Activator.log.error("Couldn't run the init method for compat module for " + classes.inverse().get(clazz));
									e.printStackTrace();
								}
							});
				});
	}

	public static void postInit(FMLPostInitializationEvent event) {
		classes.entrySet().stream()
				.filter(e -> Loader.isModLoaded(e.getKey()))
				.map(Map.Entry::getValue)
				.forEach(clazz -> {
					Arrays.stream(clazz.getMethods())
							.filter(m -> m.isAnnotationPresent(Compat.PostInit.class))
							.forEach(m -> {
								try {
									m.invoke(null, event);
								} catch (IllegalAccessException | InvocationTargetException e) {
									Activator.log.error("Couldn't run the post-init method for compat module for " + classes.inverse().get(clazz));
									e.printStackTrace();
								}
							});
				});
	}


}
