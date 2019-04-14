package com.jia.dbsharding.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
@SuppressWarnings("unchecked")
public final class RedisTemplateUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisTemplateUtil(RedisTemplate<String, Object> template){
        this.redisTemplate = template;
    }

    //===================== String ========================/

    /**
     * 设置key的缓存时间
     * 对应redis命令（下文均省略） SETEX key seconds value
     * @param key 对象key
     * @param time 缓存时间
     * @return 设置成功返回true， 否则返回false
     */
    public boolean expire(String key, long time){
        try {
            if(time > 0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key key 键不可以null
     * @return 时间， 0 代表永久有效
     */
    public Long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     * EXISTS key (存在返回1， 否则返回0)
     * @param key key 不可以为null
     * @return true 存在，false 不存在
     */
    public Boolean hasKey(String key){
        try{
            if(!StringUtils.isEmpty(key)) {
                return redisTemplate.hasKey(key);
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
     * DEL key
     * @param key 可以传一个值，也可以多个值
     */
    public void delete(String... key){
        if(key != null && key.length > 0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     * GET key
     * @param key key 键
     * @return value
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * SET key value
     * @param key 键
     * @param value 值
     * @return true 成功， false 失败
     */
    public boolean set(String key, Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * SET key value
     * EXPIRE key seconds value
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @return true 成功， false 失败
     */
    public boolean set(String key, Object value, long time){
        try{
            if(!StringUtils.isEmpty(key) && time > 0){
                redisTemplate.opsForValue().set(key, value, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * INCRBY key delta
     * @param key 键
     * @param delta 递增因子，即增加多少
     */
    public Long incrmentBy(String key, long delta){
        if(delta < 0 ){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * DECRBY key delta
     * @param key 键
     * @param delta 递减因子，即减多少
     */
    public Long decrementBy(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("递减因子必须大于 0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    //================== Hash ===============================/

    /**
     * 获取哈希表 key 中 item 键的值
     * HGET key item
     */
    public Object hGet(String key, String item){
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hash key对应的所有键值
     * HGETALL key
     */
    public Map<Object, Object> hGetAll(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 插入多个键值对
     * HMSET key item1 value1 item2 value2...
     * @param key 哈希表的名字
     * @param map 多个键值对
     * @return true 成功， false 失败
     */
    public boolean hmSet(String key, Map<String, Object> map){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将多个键值对插入值哈希表key，并设置过期时间
     * HMSET key item1 value1 item2 value2
     * EXPIRE key seconds time
     */
    public boolean hmSet(String key, Map<String, Object> map, long time){
        try{
            redisTemplate.opsForHash().putAll(key, map);
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
     * HMSET key item value
     */
    public boolean hmSet(String key, String item, Object value){
        try{
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中插入数据并设置过期时间
     * HMSET key item value
     */
    public boolean hmSet(String key, String item, Object value, long time){
        try{
            redisTemplate.opsForHash().put(key, item, value);
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
     */
    public void hDelete(String key, Object... item){
        if(!StringUtils.isEmpty(key)){
            redisTemplate.opsForHash().delete(key, item);
        }
    }

    /**
     * 判断hash表中是否有该项
     * HEXISTS key item
     */
    public boolean hHasKey(String key, String item){
        if(!StringUtils.isEmpty(key)){
            return redisTemplate.opsForHash().hasKey(key, item);
        }
        return false;
    }

    /**
     * hash表指定项的值递增，增加 delta， 并将增加后的值返回
     * HINCRBY key item delta
     */
    public double hIncrementBy(String key, String item, double delta){
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * 递减，减掉 delta ， 并将结果返回
     * HINCRBY key item delta
     */
    public double hDecrementBy(String key, String item, double delta){
        return redisTemplate.opsForHash().increment(key, item, -delta);
    }

    //================================ Set =====================//

    /**
     * 根据 key 获取 Set 中的所有值
     * SMEMBERS key
     */
    public Set<Object> sMembers(String key){
        try{
            return redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询 set 中是否有该key
     * SISMEMBERS key value
     */
    public boolean sIsMembers(String key, Object value){
        try{
            redisTemplate.opsForSet().isMember(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存，并返回成功个数
     * SADD key value1 value2...
     */
    public Long sAdd(String key, Object... values){
        try{
            return redisTemplate.opsForSet().add(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将数据放入Set中，并设置过期时间，返回成功个数
     * SADD key value1 value2...
     */
    public Long sAdd(String key, long expireTime, Object... values){
        try{
            Long count = redisTemplate.opsForSet().add(key, values);
            if(expireTime > 0){
                expire(key, expireTime);
            }
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     * SCARD key
     */
    public Long sSize(String key){
        try{
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 移除value，一个或者多个，并返回移除成功的个数
     * SREM key value1 value2...
     */
    public Long sRemove(String key, Object... values){
        try{
            return redisTemplate.opsForSet().remove(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    //============================= List ===========================//

    /**
     * 返回范围[start， end] 内的list缓存
     * LRANGE key start end
     */
    public List<Object> lRange(String key, long start, long end){
        try{
            return redisTemplate.opsForList().range(key, start, end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回 List 缓存的长度
     * LLEN key
     */
    public Long lSize(String key){
        try{
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 根据索引获得list中的值
     * LINDEX key index
     */
    public Object lIndex(String key, long index){
        try{
            return redisTemplate.opsForList().index(key, index);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将 value 放入list缓存中
     * RPUSH key value
     */
    public boolean lRPush(String key, Object value){
        try{
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 value 放入list缓存中，并设置过期时间
     * RPUSH key value
     */
    public boolean lRPush(String key, Object value, long time){
        try{
            redisTemplate.opsForList().rightPush(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 将list列表中的元素放入list缓存中
     * LPUSH key value1 value2
     */
    public boolean lPush(String key, List<Object> values){
        try{
            redisTemplate.opsForList().rightPushAll(key, values);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将多个 value 放入list缓存，并设置过期时间
     * LPUSH key value1 value2
     */
    public boolean lPush(String key, List<Object> values, long time){
        try{
            redisTemplate.opsForList().rightPushAll(key, values);
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
     * 修改 index 位置的值为 value
     * LSET key index value
     */
    public boolean lSetByIndex(String key, long index, Object value){
        try{
            redisTemplate.opsForList().set(key, index, value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 移除 count 个值为 value 的元素，并返回成功个数
     * LREM key count value
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     */
    public Long lRemove(String key, long count, Object value){
        try{
            return redisTemplate.opsForList().remove(key, count, value);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }
}
