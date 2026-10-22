
import api from "../../config/api";

import {
  FETCH_BOOKINGS_FAILURE,
  FETCH_BOOKINGS_REQUEST,
  FETCH_BOOKINGS_SUCCESS,
  FETCH_EARNINGS_FAILURE,
  FETCH_EARNINGS_REQUEST,
  FETCH_EARNINGS_SUCCESS,
} from "./actionTypes";

const API_BASE_URL = "/api/bookings/chart";

// =========================
// EARNINGS
// =========================

export const fetchEarnings = (token) => async (dispatch) => {
  console.log("🔥 fetchEarnings CALLED");

  dispatch({ type: FETCH_EARNINGS_REQUEST });

  try {
    console.log("🔥 Calling earnings API...");

    const response = await api.get(`${API_BASE_URL}/earnings`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    console.log("✅ EARNING CHART RESPONSE:", response.data);

    dispatch({
      type: FETCH_EARNINGS_SUCCESS,
      payload: response.data,
    });
  } catch (error) {
    console.log("❌ EARNING CHART ERROR:", error);
    console.log("❌ EARNING ERROR RESPONSE:", error.response?.data);

    dispatch({
      type: FETCH_EARNINGS_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

// =========================
// BOOKINGS
// =========================

export const fetchBookings = (token) => async (dispatch) => {
  console.log("🔥 fetchBookings CALLED");

  dispatch({ type: FETCH_BOOKINGS_REQUEST });

  try {
    console.log("🔥 Calling bookings API...");
    console.log(
      "🔥 URL:",
      `${API_BASE_URL}/bookings`
    );

    const response = await api.get(`${API_BASE_URL}/bookings`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    console.log(
      "✅ BOOKING CHART RESPONSE:",
      JSON.stringify(response.data, null, 2)
    );

    dispatch({
      type: FETCH_BOOKINGS_SUCCESS,
      payload: response.data,
    });
  } catch (error) {
    console.log("❌ BOOKING CHART ERROR:", error);
    console.log("❌ BOOKING ERROR RESPONSE:", error.response?.data);

    dispatch({
      type: FETCH_BOOKINGS_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};
