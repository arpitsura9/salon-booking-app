import React from 'react'
import Home from '../Customer/Home/Home';
import SalonDetails from '../Salon/Salon Details/SalonDetails';
import Bookings from '../Booking/Bookings';
import Notifications from '../Notification/Notifications';
import Navbar from '../Navbar/Navbar';
import SalonDashboard from '../Seller/SalonDashboard';

import { Route, Routes } from 'react-router-dom'
import NotFound from '../NotFound/NotFound';
import PaymentSuccess from '../Payment/PaymentSuccess';

const CustomerRoutes = () => {
  return (
    <div className='pb-20'>
        <Navbar />
        <Routes>
        <Route path="/" element={<Home />} />

        <Route
          path="/notifications"
          element={<Notifications />}
        />

        <Route
          path="/bookings"
          element={<Bookings />}
        />

        <Route
          path="/salon/:id"
          element={<SalonDetails />}
        />

        <Route
          path="/payment_success/:id"
          element={<PaymentSuccess />}
        />

        <Route
          path="*"
          element={<NotFound/>}
        />
      </Routes>

    </div>
  )
}

export default CustomerRoutes