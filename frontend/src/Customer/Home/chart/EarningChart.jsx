
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
  CartesianGrid,
} from "recharts";
import { fetchEarnings } from "../../../Redux/Chart/action";
import Backdrop from "@mui/material/Backdrop";
import CircularProgress from "@mui/material/CircularProgress";

const EarningChart = () => {
  const dispatch = useDispatch();

  // Select only the chart state instead of the entire Redux store
  const chart = useSelector((store) => store.chart);

  useEffect(() => {
    dispatch(fetchEarnings(localStorage.getItem("jwt")));
  }, [dispatch]);

  const earningsData = chart?.earnings?.data || [];
  const loading = chart?.earnings?.loading;

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
          data={earningsData}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5,
          }}
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

export default EarningChart;