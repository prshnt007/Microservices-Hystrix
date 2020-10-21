package com.prshnt.microservice.moviescatalogservice.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.prshnt.microservice.moviescatalogservice.model.Rating;
import com.prshnt.microservice.moviescatalogservice.model.UserRatings;
//Bulk head pattern
@Service
public class UserRating {
	
	@Autowired
	WebClient.Builder webClientbuilder;
	
	@Value("${rating.service.url}")
	private String ratingsDataUrl;
	
	@HystrixCommand(fallbackMethod = "getFallBackUserRating"
			/*threadPoolKey = "ratingpool",
			threadPoolProperties = {
				@HystrixProperty(name ="coresize" , value ="20"),
				@HystrixProperty(name ="maxQueueSize" ,value = "10")
			}*/
		)
	public UserRatings getUserRating(String userId) {
		return webClientbuilder.build()
		.get()
		.uri(ratingsDataUrl+userId)
		.retrieve()
		.bodyToMono(UserRatings.class)
		//.timeout(Duration.ofMillis(5000))
		.block();
	}
	
	
	private UserRatings getFallBackUserRating(String userId) {
		return new UserRatings(Arrays.asList(new Rating("Movie Name not found",0)));
	}

}
