import React, { useEffect } from 'react'
import SalonDrawerList from './components/SalonDrawerList'
import Navbar from '../Admin Salon/Navbar'
import BookingTables from './Booking/BookingTable'
import ServiceTables from './Services/ServiceTable'
import TransactionTables from './Tarnsaction/TransactionTable'
import Category from './Category/Category'
import { Route, Routes } from 'react-router-dom'
import Home from '../Customer/Home/Home'
import CreateServiceForm from './Services/CreateServiceForm'
import Notifications from '../Notification/Notifications'
import HomePage from '../Customer/Home/HomePage'
import Payment from './Payment/Payment'
import SalonRoutes from '../Routes/SalonRoutes'
import { useDispatch, useSelector } from 'react-redux'
import { fetchSalonByOwner } from '../Redux/Salon/action'
import { getUser } from '../Redux/Auth/action'
import { fetchNotificationsBySalon } from '../Redux/Notifications/action'
import useNotificationWebsocket from '../util/useNotificationWebsocket'


const SalonDashboard = () => {
  const dispatch= useDispatch();
  const {salon} = useSelector((store)=>store);

  useEffect(()=>{
    dispatch(fetchSalonByOwner(localStorage.getItem("jwt")));
    dispatch(getUser(localStorage.getItem("jwt")));
  },[]);

  useEffect(()=>{
    if(salon.salon){
      dispatch(fetchNotificationsBySalon({jwt:localStorage.getItem("jwt"),salonId:salon.salon.id}));
    }
  },[])

  useNotificationWebsocket(salon.salon?.id,"salon")
  return (
    <div className='min-h-screen'>
      <Navbar DrawerList={SalonDrawerList}/>
      <section className='lg:flex lg:h-[90vh]'>
        <div className="hidden lg:block h-full">
          <SalonDrawerList/>
        </div>

        <div className="p-10 w-full lg:w-[80%] overflow-y-auto">
          
          <SalonRoutes/>

        </div>

      </section>
    </div>
  )
}

export default SalonDashboard