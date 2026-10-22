import './App.css';
import { ThemeProvider } from "@mui/material";
import greenTheme from "./theme/greenTheme";

import Home from './Customer/Home/Home';
import SalonDetails from './Salon/Salon Details/SalonDetails';
import Bookings from './Booking/Bookings';
import Notifications from './Notification/Notifications';
import Navbar from './Navbar/Navbar';
import SalonDashboard from './Seller/SalonDashboard';

import { Route, Routes } from 'react-router-dom';
import CustomerRoutes from './Routes/CustomerRoutes';
import LoginForm from './Auth/LoginForm';
import Auth from './Auth/Auth';
import BecomePartner from './Seller/BecomePartner/BecomePartner';

function App() {
  return (
    <ThemeProvider theme={greenTheme}>

      <Routes>
        

        <Route
          path="/salon-dashboard/*"
          element={<SalonDashboard />}
        />

        <Route
          path="/register"
          element={<Auth />}
        />

        <Route
          path="/login"
          element={<Auth />}
        />

        <Route
          path="/become-partner"
          element={<BecomePartner />}
        />

        <Route
          path="*"
          element={<CustomerRoutes />}
        />

        
      </Routes>

    </ThemeProvider>
  );
}

export default App;