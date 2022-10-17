package top.lrshuai.neo4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.neo4j.service.IMovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private IMovieService movieService;

    @Autowired
    public void setMovieService(IMovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object save() {
        movieService.saveMovieAndPerson();
        return "success";
    }
}
