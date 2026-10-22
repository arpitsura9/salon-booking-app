import { AccountCircle, NotificationsActive } from '@mui/icons-material'
import { Avatar, Badge, Button, IconButton, Menu, MenuItem } from '@mui/material'
import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { getUser, logout } from '../Redux/Auth/action';
import useNotificationWebsocket from '../util/useNotificationWebsocket';

const Navbar = () => {
    const [anchorE1 , setAnchorE1] = React.useState(null);
    const dispatch= useDispatch();
    const {auth, notification} = useSelector(store=>store)
    const navigate=useNavigate();
    const open = Boolean(anchorE1);
    const handleClick = (event) => {
        setAnchorE1(event.currentTarget);
    }
    const handleClose = () => {
        setAnchorE1(null);
    }

    useEffect(()=>{
        dispatch(getUser(localStorage.getItem("jwt")));
    },[auth.jwt]);


    const handleLogout=()=>{
        dispatch(logout())
        handleClose()
    }

    useNotificationWebsocket(auth.user?.id,"user")

  return (
    <div className='z-50 px-6 flex items-center justify-between py-2'>
        <div className='flex items-center gap-10'>
            <h1 onClick={()=>navigate("/")} className='cursor-pointer font-bold text-2xl'>Salon Service</h1>
            <div className='flex items-center gap-5'>
                <h1>Home</h1>
            </div>

        </div>
        <div className='flex items-center gap-3 md:gap-6'>
            <Button onClick={()=>navigate("/become-partner")} variant='outlined'>Become partner</Button>
            <IconButton onClick={()=>navigate("/notifications")}>
                <Badge badgeContent={notification.unreadCount}>
                    <NotificationsActive color='primary'/>
                </Badge>
            </IconButton>

            {auth.user?.id ? <div className='flex gap-1 items-center'>
                <h1 className='text-lg font-semibold'>{auth.user?.fullName}</h1>
                <IconButton 
                id="basic-button"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-haspopup='true'
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}>
                    <Avatar sx={{bgcolor:"green"}}>
                        {auth.user?.fullName?.[0]}
                    </Avatar>
                </IconButton>

                <Menu
                id="basic-menu"
                anchorEl={anchorE1}
                open={open}
                onClose={handleClose}
                MenuListProps={{'aria-labelledby':'basic-button',}}
                >
                    
                    <MenuItem onClick={()=>{
                        navigate("/bookings")
                        handleClose()}}>
                            My Bookings
                    </MenuItem>

                    {auth.user?.role==="SALON_OWNER" && <MenuItem onClick={()=>{
                        navigate("/salon-dashboard")
                        handleClose();
                    }}>Salon Dashboard</MenuItem>}
                    <MenuItem onClick={handleLogout}>Logout</MenuItem>
                </Menu>

            </div>

            :<IconButton onClick={()=>navigate("/login")}>
                <AccountCircle sx = {{fontSize:"45px", color:"green"}}/>
            </IconButton>}

        </div>


    </div>
  )
}

export default Navbar