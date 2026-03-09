import React, { useState } from 'react';
import ChatSidebar from '../features/chat/ChatSidebar';
import ChatRoom from '../features/chat/ChatRoom';

const ChatPage = ({ currentUser }) => {
  const [selectedUser, setSelectedUser] = useState(null);

  return (
    <div className="flex h-[80vh] bg-white rounded-3xl shadow-xl overflow-hidden border border-gray-100">
      <ChatSidebar 
        currentUser={currentUser} 
        onSelectUser={(user) => setSelectedUser(user)} 
      />
      
      <div className="flex-1 bg-[#FAFAFA]">
        {selectedUser ? (
          <ChatRoom currentUser={currentUser} recipientId={selectedUser.id} />
        ) : (
          <div className="h-full flex flex-col items-center justify-center text-gray-400">
             <div className="p-6 bg-white rounded-full shadow-sm mb-4">💬</div>
             <p>Select a friend to start chatting</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ChatPage;