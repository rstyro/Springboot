package top.lrshuai.neo4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.ExampleMatcher;
import top.lrshuai.neo4j.node.MovieNode;
import top.lrshuai.neo4j.repository.IMovieRepository;
import top.lrshuai.neo4j.service.IMovieService;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SpringbootNeo4jApplicationTests {

    @Resource
    private IMovieRepository movieRepository;

    @Resource
    private IMovieService movieService;

    @Test
    void contextLoads() {
        movieService.saveMovieAndPerson();
    }

    @Test
    void contextLoads2() {
//        movieRepository.deleteById(10l);
//        MovieNode movieNode = new MovieNode();
//        movieNode.setTitle("国产007修改");
//        List<MovieNode> all = movieRepository.findAll(Example.of(movieNode));
//        System.out.println(all);
//        movieRepository.deleteAll(all);
//        movieRepository.delete(all.get(0));

//        movieRepository.deleteAll();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("lastname")
                .withIncludeNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);

        List<MovieNode> movieNodeByRolesLikeOrTitleIsLike = movieRepository.getMovieNodeByRolesOrTitleIsLike("达闻西", "国产11.*");
        System.out.println(movieNodeByRolesLikeOrTitleIsLike);

    }

}
