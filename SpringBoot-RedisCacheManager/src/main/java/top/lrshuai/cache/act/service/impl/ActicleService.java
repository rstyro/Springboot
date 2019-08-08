package top.lrshuai.cache.act.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import top.lrshuai.cache.act.entity.Acticle;
import top.lrshuai.cache.act.mapper.ActicleMapper;
import top.lrshuai.cache.act.service.IActicleService;

import java.time.LocalDateTime;
import java.util.List;

@CacheConfig(cacheNames = "act")
@Service
public class ActicleService implements IActicleService {

    @Autowired
    private ActicleMapper acticleMapper;

    /**
     * @Cacheable
     * 1、先查缓存，
     * 2、若没有缓存，就执行方法
     * 3、若有缓存。则返回，不执行方法
     *
     * 所以@Cacheable 不能使用result
     *
     * @return
     * @throws Exception
     */
    @Cacheable(key = "#root.methodName")
    public List<Acticle> list() throws Exception {
        return acticleMapper.getActicleList();
    }

    /**
     * @CachePut 更新并刷新缓存
     * 1、先调用目标方法
     * 2、把结果缓存
     * @param acticle
     * @return
     * @throws Exception
     */
    @CachePut(key = "#result.id" ,unless = "#result.id == null" )
    public Acticle save(Acticle acticle) throws Exception {
        acticle.setCreateBy(1l);
        acticle.setCreateTime(LocalDateTime.now());
        acticle.setModifyBy(1l);
        acticle.setModifyTime(LocalDateTime.now());
        acticleMapper.save(acticle);
        System.out.println("acticle="+acticle);
        return acticle;
    }

    /**
     * 删除指定key 的缓存
     * beforeInvocation=false  缓存的清除是否在方法之前执行
     * 默认是在方法之后执行
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(key = "#id",beforeInvocation = true)
    public int del(Long id) throws Exception {
        int isDel = 0;
        isDel = acticleMapper.del(id);
        return isDel;
    }

    /**
     * 删除所有缓存
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public int delAll() throws Exception {
        return 1;
    }

    @CachePut(key = "#result.id" ,unless = "#result.id == null" )
    public Acticle update(Acticle acticle) throws Exception {
        acticle.setModifyBy(1l);
        acticle.setModifyTime(LocalDateTime.now());
        return acticleMapper.update(acticle);
    }

    @Cacheable(key = "#id",condition = "#id > 0")
    public Acticle queryById(Long id) throws Exception {
        return acticleMapper.queryById(id);
    }

    /**
     * @Caching复杂组合缓存注解
     *
     * @param title
     * @return
     * @throws Exception
     */
    @Caching(cacheable = { @Cacheable(key = "#title")},
            put = {@CachePut(key = "#result.id"),
//            @CachePut(key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
            @CachePut(key = "T(String).valueOf('tag').concat('-').concat(#result.tagId)")
    })
    public Acticle queryByTitle(String title) throws Exception {
        return acticleMapper.queryByTitle(title);
    }

    @Cacheable(key = "T(String).valueOf('tag').concat('-').concat(#tagId)")
    public Acticle queryByTag(Long tagId) throws Exception {
        return null;
    }
}
