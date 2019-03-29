package chapter01;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * 并发和单线程执行测试
 *
 * @author qiubaisen
 * @date 2019-03-29
 */

@Slf4j
public class ConcurrencyTest {
    @Test
    public void testInDifferentScale() {
        long tenThousand = 10000L; //一万
        IntStream.of(1, 10, 100, 1000, 10000).mapToLong(i -> i * tenThousand)
                .forEachOrdered(this::test);
    }

    @Test
    public void baseTest(){
        test(100000000L);
    }


    private void test(long count) {
        //并发计算
        try {
            concurrency(count);
        } catch (InterruptedException e) {
            log.error("interrupted!");
            Thread.currentThread().interrupt();
        }
        //单线程计算
        serial(count);
    }

    private static void concurrency(long count) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            int a = 0;
            for (long i = 0; i < count; i++) {
                a += 5;
            }
            log.info("a={}", a);
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        log.info("concurrency:{} ms,b={}", time, b);
    }

    private static void serial(long count) {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        log.info("serial:{} ms,b={} ,a={}", time, b, a);
    }

}