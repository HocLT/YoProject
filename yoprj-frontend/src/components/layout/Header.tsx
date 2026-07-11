import React from 'react';
import { useAuth } from '../../context/AuthProvider';
import { LogOut, User } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white border-b border-gray-200 h-16 flex items-center justify-between px-6 shadow-sm">
      <div className="flex items-center">
        <h2 className="text-xl font-semibold text-gray-800">
          {/* We can put dynamic page titles here later based on route */}
        </h2>
      </div>
      <div className="flex items-center space-x-4">
        <div className="flex items-center space-x-2 text-gray-700">
          <User className="w-5 h-5 text-gray-500" />
          <div className="flex flex-col">
            <span className="text-sm font-medium">{user?.fullName || 'User'}</span>
            <span className="text-xs text-gray-500">{user?.role}</span>
          </div>
        </div>
        <button
          onClick={handleLogout}
          className="p-2 text-gray-500 hover:text-red-600 hover:bg-red-50 rounded-full transition-colors focus:outline-none"
          title="Logout"
        >
          <LogOut className="w-5 h-5" />
        </button>
      </div>
    </header>
  );
};
