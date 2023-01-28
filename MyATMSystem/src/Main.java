import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args)  {
        Counter t1 = new Counter();
        new Thread(() -> t1.getTask()).start();
        new Thread(() -> t1.add(100)).start();
    }
}
class Counter  {
    Queue<Integer> queue = new LinkedList<>();
    public synchronized void add(int m) {
        this.queue.add(m);
        System.out.println("after add");
        try {
            System.out.println("after while");
            this.wait();
            System.out.println("while等待结束");
        }catch (Exception e){  }
        this.notify();
    }

    public synchronized void getTask()  {
        if(this.queue.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("task任务开启");
            this.notify();
        }

    }
}
