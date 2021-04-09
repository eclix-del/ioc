package com.l.testIoc;

import com.l.testIoc.annotation.A;
import com.l.testIoc.annotation.C;
import com.l.testIoc.annotation.R;
import com.l.testIoc.ioc.Book;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Test {

    private Map<String, Object> instancesMap = new HashMap<>();
    private Map<Class, Object> classInstancesMap = new HashMap<>();

    public Test(){
        inversionControl();
        inject();
    }

    public void inversionControl(String... componentScan){

        String packageName = null;

        if(componentScan == null || componentScan.length == 0){
            packageName = this.getClass().getPackage().getName();
        }else{
            packageName = componentScan[0];
        }

        String filePath = null;

        String classPath = this.getClass().getResource("/").getPath();

        // 处理空格
        classPath = classPath.replaceAll("%20", " ");

        String packagePath = packageName.replaceAll("\\.","/");

        filePath = classPath + packagePath;


        File file = new File(filePath);

        File[] files = file.listFiles();

        String[] fileNames = file.list();

        int i = 0;

        for (File f : files){

            // 如果是文件夹
            if(f.isDirectory()){

                inversionControl(packageName + "." + fileNames[i]);

            }else{

                try {

                    String className = f.getName().split("\\.")[0];

                    Class clazz = Class.forName(packageName +"."+ className);

                    if(clazz.isAnnotationPresent(C.class)){
                        Object obj = clazz.newInstance();
                        instancesMap.put(firstOneToLowerCase(className), obj);
                        classInstancesMap.put(clazz, obj);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }

            i++;
        }

    }

    /**
     * 功能描述: 注入
     * @Author  lyd
     * @Date  2021/4/6 12:02
     * @return
     **/
    public void inject(){

        if(instancesMap.size() > 0){

            for (Map.Entry<String, Object> entry : instancesMap.entrySet()){

                // 获取类中的所有字段
                Field[] fields = entry.getValue().getClass().getDeclaredFields();

                // 设置字段值
                for (Field field : fields){

                    if(field.isAnnotationPresent(R.class)){

                        R r = field.getAnnotation(R.class);

                        field.setAccessible(true);  // 可编辑
                        try {

                            String fieldName = r.value();

                            if(fieldName == null || fieldName.equals("")){
                                fieldName = field.getName();
                            }

                            field.set(entry.getValue(), instancesMap.get(fieldName));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }else if(field.isAnnotationPresent(A.class)){

                        field.setAccessible(true);

                        try {
                            field.set(entry.getValue(), classInstancesMap.get(field.getType()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }

        }

    }

    /**
     * 功能描述: 获取bean
     * @Author  lyd
     * @Date  2021/4/6 14:51
     * @return
     **/
    public Object getBean(String beanName){
        return instancesMap.get(beanName);
    }

    /**
     * 功能描述: 首字母小写
     * @Author  lyd
     * @Date  2021/4/6 14:50
     * @return
     **/
    public String firstOneToLowerCase(String str){

        if(Character.isLowerCase(str.charAt(0))){
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] +=32;

        return String.valueOf(chars);

    }

    public static void main(String[] args) {
        Test t = new Test();
        Book book = (Book) t.getBean("book");
        book.speak();
    }

}
