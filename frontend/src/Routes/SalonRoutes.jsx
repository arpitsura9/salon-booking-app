import React from 'react'
import HomePage from '../Customer/Home/HomePage'
import { Route, Routes } from 'react-router-dom'
import ServiceTables from '../Seller/Services/ServiceTable'
import CreateServiceForm from '../Seller/Services/CreateServiceForm'
import BookingTables from '../Seller/Booking/BookingTable'
import TransactionTables from '../Seller/Tarnsaction/TransactionTable'
import Notifications from '../Notification/Notifications'
import Payment from '../Seller/Payment/Payment'
import Category from '../Seller/Category/Category'
import Profile from '../Seller/Profile/Profile'


const SalonRoutes = () => {
  return (
    <div>
        <Routes>
            <Route path='/' element={<HomePage/>}/>
            <Route path='/services' element={<ServiceTables/>}/>
            <Route path='/add-services' element={<CreateServiceForm/>}/>
            <Route path='/bookings' element={<BookingTables/>}/>
            <Route path='/category' element={<Category/>}/>
            <Route path='/transaction' element={<TransactionTables/>}/>
            <Route path='/notifications' element={<Notifications/>}/>
            <Route path='/payment' element={<Payment/>}/>
            <Route path='/account' element={<Profile/>}/>
          </Routes>
    </div>
  )
}

export default SalonRoutes