package limiters;

/**
 * 令牌桶限流算法
 */
public class MyRateLimiter extends Limiter {

    final int capacity;                             // 桶内能装多少令牌
    int curTokenNum;                                // 现在桶内令牌数量
    long lastTime;                                  // 时间戳

    MyRateLimiter(int qps) {
        super(qps);
        capacity = qps;
        curTokenNum = 0;
        lastTime = 0;
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        int intoToken = (int)((now - lastTime)/1000.0 * capacity);
        lastTime = now;
        if (intoToken + curTokenNum > capacity) {
            // 令牌已放满
            curTokenNum = capacity - 1;
            return true;
        } else if (intoToken + curTokenNum >= 1) {
            // 还有令牌
            curTokenNum = intoToken + curTokenNum - 1;
            return true;
        } else return false;
    }

    @Override
    public String getStatus() {
        return " 令牌个数： " + curTokenNum;
    }
}
