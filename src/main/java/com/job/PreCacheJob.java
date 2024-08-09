package com.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.model.domain.User;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 预热缓存任务
 * @className: PreCacheJob
 * @Version: 1.0
 * @description:
 */

@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;
    //重点用户
    private List<Long> mainUserList = Arrays.asList(13L);

    @Scheduled(cron = "0 0 * * * *")
    public void doCacheRecommendUser(){
        RLock lock = redissonClient.getLock("PartnerLock:precache:doCache:lock");
        try {
            //仅允许一个服务器获取到锁
            if(lock.tryLock(0, 30000, TimeUnit.MILLISECONDS)){
                System.out.println("getLock" + Thread.currentThread().getId());
                //根据每个用户的id存储对应的缓存数据
                for (Long userId : mainUserList) {
                    //无缓存，查数据库
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format("timess:user:recommend:%s",userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    //写缓存
                    try {
                        valueOperations.set(redisKey,userPage,5, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        log.error("Timed redis set key error",e);
                    }
                }
            }
        } catch (InterruptedException e) {
           log.error("doCacheRecommendUser error",e);
        } finally {
            //判定当前持有该锁的服务器，是否为本服务器
            if(lock.isHeldByCurrentThread()){
                lock.unlock(); //释放锁
                System.out.println("unLock" + Thread.currentThread().getId());
            }
        }
    }

}
