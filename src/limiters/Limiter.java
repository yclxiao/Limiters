package limiters;

/**
 * @Aauthor ChenCheng
 * @Description 限流器抽象父类
 * @Date 2019/8/4
 */
public abstract class Limiter {

    final int qps;

    Limiter(int qps) {
        this.qps = qps;
    }

    // 获取继续执行的资格（非阻塞）立刻返回成功或失败
    public abstract boolean tryAcquire();

}
