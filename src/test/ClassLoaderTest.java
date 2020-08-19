package test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
//        File file = new File("/Users/panshen/IdeaProjects/commission-gateway/commission-gateway/target/classes/");
//        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
//        Class myClass = loader.loadClass("com.ggj.center.commission.gateway.biz.impl.DisasterRecoveryServiceImpl");
//        System.out.println(myClass.getClassLoader());

        System.out.println(new MyClassLoader(ClassLoader.getSystemClassLoader()).loadClass("com.ggj.center.commission.gateway.dto.AccountRegRelationDTO").getClassLoader());
    }

    static class MyClassLoader extends ClassLoader {

        private ClassLoader parent;

        public MyClassLoader(ClassLoader parent) {
            this.parent = parent;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {

            // 这个if如果去掉，会报错。
            // 因为AccountRegRelationDTO继承自Object，这里if去掉打破了双亲委派。自定义的类加载是不允许加载以【java.】开通的类的。
            if (parent != null) {
                try {
                    Class clazz = parent.loadClass(name);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (ClassNotFoundException e) {

                }

            }

            return findClass(name);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = getData();
            return defineClass(name, bytes, 0, bytes.length);
        }

        private byte[] getData() {

            File file = new File("/Users/panshen/IdeaProjects/commission-gateway/commission-gateway/target/classes/com/ggj/center/commission/gateway/dto/AccountRegRelationDTO.class");
            if (file.exists()){
                FileInputStream in = null;
                ByteArrayOutputStream out = null;
                try {
                    in = new FileInputStream(file);
                    out = new ByteArrayOutputStream();

                    byte[] buffer = new byte[10000];
                    int size = 0;
                    while ((size = in.read(buffer)) != -1) {
                        out.write(buffer, 0, size);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                return out.toByteArray();
            }else{
                return null;
            }


        }
    }
}
