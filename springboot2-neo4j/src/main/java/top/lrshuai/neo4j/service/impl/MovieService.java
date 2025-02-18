package top.lrshuai.neo4j.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lrshuai.neo4j.node.MovieNode;
import top.lrshuai.neo4j.node.PersonNode;
import top.lrshuai.neo4j.relationship.Director;
import top.lrshuai.neo4j.relationship.Role;
import top.lrshuai.neo4j.repository.IMovieRepository;
import top.lrshuai.neo4j.service.IMovieService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MovieService implements IMovieService {

    private IMovieRepository movieRepository;

    @Autowired
    public void setMovieRepository(IMovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void saveMovieAndPerson() {
        // 创建节点
        MovieNode movieNode = new MovieNode().setId(7l).setTitle("国产凌凌漆").setDescription("该片讲述了后备国家特工赶赴香港，去追踪一具价值连城的恐龙头骨架的故事。");

        Role role1 = new Role().setPerson(new PersonNode(1971,"袁咏仪").setId(1l)).setRoles(Collections.singletonList("李香琴"));
        Role role2 = new Role().setPerson(new PersonNode(1946,"罗家英").setId(2l)).setRoles(Collections.singletonList("达闻西"));
        Role role3 = new Role().setPerson(new PersonNode(1962,"周星驰").setId(3l)).setRoles(Collections.singletonList("凌凌漆"));
        // 添加关系
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        roles.add(role3);
        movieNode.setRoles(roles);
        movieNode.setDirectors(Collections.singletonList(new Director().setRoles(Collections.singletonList("导演")).setPerson(role3.getPerson())));
        // 存入图数据库持久化
        movieNode.setTitle("国产007");
        movieRepository.save(movieNode);

//        movieRepository.delete(movieNode);
    }
}
