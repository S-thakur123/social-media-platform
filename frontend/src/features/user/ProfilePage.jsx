import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { User, Camera, Mail, MapPin, Calendar } from 'lucide-react';

const ProfilePage = ({ userId }) => {
  const [user, setUser] = useState(null);
  const [imageUrl, setImageUrl] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        // 1. Get User Data
        const userRes = await api.get(`/v1/users/${userId}`);
        setUser(userRes.data);

        // 2. Get the real image URL from Media Service
        if (userRes.data.profileImageName) {
          const mediaRes = await api.get(`/v1/media/url/${userRes.data.profileImageName}`);
          setImageUrl(mediaRes.data); // This is the temporary MinIO link
        }
      } catch (err) {
        console.error("Error loading profile:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, [userId]);

  const handleImageChange = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 1. Prepare the form data (Standard for file uploads)
  const formData = new FormData();
  formData.append('file', file);

  try {
    // 2. Upload to Media Service via Gateway
    const uploadRes = await api.post('/v1/media/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });

    const newImageName = uploadRes.data; // e.g., "123-profile.jpg"

    // 3. Update the User Service with the new image name
    await api.put(`/v1/users/${userId}/profile-image`, { 
        imageName: newImageName 
    });

    // 4. Refresh the UI
    window.location.reload(); 
  } catch (err) {
    console.error("Upload failed:", err);
    alert("Could not update profile picture.");
  }
};

  if (loading) return <div className="p-10 text-center">Loading Experience...</div>;

  return (
    <div className="max-w-4xl mx-auto mt-10 p-6 bg-[#FDFCFB] rounded-3xl shadow-sm border border-orange-50">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row items-center gap-8">
        <div className="relative group">
          <div className="w-32 h-32 rounded-full overflow-hidden border-4 border-white shadow-lg bg-gray-100">
            {imageUrl ? (
              <img src={imageUrl} alt="Profile" className="w-full h-full object-cover" />
            ) : (
              <User size={64} className="m-auto mt-8 text-gray-400" />
            )}
          </div>
          <button className="absolute bottom-0 right-0 p-2 bg-white rounded-full shadow-md hover:bg-orange-50 transition-colors">
            <Camera size={18} className="text-gray-600" />
          </button>
        </div>

        <div className="flex-1 text-center md:text-left">
          <h1 className="text-3xl font-bold text-gray-800">{user?.name}</h1>
          <p className="text-gray-500 italic mt-1">{user?.bio || "No bio yet..."}</p>
          
          <div className="flex flex-wrap justify-center md:justify-start gap-4 mt-4 text-sm text-gray-600">
            <span className="flex items-center gap-1"><Mail size={14}/> {user?.email}</span>
            <span className="flex items-center gap-1"><MapPin size={14}/> {user?.location || "Global"}</span>
            <span className="flex items-center gap-1"><Calendar size={14}/> Joined {user?.joinDate}</span>
          </div>
        </div>
      </div>

      {/* Bento-style Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-10">
        <div className="p-6 bg-white rounded-2xl border border-gray-100 shadow-sm hover:shadow-md transition-shadow">
          <p className="text-gray-400 text-xs uppercase tracking-wider font-semibold">Posts</p>
          <p className="text-2xl font-bold mt-1">128</p>
        </div>
        <div className="p-6 bg-white rounded-2xl border border-gray-100 shadow-sm">
          <p className="text-gray-400 text-xs uppercase tracking-wider font-semibold">Followers</p>
          <p className="text-2xl font-bold mt-1">4.2k</p>
        </div>
        <div className="p-6 bg-orange-50 rounded-2xl border border-orange-100 shadow-sm">
          <p className="text-orange-400 text-xs uppercase tracking-wider font-semibold">Engagement</p>
          <p className="text-2xl font-bold mt-1 text-orange-600">98%</p>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;