package test;

import java.util.concurrent.TimeUnit;

public class VolatileTest {

    public static void main(String[] args) throws InterruptedException {

        VolatileClass volatileClass = new VolatileClass();

        new Thread(()->{
            int s;
            while ((volatileClass.state) == 0) {
                System.out.println("state == 0");
            }
            System.out.println("state != 0");
        }).start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(()->{
            volatileClass.state = 1;

//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }).start();

        TimeUnit.SECONDS.sleep(100);


    }

    static class VolatileClass {

        volatile int state = 0;

    }

}
