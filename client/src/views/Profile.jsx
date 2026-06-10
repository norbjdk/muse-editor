import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext'; 
import { userAPI } from '../services/api';

function Profile() {
    const { user, fetchCurrentUser, logout } = useAuth();
    const navigate = useNavigate();

    if (!user) {
         return (
            <div>
            </div>
        );
    }
}

export default Profile;