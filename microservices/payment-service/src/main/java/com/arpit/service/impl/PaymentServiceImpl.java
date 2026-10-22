package com.arpit.service.impl;

import com.arpit.domain.PaymentMethod;
import com.arpit.domain.PaymentOrderStatus;
import com.arpit.messagining.BookingEventProducer;
import com.arpit.messagining.NotificationEventProducer;
import com.arpit.modal.PaymentOrder;
import com.arpit.messagining.NotificationEventProducer;
import com.arpit.payload.dto.BookingDTO;
import com.arpit.payload.dto.UserDTO;
import com.arpit.payload.response.PaymentLinkResponse;
import com.arpit.repository.PaymentRepository;
import com.arpit.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingEventProducer bookingEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;


    // ============================================================
    // CREATE PAYMENT ORDER
    // ============================================================

    @Override
    public PaymentLinkResponse createOrder(
            UserDTO user,
            BookingDTO booking,
            PaymentMethod paymentMethod
    ) throws RazorpayException {

        Long amount = (long) booking.getTotalPrice();

        // Create PaymentOrder
        PaymentOrder order = new PaymentOrder();

        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setBookingId(booking.getId());
        order.setSalonId(booking.getSalonId());
        order.setUserId(user.getId());

        // Save PaymentOrder first
        PaymentOrder savedOrder = paymentRepository.save(order);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();


        // ========================================================
        // RAZORPAY
        // ========================================================

        if (PaymentMethod.RAZORPAY.equals(paymentMethod)) {

            PaymentLink payment = createRazorpayPaymentLink(
                    user,
                    savedOrder.getAmount(),
                    savedOrder.getId()
            );

            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            /*
             * IMPORTANT:
             *
             * Razorpay payment link ID ko PaymentOrder ke andar
             * save karna zaroori hai.
             *
             * Example:
             * plink_xxxxxxxxx
             */
            savedOrder.setPaymentLinkId(paymentUrlId);

            // Set response
            paymentLinkResponse.setPayment_link_url(paymentUrl);
            paymentLinkResponse.setGetPayment_link_id(paymentUrlId);

            // Save updated PaymentOrder
            paymentRepository.save(savedOrder);

        } else {

            // ====================================================
            // STRIPE
            // ====================================================

            String paymentUrl = createStripePaymentLink(
                    user,
                    savedOrder.getAmount(),
                    savedOrder.getId()
            );

            paymentLinkResponse.setPayment_link_url(paymentUrl);
        }

        return paymentLinkResponse;
    }


    // ============================================================
    // GET PAYMENT ORDER BY ID
    // ============================================================

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {

        PaymentOrder paymentOrder = paymentRepository
                .findById(id)
                .orElse(null);

        if (paymentOrder == null) {
            throw new Exception(
                    "Payment order not found with id: " + id
            );
        }

        return paymentOrder;
    }


    // ============================================================
    // GET PAYMENT ORDER BY PAYMENT LINK ID
    // ============================================================

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) {

        PaymentOrder paymentOrder =
                paymentRepository.findByPaymentLinkId(paymentLinkId);

        if (paymentOrder == null) {
            throw new IllegalArgumentException(
                    "Payment order not found for paymentLinkId: "
                            + paymentLinkId
            );
        }

        return paymentOrder;
    }


    // ============================================================
    // CREATE RAZORPAY PAYMENT LINK
    // ============================================================

    @Override
    public PaymentLink createRazorpayPaymentLink(
            UserDTO user,
            Long amount,
            Long orderId
    ) throws RazorpayException {

        // Razorpay amount is in paise
        Long razorpayAmount = amount * 100;

        RazorpayClient razorpay = new RazorpayClient(
                razorpayApiKey,
                razorpayApiSecret
        );

        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put(
                "amount",
                razorpayAmount
        );

        paymentLinkRequest.put(
                "currency",
                "INR"
        );


        // ========================================================
        // CUSTOMER DETAILS
        // ========================================================

        JSONObject customer = new JSONObject();

        customer.put(
                "name",
                user.getFullName()
        );

        customer.put(
                "email",
                user.getEmail()
        );

        paymentLinkRequest.put(
                "customer",
                customer
        );


        // ========================================================
        // NOTIFICATION
        // ========================================================

        JSONObject notify = new JSONObject();

        notify.put(
                "email",
                true
        );

        paymentLinkRequest.put(
                "notify",
                notify
        );


        // ========================================================
        // REMINDER
        // ========================================================

        paymentLinkRequest.put(
                "reminder_enable",
                true
        );


        // ========================================================
        // CALLBACK
        // ========================================================

        paymentLinkRequest.put(
                "callback_url",
                "http://localhost:3000/payment_success/" + orderId
        );

        paymentLinkRequest.put(
                "callback_method",
                "get"
        );


        // Create Razorpay Payment Link
        return razorpay.paymentLink.create(
                paymentLinkRequest
        );
    }


    // ============================================================
    // CREATE STRIPE PAYMENT LINK
    // ============================================================

    @Override
    public String createStripePaymentLink(
            UserDTO userDTO,
            Long amount,
            Long orderId
    ) {

        // Stripe implementation will be added later
        return "";
    }


    // ============================================================
    // PROCEED PAYMENT
    // ============================================================

    @Override
    public Boolean proceedPayment(
            PaymentOrder paymentOrder,
            String paymentId,
            String paymentLinkId
    ) throws RazorpayException {

        // ========================================================
        // NULL CHECK
        // ========================================================

        if (paymentOrder == null) {

            throw new IllegalArgumentException(
                    "Payment order not found for paymentLinkId: "
                            + paymentLinkId
            );
        }


        // ========================================================
        // ONLY PROCESS PENDING ORDERS
        // ========================================================

        if (!PaymentOrderStatus.PENDING.equals(
                paymentOrder.getStatus()
        )) {

            return false;
        }


        // ========================================================
        // RAZORPAY PAYMENT
        // ========================================================

        if (PaymentMethod.RAZORPAY.equals(
                paymentOrder.getPaymentMethod()
        )) {

            RazorpayClient razorpay = new RazorpayClient(
                    razorpayApiKey,
                    razorpayApiSecret
            );


            // ====================================================
            // FETCH PAYMENT FROM RAZORPAY
            // ====================================================

            Payment payment =
                    razorpay.payments.fetch(paymentId);

            String status = payment.get("status");


            // ====================================================
            // PAYMENT CAPTURED SUCCESSFULLY
            // ====================================================

            if ("captured".equals(status)) {

                /*
                 * Send booking update event
                 */
                bookingEventProducer.sentBookingUpdateEvent(
                        paymentOrder
                );


                /*
                 * Send notification
                 */
                notificationEventProducer.sentNotification(
                        paymentOrder.getBookingId(),
                        paymentOrder.getUserId(),
                        paymentOrder.getSalonId()
                );


                /*
                 * Update payment status
                 */
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);


                /*
                 * Save PaymentOrder
                 */
                paymentRepository.save(paymentOrder);


                return true;
            }


            // Payment not captured
            return false;
        }


        // ========================================================
        // STRIPE PAYMENT
        // ========================================================

        paymentOrder.setStatus(
                PaymentOrderStatus.SUCCESS
        );

        paymentRepository.save(paymentOrder);

        return true;
    }
}