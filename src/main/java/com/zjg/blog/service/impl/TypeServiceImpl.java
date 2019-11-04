package com.zjg.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.annotation.CacheRemove;
import com.zjg.blog.mapper.TypeMapper;
import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.BlogService;
import com.zjg.blog.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjg
 */
@SuppressWarnings("ALL")
@CacheConfig(cacheNames = "typeCache"/*, cacheManager = "typeCacheManager"*/)  //抽取配置公共的部分
@Service
public class TypeServiceImpl implements TypeService {

    private static final Logger logger = LoggerFactory.getLogger(TypeServiceImpl.class);

    /**
     * 将方法的运行结果进行缓存；以后再要相同的数据，直接从缓存中获取，不用调用方法；
     * CacheManager管理多个Cache组件的，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一一个名字；
     *

     *
     * 原理：
     *   1、自动配置类；CacheAutoConfiguration
     *   2、缓存的配置类
     *   org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.GuavaCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration【默认】
     *   org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     *   3、哪个配置类默认生效：SimpleCacheConfiguration；
     *
     *   4、给容器中注册了一个CacheManager：ConcurrentMapCacheManager
     *   5、可以获取和创建ConcurrentMapCache类型的缓存组件；他的作用将数据保存在ConcurrentMap中；
     *
     *      先是在 CacheAutoConfiguration 中的类头的注解上面的 @Import 引入了 CacheConfigurationImportSelector
     *      这个类的 selectImports(AnnotationMetadata importingClassMetadata) 方法返回 springboot
     *          默认配置的所有缓存配置类
     *      在通过 debug=true 这项配置详细打印配置文件，通过日志发现 SimpleCacheConfiguration 类匹配上
     *      Simple... 这个类给容器注入一个 ConcurrentMapCacheManager 组件
     *      ConcurrentMapCacheManager 这个组件实现了 CacheManager 这个接口
     *      cacheManager 这个接口内有 getCache(String name) 这个方法，获取指定名字的 Cache
     *      ConcurrentMapCacheManager 重写了 getCache 方法：
     *          Cache cache = this.cacheMap.get(name);
     * 		    if (cache == null && this.dynamic) {
     * 		    	synchronized (this.cacheMap) {
     * 		    		cache = this.cacheMap.get(name);
     * 			    	if (cache == null) {
     * 			    		cache = createConcurrentMapCache(name);
     * 			    		this.cacheMap.put(name, cache);
     *                   }
     *              }
     *          }
     * 		    return cache;
     * 		方法体中的 cacheMap 是 ConcurrentMap<String, Cache> 类型的，此方法使用了单例设计模式
     * 	    ConcurrentMapCacheManager 中的 createConcurrentMapCache(String name) 会
     * 	        创建一个指定名字的 ConcurrentMapCache 类型的对象
     *
     *
     *   运行流程：
     *   @Cacheable：
     *   1、方法运行之前，先去查询Cache（缓存组件），按照cacheNames指定的名字获取；
     *      （CacheManager先获取相应的缓存），第一次获取缓存如果没有Cache组件会自动创建。
     *   2、去Cache中查找缓存的内容，使用一个key，默认就是方法的参数；
     *      key是按照某种策略生成的；默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key；
     *          SimpleKeyGenerator生成key的默认策略；
     *                  如果没有参数；key=new SimpleKey()；
     *                  如果有一个参数：key=参数的值
     *                  如果有多个参数：key=new SimpleKey(params)；
     *   3、没有查到缓存就调用目标方法；
     *   4、将目标方法返回的结果，放进缓存中
     *
     *   @Cacheable标注的方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存，
     *   如果没有就运行方法并将结果放入缓存；以后再来调用就可以直接使用缓存中的数据；
     *
     *   核心：
     *      1）、使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCache】组件
     *      2）、key使用keyGenerator生成的，默认是SimpleKeyGenerator
     *
     *
     *   几个属性：
     *      cacheNames/value：指定缓存组件的名字;将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓存；
     *
     *      key：缓存数据使用的key；可以用它来指定。默认是使用方法参数的值  1-方法的返回值
     *              编写SpEL； #i d;参数id的值   #a0  #p0  #root.args[0]
     *              getEmp[2]
     *
     *      keyGenerator：key的生成器；可以自己指定key的生成器的组件id
     *              key/keyGenerator：二选一使用;
     *
     *
     *      cacheManager：指定缓存管理器；或者cacheResolver指定获取解析器
     *
     *      condition：指定符合条件的情况下才缓存；
     *              ,condition = "#id>0"
     *          condition = "#a0>1"：第一个参数的值》1的时候才进行缓存
     *
     *      unless:否定缓存；当unless指定的条件为true，方法的返回值就不会被缓存；可以获取到结果进行判断
     *              unless = "#result == null"
     *              unless = "#a0==2":如果第一个参数的值是2，结果不缓存；
     *      sync：是否使用异步模式
     * @param id
     * @return
     *
     */
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private BlogService blogService;

    //添加事务注解
    //1.使用 propagation 指定事务的传播行为, 即当前的事务方法被另外一个事务方法调用时
    //如何使用事务, 默认取值为 REQUIRED, 即使用调用方法的事务
    //REQUIRES_NEW: 事务自己的事务, 调用的事务方法的事务被挂起.
    //2.使用 isolation 指定事务的隔离级别, 最常用的取值为 READ_COMMITTED
    //3.默认情况下 Spring 的声明式事务对所有的运行时异常进行回滚. 也可以通过对应的
    //属性进行设置. 通常情况下去默认值即可.
    //4.使用 readOnly 指定事务是否为只读. 表示这个事务只读取数据但不更新数据,
    //这样可以帮助数据库引擎优化事务. 若真的事一个只读取数据库值的方法, 应设置 readOnly=true
    //5.使用 timeout 指定强制回滚之前事务可以占用的时间.
    /*@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName() + ' ' +  #type", unless = "#result == null")
    @Override
    public Type findType(Type type, String... attributes) {
        return typeMapper.selectType(type, attributes);
    }*/

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public Type findTypeByTypeId(long typeId) {
        return typeMapper.selectTypeByTypeId(typeId);
    }

    /*
        此方法使用频率低并且不便于设置缓存的唯一 key 所以，不进行缓存
     */
    @Transactional(readOnly = true)
    @Override
    public Type findTypeByTypeName(String typeName) {
        return typeMapper.selectTypeByTypeName(typeName);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Cacheable(/*key = "#root.targetClass.getSimpleName()"*/)
    @Override
    public PageInfo<Type> findTypeAll(int pageNum, int pageSize) {
        return findTypeAll(pageNum, pageSize, 0);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Cacheable(/*key = "#root.targetClass.getSimpleName()"*/)
    @Override
    public PageInfo<Type> findTypeAll(int pageNum, int pageSize, int navigatePages) {
        PageHelper.startPage(pageNum, pageSize);
        List<Type> types = typeMapper.selectTypeAll();
        PageInfo<Type> pageInfo = new PageInfo(types, navigatePages);
        return pageInfo;
    }

    //添加方法不需要缓存吧？原本就没有新曾内容的数据
    @Transactional(propagation = Propagation.REQUIRED)
    @CachePut(key = "#root.targetClass.getSimpleName() + ' ' + #type.getTypeId()", unless = "#result == null")
    @CacheRemove( key = {"*TypeServiceImpl \\[*","*BlogServiceImpl*"})
    @Override
    public Type saveType(Type type) {
        if (typeMapper.insertType(type)>0) {
            return type;
        }else {
            return null;
        }
    }

    /*
     * @CacheEvict：缓存清除
     *  key：指定要清除的数据
     *  allEntries = true：指定清除这个缓存中所有的数据
     *  beforeInvocation = false：缓存的清除是否在方法之前执行
     *      默认代表缓存清除操作是在方法执行之后执行;如果出现异常缓存就不会清除
     *
     *  beforeInvocation = true：
     *      代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
     *
     * 不仅要清除单个 type 还要清除所有的 分页数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict( key = "#root.targetClass.getSimpleName() + ' ' + #typeId", condition = "#result != 0")
    @CacheRemove( key = {"*TypeServiceImpl \\[*","*BlogServiceImpl*"})
    @Override
    public int removeTypeByTypeId(long typeId) {
        Type type = new Type();
        type.setTypeId(typeId);
        PageInfo<Blog> blogList = blogService.findBlogByTypeAndTitleAndRecommend(0, 0, 0, type, null, null);
        List<Blog> blogs = blogList.getList();
        if (blogs.size()<=0) {
            return typeMapper.deleteTypeByTypeId(typeId);
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < blogs.size(); i++) {
                stringBuilder.append(blogs.get(i).getBlogId());
                if (i == blogs.size()-1) {
                    break;
                }
                stringBuilder.append(", ");
            }

            logger.info("分类{}下有博客: {}", type.getTypeId(), stringBuilder);
        }
        return 0;
    }


    /*
     * @CachePut：既调用方法，又更新缓存数据；z
     * 修改了数据库的某个数据，同时更新缓存；
     * 运行时机：
     *  1、先调用目标方法
     *  2、将目标方法的结果缓存起来
     *
     * 测试步骤：
     *  1、查询1号员工；查到的结果会放在缓存中；
     *          key：1  value：lastName：张三
     *  2、以后查询还是之前的结果
     *  3、更新1号员工；【lastName:zhangsan；gender:0】
     *          将方法的返回值也放进缓存了；
     *          key：传入的employee对象  值：返回的employee对象；
     *  4、查询1号员工？
     *      应该是更新后的员工；
     *          key = "#employee.id":使用传入的参数的员工id；
     *          key = "#result.id"：使用返回后的id
     *             @Cacheable的key是不能用#result
     *      为什么是没更新前的？【1号员工没有在缓存中更新】
     *      因为 key 的原因，默认用户 SimpleKeyGenerator 生成的 key 是和参数有关的，所以 没有更新
     *          要指定 @CachePut 的 key
     *              key = "#employee.id":使用传入的参数的员工id；
     *              key = "#result.id"：使用返回后的id
     *      返回的类型，还要和指定的 key 原先的类型 一致，注意
     *
     * 修改数据不仅要更新此 type 缓存，还要清空分页数据
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //@Caching 定义复杂的规则，多个注解的集合
    @CachePut(key = "#root.targetClass.getSimpleName() + ' ' + #type.getTypeId()", unless = "#result == null")
    @CacheRemove( key = {"*TypeServiceImpl \\[*","*BlogServiceImpl*"})
    @Override
    public Type modifyType(Type type) {
        // typeId 是唯一的判断 ==1 比较合理
        if (typeMapper.updateType(type) == 1) {
            return  type;
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName() + ' [ top_' + #topCount + ' ]'")
    @Override
    public List<Type> findTopType(int topCount) {
        List<Type> list = typeMapper.topType(topCount);
        for (Type type : list) {
            type.setBlogs(blogService.findBlogByTypeId(type.getTypeId()));
        }
        return list;
    }
}
