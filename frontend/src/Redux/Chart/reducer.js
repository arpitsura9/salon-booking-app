import {
  FETCH_BOOKINGS_FAILURE,
  FETCH_BOOKINGS_REQUEST,
  FETCH_BOOKINGS_SUCCESS,
  FETCH_EARNINGS_FAILURE,
  FETCH_EARNINGS_REQUEST,
  FETCH_EARNINGS_SUCCESS,
} from "./actionTypes";

const initialState = {
  earnings: {
    loading: false,
    data: [],
    error: null,
  },

  bookings: {
    loading: false,
    data: [],
    error: null,
  },
};

const chartReducer = (state = initialState, action) => {
  switch (action.type) {
    // =========================
    // EARNINGS
    // =========================

    case FETCH_EARNINGS_REQUEST:
      return {
        ...state,
        earnings: {
          loading: true,
          data: [],
          error: null,
        },
      };

    case FETCH_EARNINGS_SUCCESS:
      return {
        ...state,
        earnings: {
          loading: false,
          data: action.payload || [],
          error: null,
        },
      };

    case FETCH_EARNINGS_FAILURE:
      return {
        ...state,
        earnings: {
          loading: false,
          data: [],
          error: action.payload,
        },
      };

    // =========================
    // BOOKINGS
    // =========================

    case FETCH_BOOKINGS_REQUEST:
      return {
        ...state,
        bookings: {
          loading: true,
          data: [],
          error: null,
        },
      };

    case FETCH_BOOKINGS_SUCCESS:
      return {
        ...state,
        bookings: {
          loading: false,
          data: action.payload || [],
          error: null,
        },
      };

    case FETCH_BOOKINGS_FAILURE:
      return {
        ...state,
        bookings: {
          loading: false,
          data: [],
          error: action.payload,
        },
      };

    default:
      return state;
  }
};

export default chartReducer;
