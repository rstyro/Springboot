package top.lrshuai.cache.act.mapper;

import org.springframework.stereotype.Component;
import top.lrshuai.cache.act.entity.Acticle;

import java.util.List;

@Component
public interface ActicleMapper {
    public int save(Acticle acticle) throws Exception;
    public int del(Long id) throws Exception;
    public Acticle update(Acticle acticle) throws Exception;
    public Acticle queryById(Long id) throws Exception;
    public Acticle queryByTitle(String title) throws Exception;
    public List<Acticle> getActicleList();
}
