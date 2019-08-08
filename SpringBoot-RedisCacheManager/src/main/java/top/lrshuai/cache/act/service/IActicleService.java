package top.lrshuai.cache.act.service;

import top.lrshuai.cache.act.entity.Acticle;

import java.util.List;

public interface IActicleService {
    public List<Acticle> list() throws Exception;
    public Acticle save(Acticle acticle) throws Exception;
    public int del(Long id) throws Exception;
    public int delAll() throws Exception;
    public Acticle update(Acticle acticle) throws Exception;
    public Acticle queryById(Long id) throws Exception;
    public Acticle queryByTitle(String title) throws Exception;
    public Acticle queryByTag(Long tagId) throws Exception;
}
