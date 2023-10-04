package com.marisia.medicare;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marisia.medicare.service.payment.PaymentService;

@AutoConfigureMockMvc
@SpringBootTest
public class PaymentTests {
  @MockBean
  PaymentService paymentService;

  
}
