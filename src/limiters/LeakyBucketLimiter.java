package limiters;

/**
 * @Aauthor ChenCheng
 * @Description 漏桶限流算法
 * @Date 2019/8/4
 */
public class LeakyBucketLimiter extends Limiter {

    private final long capacity;                                        // 水桶容量, 一秒流光
    private double remainWater;                                         // 目前水桶剩下的水量
    private long lastTime;                                              // 时间戳

    LeakyBucketLimiter(int qps) {
        super(qps);
        capacity = qps;
        remainWater = capacity;
        lastTime = 0;
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        double outWater = ((now - lastTime)/1000.0) * capacity;         // 计算这段时间匀速流出的水
        lastTime = now;
        if (outWater > remainWater) {
            // 请求已全部处理完毕
            remainWater = 1;
            return true;
        } else {
            // 还有未处理的请求
            remainWater -= outWater;
            if (remainWater + 1 <= capacity) {
                remainWater += 1;
                return true;
            } else return false;
        }
    }
}
