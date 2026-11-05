import React, { useEffect, useState } from 'react'
import { useDispatch } from 'react-redux';
import SockJS from 'sockjs-client';
import Stomp from "stompjs";
import { addNotification } from '../Redux/Notifications/action';
import { API_BASE_URL } from "../config/api";

const useNotificationWebsocket = (userId,type) => {
    const dispatch = useDispatch();
    const [stompClient,setStompClient] = useState(null);

  useEffect(()=>{
    if(userId){
        const sock = new SockJS(`${API_BASE_URL}/api/notifications/ws`)      const stomp = Stomp.over("sock")
        setStompClient(stomp)
    }
  },[userId])

  useEffect(()=>{
    if(stompClient){
      stompClient.connect({},()=>{
        setStompClient.subscribe(`/notification/${type}/${userId}`,
          (message)=>{
            const recievedMessage = JSON.parse(message.body);
            console.log("received notification from server ",recievedMessage)
            dispatch(addNotification(recievedMessage))
          }
        )
      },
      (error) => console.log("Subscription error ", error)
    );
    }
  },[stompClient,userId]);
  return (
    <div>useNotificationWebsocket</div>
  )
}

export default useNotificationWebsocket