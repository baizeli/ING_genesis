package miku.united_as_one.genesis.genesis_core;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class INGLaunchPluginService implements ILaunchPluginService {
    private static final String owner = "miku/united_as_one/genesis_core/utils/EventUtil";

    @Override
    public String name() {
        return "INGLaunchPluginService";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.AFTER);
    }

    @Override
    public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason) {
        return ILaunchPluginService.super.processClassWithFlags(phase, classNode, classType, reason);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        if (classNode.name.startsWith("miku/united_as_one/genesis")) return false;
        boolean flag = false;
        if (classNode.name.equals("net/minecraft/world/entity/LivingEntity") || is(classNode, "net/minecraft/world/entity/LivingEntity")) {
            for (MethodNode method : classNode.methods) {
                if (("getHealth".equals(method.name) || "m_21223_".equals(method.name)) && method.desc.equals("()F")) {
                    method.instructions.clear();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "getHealth", "(Lnet/minecraft/world/entity/LivingEntity;)F", false));
                    method.instructions.add(new InsnNode(Opcodes.FRETURN));
                    flag = true;
                } else if (("setHealth".equals(method.name) || "m_21153_".equals(method.name)) && method.desc.equals("(F)V")) {
                    method.instructions.clear();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new VarInsnNode(Opcodes.FLOAD, 1));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "setHealth", "(Lnet/minecraft/world/entity/LivingEntity;F)V", false));
                    method.instructions.add(new InsnNode(Opcodes.RETURN));
                    flag = true;
                } else if (("isDeadOrDying".equals(method.name) || "m_21224_".equals(method.name)) && method.desc.equals("()Z")) {
                    method.instructions.clear();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDeadOrDying", "(Lnet/minecraft/world/entity/LivingEntity;)Z", false));
                    method.instructions.add(new InsnNode(Opcodes.IRETURN));
                    flag = true;
                } else if (("isAlive".equals(method.name) || "m_6084_".equals(method.name)) && method.desc.equals("()Z")) {
                    method.instructions.clear();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isAlive", "(Lnet/minecraft/world/entity/LivingEntity;)Z", false));
                    method.instructions.add(new InsnNode(Opcodes.IRETURN));
                    flag = true;
                } else if (("getMaxHealth".equals(method.name) || "m_21233_".equals(method.name)) && method.desc.equals("()F")) {
                    method.instructions.clear();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "getMaxHealth", "(Lnet/minecraft/world/entity/LivingEntity;)F", false));
                    method.instructions.add(new InsnNode(Opcodes.FRETURN));
                    flag = true;
                }
            }
        } else if (classNode.name.equals("net/minecraft/world/entity/Entity") || is(classNode, "net/minecraft/world/entity/Entity")) {
            for (MethodNode method : classNode.methods) {
                if (("setRemoved".equals(method.name) || "m_142467_".equals(method.name)) && method.desc.equals("(Lnet/minecraft/world/entity/Entity$RemovalReason;)V")) {
                    method.instructions.clear();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "setRemoved", "(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$RemovalReason;)V", false));
                    method.instructions.add(new InsnNode(Opcodes.RETURN));
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static boolean is(ClassNode classNode, String className) {
        if (classNode == null) return false;

        for (String superName = classNode.superName; superName != null; ) {
            if (superName.equals(className)) return true;
            ClassNode superClass = loadClassNode(superName, Thread.currentThread().getContextClassLoader());
            if (superClass == null) break;
            superName = superClass.superName;
        }
        return false;
    }

    public static List<ClassNode> getAllSuperClasses(ClassNode classNode, ClassLoader loader) {
        List<ClassNode> result = new ArrayList<>();
        for (String superName = classNode.superName; superName != null; ) {
            ClassNode node = loadClassNode(superName, loader);
            if (node == null) break;
            result.add(node);
            superName = node.superName;
        }
        return result;
    }

    public static ClassNode loadClassNode(String className, ClassLoader loader) {
        try (InputStream is = loader.getResourceAsStream(className + ".class")) {
            if (is == null) return null;
            ClassNode node = new ClassNode();
            new ClassReader(is).accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
            return node;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMethodDescriptor(final Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        appendDescriptor(String.class,stringBuilder);
        stringBuilder.append(')');
        appendDescriptor(method.getReturnType(), stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder) {
        Class<?> currentClass = clazz;
        while (currentClass.isArray()) {
            stringBuilder.append('[');
            currentClass = currentClass.getComponentType();
        }
        if (currentClass.isPrimitive()) {
            char descriptor;
            if (currentClass == Integer.TYPE) {
                descriptor = 'I';
            } else if (currentClass == Void.TYPE) {
                descriptor = 'V';
            } else if (currentClass == Boolean.TYPE) {
                descriptor = 'Z';
            } else if (currentClass == Byte.TYPE) {
                descriptor = 'B';
            } else if (currentClass == Character.TYPE) {
                descriptor = 'C';
            } else if (currentClass == Short.TYPE) {
                descriptor = 'S';
            } else if (currentClass == Double.TYPE) {
                descriptor = 'D';
            } else if (currentClass == Float.TYPE) {
                descriptor = 'F';
            } else if (currentClass == Long.TYPE) {
                descriptor = 'J';
            } else {
                throw new AssertionError();
            }
            stringBuilder.append(descriptor);
        } else {
            stringBuilder.append('L').append(Type.getInternalName(currentClass)).append(';');
        }
    }
}
