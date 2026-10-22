import axios from "axios"
import { PROCEED_PAYMENT_FAILURE, PROCEED_PAYMENT_REQUEST, PROCEED_PAYMENT_SUCCESS } from "./actionTypes";
import api from "../../config/api";

export const paymentSuccessAction=({ paymentId, paymentLinkId, jwt}) => async(dispatch)=>{
    dispatch({type:PROCEED_PAYMENT_REQUEST})

    try {
        const response = await api.patch("/api/payments/proceed",null, {
            headers:{
                Authorization:`Bearer ${jwt}`,
                
            },
            params: {paymentId, paymentLinkId},
        })

        dispatch({type:PROCEED_PAYMENT_SUCCESS, payload:response.data})

        console.log("Payment", response.data);
    } catch (error) {

        console.log("Payment failed ", error);
        dispatch({type:PROCEED_PAYMENT_FAILURE, payload:error.response ? error.response.data : error.message,});
    }
}

