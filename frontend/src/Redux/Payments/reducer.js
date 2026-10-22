import { CREATE_SALON_FAILURE, CREATE_SALON_REQUEST, CREATE_SALON_SUCCESS, FETCH_SALON_BY_ID_FAILURE, FETCH_SALON_BY_ID_REQUEST, FETCH_SALON_BY_ID_SUCCESS, FETCH_SALON_BY_OWNER_FAILURE, FETCH_SALON_BY_OWNER_REQUEST, FETCH_SALON_BY_OWNER_SUCCESS, FETCH_SALONS_FAILURE, FETCH_SALONS_REQUEST, FETCH_SALONS_SUCCESS, PROCEED_PAYMENT_FAILURE, PROCEED_PAYMENT_REQUEST, PROCEED_PAYMENT_SUCCESS, SEARCH_SALONS_FAILURE, SEARCH_SALONS_REQUEST, SEARCH_SALONS_SUCCESS, UPDATE_SALON_FAILURE, UPDATE_SALON_REQUEST, UPDATE_SALON_SUCCESS } from "./actionTypes";

const initialState={
   
    loading:false,
    success:null,
    error: null,
}
export const proceedPaymentReducer =(state = initialState,action)=>{
    switch(action.type){
        case PROCEED_PAYMENT_REQUEST:
            return{
                ...state,
                loading: true,
                success: null,
                error: null,
            };
            case PROCEED_PAYMENT_SUCCESS:
                return{
                    ...state,
                    loading: false,
                    success: action.payload,
                    error: null,

                }

            case PROCEED_PAYMENT_FAILURE:
                return{
                    ...state,
                    loading: false,
                    success: false,
                    error: action.payload,

                }
    
    
            
        default:
            return state;
    }
}

export default proceedPaymentReducer;