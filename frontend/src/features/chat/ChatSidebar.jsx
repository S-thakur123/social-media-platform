import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Search, MoreVertical } from 'lucide-react';

const ChatSidebar = ({ onSelectUser, currentUser }) => {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState('');

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        // Fetch all users to chat with (excluding yourself)
        const res = await api.get('/v1/users');
        setUsers(res.data.filter(u => u.id !== currentUser.id));
      } catch (err) {
        console.error("Failed to load users:", err);
      }
    };
    fetchUsers();
  }, [currentUser.id]);

  return (
    <div className="w-80 h-full bg-white border-r border-gray-100 flex flex-col">
      {/* Sidebar Header */}
      <div className="p-4 flex justify-between items-center">
        <h2 className="text-xl font-bold text-gray-800">Messages</h2>
        <button className="p-2 hover:bg-gray-50 rounded-full transition-colors">
          <MoreVertical size={20} className="text-gray-500" />
        </button>
      </div>

      {/* Search Bar */}
      <div className="px-4 mb-4">
        <div className="relative">
          <Search className="absolute left-3 top-2.5 text-gray-400" size={18} />
          <input 
            type="text"
            placeholder="Search conversations..."
            className="w-full pl-10 pr-4 py-2 bg-gray-50 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-orange-100"
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      {/* User List */}
      <div className="flex-1 overflow-y-auto">
        {users.filter(u => u.name.toLowerCase().includes(search.toLowerCase())).map((user) => (
          <div 
            key={user.id}
            onClick={() => onSelectUser(user)}
            className="flex items-center gap-3 p-4 cursor-pointer hover:bg-orange-50/50 transition-all border-l-4 border-transparent hover:border-orange-400"
          >
            {/* Avatar with Online Status Indicator */}
            <div className="relative">
              <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200">
                <img 
                   src={user.profileImage || `https://api.dicebear.com/7.x/avataaars/svg?seed=${user.name}`} 
                   alt={user.name} 
                />
              </div>
              {/* Green Dot (logic: user.isOnline) */}
              {user.isOnline && (
                <span className="absolute bottom-0 right-0 w-3.5 h-3.5 bg-green-500 border-2 border-white rounded-full"></span>
              )}
            </div>

            <div className="flex-1 min-w-0">
              <div className="flex justify-between items-center">
                <h3 className="font-semibold text-gray-900 truncate">{user.name}</h3>
                <span className="text-[10px] text-gray-400">12:45 PM</span>
              </div>
              <p className="text-xs text-gray-500 truncate">Tap to start chatting...</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ChatSidebar;