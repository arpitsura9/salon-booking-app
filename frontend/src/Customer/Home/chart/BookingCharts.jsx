
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import { fetchBookings } from "../../../Redux/Chart/action";
import Backdrop from "@mui/material/Backdrop";
import CircularProgress from "@mui/material/CircularProgress";

const BookingCharts = () => {
  const dispatch = useDispatch();

  // Only select chart state
  const chart = useSelector((store) => store.chart);

  useEffect(() => {
    dispatch(fetchBookings(localStorage.getItem("jwt")));
  }, [dispatch]);

  // Bookings data
  const bookingsData = chart?.bookings?.data || [];
  const loading = chart?.bookings?.loading;

  if (loading) {
    return (
      <Backdrop open={true}>
        <CircularProgress color="inherit" />
      </Backdrop>
    );
  }

  return (
    <div className="h-[40vh] w-full">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          data={bookingsData}
          margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" />

          <XAxis dataKey="daily" />

          <YAxis />

          <Tooltip />

          <Line
            type="monotone"
            dataKey="earnings"
            stroke="#8884d8"
            strokeWidth={2}
            activeDot={{ r: 8 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default BookingCharts;
