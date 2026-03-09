import api from '../../services/api';

const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  try {
    // Note: No "/api" prefix needed, it's already in the baseURL!
    const response = await api.post('/v1/media/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    console.log("File uploaded, name is:", response.data);
  } catch (error) {
    console.error("Upload failed", error);
  }
};