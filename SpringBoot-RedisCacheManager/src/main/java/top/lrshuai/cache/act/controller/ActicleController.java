package top.lrshuai.cache.act.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.cache.act.entity.Acticle;
import top.lrshuai.cache.act.service.IActicleService;

@RestController
@RequestMapping("/act")
public class ActicleController {

    @Autowired
    private IActicleService acticleService;

    @GetMapping("/list")
    public Object list() throws Exception{
        return acticleService.list();
    }

    @GetMapping("/add")
    public Object add(Acticle acticle) throws Exception{
        return acticleService.save(acticle);
    }

    @GetMapping("/del/{id}")
    public Object del(@PathVariable("id") Long id) throws Exception{
        return acticleService.del(id);
    }

    @GetMapping("/delall")
    public Object delAll() throws Exception{
        return acticleService.delAll();
    }

    @GetMapping("/get/id/{id}")
    public Object getActicleById(@PathVariable("id") Long id) throws Exception{
        return acticleService.queryById(id);
    }

    @GetMapping("/get/title/{title}")
    public Object getActicleByTitle(@PathVariable("title") String title) throws Exception{
        return acticleService.queryByTitle(title);
    }
    @GetMapping("/get/tag/{tagId}")
    public Object getActicleByTag(@PathVariable("tagId") Long tagId) throws Exception{
        return acticleService.queryByTag(tagId);
    }

    @GetMapping("/update/{id}")
    public Object update(Acticle acticle) throws Exception{
        return acticleService.update(acticle);
    }
}
