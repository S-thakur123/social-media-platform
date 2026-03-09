import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

let stompClient = null;

const ChatRoom = ({ currentUser, recipientId }) => {
    const [messages, setMessages] = useState([]);
    const [userData, setUserData] = useState({
        content: '',
        connected: false
    });

    useEffect(() => {
        connect();
    }, []);

    const connect = () => {
        // Pointing to your API Gateway URL
        const socket = new SockJS('http://localhost:8080/ws-chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    };

    const onConnected = () => {
        setUserData({ ...userData, connected: true });
        
        // Subscribe to private messages 
        // Logic: /user/{userId}/queue/messages
        stompClient.subscribe(`/user/${currentUser.id}/queue/messages`, onMessageReceived);
    };

    const onMessageReceived = (payload) => {
        const payloadData = JSON.parse(payload.body);
        setMessages(prev => [...prev, payloadData]);
    };

    const sendMessage = () => {
        if (stompClient && userData.content) {
            const chatMessage = {
                senderId: currentUser.id,
                recipientId: recipientId,
                content: userData.content,
                type: 'CHAT'
            };
            // Destination: /app/chat (as defined in our Controller @MessageMapping)
            stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            
            // Optimistically add to local UI
            setMessages(prev => [...prev, chatMessage]);
            setUserData({ ...userData, content: '' });
        }
    };

    const onError = (err) => {
        console.log("Connection Error: ", err);
    };

    return (
        <div className="chat-container">
            {userData.connected ? (
                <div className="chat-box">
                    <div className="message-list">
                        {messages.map((msg, index) => (
                            <div key={index} className={msg.senderId === currentUser.id ? "self" : "other"}>
                                <b>{msg.senderId}:</b> {msg.content}
                            </div>
                        ))}
                    </div>
                    <input 
                        value={userData.content} 
                        onChange={(e) => setUserData({...userData, content: e.target.value})} 
                        placeholder="Type a message..."
                    />
                    <button onClick={sendMessage}>Send</button>
                </div>
            ) : (
                <p>Connecting to Chat...</p>
            )}
        </div>
    );
};

export default ChatRoom;