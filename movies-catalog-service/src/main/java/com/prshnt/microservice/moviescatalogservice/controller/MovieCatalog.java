package com.prshnt.microservice.moviescatalogservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.prshnt.microservice.moviescatalogservice.model.CatalogItem;
import com.prshnt.microservice.moviescatalogservice.model.Movie;
import com.prshnt.microservice.moviescatalogservice.model.Rating;

@Service
public class MovieCatalog {
	
	@Autowired
	RestTemplate restTemplate; 
	
	@Autowired
	WebClient.Builder webClientbuilder;
	
	@Value("${movie.service.url}")
	private String movieInfoUrl;
	
	
	@HystrixCommand(fallbackMethod = "getFallBackCatalogItem",
			commandProperties = {
						@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
						@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "5"),
						@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value ="50" ),
						@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value ="5000" )
								}
			)
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = webClientbuilder.build()
		.get()
		.uri(movieInfoUrl+rating.getMovieId())
		.retrieve()
		.bodyToMono(Movie.class)
		//.timeout(Duration.ofMillis(5000))
		.block();
return	new CatalogItem(movie.getMovieName(),"OLD MEN",rating.getMovieRating());
	}

	private CatalogItem getFallBackCatalogItem(Rating rating) {
		return new CatalogItem("No Movie","No Description",0);
	}
	
	

}
