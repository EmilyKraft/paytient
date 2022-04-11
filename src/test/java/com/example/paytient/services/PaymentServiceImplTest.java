 package com.example.paytient.services;

 import com.example.paytient.domain.PaymentRequest;
 import com.example.paytient.domain.PaymentResponse;
 import com.example.paytient.utils.PaymentUtils;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.Mockito;
 import org.springframework.beans.factory.annotation.Autowired;

 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
 import org.springframework.test.context.junit.jupiter.SpringExtension;
 import org.springframework.test.util.ReflectionTestUtils;

 import java.time.LocalDate;


 @SpringBootTest
 @ExtendWith(SpringExtension.class)
 public class PaymentServiceImplTest {

     @MockBean
     private PaymentUtils mockPaymentUtils;

     @Autowired
     private PaymentServiceImpl paymentService;

     @BeforeEach
     void init() {
         paymentService.getAccount1().setBalance(100);
     }

     @Test
     public void performOneTimePayment_Success() throws Exception {
         ReflectionTestUtils.setField(paymentService, "paymentFlag", true);
         PaymentRequest request = new PaymentRequest("1", 10);

         LocalDate now = LocalDate.now();
         Mockito.when(mockPaymentUtils.computeDueDate()).thenReturn(now);
         Mockito.when(mockPaymentUtils.computeMatchAmount(10)).thenReturn(.30);
         PaymentResponse response = paymentService.performOneTimePayment(request);

         Assertions.assertEquals(response.getUpdatedBalance(), "89.70");
         Assertions.assertEquals(response.getDueDate(), now);
     }

     @Test
     public void performOneTimePayment_NoMatch() {
         ReflectionTestUtils.setField(paymentService, "paymentFlag", false);
         PaymentRequest request = new PaymentRequest("1", 10);

         LocalDate now = LocalDate.now();
         Mockito.when(mockPaymentUtils.computeDueDate()).thenReturn(now);
         Mockito.when(mockPaymentUtils.computeMatchAmount(10)).thenReturn(.10);
         PaymentResponse response = paymentService.performOneTimePayment(request);

         Assertions.assertEquals(response.getUpdatedBalance(), "90.00");
         Assertions.assertEquals(response.getDueDate(), now);
     }
 }