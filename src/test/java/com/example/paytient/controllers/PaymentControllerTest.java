package com.example.paytient.controllers;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.paytient.domain.PaymentResponse;
import com.example.paytient.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

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
        PaymentResponse response = new PaymentResponse();
        LocalDate now = LocalDate.now();
        response.setDueDate(now);
        response.setUpdatedBalance(150.00);
		Mockito.when(mockPaymentService.performOneTimePayment(10)).thenReturn(response);
		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.accept(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(10)))
				.andExpect(status().isOk());
//				.andExpect(jsonPath("$.updatedBalance").value("150.00"));
	}

	@Test
	public void postOneTimePayment_BadRequest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(-10)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void postOneTimePayment_ServerError() throws Exception {
		Mockito.when(mockPaymentService.performOneTimePayment(10)).thenThrow(new Exception());
		mvc.perform(MockMvcRequestBuilders.post("/one-time-payment")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(10)))
				.andExpect(status().isInternalServerError());
	}

}