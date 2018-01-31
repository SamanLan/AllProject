//package saman.com.lib;
//
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//import static org.objectweb.asm.Opcodes.GETSTATIC;
//import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
//
//
//public class myClass {
//
//    public static void main(String[] args) throws IOException{
////        System.out.println("---");
////        ClassPrinter printer = new ClassPrinter();
////        ClassReader classReader = new ClassReader("saman.com.lib.myClass$vv");
////        classReader.accept(printer, 0);
//        ClassReader cr = new ClassReader(Bazhang.class.getName());
//        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//        ClassVisitor cv = new TestClassVisitor(cw);
//        cr.accept(cv, Opcodes.ASM5);
//        // 获取生成的class文件对应的二进制流
//        byte[] code = cw.toByteArray();
//        //将二进制流写到out/下
//        FileOutputStream fos = new FileOutputStream("Bazhang223.class");
//        fos.write(code);
//        fos.close();
//    }
//
//    public static class Bazhang {
//        private long f(int n, String s, int[] arr) {
//            return 0;
//        }
//        private void hi(double a, List<String> b) {
//        }
//        public void newFunc(String str) {
//            System.out.println(str);
//            for (int i = 0; i < 100; i++) {
//                if (i % 10 == 0) {
//                    System.out.println(i);
//                }
//            }
//        }
//    }
//
//    static class TestClassVisitor extends ClassVisitor {
//        public TestClassVisitor(final ClassVisitor cv) {
//            super(Opcodes.ASM5, cv);
//        }
//
//        @Override
//        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//            if (cv != null) {
//                cv.visit(version, access, name, signature, superName, interfaces);
//            }
//        }
//        @Override
//        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//            //如果methodName是newFunc，则返回我们自定义的TestMethodVisitor
//            if ("newFunc".equals(name)) {
//                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
//                return new TestMethodVisitor(mv);
//            }
//            if (cv != null) {
//                return cv.visitMethod(access, name, desc, signature, exceptions);
//            }
//            return null;
//        }
//    }
//
//    private static class TestMethodVisitor extends MethodVisitor {
//        public TestMethodVisitor(MethodVisitor mv) {
//            super(Opcodes.ASM5, mv);
//        }
//        @Override
//        public void visitCode() {
//            //方法体内开始时调用
//            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//            mv.visitLdcInsn("========start=========");
//            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//            super.visitCode();
//        }
//        @Override
//        public void visitInsn(int opcode) {
//            if (opcode == Opcodes.RETURN) {
//                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                mv.visitLdcInsn("========end=========");
//                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//            }
//            //每执行一个指令都会调用
//            super.visitInsn(opcode);
//        }
//    }
//}
