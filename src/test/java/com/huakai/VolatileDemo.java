package com.huakai;


public class VolatileDemo {

    private int number = 0;

    public int getNumber() {
        return this.number;
    }

    public void increase() {
        this.number++;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        VolatileDemo volDemo = new VolatileDemo();
        for (int i = 0; i < 50000; i++) {
            new Thread(() -> volDemo.increase()).start();
        }


        System.out.println("number : " + volDemo.getNumber());
    }

}
