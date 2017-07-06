package meihuai_context;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.meihuai.model.Person;

import java.io.*;

public class AnnotationTest {
    public static void main(String[] args) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                ApplicationContext context1 = new ClassPathXmlApplicationContext("applicationContext.xml");
                try {
                    Thread.sleep(5000);
                    System.out.println("thread");
                    Person person=context1.getBean("person", Person.class);
                    System.out.println(person);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Person person=context.getBean("person", Person.class);
        System.out.println(person);
    }
    @Test
    public void testResource() throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start");
                InputStream inputStream=this.getClass().getResourceAsStream("meihuai.txt");
//                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
//                try {
//                    String line=reader.readLine();
//                    System.out.print("line="+line);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try {
                    Thread.sleep(3000);
                    System.out.println("thread.....=+inputstream="+inputStream);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(500);
        File file=new File("D:\\meihuai\\info\\weibo_motan\\github_spring\\spring-framework\\spring-context\\build\\resources\\test\\meihuai_context\\meihuai.txt");
        file.delete();

        InputStream inputStream=this.getClass().getResourceAsStream("meihuai.txt");
        System.out.println("normal....inputstream="+inputStream);
        try {
            Thread.sleep(5000);
            System.out.println("finally end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testFileInputStream(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start");
                InputStream inputStream= null;
                try {
                    inputStream = new FileInputStream("D:\\meihuai\\info\\weibo_motan\\github_spring\\spring-framework\\spring-context\\build\\resources\\test\\meihuai_context\\meihuai.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
                    System.out.println("thread.....=+inputstream="+inputStream);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        InputStream inputStream= null;
        try {
            File file=new File("D:\\meihuai\\info\\weibo_motan\\github_spring\\spring-framework\\spring-context\\build\\resources\\test\\meihuai_context\\meihuai.txt");
            file.delete();

            inputStream = new FileInputStream("D:\\meihuai\\info\\weibo_motan\\github_spring\\spring-framework\\spring-context\\build\\resources\\test\\meihuai_context\\meihuai.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("normal....inputstream="+inputStream);
        try {
            Thread.sleep(3000);
            System.out.println("finally end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}