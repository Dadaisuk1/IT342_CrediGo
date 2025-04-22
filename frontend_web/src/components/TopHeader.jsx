import React from 'react';
import { useLocation } from 'react-router-dom';
import { FaBars, FaBell, FaSearch, FaUserShield } from 'react-icons/fa';

const TopHeader = ({ toggleSidebar }) => {
  const location = useLocation();
  const currentPath = location.pathname.split('/').pop();

  // Map paths to section titles
  const sectionTitles = {
    'dashboard': 'Dashboard',
    'users': 'User Management',
    'products': 'Product Management',
    'transactions': 'Transactions',
    'settings': 'Settings'
  };

  const currentTitle = sectionTitles[currentPath] || 'Dashboard';

  return (
    <header className="bg-white shadow-sm z-20">
      <div className="flex items-center justify-between p-4">
        <div className="flex items-center">
          <button
            onClick={toggleSidebar}
            className="md:hidden text-[#232946] focus:outline-none mr-4"
          >
            <FaBars size={24} />
          </button>
          <h1 className="text-xl font-semibold text-[#232946]">
            {currentTitle}
          </h1>
        </div>

        {/* Search and profile section */}
        <div className="flex items-center space-x-4">
          <div className="relative hidden md:block">
            <input
              type="text"
              placeholder="Search..."
              className="pl-10 pr-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            />
            <FaSearch className="absolute left-3 top-3 text-gray-400" />
          </div>

          <div className="relative">
            <button className="text-gray-500 hover:text-gray-700 focus:outline-none">
              <FaBell size={20} />
              <span className="absolute top-0 right-0 w-2 h-2 bg-red-500 rounded-full"></span>
            </button>
          </div>

          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 bg-[#232946] rounded-full flex items-center justify-center text-white">
              <FaUserShield />
            </div>
            <span className="hidden md:block font-medium text-gray-700">Admin</span>
          </div>
        </div>
      </div>
    </header>
  );
};

export default TopHeader;
