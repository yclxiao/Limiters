import limiters.LeakyBucketLimiter;
import limiters.Limiter;
import limiters.LimiterEnum;
import limiters.LimiterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 限速器测试类
 */
public class LimiterTest {

    public static void main(String[] args) throws InterruptedException {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        // 获取限速器
        Limiter countLimiter = LimiterFactory.getCountLimiter(LimiterEnum.LEAKY_BUCKET_LIMITER, 10);
        Limiter leakyBucketLimiter = LimiterFactory.getCountLimiter(LimiterEnum.LEAKY_BUCKET_LIMITER, 10);
        Limiter rateLimiter = LimiterFactory.getCountLimiter(LimiterEnum.MYRATE_LIMITER, 10);

        // 多个线程
        int maxNum = 100;

        // 用户主线程统计时间
        final CountDownLatch countDownLatch = new CountDownLatch(maxNum);

        // 创建若干线程执行限速逻辑
        long startTime = System.currentTimeMillis();
        for (int i=0; i< maxNum; i++){
             Thread thread = new Thread(new Runnable() {
                 @Override
                public void run() {
                    // 输出信息放在tryAcquire里比较好
                    if (rateLimiter.tryAcquire()) {
                        System.out.println("Success:" + Thread.currentThread().getName() +
                                " " + sf.format(new Date()));
                    } else {
                        System.out.println("Failed:" + Thread.currentThread().getName() +
                                " " + sf.format(new Date()));
                    }

                    countDownLatch.countDown();
                }
            });
            thread.setName("thread_" + i);
            thread.start();
            Thread.sleep(50);
        }

        // 获取整个过程执行时间
        countDownLatch.await();
        System.out.println("end takeTime:"+ (System.currentTimeMillis() - startTime));
    }
}
