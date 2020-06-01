package com.liyue.liyuetest.Utils;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {

    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    private static JedisPool jedisPool;

    /** 缓存过期时间,以秒为单位 */
    /** 三分钟 */
    public final static int EXRP_THREE_MINUTES = 60 * 3;
    /** 五分钟 */
    public final static int EXRP_FIVE_MINUTES = 60 * 5;
    /** 十分钟 */
    public final static int EXRP_TEN_MINUTES = 60 * 10;
    /** 十五分钟 */
    public final static int EXRP_FIFTEEN_MINUTES = 60 * 15;
    /** 一小时 */
    public final static int EXRP_HOUR = 60 * 60;
    /** 一天 */
    public final static int EXRP_DAY = 60 * 60 * 24;
    /** 三天 */
    public final static int EXRP_THREE_DAY = 60 * 60 * 24 * 3;
    /** 一个月 */
    public final static int EXRP_MONTH = 60 * 60 * 24 * 30;
    /** 两周 */
    public final static int EXRP_TWO_WEEK = 60 * 60 * 24 * 14;

    public void setJedisPool(JedisPool jedisPool) {
        JedisUtil.jedisPool = jedisPool;
    }

    /**
     * get jedis from pool
     *
     * @return
     */
    public static Jedis getJedis() {
        return getJedis(0);
    }

    /*
     * public static Jedis getJedis(String host, Integer port) { Jedis jedis = new
     * Jedis(host, port); jedis.connect(); if (jedis.isConnected()) return jedis;
     * return null; }
     *
     * public static Jedis getJedis(String host, Integer port, String password) {
     * try { Jedis jedis = new Jedis(host, port); jedis.auth(password);
     * jedis.connect(); if (jedis.isConnected()) return jedis; } catch (Exception e)
     * { if (e instanceof JedisConnectionException) {
     * logger.error("get jedis JedisConnectionException : " + e.getMessage()); } }
     * return null; }
     *
     * public static Jedis getJedis(String host, Integer port, String password, int
     * database) { try { Jedis jedis = new Jedis(host, port); jedis.auth(password);
     * jedis.select(database); jedis.connect(); if (jedis.isConnected()) return
     * jedis; } catch (Exception e) { if (e instanceof JedisConnectionException) {
     * logger.error("get jedis JedisConnectionException : " + e.getMessage()); } }
     * return null; }
     */

    /**
     * 获取redis链接实例，如获取失败则继续尝试failedNum次
     */
    public static Jedis getJedis(int failedNum) {
        return getJedis(failedNum, jedisPool);
    }

    /**
     * 获取redis链接实例，如获取失败则继续尝试failedNum次
     */
    public static Jedis getJedis(int failedNum, JedisPool jedisPoolParam) {
        Jedis jedis = null;
        if (failedNum >= 0) {
            try {
                jedis = jedisPoolParam.getResource();
            } catch (Exception e) {
                logger.error("get jedis error : " + e.getMessage());
                if (jedis != null)
                    returnBrokenJedis(jedis);
                try {
                    switch (failedNum) {
                        case 0:
                            Thread.sleep(100);
                            break;
                        case 1:
                            Thread.sleep(300);
                            break;
                        case 2:
                            Thread.sleep(500);
                            break;
                        default:
                            return null;
                    }
                    jedis = getJedis(--failedNum);
                } catch (InterruptedException e1) {
                    logger.error("retry get jedis error : " + e1.getMessage());
                }
            }
        }
        return jedis;
    }

    /**
     * 获取链接实例，如获取失败则继续尝试。 无最大尝试次数，有可能造成死循环，谨慎使用
     */
    @Deprecated
    public static Jedis bgetJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            logger.error("get jedis error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
            try {
                Thread.sleep(500);
                bgetJedis();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                logger.error("bgetJedis error : " + e1.getMessage());
            }
        }
        return jedis;
    }

    /**
     * 释放链接 return jedis to pool
     *
     * @param jedis
     *            jedis
     */
    public static void returnJedis(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }

    /**
     * 释放异常链接 return broken jedis to pool
     *
     * @param jedis
     *            jedis
     */
    public static void returnBrokenJedis(Jedis jedis) {
        returnBrokenJedis(jedis, jedisPool);
    }

    /**
     * 释放异常链接 return broken jedis to pool
     *
     * @param jedis
     *            jedis
     */
    public static void returnBrokenJedis(Jedis jedis, JedisPool jedisPoolParam) {
        if (jedis != null)
            jedisPoolParam.returnBrokenResource(jedis);
    }

    /**
     * 根据key获取String值
     *
     * @param key
     *            key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("get value from redis error[key:" + key + "]", e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
        return value;
    }

    /**
     * 根据key获取String值，失败时一段时间内会重复尝试
     *
     * @param key
     * @param timeout
     * @return
     */
    @Deprecated
    public static String get(String key, long timeout) {
        Jedis jedis = getJedis();
        String value = null;
        long t1 = System.currentTimeMillis();
        try {
            while (true) {
                value = jedis.get(key);
                if (!StringUtils.isEmpty(value)) {
                    break;
                }

                if (System.currentTimeMillis() - t1 > timeout)
                    break;
                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.error("get value from redis error[key:" + key + "]", e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
        return value;
    }

    /**
     * 保存数据set key -> value 如果key已存在则覆盖
     *
     * @param key
     * @param value
     * @return
     */
    public static void add(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("set key[" + key + "] to redis error ", e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 保存数据set key -> value 并指定过期时间
     *
     * @param key
     * @param value
     * @param expireTime
     *            seconds
     * @return
     */
    public static void add(String key, String value, int expireTime) {
        Jedis jedis = getJedis();
        try {
            jedis.setex(key, expireTime, value);
            /*
             * if(expireTime > 0) jedis.expire(key, expireTime);
             */
        } catch (Exception e) {
            logger.error(" setex[" + key + "] to redis error ", e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 设定过期时间
     *
     * @param key
     * @param expireTime
     *            seconds
     * @return
     */
    public static void expire(String key, int expireTime) {
        Jedis jedis = getJedis();
        try {
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            logger.error("expire key[" + key + "] to redis error ", e);
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 移除数据key -> value
     *
     * @param key
     * @return
     */
    public static void remove(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("del key[" + key + "] to redis error ", e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 判断是否存在key
     *
     * @param key
     *            key
     * @return boolean
     */
    public static boolean isExistKey(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("exist key[" + key + "] to redis error ", e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
        return false;
    }

    /**
     * 向指定通道发布消息 publish message to special channel
     *
     * @param channel
     * @param message
     * @return
     */
    public static void publish(String channel, String message) {
        Jedis jedis = getJedis();
        try {
            jedis.publish(channel, message);
        } catch (Exception e) {
            logger.error("publish message[" + message + "] to channel[" + channel + "] error: " + e.getMessage());
            e.printStackTrace();
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 订阅指定通道哦消息 subscribe special channel
     *
     * @param listener
     * @param channel
     */
    public static void subscribe(JedisPubSub listener, String channel) {
        Jedis jedis = getJedis();
        try {
            jedis.subscribe(listener, channel);
        } catch (Exception e) {
            logger.error("subscribe channel[" + channel + "] error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
            try {
                // 若订阅失败,将在一分钟后重试
                Thread.sleep(60 * 1000);
                subscribe(listener, channel);
            } catch (InterruptedException e1) {
                logger.error("retry subscribe channel[" + channel + "] error : " + e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     *
     * @param key
     *            key
     */
    public static void listPush(String key, String... value) {
        listPush(key, jedisPool, value);
    }

    public static void listPush(String key, JedisPool jedisPoolParam, String... value) {
        Jedis jedis = JedisUtil.getJedis(0, jedisPoolParam);
        try {
            jedis.rpush(key, value);
        } catch (Exception e) {
            JedisUtil.returnBrokenJedis(jedis, jedisPoolParam);
            logger.error("listPush error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            jedisPoolParam.returnResource(jedis);
        }
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     *
     * @param key
     *            key
     */
    public static void stackPush(String key, String... value) {
        Jedis jedis = getJedis();
        try {
            jedis.lpush(key, value);
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            logger.error("listPush error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 移除并返回列表key的头元素。
     *
     * @param key
     * @return String
     */
    public static String listPop(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.lpop(key);
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            logger.error("listPop error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 移除并返回列表key的头元素。
     *
     * @param key
     * @return String
     */
    public static String listBLPop(String key) {
        Jedis jedis = getJedis();
        try {
            List<String> list = jedis.blpop(EXRP_THREE_MINUTES, key);
            if (list != null && list.size() > 0) {
                return list.get(1);
            }
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 返回列表key的长度。
     *
     * @param key
     * @return long 列表长度
     */
    public static long listLength(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.llen(key);
        } catch (Exception e) {
            logger.error("listLength error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return 0;
    }

    /**
     * 获取列表指定下标的值
     *
     * @param key
     *            key
     * @param index
     *            index
     * @return 列表中下标为index的元素。 如果index参数的值不在列表的区间范围内(out of range)，返回nil。
     */
    public static String listIndex(String key, int index) {
        Jedis jedis = getJedis();
        try {
            return jedis.lindex(key, index);
        } catch (Exception e) {
            logger.error("listIndex error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 将列表key下标为index的元素的值甚至为value。
     *
     * @param key
     * @param index
     * @param value
     * @return 操作成功返回ok
     */
    public static String listSet(String key, int index, String value) {
        Jedis jedis = getJedis();
        try {
            return jedis.lset(key, index, value);
        } catch (Exception e) {
            logger.error("listSet error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 返回列表key中指定区间内的元素。
     *
     * @param key
     * @param start
     * @param end
     * @return List<String>
     */
    public static List<String> listRang(String key, int start, int end) {
        Jedis jedis = getJedis();
        try {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("listRang error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 取值队列中所有值
     *
     * @param key
     * @return List<String>
     */
    public static List<String> listAll(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            logger.error("listAll error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 移除队列中所有值
     *
     * @param key
     * @return 命令执行成功时，返回ok。
     */
    public static String listDelAll(String key, int start, int end) {
        Jedis jedis = getJedis();
        try {
            return jedis.ltrim(key, 0, -1);
        } catch (Exception e) {
            logger.error("listDelAll error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 将哈希表key中的域field的值设为value。
     *
     * @param key
     *            key
     * @param field
     *            field
     * @param value
     *            value
     * @return 如果field是哈希表中的一个新建域，并且值设置成功，返回1。 如果哈希表中域field已经存在且旧值已被新值覆盖，返回0。
     */
    public static long hashSet(String key, String field, String value) {
        Jedis jedis = getJedis();
        try {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("hashSet error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return 2;
    }

    /**
     * 返回哈希表key中给定域field的值。
     *
     * @param key
     * @param field
     * @return 给定域的值。当给定域不存在或是给定key不存在时，返回nil。
     */
    public static String hashGet(String key, String field) {
        Jedis jedis = getJedis();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("hashGet error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 返回哈希表key中，所有的域和值。
     *
     * @param key
     * @return 以列表形式返回哈希表的域和域的值。 若key不存在，返回空列表。
     */
    public static Map<String, String> hashGetAll(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("hashGetAll error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 删除哈希表key中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param field
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    public static long hashDel(String key, String field) {
        Jedis jedis = getJedis();
        try {
            return jedis.hdel(key, field);
        } catch (Exception e) {
            logger.error("hashDel error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return 0;
    }

    /**
     * 查看哈希表key中，给定域field是否存在。
     *
     * @param key
     * @param field
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    public static boolean hashExistField(String key, String field) {
        Jedis jedis = getJedis();
        try {
            return jedis.hexists(key, field);
        } catch (Exception e) {
            logger.error("hashExistField error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return false;
    }

    /**
     * 返回哈希表key中域的数量。
     *
     * @param key
     * @return 哈希表中域的数量, 当key不存在时，返回0。
     */
    public static long hashCount(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.hlen(key);
        } catch (Exception e) {
            logger.error("hashCount error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return -1;
    }

    /**
     * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略。
     * 假如key不存在，则创建一个只包含member元素作成员的集合。 当key不是集合类型时，返回一个错误。
     *
     * @param key
     * @param value
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     */
    public static long setAdd(String key, String... value) {
        Jedis jedis = getJedis();
        try {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("setAdd error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return 0;
    }

    /**
     * 返回集合中的一个随机元素。 当key不是集合类型，返回一个错误。
     *
     * @param key
     * @return 被选中的随机元素。 当key不存在或key是空集时，返回nil。
     */
    public static String setGet(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.srandmember(key);
        } catch (Exception e) {
            logger.error("setGet error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 返回集合key中的所有成员。 当key不是集合类型，返回一个错误。
     *
     * @param key
     * @return 集合中的所有成员。
     */
    public static Set<String> setGetAll(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.smembers(key);
        } catch (Exception e) {
            logger.error("setGetAll error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 移除集合key中的一个或多个member元素，不存在的member元素会被忽略。 当key不是集合类型，返回一个错误。
     *
     * @param key
     * @param value
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    public static long setDel(String key, String... value) {
        Jedis jedis = getJedis();
        try {
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("setDel error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return 0;
    }

    /**
     * 判断value是否在列表中 当key不是集合类型，返回一个错误。
     *
     * @param key
     * @param value
     * @return true： 存在 false:不存在
     */
    public static boolean setExistValue(String key, String value) {
        Jedis jedis = getJedis();
        try {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("setExistValue error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return false;
    }

    /**
     * 将存储在key上的数值+1 当key不存在或非数值类型，将值重置为0开始自增。
     *
     * @param key
     * @param expireTime
     * @return 当前累计数
     */
    public static int setIncr(String key, int expireTime) {
        Jedis jedis = getJedis();
        int v = 0;
        try {
            Transaction tx = jedis.multi();
            Response<Long> vp = tx.incr(key);
            tx.expire(key, expireTime);
            tx.exec();
            v = vp.get().intValue();
        } catch (Exception e) {
            logger.error("setIncr error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return v;
    }

    /**
     * 将存储在key上的数值-1 当key不存在或非数值类型，将值重置为0开始自增。
     *
     * @param key
     * @param expireTime
     * @return 当前累计数
     */
    public static int setDecr(String key, int expireTime) {
        Jedis jedis = getJedis();
        int v = 0;
        try {
            Transaction tx = jedis.multi();
            v = jedis.decr(key).intValue();
            jedis.expire(key, EXRP_THREE_MINUTES);
            jedis.watch(key);
            tx.exec();
        } catch (Exception e) {
            logger.error("setDecr error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return v;
    }

    /**
     * 将存储在key上的数值-1 当key不存在或非数值类型，将值重置为0开始递减。
     *
     * @param key
     * @return 当前累计数
     */
    public static int setDecr(String key) {
        Jedis jedis = getJedis();
        int v = 0;
        try {
            v = jedis.decr(key).intValue();
        } catch (Exception e) {
            logger.error("setDecr error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return v;
    }

    /**
     * 返回集合中元素的数量。 当key不是集合类型，返回一个错误。
     *
     * @param key
     * @return 集合中元素的数量, 当key不存在时，返回0。
     */
    public static long setCount(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("setCount error : " + e.getMessage());
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return -1;
    }

    /**
     * 设置 Object 和 过期时间
     *
     * @param <T>
     *
     * @param key
     *            key
     * @param seconds
     *            以秒为单位
     */
    public static <T> void setObject(String key, int seconds, T obj) {
        Jedis jedis = null;
        try {
            //obj null 序列化会报错
            if (obj == null){
                return;
            }
            jedis = getJedis();
            // jedis.setex(key.getBytes(), seconds, serialize(obj));
            jedis.setex(key.getBytes(), seconds, SerializationUtil.serialize(obj));
        } catch (Exception e) {
            logger.error("RedisUtil setObject error : " + e);
            returnBrokenJedis(jedis);
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 获取Object
     *
     * @param <T>
     * @param key
     * @return value
     */
    public static <T> T getObject(String key, Class<T> cls) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null || !jedis.exists(key.getBytes())) {
                return null;
            }
            byte[] value = jedis.get(key.getBytes());
            if (value == null) {
                return null;
            } else {
                return SerializationUtil.deserialize(value, cls);
                // return unserialize(value);
            }
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            logger.error("RedisUtil getObject error : " + e);
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    /**
     * 设置Object List 和 过期时间
     *
     * @param <T>
     *
     * @param key
     *            key
     * @param seconds
     *            以秒为单位
     * @param objList
     *            objList
     */
//    public static <T> void setObjectList(String key, int seconds, List<T> objList) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            ObjectListContainer<T> container = new ObjectListContainer<T>();
//            container.settList(objList);
//            jedis.setex(key.getBytes(), seconds, SerializationUtil.serialize(container));
//        } catch (Exception e) {
//            returnBrokenJedis(jedis);
//            logger.error("RedisUtil setObjectList error : " + e);
//        } finally {
//            returnJedis(jedis);
//        }
//    }

    /**
     * 获取Object List
     *
     * @param <T>
     *
     * @param <T>
     * @param key
     *            key
     * @return value
     */
//    public static <T> List<T> getObjectList(String key, Class<T> cls) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis == null || !jedis.exists(key.getBytes())) {
//                return null;
//            }
//            byte[] value = jedis.get(key.getBytes());
//            if (value == null) {
//                return null;
//            } else {
//                @SuppressWarnings("unchecked")
//                ObjectListContainer<T> container = SerializationUtil.deserialize(value, ObjectListContainer.class);
//                return container.gettList();
//            }
//        } catch (Exception e) {
//            returnBrokenJedis(jedis);
//            logger.error("RedisUtil getObjectList error : " + e);
//        } finally {
//            returnJedis(jedis);
//        }
//        return null;
//    }

    /**
     * 设置Object Map 和 过期时间
     *
     * @param key
     * @param seconds
     *            以秒为单位
     */
//    public static <T1, T2> void setObjectMap(String key, int seconds, Map<T1, T2> objMap) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            ObjectMapContainer<T1, T2> container = new ObjectMapContainer<T1, T2>();
//            container.settMap(objMap);
//            jedis.setex(key.getBytes(), seconds, SerializationUtil.serialize(container));
//        } catch (Exception e) {
//            returnBrokenJedis(jedis);
//            logger.error("RedisUtil setObjectList error : " + e);
//        } finally {
//            returnJedis(jedis);
//        }
//    }

    /**
     * 获取Object Map
     *
     * @param key
     *            key
     * @return value
     */
//    public static <T1, T2> Map<T1, T2> getObjectMap(String key, Class<T1> c1, Class<T2> c2) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis == null || !jedis.exists(key.getBytes())) {
//                return null;
//            }
//            byte[] value = jedis.get(key.getBytes());
//            if (value == null) {
//                return null;
//            } else {
//                @SuppressWarnings("unchecked")
//                ObjectMapContainer<T1, T2> container = SerializationUtil.deserialize(value, ObjectMapContainer.class);
//                return container.gettMap();
//            }
//        } catch (Exception e) {
//            returnBrokenJedis(jedis);
//            logger.error("RedisUtil getObjectList error : " + e);
//        } finally {
//            returnJedis(jedis);
//        }
//        return null;
//    }

    /**
     * publish message to special channel and queue
     *
     * @param channel
     * @param message
     */
    public static void queuePublish(String channel, String message) {
        queuePublish(channel, message, 0);
    }

    public static void queuePublish(String channel, String message, int failedNum) {
        if (failedNum < 3) {
            Jedis jedis = getJedis();
            try {
                jedis.lpush(channel, message);
                jedis.publish(channel, message);
            } catch (Exception e) {
                logger.error("queuePublish message[" + message + "] to channel[" + channel + "] error[" + failedNum
                        + "] : " + e.getMessage());
                e.printStackTrace();
                returnBrokenJedis(jedis);
                queuePublish(channel, message, failedNum++);
            } finally {
                returnJedis(jedis);
            }
        }
    }

    public static Set<String> setAll(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.smembers(key);
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return null;
    }

    public static void setDelAll(String key) {
        Jedis jedis = getJedis();
        try {
            long total = jedis.scard(key);
            for (int i = 0; i < total; i++) {
                jedis.spop(key);
            }
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * key值的当前剩余时间
     *
     * @param key
     * @return 当前剩余时间秒数
     */
    public static Long ttl(String key) {
        Jedis jedis = getJedis();
        Long v = 0l;
        try {
            v = jedis.ttl(key);
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return v;
    }

    /**
     * key值自增，如果不存在返回0
     *
     * @param key
     * @return
     */
    public static Long incr(String key) {
        Jedis jedis = getJedis();
        Long v = 0l;
        try {
            v = jedis.incr(key.getBytes());
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return v;
    }

    /**
     * 模糊查询获取key值
     *
     * @param pattern
     * @return
     */
    public static Set<String> queryAllKeys(String pattern) {
        Jedis jedis = getJedis();
        Set<String> v = null;
        try {
            v = jedis.keys(pattern);
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
        return v;
    }

    /**
     * 以Pipeline方式存放数据（数据量大的情况下提高效率）
     *
     * @param key
     * @param messages
     */
    public static void pipeListMessages(String key, List<String> messages) {

        Jedis jedis = getJedis();
        Pipeline pipeline = jedis.pipelined();
        try {
            for (String message : messages) {
                pipeline.lpush(key, message);
            }
            pipeline.sync();
        } catch (Exception e) {
            returnBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            returnJedis(jedis);
        }
    }
}
