package com.arpit.service;

import com.arpit.domain.PaymentMethod;
import com.arpit.modal.PaymentOrder;
import com.arpit.payload.dto.BookingDTO;
import com.arpit.payload.dto.UserDTO;
import com.arpit.payload.response.PaymentLinkResponse;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO user,
                                    BookingDTO booking,
                                    PaymentMethod paymentMethod) throws RazorpayException;

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorpayPaymentLink(UserDTO user,
                                          Long amount,
                                          Long orderId) throws RazorpayException;


    String createStripePaymentLink(UserDTO user,
                                     Long amount,
                                     Long orderId);

    Boolean proceedPayment(PaymentOrder paymentOrder,String paymentId,String paymentLinkId) throws RazorpayException;
}
