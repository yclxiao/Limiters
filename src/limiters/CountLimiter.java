package limiters;

/**
 * @Aauthor ChenCheng
 * @Description 计数器限流算法
 * @Date 2019/8/4
 */
public class CountLimiter extends Limiter {

    private int count;                          // 计数器
    private long lastTime;                      // 时间戳

    public CountLimiter(int qps) {
        super(qps);
        count = 0;
        lastTime = 0;
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        if (now - lastTime > 1000) {
            lastTime = now>>3<<3;               // 保证时间戳后三位都是0, 更精确
            count = 1;
            return true;
        } else if (count < qps) {
            count++;
            return true;
        } else {
            return false;
        }
    }
}
