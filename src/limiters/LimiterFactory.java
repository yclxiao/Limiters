package limiters;

/**
 * @Aauthor ChenCheng
 * @Description 限流器工厂类
 * @Date 2019/8/4
 */
public class LimiterFactory {

    public static Limiter getCountLimiter(LimiterEnum limiterEnum, int qps) {
        switch (limiterEnum) {
            case COUNT_LIMITER:
                return new CountLimiter(qps);
            case LEAKY_BUCKET_LIMITER:
                return new LeakyBucketLimiter(qps);
            case MYRATE_LIMITER:
                return new MyRateLimiter(qps);
            default:
                return null;
        }
    }

}
