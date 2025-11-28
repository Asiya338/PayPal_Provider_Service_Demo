package com.example.demo.paypal.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paypal {

	@JsonProperty("experience_context")
	private ExperienceContext experienceContext;

}
