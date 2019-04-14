package com.jia.dbsharding.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
@SuppressWarnings("unchecked")
public final class RedisTemplateUtil {

    private final RedisTemplate<String, Object> template;

    @Autowired
    public RedisTemplateUtil(RedisTemplate<String, Object> template){
        this.template = template;
    }

    /******************** String ***************************/

    /**
     * 设置key的缓存时间
     * @param key 对象key
     * @param time 缓存时间
     * @return 设置成功返回true， 否则返回false
     */
    public boolean expire(String key, long time){
        try {
            if(time > 0){
                template.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据 key 获取过期时间
     * @param key key 键不可以null
     * @return 时间， 0 代表永久有效
     */
    public long getExpire(String key){
        if(!StringUtils.isEmpty(key)) {
            return template.getExpire(key, TimeUnit.SECONDS);
        }
        return -1;
    }

    /**
     * 判断 key 是否存在
     * @param key key 不可以为null
     * @return true 存在，false 不存在
     */
    public boolean hasKey(String key){
        try{
            if(!StringUtils.isEmpty(key)) {
                return template.hasKey(key);
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值，也可以多个值
     */
    public void del(String... key){
        if(key != null && key.length > 0){
            if(key.length == 1){
                template.delete(key[0]);
            }else{
                template.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     * @param key key 键
     * @return value
     */
    public Object get(String key){
        return key == null ? null : template.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true 成功， false 失败
     */
    public boolean set(String key, Object value){
        try{
            template.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @return true 成功， false 失败
     */
    public boolean set(String key, Object value, long time){
        try{
            if(!StringUtils.isEmpty(key) && time > 0){
                template.opsForValue().set(key, value, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 递增因子，即增加多少
     * @return
     */
    public long incr(String key, long delta){
        if(delta < 0 ){
            throw new RuntimeException("递增因子必须大于0");
        }
        return template.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 递减因子，即减多少
     * @return
     */
    public long decr(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("递减因子必须大于 0");
        }
        return template.opsForValue().decrement(key, delta);
    }

    /******************** Map ***************************/

    /**
     * 获取哈希表 key 中 item 键的值
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key, String item){
        return template.opsForHash().get(key, item);
    }

    /**
     * 获取hash key对应的所有键值
     * @param key
     * @return 对应多个键值
     */
    public Map<Object, Object> hmget(String key){
        return template.opsForHash().entries(key);
    }

    /**
     * 插入多个键值对
     * @param key 哈希表的名字
     * @param map 多个键值对
     * @return true 成功， false 失败
     */
    public boolean hmset(String key, Map<String, Object> map){
        try{
            template.opsForHash().putAll(key, map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将多个键值对插入值哈希表key，并设置过期时间
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time){
        try{
            template.opsForHash().putAll(key, map);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 向一张hash表中插入数据，如果不存在将创建
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hset(String key, String item, Object value){
        try{
            template.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中插入数据并设置过期时间
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    public boolean hset(String key, String item, Object value, long time){
        try{
            template.opsForHash().put(key, item, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中对应的item项
     * @param key
     * @param item
     */
    public void hdel(String key, Object... item){
        if(!StringUtils.isEmpty(key)){
            template.opsForHash().delete(key, item);
        }
    }

    /**
     * 判断hash表中是否有该项
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String item){
        if(!StringUtils.isEmpty(key)){
            return template.opsForHash().hasKey(key, item);
        }
        return false;
    }


}
