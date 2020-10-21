package com.prshnt.microservice.moviescatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prshnt.microservice.moviescatalogservice.model.CatalogItem;
import com.prshnt.microservice.moviescatalogservice.model.UserRatings;

@RestController
@RequestMapping("/Catalog")
public class MoviesCatalogResource {
	
	
	@Autowired
	MovieCatalog movieCatalog;
	
	@Autowired
	UserRating userRating;
	
	@RequestMapping("/{userid}")
	public List<CatalogItem> getCatalog(@PathVariable("userid") String userId) {
		UserRatings userRatings = userRating.getUserRating(userId);
		return userRatings.getUserRatings().stream().map(rating ->{
			return movieCatalog.getCatalogItem(rating);
		})
		.collect(Collectors.toList());
	}

	
	
	//Using Restemplates
	//Movie movie = restTemplate.getForObject("http://localhost:8081/movies/"+rating.getMovieId(), Movie.class);
	//List<Rating> userRatings =  (List<Rating>) restTemplate.getForObject("http://localhost:8082/ratingdata/"+userId, UserRatings.class);
}
