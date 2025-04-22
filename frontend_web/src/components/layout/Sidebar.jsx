import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { FaHome, FaUsers, FaBoxOpen, FaExchangeAlt, FaSignOutAlt } from 'react-icons/fa';
import { IoMdSettings } from 'react-icons/io';
import { FaTimes } from 'react-icons/fa';

const Sidebar = ({ sidebarOpen, toggleSidebar }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname.split('/').pop();

  const handleLogout = () => {
    // Clear authentication data
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    // Redirect to login page
    navigate('/sign-in');
  };

  // Navigation items with icons
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: <FaHome size={20} />, path: '/admin/dashboard' },
    { id: 'users', label: 'User Management', icon: <FaUsers size={20} />, path: '/admin/users' },
    { id: 'products', label: 'Product Management', icon: <FaBoxOpen size={20} />, path: '/admin/products' },
    { id: 'transactions', label: 'Transactions', icon: <FaExchangeAlt size={20} />, path: '/admin/transactions' },
    { id: 'settings', label: 'Settings', icon: <IoMdSettings size={20} />, path: '/admin/settings' },
  ];

  return (
    <div className={`${sidebarOpen ? 'translate-x-0 w-64' : '-translate-x-full w-0 md:w-20 md:translate-x-0'} bg-[#232946] text-white transition-all duration-300 ease-in-out fixed md:relative z-30 h-full`}>
      <div className="flex justify-between items-center p-4 h-16 border-b border-[#121629]">
        <div className={`${sidebarOpen ? 'block' : 'hidden md:block'} font-bold text-xl`}>
          <span className="md:hidden lg:inline">CrediGo Admin</span>
          <span className="hidden md:inline lg:hidden">CG</span>
        </div>
        <button
          onClick={toggleSidebar}
          className="md:hidden text-white focus:outline-none"
        >
          <FaTimes size={24} />
        </button>
      </div>

      <nav className="mt-6">
        <ul>
          {navItems.map((item) => (
            <li key={item.id}>
              <button
                onClick={() => navigate(item.path)}
                className={`${
                  currentPath === item.id
                    ? 'bg-[#121629] border-l-4 border-[#eebbc3]'
                    : 'border-l-4 border-transparent hover:bg-[#2e3a5c]'
                } flex items-center w-full p-4 transition-colors duration-200`}
              >
                <span className="mr-4">{item.icon}</span>
                <span className={`${sidebarOpen ? 'opacity-100' : 'hidden md:block md:opacity-0 lg:opacity-100'} transition-opacity duration-200`}>
                  {item.label}
                </span>
              </button>
            </li>
          ))}
          <li>
            <button
              onClick={handleLogout}
              className="flex items-center w-full p-4 border-l-4 border-transparent hover:bg-[#2e3a5c] transition-colors duration-200"
            >
              <span className="mr-4"><FaSignOutAlt size={20} /></span>
              <span className={`${sidebarOpen ? 'opacity-100' : 'hidden md:block md:opacity-0 lg:opacity-100'} transition-opacity duration-200`}>
                Logout
              </span>
            </button>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;
