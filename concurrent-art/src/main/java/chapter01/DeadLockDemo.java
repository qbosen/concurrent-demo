package chapter01;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author qiubaisen
 * @date 2019-03-29
 */

@Slf4j
public class DeadLockDemo {

    private static final String A = "A";

    private static final String B = "B";

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    private void deadLock() {
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error("interrupted");
                    Thread.currentThread().interrupt();
                }
                synchronized (B) {
                    log.info("1");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (B) {
                synchronized (A) {
                    log.info("2");
                }
            }
        });
        t1.start();
        t2.start();
    }
}
// dump 线程查看问题
/*
"Thread-1" #13 prio=5 os_prio=31 tid=0x00007fa5c4a0b000 nid=0xa403 waiting for monitor entry [0x00007000043ef000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at chapter01.DeadLockDemo.lambda$deadLock$1(DeadLockDemo.java:37)
        - waiting to lock <0x00000007156ff880> (a java.lang.String)
        - locked <0x00000007156ff8b0> (a java.lang.String)
        at chapter01.DeadLockDemo$$Lambda$2/1018547642.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
        - None

"Thread-0" #12 prio=5 os_prio=31 tid=0x00007fa5c483d800 nid=0xa503 waiting for monitor entry [0x00007000042ec000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at chapter01.DeadLockDemo.lambda$deadLock$0(DeadLockDemo.java:30)
        - waiting to lock <0x00000007156ff8b0> (a java.lang.String)
        - locked <0x00000007156ff880> (a java.lang.String)
        at chapter01.DeadLockDemo$$Lambda$1/901506536.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
        - None

Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007fa5c6003408 (object 0x00000007156ff880, a java.lang.String),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007fa5c6007558 (object 0x00000007156ff8b0, a java.lang.String),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
        at chapter01.DeadLockDemo.lambda$deadLock$1(DeadLockDemo.java:37)
        - waiting to lock <0x00000007156ff880> (a java.lang.String)
        - locked <0x00000007156ff8b0> (a java.lang.String)
        at chapter01.DeadLockDemo$$Lambda$2/1018547642.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)
"Thread-0":
        at chapter01.DeadLockDemo.lambda$deadLock$0(DeadLockDemo.java:30)
        - waiting to lock <0x00000007156ff8b0> (a java.lang.String)
        - locked <0x00000007156ff880> (a java.lang.String)
        at chapter01.DeadLockDemo$$Lambda$1/901506536.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.

 */