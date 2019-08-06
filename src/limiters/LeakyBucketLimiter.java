package limiters;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Aauthor ChenCheng
 * @Description 漏桶限流算法
 * @Date 2019/8/4
 */
public class LeakyBucketLimiter extends Limiter {

    private final long capacity;                                            // 水桶容量, 一秒流光
    private double remainWater;                                             // 目前水桶剩下的水量
    private long lastTime;                                                  // 时间戳
    private ReentrantLock lock = new ReentrantLock();                       // 可重入锁

    LeakyBucketLimiter(int qps) {
        super(qps);
        capacity = qps;
        remainWater = capacity;
        lastTime = 0;
    }

    @Override
    public boolean tryAcquire() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            double outWater = ((now - lastTime)/1000.0) * capacity;         // 计算这段时间匀速流出的水
            lastTime = now;
            remainWater = Math.max(0, remainWater - outWater);
            if (remainWater + 1 <= capacity) {
                remainWater += 1;
                long waitingMs = (long)((remainWater / capacity) * 1000);   // 计算刚加入的水滴完全滴出漏桶需要的时间（毫秒）
                lock.unlock();
                try {
                    //System.out.println("睡了！" + waitingMs);
                    Thread.sleep(waitingMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println("睡醒了！" + System.currentTimeMillis());
                return true;
            } else return false;
        } finally {
            if (lock.isLocked()) lock.unlock();
        }

    }
}
