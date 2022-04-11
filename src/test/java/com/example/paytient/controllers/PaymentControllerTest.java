package com.example.paytient.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.paytient.domain.PaymentRequest;
import com.example.paytient.domain.PaymentResponse;
import com.example.paytient.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PaymentService mockPaymentService;

  @Test
	public void getIndex() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Welcome to Paytient")));
	}

	@Test
	public void postOneTimePayment_Success() throws Exception {
		PaymentRequest request = new PaymentRequest("1", 10);

        PaymentResponse response = new PaymentResponse();
        LocalDate now = LocalDate.now();
        response.setDueDate(now);
        response.setUpdatedBalance("150.00");
		Mockito.when(mockPaymentService.performOneTimePayment(request)).thenReturn(response);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		PaymentResponse mappedResponse = objectMapper.readValue(contentAsString, PaymentResponse.class);
		Assertions.assertEquals(mappedResponse.getDueDate(), now);
		Assertions.assertEquals(mappedResponse.getUpdatedBalance(), "150.00");
	}

	@Test
	public void postOneTimePayment_BadPaymentAmount() throws Exception {
		PaymentRequest request = new PaymentRequest("1", -10);

		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Payment must be greater than $0.00"));

	}

	@Test
	public void postOneTimePayment_NullId() throws Exception {
		PaymentRequest request = new PaymentRequest(null, 10);

		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Payment request must include user ID"));

	}

	@Test
	public void postOneTimePayment_ServerError() throws Exception {
		PaymentRequest request = new PaymentRequest("1", 10);

		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)));

		Mockito.when(mockPaymentService.performOneTimePayment(request)).thenThrow(new
				IllegalArgumentException());
		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("An error has occurred"));
	}
}