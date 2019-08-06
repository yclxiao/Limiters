package limiters;

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
